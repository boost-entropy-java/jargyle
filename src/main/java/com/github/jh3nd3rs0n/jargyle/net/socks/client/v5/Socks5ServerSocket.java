package com.github.jh3nd3rs0n.jargyle.net.socks.client.v5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketOption;
import java.net.StandardSocketOptions;
import java.nio.channels.ServerSocketChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.github.jh3nd3rs0n.jargyle.internal.net.InetAddressHelper;
import com.github.jh3nd3rs0n.jargyle.net.FilterSocket;
import com.github.jh3nd3rs0n.jargyle.net.PerformancePreferences;
import com.github.jh3nd3rs0n.jargyle.net.StandardSocketSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.Command;
import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.MethodEncapsulation;
import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.Reply;
import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.Socks5Reply;
import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.Socks5Request;
import com.github.jh3nd3rs0n.jargyle.util.NonnegativeInteger;
import com.github.jh3nd3rs0n.jargyle.util.PositiveInteger;

public final class Socks5ServerSocket extends ServerSocket {

	private static final class AcceptedSocks5Socket extends FilterSocket {
		
		private InetAddress localInetAddress;
		private int localPort;
		private SocketAddress localSocketAddress;
		private InetAddress remoteInetAddress;
		private int remotePort;
		private SocketAddress remoteSocketAddress;
		
		public AcceptedSocks5Socket(
				final Socks5Socket socks5Socket, 
				final InetAddress address, 
				final int port, 
				final InetAddress localAddr, 
				final int localPort) {
			super(socks5Socket);
			this.localInetAddress = localAddr;
			this.localPort = localPort;
			this.localSocketAddress = new InetSocketAddress(
					localAddr, localPort);
			this.remoteInetAddress = address;
			this.remotePort = port;
			this.remoteSocketAddress = new InetSocketAddress(address, port);
		}

		@Override
		public void bind(SocketAddress bindpoint) throws IOException {
			super.bind(bindpoint);
			this.localInetAddress = super.getLocalAddress();
			this.localPort = super.getLocalPort();
			this.localSocketAddress = super.getLocalSocketAddress();
		}
		
		@Override
		public synchronized void close() throws IOException {
			super.close();
			this.localInetAddress = InetAddressHelper.getInet4AllZerosAddress();
			this.localPort = -1;
			this.localSocketAddress = null;
			this.remoteInetAddress = null;
			this.remotePort = 0;
			this.remoteSocketAddress = null;
		}
	
		@Override
		public void connect(SocketAddress endpoint) throws IOException {
			super.connect(endpoint);
			this.remoteInetAddress = super.getInetAddress();
			this.remotePort = super.getPort();
			this.remoteSocketAddress = super.getRemoteSocketAddress();
		}
		
		@Override
		public void connect(
				SocketAddress endpoint,	int timeout) throws IOException {
			super.connect(endpoint, timeout);
			this.remoteInetAddress = super.getInetAddress();
			this.remotePort = super.getPort();
			this.remoteSocketAddress = super.getRemoteSocketAddress();
		}

		@Override
		public InetAddress getInetAddress() {
			return this.remoteInetAddress;
		}

		@Override
		public InetAddress getLocalAddress() {
			return this.localInetAddress;
		}

		@Override
		public int getLocalPort() {
			return this.localPort;
		}

		@Override
		public SocketAddress getLocalSocketAddress() {
			return this.localSocketAddress;
		}

		@Override
		public int getPort() {
			return this.remotePort;
		}

		@Override
		public SocketAddress getRemoteSocketAddress() {
			return this.remoteSocketAddress;
		}
		
	}
	
	private static abstract class ServerSocketOptionHelper<T> {
		
		private static final Map<SocketOption<?>, ServerSocketOptionHelper<?>> SERVER_SOCKET_OPTION_HELPER_MAP =
				new HashMap<SocketOption<?>, ServerSocketOptionHelper<?>>();
		
		private static final Set<SocketOption<?>> SUPPORTED_SOCKET_OPTIONS =
				new HashSet<SocketOption<?>>();
		
		@SuppressWarnings("unused")
		private static final ServerSocketOptionHelper<Integer> SO_RCVBUF_SERVER_SOCKET_OPTION_HELPER = new ServerSocketOptionHelper<Integer>(
				StandardSocketOptions.SO_RCVBUF) {

					@Override
					public Integer getOption(
							final ServerSocket serverSocket) throws IOException {
						return Integer.valueOf(serverSocket.getReceiveBufferSize());
					}

					@Override
					public ServerSocket setOption(
							final Integer value,
							final ServerSocket serverSocket) throws IOException {
						serverSocket.setReceiveBufferSize(value.intValue());
						return serverSocket;
					}
			
		};
		
