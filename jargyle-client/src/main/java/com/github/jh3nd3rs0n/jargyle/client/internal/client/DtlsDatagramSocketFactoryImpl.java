package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import com.github.jh3nd3rs0n.jargyle.client.DtlsPropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocket;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslContextHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.TrustManagerHelper;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramSocket;
import java.nio.file.Files;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

final class DtlsDatagramSocketFactoryImpl extends DtlsDatagramSocketFactory {
	
	public static boolean isDtlsEnabled(final Properties props) {
		return props.getValue(
				DtlsPropertySpecConstants.DTLS_ENABLED).booleanValue();
	}
	
	private SSLContext dtlsContext;
	private final ReentrantLock lock;
	private final Properties properties;
		
	public DtlsDatagramSocketFactoryImpl(final Properties props) {
		this.dtlsContext = null;
		this.lock = new ReentrantLock();
		this.properties = props;
	}
	
	private SSLContext getDtlsContext() throws IOException {
		TrustManager[] trustManagers = null;
		InputStream trustStoreInputStream = this.properties.getValue(
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_INPUT_STREAM);
		File trustStoreFile = this.properties.getValue(
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_FILE);
		if (trustStoreFile != null) {
			trustStoreInputStream = Files.newInputStream(
					trustStoreFile.toPath());
		}
		if (trustStoreInputStream != null) {
			char[] trustStorePassword = this.properties.getValue(
					DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD).getPassword();
			String trustStoreType = this.properties.getValue(
					DtlsPropertySpecConstants.DTLS_TRUST_STORE_TYPE);
			trustManagers = TrustManagerHelper.getTrustManagers(
					trustStoreInputStream, trustStorePassword, trustStoreType);
			Arrays.fill(trustStorePassword, '\0');
		}
		SSLContext context = null;
		try {
			context = SslContextHelper.getSslContext(
					this.properties.getValue(
							DtlsPropertySpecConstants.DTLS_PROTOCOL), 
					null,
					trustManagers);
		} catch (KeyManagementException e) {
			throw new IOException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
		return context;		
	}
	
	@Override
	public DatagramSocket newDatagramSocket(
			final DatagramSocket datagramSocket) throws IOException {
		this.lock.lock();
		try {
			if (this.dtlsContext == null) {
				this.dtlsContext = this.getDtlsContext();
			}
		} finally {
			this.lock.unlock();
		}
		DtlsDatagramSocketFactory factory = 
				DtlsDatagramSocketFactory.newInstance(this.dtlsContext);
		DtlsDatagramSocket dtlsDatagramSocket = 
				(DtlsDatagramSocket) factory.newDatagramSocket(datagramSocket);
		dtlsDatagramSocket.setUseClientMode(true);
		CommaSeparatedValues enabledCipherSuites = this.properties.getValue(
				DtlsPropertySpecConstants.DTLS_ENABLED_CIPHER_SUITES);
		if (enabledCipherSuites != null) {
			dtlsDatagramSocket.setEnabledCipherSuites(
					enabledCipherSuites.toArray());
		}
		CommaSeparatedValues enabledProtocols = this.properties.getValue(
				DtlsPropertySpecConstants.DTLS_ENABLED_PROTOCOLS);
		if (enabledProtocols != null) {
			dtlsDatagramSocket.setEnabledProtocols(
					enabledProtocols.toArray());
		}
		PositiveInteger wrappedReceiveBufferSize = this.properties.getValue(
				DtlsPropertySpecConstants.DTLS_WRAPPED_RECEIVE_BUFFER_SIZE);
		if (wrappedReceiveBufferSize != null) {
			dtlsDatagramSocket.setWrappedReceiveBufferSize(
					wrappedReceiveBufferSize.intValue());
		}
		return dtlsDatagramSocket;
	}

}
