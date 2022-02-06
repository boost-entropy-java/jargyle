package com.github.jh3nd3rs0n.jargyle.server.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.MessageProp;

import com.github.jh3nd3rs0n.jargyle.internal.net.IOExceptionHandler;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.UsernamePasswordAuthenticator;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.MethodEncapsulation;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.MethodSubnegotiationException;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.NullMethodEncapsulation;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.GssSocket;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.GssapiMethodEncapsulation;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.Message;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.MessageType;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.ProtectionLevel;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.ProtectionLevels;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.userpassauth.UsernamePasswordRequest;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.userpassauth.UsernamePasswordResponse;

abstract class MethodSubnegotiator {
	
	private static final Map<Method, MethodSubnegotiator> METHOD_SUBNEGOTIATOR_MAP =
			new HashMap<Method, MethodSubnegotiator>();

	@SuppressWarnings("unused")
	private static final MethodSubnegotiator GSSAPI_METHOD_SUBNEGOTIATOR = new MethodSubnegotiator(
			Method.GSSAPI) {
		
		private void establishContext(
				final Socket socket,
				final GSSContext context,
				final Configuration configuration) throws IOException {
			InputStream inStream = socket.getInputStream();
			OutputStream outStream = socket.getOutputStream();
			byte[] token = null;
			while (!context.isEstablished()) {
				Message message = Message.newInstanceFrom(inStream);
				if (message.getMessageType().equals(MessageType.ABORT)) {
					throw new MethodSubnegotiationException(
							this.getMethod(), 
							String.format(
									"client %s aborted process of context "
									+ "establishment",
									socket));
				}
				token = message.getToken();
				try {
					token = context.acceptSecContext(token, 0, token.length);
				} catch (GSSException e) {
					outStream.write(Message.newInstance(
							MessageType.ABORT, 
							null).toByteArray());
					outStream.flush();
					throw new MethodSubnegotiationException(this.getMethod(), e);
				}
				if (token == null) {
					outStream.write(Message.newInstance(
							MessageType.AUTHENTICATION, 
							new byte[] { }).toByteArray());
					outStream.flush();
				} else {
					outStream.write(Message.newInstance(
							MessageType.AUTHENTICATION, 
							token).toByteArray());
					outStream.flush();
				}
			}
		}
		
		private ProtectionLevel negotiateProtectionLevel(
				final Socket socket, 
				final GSSContext context,
				final Configuration configuration) throws IOException {
			InputStream inStream = socket.getInputStream();
			OutputStream outStream = socket.getOutputStream();
			Message message = Message.newInstanceFrom(inStream);
			if (message.getMessageType().equals(MessageType.ABORT)) {
				throw new MethodSubnegotiationException(
						this.getMethod(), 
						String.format(
								"client %s aborted protection level negotiation",
								socket));
			}
			boolean necReferenceImpl = configuration.getSettings().getLastValue(
					Socks5SettingSpecConstants.SOCKS5_GSSAPIAUTH_NEC_REFERENCE_IMPL).booleanValue();
			byte[] token = message.getToken();
			MessageProp prop = null;
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
					throw new MethodSubnegotiationException(
							this.getMethod(), e);
				}			
			}
			ProtectionLevel protectionLevel = null;
			try {
				protectionLevel = ProtectionLevel.valueOfByte(token[0]);
			} catch (IllegalArgumentException e) {
				throw new MethodSubnegotiationException(this.getMethod(), e);
			}
			ProtectionLevels protectionLevels = 
					configuration.getSettings().getLastValue(
							Socks5SettingSpecConstants.SOCKS5_GSSAPIAUTH_PROTECTION_LEVELS);
			List<ProtectionLevel> protectionLevelList = 
					protectionLevels.toList();
			ProtectionLevel protectionLevelChoice = protectionLevel;
			if (!protectionLevelList.contains(protectionLevelChoice)) {
				ProtectionLevel firstProtectionLevel = 
						protectionLevelList.get(0);
				protectionLevelChoice = firstProtectionLevel;
			}
			token = new byte[] { protectionLevelChoice.byteValue() };
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
					throw new MethodSubnegotiationException(
							this.getMethod(), e);
				}
			}
			outStream.write(Message.newInstance(
					MessageType.PROTECTION_LEVEL_NEGOTIATION, 
					token).toByteArray());
			outStream.flush();
			if (socket.isClosed()) {
				throw new MethodSubnegotiationException(
						this.getMethod(), 
						String.format(
								"client %s closed due to client finding "
								+ "choice of protection level unacceptable",
								socket));
			}
			return protectionLevelChoice;
		}
		
		private GSSContext newContext() throws IOException {
			GSSManager manager = GSSManager.getInstance();
			GSSContext context = null;
			try {
				context = manager.createContext((GSSCredential) null);
			} catch (GSSException e) {
				throw new MethodSubnegotiationException(this.getMethod(), e);
			}
			return context;
		}

		@Override
		public MethodSubnegotiationResults subnegotiate(
				final Socket socket, 
				final Configuration configuration) throws IOException {
			GSSContext context = null;
			ProtectionLevel protectionLevelChoice = null;
			try {
				context = this.newContext();
				this.establishContext(socket, context, configuration);
				protectionLevelChoice =	this.negotiateProtectionLevel(
						socket, context, configuration);
			} catch (IOException e) {
				IOExceptionHandler.INSTANCE.handle(
						e, 
						new MethodSubnegotiationException(
								this.getMethod(), e));
			}
			MessageProp msgProp = protectionLevelChoice.getMessageProp();
			GssSocket gssSocket = new GssSocket(socket, context, msgProp);
			MethodEncapsulation methodEncapsulation = 
					new GssapiMethodEncapsulation(gssSocket);
			String user = null;
			try {
				user = context.getSrcName().toString();
			} catch (GSSException e) {
				throw new MethodSubnegotiationException(this.getMethod(), e);
			}
			return new MethodSubnegotiationResults(
					this.getMethod(), methodEncapsulation, user);
		}
		
	};
	
	@SuppressWarnings("unused")
	private static final MethodSubnegotiator NO_ACCEPTABLE_METHODS_METHOD_SUBNEGOTIATOR = new MethodSubnegotiator(
			Method.NO_ACCEPTABLE_METHODS) {

		@Override
		public MethodSubnegotiationResults subnegotiate(
				final Socket socket, 
				final Configuration configuration) throws IOException {
			throw new MethodSubnegotiationException(
					this.getMethod(), 
					String.format("no acceptable methods from %s", socket));
		}
		
	};
	
	@SuppressWarnings("unused")
	private static final MethodSubnegotiator NO_AUTHENTICATION_REQUIRED_METHOD_SUBNEGOTIATOR = new MethodSubnegotiator(
			Method.NO_AUTHENTICATION_REQUIRED) {

		@Override
		public MethodSubnegotiationResults subnegotiate(
				final Socket socket, 
				final Configuration configuration) throws IOException {
			MethodEncapsulation methodEncapsulation = 
					new NullMethodEncapsulation(socket); 
			return new MethodSubnegotiationResults(
					this.getMethod(), methodEncapsulation, null);
		}
		
	};
	
	@SuppressWarnings("unused")
	private static final MethodSubnegotiator USERNAME_PASSWORD_METHOD_SUBNEGOTIATOR = new MethodSubnegotiator(
			Method.USERNAME_PASSWORD) {

		@Override
		public MethodSubnegotiationResults subnegotiate(
				final Socket socket, 
				final Configuration configuration) throws IOException {
			String username = null;
			char[] password = null;
			try {
				InputStream inputStream = socket.getInputStream();
				OutputStream outputStream = socket.getOutputStream();
				UsernamePasswordRequest usernamePasswordReq = 
						UsernamePasswordRequest.newInstanceFrom(inputStream);
				UsernamePasswordResponse usernamePasswordResp = null;
				username = usernamePasswordReq.getUsername();
				password = usernamePasswordReq.getPassword();
				UsernamePasswordAuthenticator authenticator = 
						configuration.getSettings().getLastValue(
								Socks5SettingSpecConstants.SOCKS5_USERPASSAUTH_USERNAME_PASSWORD_AUTHENTICATOR);
				if (authenticator == null) { 
					authenticator = UsernamePasswordAuthenticator.newInstance(); 
				}
				if (!authenticator.authenticate(username, password)) {
					usernamePasswordResp = UsernamePasswordResponse.newInstance(
							(byte) 0x01);
					outputStream.write(usernamePasswordResp.toByteArray());
					outputStream.flush();
					throw new MethodSubnegotiationException(
							this.getMethod(), 
							String.format(
									"invalid username password from %s",
									socket));
				}
				usernamePasswordResp = UsernamePasswordResponse.newInstance(
						UsernamePasswordResponse.STATUS_SUCCESS);
				outputStream.write(usernamePasswordResp.toByteArray());
				outputStream.flush();
			} catch (IOException e) {
				IOExceptionHandler.INSTANCE.handle(
						e, 
						new MethodSubnegotiationException(
								this.getMethod(), e));
			} finally {
				if (password != null) { Arrays.fill(password, '\0'); } 
			}
			MethodEncapsulation methodEncapsulation = 
					new NullMethodEncapsulation(socket);
			return new MethodSubnegotiationResults(
					this.getMethod(), methodEncapsulation, username);
		}
		
	};
	
	public static MethodSubnegotiator getInstance(final Method meth) {
		MethodSubnegotiator methodSubnegotiator = METHOD_SUBNEGOTIATOR_MAP.get(
				meth);
		if (methodSubnegotiator != null) {
			return methodSubnegotiator;
		}
		String str = METHOD_SUBNEGOTIATOR_MAP.keySet().stream()
				.map(Method::toString)
				.collect(Collectors.joining(", "));
		throw new IllegalArgumentException(String.format(
				"expected method must be one of the following values: %s. "
				+ "actual value is %s",
				str,
				meth));
	}
	
	private final Method method;
	
	private MethodSubnegotiator(final Method meth) {
		this.method = meth;
		METHOD_SUBNEGOTIATOR_MAP.put(meth, this);
	}
	
	public Method getMethod() {
		return this.method;
	}
	
	public abstract MethodSubnegotiationResults subnegotiate(
			final Socket socket,
			final Configuration configuration) throws IOException;
	
}