		@SuppressWarnings("unused")
		private static final ServerSocketOptionHelper<Boolean> SO_REUSEADDR_SERVER_SOCKET_OPTION_HELPER = new ServerSocketOptionHelper<Boolean>(
				StandardSocketOptions.SO_REUSEADDR) {

					@Override
					public Boolean getOption(
							final ServerSocket serverSocket) throws IOException {
						return Boolean.valueOf(serverSocket.getReuseAddress());
					}

					@Override
					public ServerSocket setOption(
							final Boolean value,
							final ServerSocket serverSocket) throws IOException {
						serverSocket.setReuseAddress(value.booleanValue());
						return serverSocket;
					}
			
		};
		
		public static <T> T getSocketOption(
				final SocketOption<T> name,
				final ServerSocket serverSocket) throws IOException {
			Objects.requireNonNull(name);
			ServerSocketOptionHelper<?> serverSocketOptionHelper =
					SERVER_SOCKET_OPTION_HELPER_MAP.get(name);
			if (serverSocketOptionHelper == null) {
				throw new UnsupportedOperationException();
			}
			@SuppressWarnings("unchecked")
			ServerSocketOptionHelper<T> serverSockOptionHelper = 
					(ServerSocketOptionHelper<T>) serverSocketOptionHelper;
			return serverSockOptionHelper.getOption(serverSocket);
		}
		
		public static <T> ServerSocket setSocketOption(
				final SocketOption<T> name,
				final T value,
				final ServerSocket serverSocket) throws IOException {
			Objects.requireNonNull(name);
			ServerSocketOptionHelper<?> serverSocketOptionHelper =
					SERVER_SOCKET_OPTION_HELPER_MAP.get(name);
			if (serverSocketOptionHelper == null) {
				throw new UnsupportedOperationException();
			}
			@SuppressWarnings("unchecked")
			ServerSocketOptionHelper<T> serverSockOptionHelper = 
					(ServerSocketOptionHelper<T>) serverSocketOptionHelper;
			return serverSockOptionHelper.setOption(value, serverSocket);
		}
		
		public static Set<SocketOption<?>> supportedSocketOptions() {
			return Collections.unmodifiableSet(SUPPORTED_SOCKET_OPTIONS);
		}
		
		private ServerSocketOptionHelper(final SocketOption<T> sockOption) {
			SERVER_SOCKET_OPTION_HELPER_MAP.put(sockOption, this);
			SUPPORTED_SOCKET_OPTIONS.add(sockOption);
		}
		
		public abstract T getOption(
				final ServerSocket serverSocket) throws IOException;
		
		public abstract ServerSocket setOption(
				final T value,
				final ServerSocket serverSocket) throws IOException;
		
	}
	
	private static final class Socks5ServerSocketImpl {
		
		private static final int DEFAULT_BACKLOG = 50;
		
		private boolean bound;
		private boolean closed;
		private InetAddress localInetAddress;
		private int localPort;
		private SocketAddress localSocketAddress;
		private Socket originalSocket;
		private Socket socket;
		private SocketSettings socketSettings;
		private boolean socks5Bound;
		private final Socks5Client socks5Client;
		
		public Socks5ServerSocketImpl(
				final Socks5Client client) throws IOException {
			Socket originalSock = client.newInternalSocket();
			this.bound = false;
			this.closed = false;
			this.localInetAddress = null;
			this.localPort = -1;
			this.localSocketAddress = null;
			this.originalSocket = originalSock;
			this.socket = originalSock;
			this.socketSettings = SocketSettings.newInstance();
			this.socks5Client = client;
			this.socks5Bound = false;
		}
		
		public Socket accept() throws IOException {
			if (this.closed) {
				throw new SocketException("socket is closed");
			}
			if (!this.bound) {
				throw new SocketException("socket is not bound");
			}
			if (!this.socks5Bound) {
				this.socks5Bind(this.localPort, this.localInetAddress);
			}
			Socket acceptedSocks5Socket = null;
			try {
				InputStream inputStream = this.socket.getInputStream();
				Socks5Reply socks5Rep = Socks5Reply.newInstanceFrom(inputStream);
				Reply reply = socks5Rep.getReply();
				if (!reply.equals(Reply.SUCCEEDED)) {
					throw new IOException(String.format(
							"received reply: %s", reply));
				}
				acceptedSocks5Socket = new AcceptedSocks5Socket(
						new Socks5Socket(
								this.socks5Client, 
								this.originalSocket, 
								this.socket),
						InetAddress.getByName(
								socks5Rep.getServerBoundAddress()),
						socks5Rep.getServerBoundPort(),
						this.localInetAddress,
						this.localPort);
				Socket newOriginalSocket = this.socks5Client.newInternalSocket();
				this.socketSettings.applyTo(newOriginalSocket);
				this.originalSocket = newOriginalSocket;
				this.socket = newOriginalSocket;
			} finally {
				this.socks5Bound = false;
			}
			return acceptedSocks5Socket;
		}

