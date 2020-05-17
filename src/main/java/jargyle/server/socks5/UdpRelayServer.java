package jargyle.server.socks5;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import jargyle.common.net.socks5.AddressType;
import jargyle.common.net.socks5.UdpRequestHeader;

final class UdpRelayServer {
	
	private static final class IncomingPacketsWorker extends PacketsWorker {
		
		public IncomingPacketsWorker(final UdpRelayServer server) {
			super(server);
		}
		
		@Override
		public void run() {
			this.getUdpRelayServer().lastReceiveTime = System.currentTimeMillis();
			while (true) {
				try {
					byte[] buffer = new byte[this.getUdpRelayServer().bufferSize];
					DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
					try {
						this.getServerDatagramSocket().receive(packet);
						this.getUdpRelayServer().lastReceiveTime = System.currentTimeMillis();
					} catch (SocketException e) {
						// socket closed
						break;
					} catch (SocketTimeoutException e) {
						long timeSinceReceive = 
								System.currentTimeMillis() - this.getUdpRelayServer().lastReceiveTime;
						if (timeSinceReceive >= this.getUdpRelayServer().timeout) {
							break;
						}
						continue;
					} catch (IOException e) {
						this.log(
								Level.WARNING, 
								"Error in receiving the packet from the server", 
								e);
						continue;
					}
					this.log(
							Level.FINER, 
							String.format(
									"Packet data received: %s byte(s)", 
									packet.getLength()));
					String address = packet.getAddress().getHostAddress();
					int port = packet.getPort();
					String desiredDestinationAddr = 
							this.getUdpRelayServer().desiredDestinationAddress;
					int desiredDestinationPrt =
							this.getUdpRelayServer().desiredDestinationPort;
					if (desiredDestinationAddr != null 
							&& desiredDestinationPrt > -1) {
						InetAddress desiredDestinationInetAddr = null;
						try {
							desiredDestinationInetAddr = InetAddress.getByName(
									desiredDestinationAddr);
						} catch (UnknownHostException e) {
							this.log(
									Level.WARNING, 
									"Error in determining the IP address from the server", 
									e);
							continue;
						}
						InetAddress inetAddr = null;
						try {
							inetAddr = InetAddress.getByName(address);
						} catch (UnknownHostException e) {
							this.log(
									Level.WARNING, 
									"Error in determining the IP address from the server", 
									e);
							continue;
						}
						if (!desiredDestinationInetAddr.isLoopbackAddress()
								|| !inetAddr.isLoopbackAddress()) {
							if (!desiredDestinationInetAddr.equals(inetAddr)) {
								continue;
							}
						}
						if (desiredDestinationPrt != port) {
							continue;
						}
					}
					AddressType addressType = AddressType.get(address);
					UdpRequestHeader header = UdpRequestHeader.newInstance(
							0,
							addressType,
							address,
							port,
							packet.getData());
					this.log(Level.FINER, header.toString());
					byte[] headerBytes = header.toByteArray();
					InetAddress inetAddress = null;
					try {
						inetAddress = InetAddress.getByName(
								this.getUdpRelayServer().sourceAddress);
					} catch (UnknownHostException e) {
						this.log(
								Level.WARNING, 
								"Error in determining the IP address from the client", 
								e);
						continue;
					}
					int inetPort = this.getUdpRelayServer().sourcePort;
					packet = new DatagramPacket(
							headerBytes, 
							headerBytes.length,
							inetAddress,
							inetPort);
					try {
						this.getClientDatagramSocket().send(packet);
					} catch (SocketException e) {
						// socket closed
						break;
					} catch (IOException e) {
						this.log(
								Level.WARNING, 
								"Error in sending the packet to the client", 
								e);
					}
				} catch (Throwable t) {
					this.log(
							Level.WARNING, 
							"Error occurred in the process of relaying of a "
							+ "packet from the server to the client", 
							t);
				}
			}
			if (!this.getUdpRelayServer().firstPacketsWorkerFinished) {
				this.getUdpRelayServer().firstPacketsWorkerFinished = true;
			} else {
				if (!this.getUdpRelayServer().stopped) {
					this.getUdpRelayServer().stop();
				}				
			}
		}
		
	}
	
	private static final class OutgoingPacketsWorker extends PacketsWorker {
		
		public OutgoingPacketsWorker(final UdpRelayServer server) {
			super(server);
		}
		
