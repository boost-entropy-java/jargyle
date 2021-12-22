package com.github.jh3nd3rs0n.jargyle.client.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.MessageProp;
import org.ietf.jgss.Oid;

import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.MethodEncapsulation;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.MethodSubnegotiationException;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.NullMethodEncapsulation;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Exception;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.GssSocket;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.GssapiMethodEncapsulation;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.Message;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.MessageType;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.ProtectionLevel;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.ProtectionLevels;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.userpassauth.UsernamePasswordRequest;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.userpassauth.UsernamePasswordResponse;

enum MethodSubnegotiator {
	
	GSSAPI_METHOD_SUBNEGOTIATOR(Method.GSSAPI) {
		
		private void establishContext(
				final Socket socket,
				final GSSContext context,
				final Socks5Client socks5Client) throws IOException {
			InputStream inStream = socket.getInputStream();
			OutputStream outStream = socket.getOutputStream();
			byte[] token = new byte[] { };
			while (!context.isEstablished()) {
				if (token == null) {
					token = new byte[] { };
				}
				try {
					token = context.initSecContext(token, 0, token.length);
				} catch (GSSException e) {
					throw new Socks5Exception(e);
				}
				if (token != null) {
					outStream.write(Message.newInstance(
							MessageType.AUTHENTICATION, 
							token).toByteArray());
					outStream.flush();
				}
				if (!context.isEstablished()) {
					Message message = Message.newInstanceFrom(inStream);
					if (message.getMessageType().equals(MessageType.ABORT)) {
						throw new MethodSubnegotiationException(
								"server aborted process of context establishment");
					}
					token = message.getToken();
				}
			}
		}
		
		private ProtectionLevel negotiateProtectionLevel(
				final Socket socket,
				final GSSContext context,
				final Socks5Client socks5Client) throws IOException {
			InputStream inStream = socket.getInputStream();
			OutputStream outStream = socket.getOutputStream();
			boolean necReferenceImpl = socks5Client.getProperties().getValue(
					Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTH_NEC_REFERENCE_IMPL).booleanValue();
			ProtectionLevels protectionLevels = 
					socks5Client.getProperties().getValue(
							Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTH_PROTECTION_LEVELS);
			List<ProtectionLevel> protectionLevelList = 
					protectionLevels.toList(); 
			ProtectionLevel firstProtectionLevel = protectionLevelList.get(0);
			byte[] token = new byte[] { firstProtectionLevel.byteValue() };
			MessageProp prop = null;
			if (!necReferenceImpl) {
				prop = new MessageProp(0, true);
				try {
					token = context.wrap(token, 0, token.length, 
							new MessageProp(prop.getQOP(), prop.getPrivacy()));
				} catch (GSSException e) {
					outStream.write(Message.newInstance(
							MessageType.ABORT, 
							null).toByteArray());
					outStream.flush();
					throw new Socks5Exception(e);
				}
			}
			outStream.write(Message.newInstance(
					MessageType.PROTECTION_LEVEL_NEGOTIATION, 
					token).toByteArray());
			outStream.flush();
			Message message = Message.newInstanceFrom(inStream);
			if (message.getMessageType().equals(MessageType.ABORT)) {
				throw new MethodSubnegotiationException(
						"server aborted protection level negotiation");
			}
			token = message.getToken();
			if (!necReferenceImpl) {
				prop = new MessageProp(0, false);
				try {
					token = context.unwrap(token, 0, token.length, 
							new MessageProp(prop.getQOP(), prop.getPrivacy()));
				} catch (GSSException e) {
					outStream.write(Message.newInstance(
							MessageType.ABORT, 
							null).toByteArray());
					outStream.flush();
					throw new Socks5Exception(e);
				}
			}
			ProtectionLevel protectionLevelSelection = null;
			try {
				protectionLevelSelection = ProtectionLevel.valueOfByte(
						token[0]);
			} catch (IllegalArgumentException e) {
				throw new Socks5Exception(e);
			}
			if (!protectionLevelList.contains(protectionLevelSelection)) {
				throw new MethodSubnegotiationException(String.format(
						"server selected %s which is not acceptable by this socket", 
						protectionLevelSelection));
			}
			return protectionLevelSelection;
		}
		
		private GSSContext newContext(
				final Socks5Client socks5Client) throws IOException {
			GSSManager manager = GSSManager.getInstance();
			String server = socks5Client.getProperties().getValue(
					Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTH_SERVICE_NAME);
			GSSName serverName = null;
			try {
				serverName = manager.createName(server, null);
			} catch (GSSException e) {
				throw new Socks5Exception(e);
			}
			Oid mechanismOid = socks5Client.getProperties().getValue(
					Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTH_MECHANISM_OID);
			GSSContext context = null;
			try {
				context = manager.createContext(
						serverName, 
						mechanismOid,
				        null,
				        GSSContext.DEFAULT_LIFETIME);
			} catch (GSSException e) {
				throw new Socks5Exception(e);
			}
			try {
				context.requestMutualAuth(true);
			} catch (GSSException e) {
				throw new Socks5Exception(e);
			}
			try {
				context.requestConf(true);
			} catch (GSSException e) {
				throw new Socks5Exception(e);
			}
			try {
				context.requestInteg(true);
			} catch (GSSException e) {
				throw new Socks5Exception(e);
			}
			return context;
		}

		@Override
		public MethodEncapsulation subnegotiate(
				final Socket socket, 
				final Socks5Client socks5Client) throws IOException {
			GSSContext context = this.newContext(socks5Client);
			this.establishContext(socket, context, socks5Client);
			ProtectionLevel protectionLevelSelection =
					this.negotiateProtectionLevel(
							socket, context, socks5Client);
			MessageProp msgProp = protectionLevelSelection.getMessageProp();
			GssSocket gssSocket = new GssSocket(socket, context, msgProp);
			return new GssapiMethodEncapsulation(gssSocket);
		}
		
	},
	
