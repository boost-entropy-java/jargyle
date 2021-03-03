package jargyle.net.socks.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jargyle.net.NetFactory;
import jargyle.net.SocketSettings;
import jargyle.net.ssl.DtlsDatagramSocketFactory;
import jargyle.net.ssl.SslSocketFactory;
import jargyle.util.Criteria;
import jargyle.util.Criterion;

final class Listener implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(
			Listener.class);
	
	private final Configuration configuration;
	private DtlsDatagramSocketFactory dtlsDatagramSocketFactory;
	private final NetFactory externalNetFactory;
	private final ServerSocket serverSocket;
	private SslSocketFactory sslSocketFactory;
			
	public Listener(final ServerSocket serverSock, final Configuration config) {
		this.configuration = config;
		this.dtlsDatagramSocketFactory = null;
		this.externalNetFactory = new ExternalNetFactory(config);
		this.serverSocket = serverSock;
		this.sslSocketFactory = null;
	}
	
	private boolean canAcceptClientSocket(final Socket clientSocket) {
		Settings settings = this.configuration.getSettings();
		String clientAddress = 
				clientSocket.getInetAddress().getHostAddress();
		Criteria allowedClientAddressCriteria = settings.getLastValue(
				SettingSpec.ALLOWED_CLIENT_ADDRESS_CRITERIA, Criteria.class);
		Criterion criterion = allowedClientAddressCriteria.anyEvaluatesTrue(
				clientAddress);
		if (criterion == null) {
			LOGGER.debug(this.format(String.format(
					"Client address %s not allowed",
					clientAddress)));
			return false;
		}
		Criteria blockedClientAddressCriteria = settings.getLastValue(
				SettingSpec.BLOCKED_CLIENT_ADDRESS_CRITERIA, Criteria.class);
		criterion = blockedClientAddressCriteria.anyEvaluatesTrue(
				clientAddress);
		if (criterion != null) {
			LOGGER.debug(this.format(String.format(
					"Client address %s blocked based on the following "
					+ "criterion: %s",
					clientAddress,
					criterion)));
			return false;
		}
		return true;
	}
	
	private void closeClientSocket(
			final Socket clientSocket) {
		if (!clientSocket.isClosed()) {
			try {
				clientSocket.close();
			} catch (IOException e) {
				LOGGER.warn( 
						this.format("Error in closing the client socket"), 
						e);
			}
		}
	}
	
	private boolean configureClientSocket(final Socket clientSocket) {
		Settings settings = this.configuration.getSettings();
		try {
			SocketSettings socketSettings =	settings.getLastValue(
					SettingSpec.CLIENT_SOCKET_SETTINGS, SocketSettings.class);
			socketSettings.applyTo(clientSocket);
		} catch (SocketException e) {
			LOGGER.warn(
					this.format("Error in setting the client socket"), 
					e);
			return false;
		}
		return true;
	}
	
	private String format(final String message) {
		return String.format("%s: %s", this, message);
	}
	
	public void run() {
		ExecutorService executor = Executors.newCachedThreadPool();
		while (true) {
			Socket clientSocket = null;
			try {
				clientSocket = this.serverSocket.accept();
			} catch (SocketException e) {
				// closed by SocksServer.stop()
				break;
			} catch (IOException e) {
				LOGGER.warn(
						this.format("Error in waiting for a connection"), 
						e);
				continue;
			}
			try {
				if (!this.canAcceptClientSocket(clientSocket)) {
					this.closeClientSocket(clientSocket);
					continue;
				}
				if (!this.configureClientSocket(clientSocket)) {
					this.closeClientSocket(clientSocket);
					continue;
				}
				Socket clientSock = this.wrapClientSocket(clientSocket); 
				if (clientSock == null) {
					this.closeClientSocket(clientSocket);
					continue; 
				}
				clientSocket = clientSock;
			} catch (Throwable t) {
				LOGGER.warn(
						this.format("Internal server error"), 
						t);
				this.closeClientSocket(clientSocket);
				continue;
			}
			DtlsDatagramSocketFactory dtlsDatagramSockFactory = null;
			if (this.configuration.getSettings().getLastValue(
					SettingSpec.SSL_ENABLED, Boolean.class).booleanValue()) {
				if (this.dtlsDatagramSocketFactory == null) {
					this.dtlsDatagramSocketFactory = 
							new DtlsDatagramSocketFactoryImpl();
				}
				dtlsDatagramSockFactory = this.dtlsDatagramSocketFactory;
			}
			executor.execute(new Worker(
					clientSocket, 
					this.configuration, 
					this.externalNetFactory, 
					dtlsDatagramSockFactory));
		}
		executor.shutdownNow();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [serverSocket=")
			.append(this.serverSocket)
			.append("]");
		return builder.toString();
	}
	
	private Socket wrapClientSocket(final Socket clientSocket) {
		Socket clientSock = clientSocket;
		if (this.configuration.getSettings().getLastValue(
				SettingSpec.SSL_ENABLED, Boolean.class).booleanValue()) {
			if (this.sslSocketFactory == null) {
				this.sslSocketFactory = new SslSocketFactoryImpl(
						this.configuration);
			}
			try {
				clientSock = this.sslSocketFactory.newSocket(
						clientSock, null, true);
			} catch (IOException e) {
				LOGGER.warn(
						this.format("Error in wrapping the client socket"), 
						e);
				return null;
			}
		}
		return clientSock;
	}

}