		@Override
		public void run() {
			this.getUdpRelayServer().lastReceiveTime = System.currentTimeMillis();
			while (true) {
				try {
					byte[] buffer = new byte[this.getUdpRelayServer().bufferSize];
					DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
					try {
						this.getClientDatagramSocket().receive(packet);
						this.getUdpRelayServer().lastReceiveTime = System.currentTimeMillis();
					} catch (SocketException e) {
						// socket closed
						break;
					} catch (SocketTimeoutException e) {
						long timeSinceReceive = 
								System.currentTimeMillis() - this.getUdpRelayServer().lastReceiveTime;
						if (timeSinceReceive >= this.getUdpRelayServer().timeout) {
							break;
						}
						continue;
					} catch (IOException e) {
						this.log(
								Level.WARNING, 
								"Error in receiving packet from the client", 
								e);
						continue;
					}
					this.log(
							Level.FINER, 
							String.format(
									"Packet data received: %s byte(s)", 
									packet.getLength()));					
					String address = packet.getAddress().getHostAddress();
					int port = packet.getPort();
					InetAddress sourceInetAddr = null;
					try {
						sourceInetAddr = InetAddress.getByName(
								this.getUdpRelayServer().sourceAddress);
					} catch (UnknownHostException e) {
						this.log(
								Level.WARNING, 
								"Error in determining the IP address from the client", 
								e);
						continue;
					}
					InetAddress inetAddr = null;
					try {
						inetAddr = InetAddress.getByName(address);
					} catch (UnknownHostException e) {
						this.log(
								Level.WARNING, 
								"Error in determining the IP address from the client", 
								e);
						continue;
					}
					if (!sourceInetAddr.isLoopbackAddress() 
							|| !inetAddr.isLoopbackAddress()) {
						if (!sourceInetAddr.equals(inetAddr)) {
							continue;
						}
					}
					if (this.getUdpRelayServer().sourcePort == -1) {
						this.getUdpRelayServer().sourcePort = port;
					}
					UdpRequestHeader header = null; 
					try {
						header = UdpRequestHeader.newInstance(packet.getData());
					} catch (IllegalArgumentException e) {
						this.log(
								Level.WARNING, 
								"Error in parsing the UDP header request from the client", 
								e);
						continue;
					}
					this.log(Level.FINER, header.toString());
					if (header.getCurrentFragmentNumber() != 0) {
						continue;
					}
					String desiredDestinationAddr = 
							this.getUdpRelayServer().desiredDestinationAddress;
					int desiredDestinationPrt =
							this.getUdpRelayServer().desiredDestinationPort;
					String desiredDestAddr = 
							header.getDesiredDestinationAddress();
					int desiredDestPrt =
							header.getDesiredDestinationPort();
					if (desiredDestinationAddr != null 
							&& desiredDestinationPrt > -1) {
						InetAddress desiredDestinationInetAddr = null;
						try {
							desiredDestinationInetAddr = InetAddress.getByName(
									desiredDestinationAddr);
						} catch (UnknownHostException e) {
							this.log(
									Level.WARNING, 
									"Error in determining the IP address from the server", 
									e);
							continue;
						}
						InetAddress desiredDestInetAddr = null;
						try {
							desiredDestInetAddr = InetAddress.getByName(
									desiredDestAddr);
						} catch (UnknownHostException e) {
							this.log(
									Level.WARNING, 
									"Error in determining the IP address from the server", 
									e);
							continue;
						}
						if (!desiredDestinationInetAddr.isLoopbackAddress()
								|| !desiredDestInetAddr.isLoopbackAddress()) {
							if (!desiredDestinationInetAddr.equals(
									desiredDestInetAddr)) {
								continue;
							}
						}
						if (desiredDestinationPrt != desiredDestPrt) {
							continue;
						}
					}
					byte[] userData = header.getUserData();
					InetAddress inetAddress = null;
					try {
						inetAddress = InetAddress.getByName(desiredDestAddr);
					} catch (UnknownHostException e) {
						this.log(
								Level.WARNING, 
								"Error in determining the IP address from the server", 
								e);
						continue;
					}
					int inetPort = desiredDestPrt;
					packet = new DatagramPacket(
							userData,
							userData.length,
							inetAddress,
							inetPort);
					try {
						this.getServerDatagramSocket().send(packet);
					} catch (SocketException e) {
						// socket closed
						break;
					} catch (IOException e) {
						this.log(
								Level.WARNING, 
								"Error in sending the packet to the server", 
								e);
					}
				} catch (Throwable t) {
					this.log(
							Level.WARNING, 
							"Error occurred in the process of relaying of a "
							+ "packet from the client to the server", 
							t);
				}
			}
			if (!this.getUdpRelayServer().firstPacketsWorkerFinished) {
				this.getUdpRelayServer().firstPacketsWorkerFinished = true;
			} else {
				if (!this.getUdpRelayServer().stopped) {
					this.getUdpRelayServer().stop();
				}				
			}
		}

	}
	
