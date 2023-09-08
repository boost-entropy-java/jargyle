package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.common.lang.UnsignedByte;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSetting;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.internal.lang.ThrowableHelper;
import com.github.jh3nd3rs0n.jargyle.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslSocketFactory;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.FirewallAction;
import com.github.jh3nd3rs0n.jargyle.server.GeneralRuleArgSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.GeneralRuleResultSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.GeneralSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.NonnegativeIntegerLimit;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.RuleContext;
import com.github.jh3nd3rs0n.jargyle.server.SelectionStrategy;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5.Socks5Worker;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5.Socks5WorkerContext;
import com.github.jh3nd3rs0n.jargyle.transport.SocksException;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Version;

public class Worker implements Runnable {
	
	private static final class RuleHolder {
		private Rule rule;
	}
	
	private DtlsDatagramSocketFactory clientFacingDtlsDatagramSocketFactory;
	private Socket clientSocket;
	private SslSocketFactory clientSslSocketFactory;
	private final Configuration configuration;
	private final Logger logger;
	private final Routes routes;
	private final Rules rules;	
	private final AtomicInteger totalWorkerCount;
	
	public Worker(final Socket clientSock) {
		this.clientFacingDtlsDatagramSocketFactory = null;
		this.clientSocket = clientSock;
		this.clientSslSocketFactory = null;
		this.configuration = null;
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.routes = null;
		this.rules = null;
		this.totalWorkerCount = null;
	}
	
	Worker(
			final Socket clientSock, 
			final AtomicInteger workerCount,
			final Configuration config) {
		this.clientFacingDtlsDatagramSocketFactory = 
				DtlsDatagramSocketFactoryImpl.isDtlsEnabled(config) ? 
						new DtlsDatagramSocketFactoryImpl(config) : null;
		this.clientSocket = clientSock;
		this.clientSslSocketFactory =
				SslSocketFactoryImpl.isSslEnabled(config) ?
						new SslSocketFactoryImpl(config) : null;
		this.configuration = config;
		this.logger = LoggerFactory.getLogger(Worker.class);
		this.routes = Routes.newInstance(config);
		this.rules = Rules.newInstance(config);
		this.totalWorkerCount = workerCount;
	}
	