	NO_ACCEPTABLE_METHODS_METHOD_SUBNEGOTIATOR(Method.NO_ACCEPTABLE_METHODS) {

		@Override
		public MethodEncapsulation subnegotiate(
				final Socket Socket, 
				final Socks5Client socks5Client) throws IOException {
			throw new MethodSubnegotiationException("no acceptable methods");
		}
		
	},
	
	NO_AUTHENTICATION_REQUIRED_METHOD_SUBNEGOTIATOR(
			Method.NO_AUTHENTICATION_REQUIRED) {

		@Override
		public MethodEncapsulation subnegotiate(
				final Socket socket, 
				final Socks5Client socks5Client) throws IOException {
			return new NullMethodEncapsulation(socket);
		}
		
	},
	
	USERNAME_PASSWORD_METHOD_SUBNEGOTIATOR(Method.USERNAME_PASSWORD) {

		@Override
		public MethodEncapsulation subnegotiate(
				final Socket socket, 
				final Socks5Client socks5Client) throws IOException {
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			String username = socks5Client.getProperties().getValue(
					Socks5PropertySpecConstants.SOCKS5_USERPASSAUTH_USERNAME);
			char[] password = socks5Client.getProperties().getValue(
					Socks5PropertySpecConstants.SOCKS5_USERPASSAUTH_PASSWORD).getPassword();
			UsernamePasswordRequest usernamePasswordReq = 
					UsernamePasswordRequest.newInstance(username, password);
			outputStream.write(usernamePasswordReq.toByteArray());
			outputStream.flush();
			Arrays.fill(password, '\0');
			UsernamePasswordResponse usernamePasswordResp = 
					UsernamePasswordResponse.newInstanceFrom(inputStream);
			if (usernamePasswordResp.getStatus() != 
					UsernamePasswordResponse.STATUS_SUCCESS) {
				throw new MethodSubnegotiationException(
						"invalid username password");
			}
			return new NullMethodEncapsulation(socket);
		}
		
	};
	
	public static MethodSubnegotiator valueOfMethod(final Method meth) {
		for (MethodSubnegotiator value : MethodSubnegotiator.values()) {
			if (value.methodValue().equals(meth)) {
				return value;
			}
		}
		StringBuilder sb = new StringBuilder();
		List<MethodSubnegotiator> list = Arrays.asList(
				MethodSubnegotiator.values());
		for (Iterator<MethodSubnegotiator> iterator = list.iterator();
				iterator.hasNext();) {
			MethodSubnegotiator value = iterator.next();
			Method method = value.methodValue();
			sb.append(method);
			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}
		throw new IllegalArgumentException(
				String.format(
						"expected method must be one of the following values: "
						+ "%s. actual value is %s",
						sb.toString(),
						meth));
	}
	
	private final Method methodValue;
	
	private MethodSubnegotiator(final Method methValue) {
		this.methodValue = methValue;
	}
	
	public Method methodValue() {
		return this.methodValue;
	}
	
	public abstract MethodEncapsulation subnegotiate(
			final Socket socket,
			final Socks5Client socks5Client) throws IOException;
	
}
