package jargyle.net.socks.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Objects;

import jargyle.net.Host;
import jargyle.net.NetFactory;
import jargyle.net.Port;
import jargyle.net.SocketInterface;
import jargyle.util.PositiveInteger;

public abstract class SocksClient {

	public static SocksClient newInstance() {
		SocksServerUri socksServerUri = SocksServerUri.newInstance();
		SocksClient socksClient = null;
		if (socksServerUri != null) {
			socksClient = socksServerUri.newSocksClient(Properties.newInstance());
		}
		return socksClient;
	}

	private final Properties properties;
	private final SocksServerUri socksServerUri;
	private final SslWrapper sslWrapper;	
		
	protected SocksClient(final SocksServerUri serverUri, final Properties props) {
		Objects.requireNonNull(
				serverUri, "SOCKS server URI must not be null");
		Objects.requireNonNull(props, "Properties must not be null");
		this.properties = props;
		this.socksServerUri = serverUri;
		this.sslWrapper = new SslWrapper(props);		
	}
	
	public SocketInterface connectToSocksServerWith(
			final SocketInterface socketInterface) throws IOException {
		return this.connectToSocksServerWith(
				socketInterface, 
				this.properties.getValue(PropertySpec.CONNECT_TIMEOUT, 
						PositiveInteger.class).intValue(), 
				false);
	}
	
	public SocketInterface connectToSocksServerWith(
			final SocketInterface socketInterface, 
			final boolean bindBeforeConnect) throws IOException {
		return this.connectToSocksServerWith(
				socketInterface, 
				this.properties.getValue(PropertySpec.CONNECT_TIMEOUT, 
						PositiveInteger.class).intValue(), 
				bindBeforeConnect);
	}
	
	public SocketInterface connectToSocksServerWith(
			final SocketInterface socketInterface, 
			final int timeout) throws IOException {
		return this.connectToSocksServerWith(socketInterface, timeout, false);
	}
	
	public SocketInterface connectToSocksServerWith(
			final SocketInterface socketInterface, 
			final int timeout, 
			final boolean bindBeforeConnect) throws IOException {
		if (bindBeforeConnect) {
			socketInterface.bind(new InetSocketAddress(
					this.properties.getValue(
							PropertySpec.BIND_HOST, Host.class).toInetAddress(), 
					this.properties.getValue(
							PropertySpec.BIND_PORT, Port.class).intValue()));
		}
		SocksServerUri socksServerUri = this.getSocksServerUri();
		socketInterface.connect(
				new InetSocketAddress(
						InetAddress.getByName(socksServerUri.getHost()), 
						socksServerUri.getPort()), 
				timeout);
		return this.sslWrapper.wrapIfSslEnabled(
				socketInterface, 
				this.socksServerUri.getHost(), 
				this.socksServerUri.getPort(), 
				true);
	}
	
	public final Properties getProperties() {
		return this.properties;
	}
	
	public final SocksServerUri getSocksServerUri() {
		return this.socksServerUri;
	}
	
	public final SslWrapper getSslWrapper() {
		return this.sslWrapper;
	}
	
	public abstract NetFactory newNetFactory();
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [socksServerUri=")
			.append(this.socksServerUri)
			.append("]");
		return builder.toString();
	}
	
}