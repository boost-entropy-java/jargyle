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
	
	public static void main(final String[] args) {
		SocksServerCli socksServerCli = new SocksServerCli();
		ProcessResult processResult = socksServerCli.process(args);
		Configuration configuration = processResult.getConfiguration();
		SocksServer socksServer = new SocksServer(
				configuration, LoggerHolder.LOGGER);
		try {
			socksServer.start();
		} catch (IOException e) {
			String message = "Error in starting SocksServer";
			if (e instanceof BindException) {
				message = String.format(
						"Unable to listen on port %s at address %s", 
						configuration.getSettings().getLastValue(
								SettingSpec.PORT, Port.class),
						configuration.getSettings().getLastValue(
								SettingSpec.ADDRESS, Address.class));
			}
			LoggerHolder.LOGGER.log(
					Level.SEVERE, 
					message, 
					e);
			System.exit(-1);
		}
		LoggerHolder.LOGGER.info(String.format(
				"Listening on port %s at address %s", 
				socksServer.getPort(),
				socksServer.getAddress()));
	}

	private final Address address;
	private final int backlog;
	private final Configuration configuration;
	private ExecutorService executor;
	private final Logger logger;
	private final Port port;
	private ServerSocket serverSocket;
	private final SocketSettings socketSettings;
	private boolean started;
	private boolean stopped;
	
	public SocksServer(final Configuration config, final Logger lggr) {
		this.address = config.getSettings().getLastValue(
				SettingSpec.ADDRESS, Address.class);
		this.backlog = config.getSettings().getLastValue(
				SettingSpec.BACKLOG, NonnegativeInteger.class).intValue();
		this.configuration = config;
		this.executor = null;
		this.logger = lggr;
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
	
	public Address getAddress() {
		return this.address;
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
				this.address.toInetAddress(), this.port.intValue()), 
				this.backlog);
		this.executor = Executors.newSingleThreadExecutor();
		this.executor.execute(new Listener(
				this.serverSocket, this.configuration, this.logger));
		this.started = true;
		this.stopped = false;
	}
	
	public void stop() throws IOException {
		if (this.stopped) {
			throw new IllegalStateException("SocksServer already stopped");
		}
		this.serverSocket.close();
		this.serverSocket = null;
		this.executor.shutdownNow();
		this.executor = null;
		this.started = false;
		this.stopped = true;
	}

}
