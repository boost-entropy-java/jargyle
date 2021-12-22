package com.github.jh3nd3rs0n.jargyle.client.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketOption;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;
import java.util.Set;

import com.github.jh3nd3rs0n.jargyle.transport.socks5.Command;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.MethodEncapsulation;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Exception;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;

public final class Socks5Socket extends Socket {

	private static final class Socks5SocketImpl {
		
		private boolean connected;
		private Socket originalSocket;
		private InetAddress remoteInetAddress;
		private int remotePort;
		private SocketAddress remoteSocketAddress;
		private Socket socket;
		private final Socks5Client socks5Client;
		
		public Socks5SocketImpl(
				final Socks5Client client,
				final Socket originalSock,
				final Socket sock) {
			this.connected = originalSock.isConnected();
			this.originalSocket = originalSock;
			this.remoteInetAddress = originalSock.getInetAddress();
			this.remotePort = originalSock.getPort();
			this.remoteSocketAddress = originalSock.getRemoteSocketAddress();
			this.socket = (sock == null) ? originalSock : sock;
			this.socks5Client = client;
		}
		
		public void close() throws IOException {
			this.connected = false;
			this.remoteInetAddress = null;
			this.remotePort = 0;
			this.remoteSocketAddress = null;
			this.socket.close();
		}
		
		public void connect(SocketAddress endpoint) throws IOException {
			this.connect(endpoint, 0);
		}
		
		public void connect(
				SocketAddress endpoint,	int timeout) throws IOException {
			if (endpoint == null || !(endpoint instanceof InetSocketAddress)) {
				throw new IllegalArgumentException(
						"endpoint must be an instance of InetSocketAddress");
			}
			InetSocketAddress inetSocketAddress = (InetSocketAddress) endpoint;
			this.socks5Connect(
					inetSocketAddress.getAddress(), 
					inetSocketAddress.getPort(), 
					timeout);
		}

		public void socks5Connect(
				final InetAddress inetAddress,
				final int port,
				final int timeout) throws IOException {
			if (!this.socket.equals(this.originalSocket)) {
				this.socket = this.originalSocket;
			}
			Socket sock = this.socks5Client.getConnectedInternalSocket(
					this.socket, timeout);
			String address = inetAddress.getHostAddress();
			this.socks5Connect(sock, address, port);
		}
		
		public void socks5Connect(
				final Socket connectedSocket,
				final String address,
				final int port) throws IOException {
			MethodEncapsulation methodEncapsulation = 
					this.socks5Client.negotiateMethod(connectedSocket); 
			Socket connectedSock = methodEncapsulation.getSocket(); 
			InputStream inputStream = connectedSock.getInputStream();
			OutputStream outputStream = connectedSock.getOutputStream();
			Socks5Request socks5Req = Socks5Request.newInstance(
					Command.CONNECT, 
					address, 
					port);
			outputStream.write(socks5Req.toByteArray());
			outputStream.flush();
			Socks5Reply socks5Rep = Socks5Reply.newInstanceFrom(inputStream);
			Reply reply = socks5Rep.getReply();
			if (!reply.equals(Reply.SUCCEEDED)) {
				if (reply.equals(Reply.HOST_UNREACHABLE)) {
					throw new UnknownHostException(String.format(
							"unknown host: %s", address));
				}
				throw new Socks5Exception(String.format(
						"received reply: %s", 
						reply));
			}
			this.connected = true;
			this.remoteInetAddress = 
					InetAddress.getByName(socks5Rep.getServerBoundAddress());
			this.remotePort = socks5Rep.getServerBoundPort();
			this.remoteSocketAddress = new InetSocketAddress(
					this.remoteInetAddress,
					this.remotePort);
			this.socket = connectedSock;			
		}
		
	}
	
	private final Socks5Client socks5Client;
	private final Socks5SocketImpl socks5SocketImpl;
	
	Socks5Socket(final Socks5Client client) {
		this.socks5Client = client;
		this.socks5SocketImpl = new Socks5SocketImpl(
				client, client.newInternalSocket(), null); 
	}
	