		public void bind(final SocketAddress endpoint) throws IOException {
			this.bind(endpoint, DEFAULT_BACKLOG);
		}

		public void bind(
				final SocketAddress endpoint, 
				final int backlog) throws IOException {
			if (this.bound) {
				throw new IOException("socket is already bound");
			}
			SocketAddress end = endpoint;
			if (end == null) {
				end = new InetSocketAddress((InetAddress) null, 0);
			} else {
				if (!(end instanceof InetSocketAddress)) {
					throw new IllegalArgumentException(
							"endpoint must be an instance of InetSocketAddress");
				}
			}
			InetSocketAddress inetSocketAddress = (InetSocketAddress) end;
			this.socks5Bind(
					inetSocketAddress.getPort(), 
					inetSocketAddress.getAddress());
		}
		
		public void close() throws IOException {
			this.bound = false;
			this.closed = true;
			this.localInetAddress = null;
			this.localPort = -1;
			this.localSocketAddress = null;
			this.socks5Bound = false;
			this.socket.close();
		}
		
		public int getReceiveBufferSize() throws SocketException {
			return this.socket.getReceiveBufferSize();
		}

		public boolean getReuseAddress() throws SocketException {
			return this.socket.getReuseAddress();
		}

		public int getSoTimeout() throws IOException {
			return this.socket.getSoTimeout();
		}
		
		public void setPerformancePreferences(
				int connectionTime, int latency, int bandwidth) {
			if (!this.bound) {
				PerformancePreferences pp = PerformancePreferences.newInstance(
						connectionTime, latency, bandwidth);
				this.socket.setPerformancePreferences(
						connectionTime, latency, bandwidth);
				this.socketSettings.putValue(
						StandardSocketSettingSpecConstants.PERF_PREF, pp);
			}
		}
		
		public void setReceiveBufferSize(int size) throws SocketException {
			PositiveInteger i = PositiveInteger.newInstance(size);
			this.socket.setReceiveBufferSize(size);
			this.socketSettings.putValue(
					StandardSocketSettingSpecConstants.SO_RCVBUF, i);
		}
		
		public void setReuseAddress(boolean on) throws SocketException {
			Boolean b = Boolean.valueOf(on);
			this.socket.setReuseAddress(on);
			this.socketSettings.putValue(
					StandardSocketSettingSpecConstants.SO_REUSEADDR, b);
		}

		public void setSoTimeout(int timeout) throws SocketException {
			NonnegativeInteger i = NonnegativeInteger.newInstance(timeout);
			this.socket.setSoTimeout(timeout);
			this.socketSettings.putValue(
					StandardSocketSettingSpecConstants.SO_TIMEOUT, i);
		}

		public void socks5Bind(
				final int port, final InetAddress bindAddr) throws IOException {
			if (!this.socket.equals(this.originalSocket)) {
				this.socket = this.originalSocket;
			}
			Socket sock = this.socks5Client.getConnectedInternalSocket(
					this.socket, true);
			MethodEncapsulation methodEncapsulation = 
					this.socks5Client.negotiateMethod(sock);
			Socket sck = methodEncapsulation.getSocket();
			InputStream inStream = sck.getInputStream();
			OutputStream outStream = sck.getOutputStream();
			int prt = port;
			if (prt == -1) {
				prt = 0;
			}
			InetAddress bAddr = bindAddr;
			if (bAddr == null) {
				bAddr = InetAddressHelper.getInet4AllZerosAddress();
			}
			String address = bAddr.getHostAddress();
			Socks5Request socks5Req = Socks5Request.newInstance(
					Command.BIND, 
					address, 
					prt);
			outStream.write(socks5Req.toByteArray());
			outStream.flush();
			Socks5Reply socks5Rep = Socks5Reply.newInstanceFrom(inStream);
			Reply reply = socks5Rep.getReply();
			if (!reply.equals(Reply.SUCCEEDED)) {
				throw new IOException(String.format(
						"received reply: %s", 
						reply));
			}
			this.bound = true;
			this.localInetAddress = 
					InetAddress.getByName(socks5Rep.getServerBoundAddress());
			this.localPort = socks5Rep.getServerBoundPort();
			this.localSocketAddress = new InetSocketAddress(
					this.localInetAddress,
					this.localPort);
			this.socket = sck;
			this.socks5Bound = true;
		}

	}
	
