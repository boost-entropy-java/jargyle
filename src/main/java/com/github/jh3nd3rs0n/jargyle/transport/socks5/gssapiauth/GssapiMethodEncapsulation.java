package com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;

import com.github.jh3nd3rs0n.jargyle.transport.socks5.MethodEncapsulation;

public final class GssapiMethodEncapsulation extends MethodEncapsulation {
	
	private final GssSocket socket;
	
	public GssapiMethodEncapsulation(final GssSocket sock) {
		this.socket = sock;
	}
	
	@Override
	public DatagramSocket getDatagramSocket(
			final DatagramSocket datagramSocket) throws IOException {
		return new GssDatagramSocket(
				datagramSocket, 
				this.socket.getGSSContext(), 
				this.socket.getMessageProp());
	}

	@Override
	public Socket getSocket() {
		return this.socket;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [socket=")
			.append(this.socket)
			.append("]");
		return builder.toString();
	}

}