	Socks5Socket(
			final Socks5Client client, 
			final InetAddress address, 
			final int port) throws IOException {
		this.socks5Client = client;
		this.socks5SocketImpl = new Socks5SocketImpl(
				client, client.newInternalSocket(), null);
		this.socks5SocketImpl.socks5Connect(address, port, 0);
	}
	
	Socks5Socket(
			final Socks5Client client, 
			final InetAddress address, 
			final int port, 
			final InetAddress localAddr, 
			final int localPort) throws IOException {
		this.socks5Client = client;
		this.socks5SocketImpl = new Socks5SocketImpl(
				client, client.newInternalSocket(), null);
		this.socks5SocketImpl.socket.bind(
				new InetSocketAddress(localAddr, localPort));
		this.socks5SocketImpl.socks5Connect(address, port, 0);
	}

	Socks5Socket(
			final Socks5Client client, 
			final Socket originalSock, 
			final Socket sock) {
		this.socks5Client = client;
		this.socks5SocketImpl = new Socks5SocketImpl(client, originalSock, sock);
	}

	Socks5Socket(
			final Socks5Client client, 
			final String host, 
			final int port) throws UnknownHostException, IOException {
		Socket connectedInternalSocket = client.newConnectedInternalSocket();
		this.socks5Client = client;
		this.socks5SocketImpl = new Socks5SocketImpl(
				client, connectedInternalSocket, null);
		this.socks5SocketImpl.socks5Connect(
				connectedInternalSocket, host, port);
	}

	Socks5Socket(
			final Socks5Client client, 
			final String host, 
			final int port, 
			final InetAddress localAddr, 
			final int localPort) throws IOException {
		Socket connectedInternalSocket = client.newConnectedInternalSocket(
				localAddr, localPort);
		this.socks5Client = client;
		this.socks5SocketImpl = new Socks5SocketImpl(
				client, connectedInternalSocket, null);
		this.socks5SocketImpl.socks5Connect(
				connectedInternalSocket, host, port);
	}
	
	@Override
	public void bind(SocketAddress bindpoint) throws IOException {
		this.socks5SocketImpl.socket.bind(bindpoint);
	}

	@Override
	public synchronized void close() throws IOException {
		this.socks5SocketImpl.close();
	}

	@Override
	public void connect(SocketAddress endpoint) throws IOException {
		this.socks5SocketImpl.connect(endpoint);
	}

	@Override
	public void connect(SocketAddress endpoint, int timeout) throws IOException {
		this.socks5SocketImpl.connect(endpoint, timeout);
	}

	@Override
	public SocketChannel getChannel() {
		throw new UnsupportedOperationException();
	}

	@Override
	public InetAddress getInetAddress() {
		return this.socks5SocketImpl.remoteInetAddress;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return this.socks5SocketImpl.socket.getInputStream();
	}

	@Override
	public boolean getKeepAlive() throws SocketException {
		return this.socks5SocketImpl.socket.getKeepAlive();
	}

	@Override
	public InetAddress getLocalAddress() {
		return this.socks5SocketImpl.socket.getLocalAddress();
	}

	@Override
	public int getLocalPort() {
		return this.socks5SocketImpl.socket.getLocalPort();
	}

	@Override
	public SocketAddress getLocalSocketAddress() {
		return this.socks5SocketImpl.socket.getLocalSocketAddress();
	}

	@Override
	public boolean getOOBInline() throws SocketException {
		return this.socks5SocketImpl.socket.getOOBInline();
	}

	@Override
	public <T> T getOption(SocketOption<T> name) throws IOException {
		return this.socks5SocketImpl.socket.getOption(name);
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		return this.socks5SocketImpl.socket.getOutputStream();
	}

	@Override
	public int getPort() {
		return this.socks5SocketImpl.remotePort;
	}

	@Override
	public synchronized int getReceiveBufferSize() throws SocketException {
		return this.socks5SocketImpl.socket.getReceiveBufferSize();
	}

	@Override
	public SocketAddress getRemoteSocketAddress() {
		return this.socks5SocketImpl.remoteSocketAddress;
	}

