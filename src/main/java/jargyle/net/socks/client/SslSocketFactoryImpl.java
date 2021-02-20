package jargyle.net.socks.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import jargyle.net.ssl.CipherSuites;
import jargyle.net.ssl.Protocols;
import jargyle.net.ssl.SslSocketFactory;

final class SslSocketFactoryImpl extends SslSocketFactory {
	
	private final SslFactoryImpl sslFactoryImpl;

	public SslSocketFactoryImpl(final SslFactoryImpl factoryImpl) {
		this.sslFactoryImpl = factoryImpl;
	}

	@Override
	public Socket newSocket(
			final Socket socket, 
			final InputStream consumed, 
			final boolean autoClose) throws IOException {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Socket newSocket(
			final Socket socket, 
			final String host, 
			final int port, 
			final boolean autoClose) throws IOException {
		Properties properties = this.sslFactoryImpl.getProperties();
		if (!properties.getValue(
				PropertySpec.SSL_ENABLED, Boolean.class).booleanValue()) {
			return socket;
		}
		SSLContext sslContext = this.sslFactoryImpl.getSslContext();
		SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
		SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(
				socket, 
				host, 
				port, 
				autoClose); 
		CipherSuites enabledCipherSuites = properties.getValue(
				PropertySpec.SSL_ENABLED_CIPHER_SUITES, CipherSuites.class);
		String[] cipherSuites = enabledCipherSuites.toStringArray();
		if (cipherSuites.length > 0) {
			sslSocket.setEnabledCipherSuites(cipherSuites);
		}
		Protocols enabledProtocols = properties.getValue(
				PropertySpec.SSL_ENABLED_PROTOCOLS, Protocols.class);
		String[] protocols = enabledProtocols.toStringArray();
		if (protocols.length > 0) {
			sslSocket.setEnabledProtocols(protocols);
		}
		return sslSocket;
	}

}
