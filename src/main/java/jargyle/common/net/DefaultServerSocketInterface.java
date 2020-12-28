package jargyle.common.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.net.SocketException;

public final class DefaultServerSocketInterface extends ServerSocketInterface {

	private final ServerSocket serverSocket;
	
	public DefaultServerSocketInterface(final ServerSocket serverSock) {
		this.serverSocket = serverSock;
	}
	
	@Override
	public SocketInterface accept() throws IOException {
		return new DefaultSocketInterface(this.serverSocket.accept());
	}

	@Override
	public void bind(SocketAddress endpoint) throws IOException {
		this.serverSocket.bind(endpoint);
	}

	@Override
	public void bind(SocketAddress endpoint, int backlog) throws IOException {
		this.serverSocket.bind(endpoint, backlog);
	}

	@Override
	public void close() throws IOException {
		this.serverSocket.close();
	}

	@Override
	public InetAddress getInetAddress() {
		return this.serverSocket.getInetAddress();
	}

	@Override
	public int getLocalPort() {
		return this.serverSocket.getLocalPort();
	}

	@Override
	public SocketAddress getLocalSocketAddress() {
		return this.serverSocket.getLocalSocketAddress();
	}

	@Override
	public int getReceiveBufferSize() throws SocketException {
		return this.serverSocket.getReceiveBufferSize();
	}

	@Override
	public boolean getReuseAddress() throws SocketException {
		return this.serverSocket.getReuseAddress();
	}

	@Override
	public int getSoTimeout() throws IOException {
		return this.serverSocket.getSoTimeout();
	}

	@Override
	public boolean isBound() {
		return this.serverSocket.isBound();
	}

	@Override
	public boolean isClosed() {
		return this.serverSocket.isClosed();
	}

	@Override
	public void setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
		this.serverSocket.setPerformancePreferences(connectionTime, latency, bandwidth);
	}

	@Override
	public void setReceiveBufferSize(int size) throws SocketException {
		this.serverSocket.setReceiveBufferSize(size);
	}

	@Override
	public void setReuseAddress(boolean on) throws SocketException {
		this.serverSocket.setReuseAddress(on);
	}

	@Override
	public void setSoTimeout(int timeout) throws SocketException {
		this.serverSocket.setSoTimeout(timeout);
	}

	@Override
	public String toString() {
		return this.serverSocket.toString();
	}

}
