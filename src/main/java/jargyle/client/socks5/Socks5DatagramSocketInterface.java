package jargyle.client.socks5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import jargyle.client.PropertySpec;
import jargyle.common.net.DatagramSocketInterface;
import jargyle.common.net.DefaultDatagramSocketInterface;
import jargyle.common.net.DefaultSocketInterface;
import jargyle.common.net.SocketInterface;
import jargyle.common.net.SocketSettings;
import jargyle.common.net.socks5.AddressType;
import jargyle.common.net.socks5.Command;
import jargyle.common.net.socks5.Reply;
import jargyle.common.net.socks5.Socks5Reply;
import jargyle.common.net.socks5.Socks5Request;
import jargyle.common.net.socks5.UdpRequestHeader;
import jargyle.common.net.socks5.gssapiauth.GssDatagramSocketInterface;
import jargyle.common.net.socks5.gssapiauth.GssSocketInterface;

public final class Socks5DatagramSocketInterface 
	extends DatagramSocketInterface {

	private static final class Socks5DatagramSocketInterfaceImpl {
		
		private boolean associated;
		private boolean connected;
		private DatagramSocketInterface datagramSocketInterface;
		private DatagramSocketInterface originalDatagramSocketInterface;
		private SocketInterface originalSocketInterface;
		private InetAddress remoteInetAddress;
		private int remotePort;
		private SocketAddress remoteSocketAddress;
		private SocketInterface socketInterface;
		private final Socks5Client socks5Client;
		private InetAddress udpRelayServerInetAddress;
		private int udpRelayServerPort;
		
		public Socks5DatagramSocketInterfaceImpl(
				final Socks5Client client, 
				final DatagramSocket datagramSocket) throws SocketException {
			SocketInterface sockInterface = new DefaultSocketInterface(
					new Socket());
			SocketSettings socketSettings = client.getProperties().getValue(
					PropertySpec.SOCKET_SETTINGS, SocketSettings.class);
			socketSettings.applyTo(sockInterface);
			DatagramSocketInterface datagramSockInterface = 
					new DefaultDatagramSocketInterface(datagramSocket);
			this.associated = false;
			this.connected = false;
			this.datagramSocketInterface = datagramSockInterface;
			this.originalDatagramSocketInterface = datagramSocketInterface;
			this.originalSocketInterface = sockInterface;
			this.remoteInetAddress = null;
			this.remotePort = -1;
			this.remoteSocketAddress = null;
			this.socketInterface = sockInterface;
			this.socks5Client = client;
			this.udpRelayServerInetAddress = null;
			this.udpRelayServerPort = -1;
		}
		
		public void close() {
			try {
				this.socketInterface.close();
			} catch (IOException e) {
				throw new AssertionError(e);
			}
			this.datagramSocketInterface.close();
			this.associated = false;
			this.udpRelayServerInetAddress = null;
			this.udpRelayServerPort = -1;
		}
		
		public void connect(InetAddress address, int port) {
			if (address == null) {
				throw new IllegalArgumentException("inetAddress must not be null");
			}
			if (port < 1 || port > 0xffff) {
				throw new IllegalArgumentException("port is out of range");
			}
			this.connected = true;
			this.remoteInetAddress = address;
			this.remotePort = port;
			this.remoteSocketAddress = new InetSocketAddress(address, port);
		}
		
		public void connect(SocketAddress addr) throws SocketException {
			if (addr == null || !(addr instanceof InetSocketAddress)) {
				throw new IllegalArgumentException(
						"address must be an instance of InetSocketAddress");
			}
			InetSocketAddress inetSocketAddress = (InetSocketAddress) addr;
			this.connect(
					inetSocketAddress.getAddress(), 
					inetSocketAddress.getPort());
		}
		
		public void disconnect() {
			this.connected = false;
			this.remoteInetAddress = null;
			this.remotePort = -1;
			this.remoteSocketAddress = null;
		}
		
		public void receive(final DatagramPacket p) throws IOException {
			this.datagramSocketInterface.receive(p);
			UdpRequestHeader header = null; 
			try {
				header = UdpRequestHeader.newInstance(p.getData());
			} catch (IllegalArgumentException e) {
				throw new IOException(
						"error in parsing UDP header request", e);
			}
			byte[] userData = header.getUserData();
			InetAddress inetAddress = null;
			try {
				inetAddress = InetAddress.getByName(
						header.getDesiredDestinationAddress());
			} catch (UnknownHostException e) {
				throw new IOException("error in determining address", e);
			}
			int inetPort = header.getDesiredDestinationPort();
			p.setData(userData, 0, userData.length);
			p.setLength(userData.length);
			p.setAddress(inetAddress);
			p.setPort(inetPort);		
		}
		
		public void send(final DatagramPacket p) throws IOException {
			if (this.datagramSocketInterface.isClosed()) {
				throw new SocketException("socket is closed");
			}
			SocketAddress socketAddress = p.getSocketAddress();
			if (socketAddress != null 
					&& this.connected 
					&& !this.remoteSocketAddress.equals(socketAddress)) {
				throw new IllegalArgumentException(
						"packet address and connected socket address must be the same");
			}
			if (!this.associated) {
				this.socks5UdpAssociate();
			}
			String address = p.getAddress().getHostAddress();
			int port = p.getPort();
			AddressType addressType = AddressType.get(address);
			byte[] headerBytes = UdpRequestHeader.newInstance(
					0,
					addressType,
					address,
					port,
					p.getData()).toByteArray();
			p.setData(headerBytes, 0, headerBytes.length);
			p.setLength(headerBytes.length);
			p.setAddress(this.udpRelayServerInetAddress);
			p.setPort(this.udpRelayServerPort);
			this.datagramSocketInterface.send(p);
		}
		
		public void socks5UdpAssociate() throws IOException {
			String address = null;
			int port = -1;
			if (!this.connected) {
				address = AddressType.IP_V4_ADDRESS.getWildcardAddress();
				port = 0;
			} else {
				address = this.remoteInetAddress.getHostAddress();
				port = this.remotePort;
			}
			if (this.associated) {
				this.datagramSocketInterface = this.originalDatagramSocketInterface;
				this.socketInterface = this.originalSocketInterface;
			}
			SocketInterface sockInterface = this.socks5Client.connectToSocksServerWith(
					this.socketInterface, true);
			InputStream inputStream = sockInterface.getInputStream();
			OutputStream outputStream = sockInterface.getOutputStream();
			AddressType addressType = AddressType.get(address);
			Socks5Request socks5Req = Socks5Request.newInstance(
					Command.UDP_ASSOCIATE, 
					addressType, 
					address, 
					port);
			outputStream.write(socks5Req.toByteArray());
			outputStream.flush();
			Socks5Reply socks5Rep = Socks5Reply.newInstanceFrom(inputStream);
			Reply reply = socks5Rep.getReply();
			if (!reply.equals(Reply.SUCCEEDED)) {
				throw new IOException(String.format(
						"received reply: %s", reply));
			}
			DatagramSocketInterface datagramSockInterface = 
					this.datagramSocketInterface;
			/*
			if (this.socks5Client.getProperties().getValue(
					PropertySpec.SSL_ENABLED, Boolean.class).booleanValue()) {
				// TODO DtlsDatagramSocketInterface
			}
			*/
			if (sockInterface instanceof GssSocketInterface) {
				GssSocketInterface gssSocketInterface = 
						(GssSocketInterface) sockInterface;
				datagramSockInterface = new GssDatagramSocketInterface(
						datagramSockInterface,
						gssSocketInterface.getGSSContext(),
						gssSocketInterface.getMessageProp());
			}
			this.datagramSocketInterface = datagramSockInterface;
			this.udpRelayServerInetAddress = InetAddress.getByName(
					socks5Rep.getServerBoundAddress());
			this.udpRelayServerPort = socks5Rep.getServerBoundPort();
			this.socketInterface = sockInterface;
			this.associated = true;			
		}
		
	}
	
	private final Socks5DatagramSocketInterfaceImpl socks5DatagramSocketInterfaceImpl;
	
	public Socks5DatagramSocketInterface(
			final Socks5Client client) throws SocketException {
		this.socks5DatagramSocketInterfaceImpl = 
				new Socks5DatagramSocketInterfaceImpl(
						client, 
						new DatagramSocket()); 
	}

	public Socks5DatagramSocketInterface(
			final Socks5Client client, final int port) throws SocketException {
		this.socks5DatagramSocketInterfaceImpl = 
				new Socks5DatagramSocketInterfaceImpl(
						client, 
						new DatagramSocket(port));
	}

	public Socks5DatagramSocketInterface(
			final Socks5Client client, 
			final int port, 
			final InetAddress laddr) throws SocketException {
		this.socks5DatagramSocketInterfaceImpl = 
				new Socks5DatagramSocketInterfaceImpl(
						client, 
						new DatagramSocket(port, laddr));
	}

	public Socks5DatagramSocketInterface(
			final Socks5Client client, 
			final SocketAddress bindaddr) throws SocketException {
		this.socks5DatagramSocketInterfaceImpl = 
				new Socks5DatagramSocketInterfaceImpl(
						client, 
						new DatagramSocket(bindaddr));
	}
	
	@Override
	public void bind(SocketAddress addr) throws SocketException {
		this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.bind(addr);
	}

	@Override
	public void close() {
		this.socks5DatagramSocketInterfaceImpl.close();
	}

	@Override
	public void connect(InetAddress address, int port) {
		this.socks5DatagramSocketInterfaceImpl.connect(address, port);
	}

	@Override
	public void connect(SocketAddress addr) throws SocketException {
		this.socks5DatagramSocketInterfaceImpl.connect(addr);
	}

	@Override
	public void disconnect() {
		this.socks5DatagramSocketInterfaceImpl.disconnect();
	}

	@Override
	public boolean getBroadcast() throws SocketException {
		return this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.getBroadcast();
	}

	@Override
	public InetAddress getInetAddress() {
		return this.socks5DatagramSocketInterfaceImpl.remoteInetAddress;
	}

	@Override
	public InetAddress getLocalAddress() {
		return this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.getLocalAddress();
	}

	@Override
	public int getLocalPort() {
		return this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.getLocalPort();
	}

	@Override
	public SocketAddress getLocalSocketAddress() {
		return this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.getLocalSocketAddress();
	}

	@Override
	public int getPort() {
		return this.socks5DatagramSocketInterfaceImpl.remotePort;
	}

	@Override
	public int getReceiveBufferSize() throws SocketException {
		return this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.getReceiveBufferSize();
	}

	@Override
	public SocketAddress getRemoteSocketAddress() {
		return this.socks5DatagramSocketInterfaceImpl.remoteSocketAddress;
	}

	@Override
	public boolean getReuseAddress() throws SocketException {
		return this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.getReuseAddress();
	}

	@Override
	public int getSendBufferSize() throws SocketException {
		return this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.getSendBufferSize();
	}

	@Override
	public int getSoTimeout() throws SocketException {
		return this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.getSoTimeout();
	}

	@Override
	public int getTrafficClass() throws SocketException {
		return this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.getTrafficClass();
	}

	@Override
	public boolean isBound() {
		return this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.isBound();
	}

	@Override
	public boolean isClosed() {
		return this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.isClosed();
	}

	@Override
	public boolean isConnected() {
		return this.socks5DatagramSocketInterfaceImpl.connected;
	}

	@Override
	public void receive(DatagramPacket p) throws IOException {
		this.socks5DatagramSocketInterfaceImpl.receive(p);
	}

	@Override
	public void send(DatagramPacket p) throws IOException {
		this.socks5DatagramSocketInterfaceImpl.send(p);
	}

	@Override
	public void setBroadcast(boolean on) throws SocketException {
		this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.setBroadcast(on);
	}

	@Override
	public void setReceiveBufferSize(int size) throws SocketException {
		this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.setReceiveBufferSize(size);
	}

	@Override
	public void setReuseAddress(boolean on) throws SocketException {
		this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.setReuseAddress(on);
	}

	@Override
	public void setSendBufferSize(int size) throws SocketException {
		this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.setSendBufferSize(size);
	}

	@Override
	public void setSoTimeout(int timeout) throws SocketException {
		this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.setSoTimeout(timeout);
	}

	@Override
	public void setTrafficClass(int tc) throws SocketException {
		this.socks5DatagramSocketInterfaceImpl.datagramSocketInterface.setTrafficClass(tc);

	}

}