	private final Socks5Client socks5Client;
	private final Socks5ServerSocketImpl socks5ServerSocketImpl;
	
	Socks5ServerSocket(final Socks5Client client) throws IOException {
		this.socks5Client = client;
		this.socks5ServerSocketImpl = new Socks5ServerSocketImpl(client);
	}

	Socks5ServerSocket(
			final Socks5Client client, final int port) throws IOException {
		this.socks5Client = client;
		this.socks5ServerSocketImpl = new Socks5ServerSocketImpl(client);
		this.socks5ServerSocketImpl.socks5Bind(port, null);
	}

	Socks5ServerSocket(
			final Socks5Client client, 
			final int port, 
			final int backlog) throws IOException {
		this(client, port);
	}

	Socks5ServerSocket(
			final Socks5Client client, 
			final int port, 
			final int backlog,
			final InetAddress bindAddr) throws IOException {
		this.socks5Client = client;
		this.socks5ServerSocketImpl = new Socks5ServerSocketImpl(client);
		this.socks5ServerSocketImpl.socks5Bind(port, bindAddr);
	}
	
	@Override
	public Socket accept() throws IOException {
		return this.socks5ServerSocketImpl.accept();
	}

	@Override
	public void bind(SocketAddress endpoint) throws IOException {
		this.socks5ServerSocketImpl.bind(endpoint);
	}

	@Override
	public void bind(SocketAddress endpoint, int backlog) throws IOException {
		this.socks5ServerSocketImpl.bind(endpoint, backlog);
	}

	@Override
	public synchronized void close() throws IOException {
		this.socks5ServerSocketImpl.close();
	}

	@Override
	public ServerSocketChannel getChannel() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public InetAddress getInetAddress() {
		return this.socks5ServerSocketImpl.localInetAddress;
	}

	@Override
	public int getLocalPort() {
		return this.socks5ServerSocketImpl.localPort;
	}

	@Override
	public SocketAddress getLocalSocketAddress() {
		return this.socks5ServerSocketImpl.localSocketAddress;
	}

	@Override
	public <T> T getOption(SocketOption<T> name) throws IOException {
		return ServerSocketOptionHelper.getSocketOption(name, this);
	}

	@Override
	public synchronized int getReceiveBufferSize() throws SocketException {
		return this.socks5ServerSocketImpl.getReceiveBufferSize();
	}

	@Override
	public boolean getReuseAddress() throws SocketException {
		return this.socks5ServerSocketImpl.getReuseAddress();
	}
	
	public Socks5Client getSocks5Client() {
		return this.socks5Client;
	}

	@Override
	public synchronized int getSoTimeout() throws IOException {
		return this.socks5ServerSocketImpl.getSoTimeout();
	}

	@Override
	public boolean isBound() {
		return this.socks5ServerSocketImpl.bound;
	}

	@Override
	public boolean isClosed() {
		return this.socks5ServerSocketImpl.closed;
	}

	@Override
	public <T> ServerSocket setOption(
			SocketOption<T> name, T value) throws IOException {
		return ServerSocketOptionHelper.setSocketOption(name, value, this);
	}

	@Override
	public void setPerformancePreferences(
			int connectionTime, int latency, int bandwidth) {
		this.socks5ServerSocketImpl.setPerformancePreferences(
				connectionTime, latency, bandwidth);
	}

	@Override
	public synchronized void setReceiveBufferSize(
			int size) throws SocketException {
		this.socks5ServerSocketImpl.setReceiveBufferSize(size);
	}

	@Override
	public void setReuseAddress(boolean on) throws SocketException {
		this.socks5ServerSocketImpl.setReuseAddress(on);
	}

	@Override
	public synchronized void setSoTimeout(int timeout) throws SocketException {
		this.socks5ServerSocketImpl.setSoTimeout(timeout);
	}

	@Override
	public Set<SocketOption<?>> supportedOptions() {
		return ServerSocketOptionHelper.supportedSocketOptions();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [socks5Client=")
			.append(this.socks5Client)
			.append(", getLocalSocketAddress()=")
			.append(this.getLocalSocketAddress())
			.append("]");
		return builder.toString();
	}

}
