package jargyle.server;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import jargyle.common.net.SocketSettings;
import jargyle.common.util.NonnegativeInteger;
import jargyle.server.SocksServerCli.ProcessResult;

public final class SocksServer {

	private static final Logger LOGGER = Logger.getLogger(
			SocksServer.class.getName());
	
	public static void main(final String[] args) {
		/* 
		 * https://stackoverflow.com/questions/50237516/proper-fix-for-java-10-complaining-about-illegal-reflection-access-by-jaxb-impl#50251510
		 */
		System.setProperty(
				"com.sun.xml.bind.v2.bytecode.ClassTailor.noOptimize", "true");
		SocksServerCli socksServerCli = new SocksServerCli();
		ProcessResult processResult = socksServerCli.process(args);
		Configuration configuration = processResult.getConfiguration();
		SocksServer socksServer = new SocksServer(configuration);
		try {
			socksServer.start();
		} catch (IOException e) {
			String message = "Error in starting SocksServer";
			if (e instanceof BindException) {
				message = String.format(
						"Unable to listen on port %s at %s", 
						configuration.getSettings().getLastValue(
								SettingSpec.PORT, Port.class),
						configuration.getSettings().getLastValue(
								SettingSpec.HOST, Host.class));
			}
			LOGGER.log(
					Level.SEVERE, 
					message, 
					e);
			System.exit(-1);
		}
		LOGGER.info(String.format(
				"Listening on port %s at %s", 
				socksServer.getPort(),
				socksServer.getHost()));
	}
	
	private final int backlog;
	private final Configuration configuration;
	private ExecutorService executor;
	private final Host host;
	private Port port;
	private ServerSocket serverSocket;
	private final SocketSettings socketSettings;
	private boolean started;
	private boolean stopped;
	
	public SocksServer(final Configuration config) {
		this.backlog = config.getSettings().getLastValue(
				SettingSpec.BACKLOG, NonnegativeInteger.class).intValue();
		this.configuration = config;
		this.executor = null;
		this.host = config.getSettings().getLastValue(
				SettingSpec.HOST, Host.class);
		this.port = config.getSettings().getLastValue(
				SettingSpec.PORT, Port.class);
		this.serverSocket = null;
		this.socketSettings = config.getSettings().getLastValue(
				SettingSpec.SOCKET_SETTINGS, SocketSettings.class);
		this.started = false;
		this.stopped = true;
	}
	
	public Configuration getConfiguration() {
		return this.configuration;
	}
	
	public Host getHost() {
		return this.host;
	}
	
	public Port getPort() {
		return this.port;
	}
	
	public boolean isStarted() {
		return this.started;
	}
	
	public boolean isStopped() {
		return this.stopped;
	}
	
	public void start() throws IOException {
		if (this.started) {
			throw new IllegalStateException("SocksServer already started");
		}
		this.serverSocket = new ServerSocket();
		this.socketSettings.applyTo(this.serverSocket);
		this.serverSocket.bind(new InetSocketAddress(
				this.host.toInetAddress(), this.port.intValue()), this.backlog);
		this.port = Port.newInstance(this.serverSocket.getLocalPort());
		this.executor = Executors.newSingleThreadExecutor();
		this.executor.execute(new Listener(
				this.serverSocket, this.configuration));
		this.started = true;
		this.stopped = false;
	}
	
	public void stop() throws IOException {
		if (this.stopped) {
			throw new IllegalStateException("SocksServer already stopped");
		}
		this.port = this.configuration.getSettings().getLastValue(
				SettingSpec.PORT, Port.class);		
		this.serverSocket.close();
		this.serverSocket = null;
		this.executor.shutdownNow();
		this.executor = null;
		this.started = false;
		this.stopped = true;
	}

}
