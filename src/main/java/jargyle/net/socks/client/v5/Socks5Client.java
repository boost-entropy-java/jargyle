package jargyle.net.socks.client.v5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Set;
import java.util.TreeSet;

import jargyle.net.socks.client.Properties;
import jargyle.net.socks.client.PropertySpec;
import jargyle.net.socks.client.SocksClient;
import jargyle.net.socks.client.SocksNetObjectFactory;
import jargyle.net.socks.transport.v5.AuthMethod;
import jargyle.net.socks.transport.v5.AuthMethods;
import jargyle.net.socks.transport.v5.ClientMethodSelectionMessage;
import jargyle.net.socks.transport.v5.Method;
import jargyle.net.socks.transport.v5.ServerMethodSelectionMessage;
import jargyle.net.ssl.DtlsDatagramSocketFactory;

public final class Socks5Client extends SocksClient {

	private final DtlsDatagramSocketFactory dtlsDatagramSocketFactory;
	
	public Socks5Client(
			final Socks5ServerUri serverUri, final Properties props) {
		this(serverUri, props, null);
	}
	
	public Socks5Client(
			final Socks5ServerUri serverUri, 
			final Properties props,
			final SocksClient chainedClient) {
		super(serverUri, props, chainedClient);
		DtlsDatagramSocketFactory dtlsDatagramSockFactory = props.getValue(
				PropertySpec.DTLS_ENABLED).booleanValue() ? 
						new DtlsDatagramSocketFactoryImpl(this) : null;
		this.dtlsDatagramSocketFactory = dtlsDatagramSockFactory;
	}
	
	private Socket getAuthenticatedConnectedInternalSocket(
			final Socket connectedInternalSocket) throws IOException {
		InputStream inputStream = connectedInternalSocket.getInputStream();
		OutputStream outputStream = connectedInternalSocket.getOutputStream();
		Set<Method> methods = new TreeSet<Method>();
		AuthMethods authMethods = this.getProperties().getValue(
				PropertySpec.SOCKS5_AUTH_METHODS);
		for (AuthMethod authMethod : authMethods.toList()) {
			methods.add(authMethod.methodValue());
		}
		ClientMethodSelectionMessage cmsm = 
				ClientMethodSelectionMessage.newInstance(methods);
		outputStream.write(cmsm.toByteArray());
		outputStream.flush();
		ServerMethodSelectionMessage smsm =
				ServerMethodSelectionMessage.newInstanceFrom(inputStream);
		Method method = smsm.getMethod();
		Authenticator authenticator = null;
		try {
			authenticator = Authenticator.valueOfMethod(method);
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
		Socket newSocket = authenticator.authenticate(
				connectedInternalSocket, this);
		return newSocket;		
	}
	
	public DatagramSocket getConnectedInternalDatagramSocket(
			final DatagramSocket internalDatagramSocket,
			final String udpRelayServerHost,
			final int udpRelayServerPort) throws IOException {
		InetAddress udpRelayServerHostInetAddress = InetAddress.getByName(
				udpRelayServerHost); 
		internalDatagramSocket.connect(
				udpRelayServerHostInetAddress, udpRelayServerPort);
		if (this.dtlsDatagramSocketFactory == null) {
			return internalDatagramSocket;
		}
		return this.dtlsDatagramSocketFactory.newDatagramSocket(
				internalDatagramSocket, 
				udpRelayServerHost, 
				udpRelayServerPort);
	}
	
	@Override
	public Socket getConnectedInternalSocket(
			final Socket internalSocket, 
			final int timeout, 
			final boolean bindBeforeConnect) throws IOException {
		Socket sock = super.getConnectedInternalSocket(
				internalSocket, timeout, bindBeforeConnect);
		return this.getAuthenticatedConnectedInternalSocket(sock);
	}

	@Override
	public Socket newConnectedInternalSocket(
			final InetAddress localAddr, 
			final int localPort) throws IOException {
		Socket sock = super.newConnectedInternalSocket(localAddr, localPort);
		return this.getAuthenticatedConnectedInternalSocket(sock);
	}
	
	@Override
	public SocksNetObjectFactory newSocksNetObjectFactory() {
		return new Socks5NetObjectFactory(this);
	}
	
}
