package com.github.jh3nd3rs0n.jargyle.client.socks5;

import com.github.jh3nd3rs0n.jargyle.common.net.HostIpv4Address;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.common.net.StandardSocketSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.internal.net.FilterSocket;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.*;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.address.impl.DomainName;

import java.io.IOException;
import java.net.*;
import java.nio.channels.ServerSocketChannel;
import java.util.*;

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
			this.localInetAddress = HostIpv4Address.getAllZerosInet4Address();
			this.localPort = -1;
			this.localSocketAddress = null;
			this.remoteInetAddress = null;
			this.remotePort = 0;
			this.remoteSocketAddress = null;
			super.close();
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
		
		private static final class ServerSocketOptionHelpers {
			
			private final Map<SocketOption<?>, ServerSocketOptionHelper<?>> serverSocketOptionHelpersMap;
			private final Set<SocketOption<?>> supportedSocketOptions;
			
			public ServerSocketOptionHelpers() {
				this.serverSocketOptionHelpersMap = 
						new HashMap<SocketOption<?>, ServerSocketOptionHelper<?>>();
				this.supportedSocketOptions = new HashSet<SocketOption<?>>();
			}
			
			public void add(final ServerSocketOptionHelper<?> value) {
				SocketOption<?> valueSocketOption = value.getSocketOption();
				this.serverSocketOptionHelpersMap.put(
						valueSocketOption, value);
				this.supportedSocketOptions.add(valueSocketOption);
			}
			
			public Set<SocketOption<?>> getSupportedSocketOptions() {
				return Collections.unmodifiableSet(this.supportedSocketOptions);
			}
			
			public Map<SocketOption<?>, ServerSocketOptionHelper<?>> toMap() {
				return Collections.unmodifiableMap(
						this.serverSocketOptionHelpersMap);
			}
		}
		
		private static final class SoRcvbufServerSocketOptionHelper 
			extends ServerSocketOptionHelper<Integer> {
			
			public SoRcvbufServerSocketOptionHelper() {
				super(StandardSocketOptions.SO_RCVBUF);
			}
			
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
			
		}
		
		private static final class SoReuseaddrServerSocketOptionHelper 
			extends ServerSocketOptionHelper<Boolean> {
			
			public SoReuseaddrServerSocketOptionHelper() {
				super(StandardSocketOptions.SO_REUSEADDR);
			}
			
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
			
		}
		
		private static final Map<SocketOption<?>, ServerSocketOptionHelper<?>> SERVER_SOCKET_OPTION_HELPERS_MAP;
		
		private static final Set<SocketOption<?>> SUPPORTED_SOCKET_OPTIONS;
		
		static {
			ServerSocketOptionHelpers serverSocketOptionHelpers = 
					new ServerSocketOptionHelpers(); 
			serverSocketOptionHelpers.add(
					new SoRcvbufServerSocketOptionHelper());
			serverSocketOptionHelpers.add(
					new SoReuseaddrServerSocketOptionHelper());
			SERVER_SOCKET_OPTION_HELPERS_MAP = serverSocketOptionHelpers.toMap();
			SUPPORTED_SOCKET_OPTIONS = 
					serverSocketOptionHelpers.getSupportedSocketOptions();
		}
		
		public static <T> T getSocketOption(
				final SocketOption<T> name,
				final ServerSocket serverSocket) throws IOException {
			Objects.requireNonNull(name);
			ServerSocketOptionHelper<?> serverSocketOptionHelper =
					SERVER_SOCKET_OPTION_HELPERS_MAP.get(name);
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
					SERVER_SOCKET_OPTION_HELPERS_MAP.get(name);
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
		
		private final SocketOption<T> socketOption;
		
		private ServerSocketOptionHelper(final SocketOption<T> sockOption) {
			this.socketOption = sockOption;
		}
		
		public abstract T getOption(
				final ServerSocket serverSocket) throws IOException;
		
		public SocketOption<T> getSocketOption() {
			return this.socketOption;
		}
		
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
		private Socket socket;
		private final SocketSettings socketSettings;
		private final Socks5ClientAgent socks5ClientAgent;
		private boolean socks5Bound;
		
		public Socks5ServerSocketImpl(
				final Socks5ClientAgent clientAgent) {
			Socket sock = 
					clientAgent.newClientSocketBuilder().newClientSocket();
			this.bound = false;
			this.closed = false;
			this.socks5ClientAgent = clientAgent;
			this.localInetAddress = null;
			this.localPort = -1;
			this.localSocketAddress = null;
			this.socket = sock;
			this.socketSettings = SocketSettings.of();
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
				Reply rep = this.socks5ClientAgent.receiveReply(
						this.socket);
				String serverBoundAddress = 
						rep.getServerBoundAddress().toString();
				int serverBoundPort = rep.getServerBoundPort().intValue();
				if (rep.getServerBoundAddress() instanceof DomainName) {
					throw new Socks5ClientIOException(
							this.socks5ClientAgent.getSocks5Client(),
							String.format(
									"server bound address is not an IP "
									+ "address. actual server bound address "
									+ "is %s", 
									serverBoundAddress));
				}
				acceptedSocks5Socket = new AcceptedSocks5Socket(
						new Socks5Socket(
								this.socks5ClientAgent,
								this.socket),
						InetAddress.getByName(serverBoundAddress),
						serverBoundPort,
						this.localInetAddress,
						this.localPort);
				Socket newSocket = 
						this.socks5ClientAgent.newClientSocketBuilder()
								.newClientSocket();
				this.socketSettings.applyTo(newSocket);
				this.socket = newSocket;
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
				this.socket.setPerformancePreferences(
						connectionTime, latency, bandwidth);
			}
		}
		
		public void setReceiveBufferSize(int size) throws SocketException {
			PositiveInteger i = PositiveInteger.valueOf(size);
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
			NonNegativeInteger i = NonNegativeInteger.valueOf(timeout);
			this.socket.setSoTimeout(timeout);
			this.socketSettings.putValue(
					StandardSocketSettingSpecConstants.SO_TIMEOUT, i);
		}

		public void socks5Bind(
				final int port, final InetAddress bindAddr) throws IOException {
			Socket sock = this.socks5ClientAgent.newClientSocketBuilder()
					.proceedToConfigure(this.socket)
					.proceedToConnect()
					.setToBind(true)
					.getConnectedClientSocket();
			Method method = this.socks5ClientAgent.negotiateMethod(sock);
			MethodEncapsulation methodEncapsulation = 
					this.socks5ClientAgent.doMethodSubNegotiation(method, sock);
			Socket sck = methodEncapsulation.getSocket();
			int prt = port;
			if (prt == -1) {
				prt = 0;
			}
			InetAddress bAddr = bindAddr;
			if (bAddr == null) {
				bAddr = HostIpv4Address.getAllZerosInet4Address();
			}
			String address = bAddr.getHostAddress();
			Request req = Request.newInstance(
					Command.BIND, 
					Address.newInstanceFrom(address),
					Port.valueOf(prt));
			this.socks5ClientAgent.sendRequest(req, sck);
			Reply rep = this.socks5ClientAgent.receiveReply(sck);
			String serverBoundAddress = 
					rep.getServerBoundAddress().toString();
			int serverBoundPort = rep.getServerBoundPort().intValue();
			if (rep.getServerBoundAddress() instanceof DomainName) {
				throw new Socks5ClientIOException(
						this.socks5ClientAgent.getSocks5Client(),
						String.format(
								"server bound address is not an IP address. "
								+ "actual server bound address is %s", 
								serverBoundAddress));
			}
			this.bound = true;
			this.localInetAddress = InetAddress.getByName(serverBoundAddress);
			this.localPort = serverBoundPort;
			this.localSocketAddress = new InetSocketAddress(
					this.localInetAddress,
					this.localPort);
			this.socket = sck;
			this.socks5Bound = true;
		}

	}
	
	private final Socks5ClientAgent socks5ClientAgent;
	private final Socks5ServerSocketImpl socks5ServerSocketImpl;
	
	Socks5ServerSocket(
			final Socks5ClientAgent clientAgent) throws IOException {
		this.socks5ClientAgent = clientAgent;
		this.socks5ServerSocketImpl = new Socks5ServerSocketImpl(clientAgent);
	}

	Socks5ServerSocket(
			final Socks5ClientAgent clientAgent,
			final int port) throws IOException {
		Socks5ServerSocketImpl impl = new Socks5ServerSocketImpl(clientAgent);
		try {
			impl.socks5Bind(port, null);
		} catch (IOException e) {
			throw clientAgent.toSocksClientIOException(e);
		}
		this.socks5ClientAgent = clientAgent;
		this.socks5ServerSocketImpl = impl;
	}

	Socks5ServerSocket(
			final Socks5ClientAgent clientAgent,
			final int port, 
			final InetAddress bindAddr) throws IOException {
		Socks5ServerSocketImpl impl = new Socks5ServerSocketImpl(clientAgent);
		try {
			impl.socks5Bind(port, bindAddr);
		} catch (IOException e) {
			throw clientAgent.toSocksClientIOException(e);
		}
		this.socks5ClientAgent = clientAgent;
		this.socks5ServerSocketImpl = impl;
	}
	
	@Override
	public Socket accept() throws IOException {
		try {
			return this.socks5ServerSocketImpl.accept();
		} catch (IOException e) {
			throw this.socks5ClientAgent.toSocksClientIOException(e);
		}
    }

	@Override
	public void bind(SocketAddress endpoint) throws IOException {
		try {
			this.socks5ServerSocketImpl.bind(endpoint);
		} catch (IOException e) {
			throw this.socks5ClientAgent.toSocksClientIOException(e);
		}
	}

	@Override
	public void bind(SocketAddress endpoint, int backlog) throws IOException {
		try {
			this.socks5ServerSocketImpl.bind(endpoint, backlog);
		} catch (IOException e) {
			throw this.socks5ClientAgent.toSocksClientIOException(e);
		}
	}

	@Override
	public synchronized void close() throws IOException {
		try {
			this.socks5ServerSocketImpl.close();
		} catch (IOException e) {
			throw this.socks5ClientAgent.toSocksClientIOException(e);
		}
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
		try {
			return ServerSocketOptionHelper.getSocketOption(name, this);			
		} catch (IOException e) {
			throw this.socks5ClientAgent.toSocksClientIOException(e);
		}
    }

	@Override
	public synchronized int getReceiveBufferSize() throws SocketException {
		try {
			return this.socks5ServerSocketImpl.getReceiveBufferSize();			
		} catch (SocketException e) {
			throw this.socks5ClientAgent.toSocksClientSocketException(e);
		}
    }

	@Override
	public boolean getReuseAddress() throws SocketException {
		try {
			return this.socks5ServerSocketImpl.getReuseAddress();			
		} catch (SocketException e) {
			throw this.socks5ClientAgent.toSocksClientSocketException(e);
		}
    }
	
	public Socks5Client getSocks5Client() {
		return this.socks5ClientAgent.getSocks5Client();
	}

	@Override
	public synchronized int getSoTimeout() throws IOException {
		try {
			return this.socks5ServerSocketImpl.getSoTimeout();			
		} catch (IOException e) {
			throw this.socks5ClientAgent.toSocksClientIOException(e);
		}
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
		try {
			return ServerSocketOptionHelper.setSocketOption(name, value, this);			
		} catch (IOException e) {
			throw this.socks5ClientAgent.toSocksClientIOException(e);
		}
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
		try {
			this.socks5ServerSocketImpl.setReceiveBufferSize(size);			
		} catch (SocketException e) {
			throw this.socks5ClientAgent.toSocksClientSocketException(e);
		}
	}

	@Override
	public void setReuseAddress(boolean on) throws SocketException {
		try {
			this.socks5ServerSocketImpl.setReuseAddress(on);			
		} catch (SocketException e) {
			throw this.socks5ClientAgent.toSocksClientSocketException(e);
		}		
	}

	@Override
	public synchronized void setSoTimeout(int timeout) throws SocketException {
		try {
			this.socks5ServerSocketImpl.setSoTimeout(timeout);			
		} catch (SocketException e) {
			throw this.socks5ClientAgent.toSocksClientSocketException(e);
		}
	}

	@Override
	public Set<SocketOption<?>> supportedOptions() {
		return ServerSocketOptionHelper.supportedSocketOptions();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [getSocks5Client()=")
			.append(this.getSocks5Client())
			.append(", getLocalSocketAddress()=")
			.append(this.getLocalSocketAddress())
			.append("]");
		return builder.toString();
	}

}