	@Override
	public boolean getReuseAddress() throws SocketException {
		return this.socks5SocketImpl.socket.getReuseAddress();
	}
	
	@Override
	public synchronized int getSendBufferSize() throws SocketException {
		return this.socks5SocketImpl.socket.getSendBufferSize();
	}

	public Socks5Client getSocks5Client() {
		return this.socks5Client;
	}

	@Override
	public int getSoLinger() throws SocketException {
		return this.socks5SocketImpl.socket.getSoLinger();
	}

	@Override
	public synchronized int getSoTimeout() throws SocketException {
		return this.socks5SocketImpl.socket.getSoTimeout();
	}

	@Override
	public boolean getTcpNoDelay() throws SocketException {
		return this.socks5SocketImpl.socket.getTcpNoDelay();
	}

	@Override
	public int getTrafficClass() throws SocketException {
		return this.socks5SocketImpl.socket.getTrafficClass();
	}

	@Override
	public boolean isBound() {
		return this.socks5SocketImpl.socket.isBound();
	}

	@Override
	public boolean isClosed() {
		return this.socks5SocketImpl.socket.isClosed();
	}

	@Override
	public boolean isConnected() {
		return this.socks5SocketImpl.connected;
	}

	@Override
	public boolean isInputShutdown() {
		return this.socks5SocketImpl.socket.isInputShutdown();
	}

	@Override
	public boolean isOutputShutdown() {
		return this.socks5SocketImpl.socket.isOutputShutdown();
	}

	@Override
	public void sendUrgentData(int data) throws IOException {
		this.socks5SocketImpl.socket.sendUrgentData(data);
	}

	@Override
	public void setKeepAlive(boolean on) throws SocketException {
		this.socks5SocketImpl.socket.setKeepAlive(on);

	}

	@Override
	public void setOOBInline(boolean on) throws SocketException {
		this.socks5SocketImpl.socket.setOOBInline(on);
	}

	@Override
	public <T> Socket setOption(
			SocketOption<T> name, T value) throws IOException {
		this.socks5SocketImpl.socket.setOption(name, value);
		return this;
	}

	@Override
	public void setPerformancePreferences(
			int connectionTime, int latency, int bandwidth) {
		this.socks5SocketImpl.socket.setPerformancePreferences(
				connectionTime, latency, bandwidth);
	}

	@Override
	public synchronized void setReceiveBufferSize(int size) throws SocketException {
		this.socks5SocketImpl.socket.setReceiveBufferSize(size);
	}

	@Override
	public void setReuseAddress(boolean on) throws SocketException {
		this.socks5SocketImpl.socket.setReuseAddress(on);

	}

	@Override
	public synchronized void setSendBufferSize(int size) throws SocketException {
		this.socks5SocketImpl.socket.setSendBufferSize(size);
	}

	@Override
	public void setSoLinger(boolean on, int linger) throws SocketException {
		this.socks5SocketImpl.socket.setSoLinger(on, linger);
	}

	@Override
	public synchronized void setSoTimeout(int timeout) throws SocketException {
		this.socks5SocketImpl.socket.setSoTimeout(timeout);
	}

	@Override
	public void setTcpNoDelay(boolean on) throws SocketException {
		this.socks5SocketImpl.socket.setTcpNoDelay(on);
	}

	@Override
	public void setTrafficClass(int tc) throws SocketException {
		this.socks5SocketImpl.socket.setTrafficClass(tc);
	}

	@Override
	public void shutdownInput() throws IOException {
		this.socks5SocketImpl.socket.shutdownInput();
	}

	@Override
	public void shutdownOutput() throws IOException {
		this.socks5SocketImpl.socket.shutdownOutput();
	}

	@Override
	public Set<SocketOption<?>> supportedOptions() {
		return this.socks5SocketImpl.socket.supportedOptions();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [socks5Client=")
			.append(this.socks5Client)
			.append(", getLocalSocketAddress()=")
			.append(this.getLocalSocketAddress())
			.append(", getRemoteSocketAddress()=")
			.append(this.getRemoteSocketAddress())
			.append("]");
		return builder.toString();
	}

}
