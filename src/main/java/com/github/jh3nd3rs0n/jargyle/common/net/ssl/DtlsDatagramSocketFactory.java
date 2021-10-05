package com.github.jh3nd3rs0n.jargyle.common.net.ssl;

import java.io.IOException;
import java.net.DatagramSocket;

import javax.net.ssl.SSLContext;

public abstract class DtlsDatagramSocketFactory {

	public static DtlsDatagramSocketFactory newInstance(
			final SSLContext dtlsContext) {
		return new DefaultDtlsDatagramSocketFactory(dtlsContext);
	}
	
	public abstract DatagramSocket newDatagramSocket(
			final DatagramSocket datagramSocket,
			final String peerHost,
			final int peerPort) throws IOException;
	
}