	private static abstract class PacketsWorker implements Runnable {
		
		private final UdpRelayServer udpRelayServer;
		private final DatagramSocket clientDatagramSocket;
		private final DatagramSocket serverDatagramSocket;

		public PacketsWorker(final UdpRelayServer server) {
			this.udpRelayServer = server;
			this.clientDatagramSocket = server.clientDatagramSocket;
			this.serverDatagramSocket = server.serverDatagramSocket;
		}
		
		protected final DatagramSocket getClientDatagramSocket() {
			return this.clientDatagramSocket;
		}
		
		protected final DatagramSocket getServerDatagramSocket() {
			return this.serverDatagramSocket;
		}
		
		protected final UdpRelayServer getUdpRelayServer() {
			return this.udpRelayServer;
		}
		
		protected final void log(final Level level, final String message) {
			this.udpRelayServer.logger.log(
					level, 
					String.format("%s: %s",	this, message));
		}
		
		protected final void log(
				final Level level, final String message, final Throwable t) {
			this.udpRelayServer.logger.log(
					level, 
					String.format("%s: %s",	this, message),
					t);
		}
		
		@Override
		public abstract void run();

		@Override
		public final String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(this.getClass().getSimpleName())
				.append(" [clientDatagramSocket=")
				.append(this.clientDatagramSocket)
				.append(", serverDatagramSocket=")
				.append(this.serverDatagramSocket)
				.append("]");
			return builder.toString();
		}
		
	}
	

	private final DatagramSocket clientDatagramSocket;
	private final int bufferSize;
	private String desiredDestinationAddress;
	private int desiredDestinationPort;
	private ExecutorService executor;
	private boolean firstPacketsWorkerFinished;
	private long lastReceiveTime;
	private final Logger logger;
	private final DatagramSocket serverDatagramSocket;
	private String sourceAddress;
	private int sourcePort;
	private boolean started;
	private boolean stopped;
	private final int timeout;
	
	public UdpRelayServer(		
			final DatagramSocket clientDatagramSock,
			final DatagramSocket serverDatagramSock,
			final String sourceAddr,
			final String desiredDestinationAddr,
			final int desiredDestinationPrt, 
			final int bffrSize, 
			final int tmt, 
			final Logger lggr) {
		if (clientDatagramSock == null 
				|| serverDatagramSock == null
				|| desiredDestinationAddr == null
				|| lggr == null) {
			throw new NullPointerException();
		}
		UdpRequestHeader.validateDesiredDestinationAddress(
				desiredDestinationAddr);
		UdpRequestHeader.validateDesiredDestinationPort(
				desiredDestinationPrt);
		if (bffrSize < 1) {
			throw new IllegalArgumentException(
					"buffer size must not be less than 1");
		}
		if (tmt < 1) {
			throw new IllegalArgumentException(
					"timeout must not be less than 1");
		}
		String desiredDestAddr = desiredDestinationAddr;
		int desiredDestPrt = desiredDestinationPrt;
		if (!desiredDestAddr.matches("[a-zA-Z1-9]") && desiredDestPrt == 0) {
			desiredDestAddr = null;
			desiredDestPrt = -1;
		}
		this.clientDatagramSocket = clientDatagramSock;
		this.bufferSize = bffrSize;
		this.desiredDestinationAddress = desiredDestAddr;
		this.desiredDestinationPort = desiredDestPrt;
		this.executor = null;
		this.firstPacketsWorkerFinished = false;
		this.lastReceiveTime = 0L;
		this.logger = lggr;
		this.serverDatagramSocket = serverDatagramSock;
		this.sourceAddress = sourceAddr;
		this.sourcePort = -1;
		this.started = false;
		this.stopped = true;
		this.timeout = tmt;
	}
	
	public boolean isStarted() {
		return this.started;
	}
	
	public boolean isStopped() {
		return this.stopped;
	}
	
	public void start() throws IOException {
		if (this.started) {
			throw new IllegalStateException("UdpRelayServer already started");
		}
		this.lastReceiveTime = 0L;
		this.firstPacketsWorkerFinished = false;
		this.executor = Executors.newFixedThreadPool(2);
		this.executor.execute(new IncomingPacketsWorker(this));
		this.executor.execute(new OutgoingPacketsWorker(this));
		this.started = true;
		this.stopped = false;
	}
	
	public void stop() {
		if (this.stopped) {
			throw new IllegalStateException("UdpRelayServer already stopped");
		}
		this.lastReceiveTime = 0L;
		this.firstPacketsWorkerFinished = true;
		this.executor.shutdownNow();
		this.executor = null;
		this.started = false;
		this.stopped = true;
	}
	
}