	private boolean canAllowClientSocket(
			final Rule applicableRule,
			final RuleContext clientRuleContext,
			final RuleHolder belowAllowLimitRuleHolder) {
		if (applicableRule == null) {
			return false;
		}
		FirewallAction firewallAction = applicableRule.getLastRuleResultValue(
				GeneralRuleResultSpecConstants.FIREWALL_ACTION);
		if (firewallAction == null) {
			return false;
		}
		LogAction firewallActionLogAction = 
				applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.FIREWALL_ACTION_LOG_ACTION);
		if (firewallAction.equals(FirewallAction.ALLOW)) {
			if (!this.canAllowClientSocketWithinLimit(
					applicableRule, 
					clientRuleContext, 
					belowAllowLimitRuleHolder)) {
				return false;
			}
			if (firewallActionLogAction != null) {
				firewallActionLogAction.invoke(String.format(
						"Client allowed based on the following rule and "
						+ "context: rule: %s context: %s",
						applicableRule,
						clientRuleContext));
			}
		} else if (firewallAction.equals(FirewallAction.DENY)
				&& firewallActionLogAction != null) {
			firewallActionLogAction.invoke(String.format(
					"Client denied based on the following rule and context: "
					+ "rule: %s context: %s",
					applicableRule,
					clientRuleContext));				
		}
		return FirewallAction.ALLOW.equals(firewallAction);
	}
	
	private boolean canAllowClientSocketWithinLimit(
			final Rule applicableRule,
			final RuleContext clientRuleContext,
			final RuleHolder belowAllowLimitRuleHolder) {
		NonnegativeIntegerLimit firewallActionAllowLimit =
				applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.FIREWALL_ACTION_ALLOW_LIMIT);
		LogAction firewallActionAllowLimitReachedLogAction =
				applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.FIREWALL_ACTION_ALLOW_LIMIT_REACHED_LOG_ACTION);
		if (firewallActionAllowLimit != null) {
			if (!firewallActionAllowLimit.tryIncrementCurrentCount()) {
				if (firewallActionAllowLimitReachedLogAction != null) {
					firewallActionAllowLimitReachedLogAction.invoke(
							String.format(
									"Allowed limit has been reached based on "
									+ "the following rule and context: rule: "
									+ "%s context: %s",
									applicableRule,
									clientRuleContext));
				}
				return false;
			}
			belowAllowLimitRuleHolder.rule = applicableRule;
		}		
		return true;
	}
	
	private boolean configureClientSocket(
			final Socket clientSock, final Rule applicableRule) {
		SocketSettings socketSettings = this.getClientSocketSettings(
				applicableRule);
		try {
			socketSettings.applyTo(clientSock);
		} catch (UnsupportedOperationException e) {
			this.logger.error(
					ObjectLogMessageHelper.objectLogMessage(
							this, "Error in setting the client socket"), 
					e);
			return false;			
		} catch (SocketException e) {
			this.logger.error(
					ObjectLogMessageHelper.objectLogMessage(
							this, "Error in setting the client socket"), 
					e);
			return false;
		}
		return true;
	}
	
	protected final Socket getClientSocket() {
		return this.clientSocket;
	}
	
	
	private SocketSettings getClientSocketSettings(final Rule applicableRule) {
		List<SocketSetting<Object>> socketSettings = 
				applicableRule.getRuleResultValues(
						GeneralRuleResultSpecConstants.CLIENT_SOCKET_SETTING);
		if (socketSettings.size() > 0) {
			return SocketSettings.newInstance(
					socketSettings.stream().collect(Collectors.toList()));
		}
		socketSettings = applicableRule.getRuleResultValues(
				GeneralRuleResultSpecConstants.SOCKET_SETTING);
		if (socketSettings.size() > 0) {
			return SocketSettings.newInstance(
					socketSettings.stream().collect(Collectors.toList()));
		}
		Settings settings = this.configuration.getSettings();
		SocketSettings socketSttngs = settings.getLastValue(
				GeneralSettingSpecConstants.CLIENT_SOCKET_SETTINGS);
		if (socketSttngs.toMap().size() > 0) {
			return socketSttngs;
		}
		socketSttngs = settings.getLastValue(
				GeneralSettingSpecConstants.SOCKET_SETTINGS);
		return socketSttngs;
	}
	
	private Routes getRoutes(final Rule applicableRule) {
		List<Route> rtes = applicableRule.getRuleResultValues(
				GeneralRuleResultSpecConstants.SELECTABLE_ROUTE_ID)
				.stream()
				.map(rteId -> this.routes.get(rteId))
				.filter(rte -> rte != null)
				.collect(Collectors.toList());
		if (rtes.size() > 0) {
			return Routes.newInstance(rtes);
		}
		return this.routes;
	}
	
	private LogAction getRouteSelectionLogAction(final Rule applicableRule) {
		LogAction routeSelectionLogAction = 
				applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.ROUTE_SELECTION_LOG_ACTION);
		if (routeSelectionLogAction != null) {
			return routeSelectionLogAction;
		}
		return this.configuration.getSettings().getLastValue(
				GeneralSettingSpecConstants.ROUTE_SELECTION_LOG_ACTION);
	}
	
	protected final void logClientIoException(
			final String message, final IOException e) {
		if (ThrowableHelper.isOrHasInstanceOf(e, EOFException.class)) {
			this.logger.debug(message, e);
			return;
		}
		if (ThrowableHelper.isOrHasInstanceOf(e, SocketException.class)) {
			this.logger.debug(message, e);
			return;
		}
		if (ThrowableHelper.isOrHasInstanceOf(
				e, SocketTimeoutException.class)) {
			this.logger.debug(message, e);
			return;
		}
		if (ThrowableHelper.isOrHasInstanceOf(e, SocksException.class)) {
			this.logger.debug(message, e);
			return;
		}
		this.logger.error(message, e);		
	}
	
	private RuleContext newClientRuleContext() {
		RuleContext clientRuleContext = new RuleContext();
		Socket clientSocket = this.getClientSocket();
		clientRuleContext.putRuleArgValue(
				GeneralRuleArgSpecConstants.CLIENT_ADDRESS, 
				clientSocket.getInetAddress().getHostAddress());
		clientRuleContext.putRuleArgValue(
				GeneralRuleArgSpecConstants.SOCKS_SERVER_ADDRESS, 
				clientSocket.getLocalAddress().getHostAddress());
		return clientRuleContext;
	}
	
	public void run() {
		long startTime = System.currentTimeMillis();
		WorkerContext workerContext = null;
		try {
			this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
					this, 
					"Started. Total Worker count: %s",
					this.totalWorkerCount.incrementAndGet()));
			RuleContext clientRuleContext = this.newClientRuleContext();
			Rule applicableRule = this.rules.firstAppliesTo(clientRuleContext);
			RuleHolder belowAllowLimitRuleHolder = new RuleHolder();
			if (!this.canAllowClientSocket(
					applicableRule, 
					clientRuleContext, 
					belowAllowLimitRuleHolder)) {
				return;
			}
			Socket clientSocket = this.getClientSocket();
			if (!this.configureClientSocket(clientSocket, applicableRule)) {
				return;
			}
			clientSocket = this.wrapClientSocket(clientSocket);
			if (clientSocket == null) {
				return;
			}
			this.setClientSocket(clientSocket);
			Route selectedRoute = this.selectRoute(
					applicableRule, clientRuleContext);
			workerContext = new WorkerContext(
					this.configuration,
					this.rules,
					this.routes,
					this.clientFacingDtlsDatagramSocketFactory);
			Rule belowAllowLimitRule = belowAllowLimitRuleHolder.rule;
			if (belowAllowLimitRule != null) {
				workerContext.addBelowAllowLimitRule(belowAllowLimitRule);
			}
			workerContext.setSelectedRoute(selectedRoute);
			InputStream clientInputStream =	
					this.getClientSocket().getInputStream();
			UnsignedByte version = null;
			try {
				version = UnsignedByte.newInstanceFrom(clientInputStream);
			} catch (IOException e) {
				this.logClientIoException(
						ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Error in getting the SOCKS version from the "
								+ "client"), 
						e);
				return;
			}
			if (version.byteValue() == Version.V5.byteValue()) {
				Socks5Worker socks5Worker = new Socks5Worker(
						this.getClientSocket(),
						new Socks5WorkerContext(workerContext));
				socks5Worker.run();
			} else {
				this.logger.error(ObjectLogMessageHelper.objectLogMessage(
						this, 
						"Unknown SOCKS version: %s",
						version.intValue()));
			}
		} catch (Throwable t) {
			this.logger.error(
					ObjectLogMessageHelper.objectLogMessage(
							this, "Internal server error"), 
					t);
		} finally {
			if (workerContext != null) {
				workerContext.decrementCurrentAllowCount();
			}
			if (!this.getClientSocket().isClosed()) {
				try {
					this.getClientSocket().close();
				} catch (IOException e) {
					this.logger.error(
							ObjectLogMessageHelper.objectLogMessage(
									this, 
									"Error upon closing connection to the "
									+ "client"), 
							e);
				}
			}
			this.logger.debug(ObjectLogMessageHelper.objectLogMessage(
					this, 
					"Finished in %s ms. Total Worker count: %s",
					System.currentTimeMillis() - startTime,
					this.totalWorkerCount.decrementAndGet()));
		}
	}
	
	private Route selectRoute(
			final Rule applicableRule,
			final RuleContext clientRuleContext) {
		SelectionStrategy rteSelectionStrategy = 
				applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.ROUTE_SELECTION_STRATEGY);
		Routes rtes = null;
		LogAction rteSelectionLogAction = null;
		String rteSelectionLogMessageFormat = null;
		if (rteSelectionStrategy != null) {
			rtes = this.getRoutes(applicableRule);
			rteSelectionLogAction =	this.getRouteSelectionLogAction(
					applicableRule);
			rteSelectionLogMessageFormat = String.format(
					"Route '%s' selected based on the following rule "
					+ "and context: rule: %s context: %s",
					"%s",
					applicableRule,
					clientRuleContext);
		} else {
			Settings settings = this.configuration.getSettings();
			rteSelectionStrategy = settings.getLastValue(
					GeneralSettingSpecConstants.ROUTE_SELECTION_STRATEGY);
			rtes = this.routes;
			rteSelectionLogAction = settings.getLastValue(
					GeneralSettingSpecConstants.ROUTE_SELECTION_LOG_ACTION);
			rteSelectionLogMessageFormat = "Route '%s' selected";
		}
		Route selectedRte = rteSelectionStrategy.selectFrom(
				rtes.toMap().values().stream().collect(Collectors.toList()));
		if (rteSelectionLogAction != null) {
			rteSelectionLogAction.invoke(String.format(
					rteSelectionLogMessageFormat, 
					selectedRte.getId()));
		}
		return selectedRte;		
	}
	
	protected final void sendToClient(
			final byte[] b) throws IOException {
		OutputStream clientFacingOutputStream = 
				this.clientSocket.getOutputStream();
		clientFacingOutputStream.write(b);
		clientFacingOutputStream.flush();
	}
	
	protected final void setClientSocket(final Socket clientSock) {
		this.clientSocket = clientSock;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [getClientSocket()=")
			.append(this.getClientSocket())
			.append("]");
		return builder.toString();
	}
	
	private Socket wrapClientSocket(final Socket clientSock) {
		if (this.clientSslSocketFactory != null) {
			try {
				return this.clientSslSocketFactory.newSocket(
						clientSock, null, true);
			} catch (IOException e) {
				this.logger.error(
						ObjectLogMessageHelper.objectLogMessage(
								this, "Error in wrapping the client socket"), 
						e);
				return null;
			}
		}
		return clientSock;
	}
	
}
