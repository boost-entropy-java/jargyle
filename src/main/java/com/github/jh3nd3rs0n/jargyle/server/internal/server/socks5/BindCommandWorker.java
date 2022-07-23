package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSetting;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.AddressAndPortHelper;
import com.github.jh3nd3rs0n.jargyle.server.FirewallAction;
import com.github.jh3nd3rs0n.jargyle.server.GeneralRuleArgSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.GeneralRuleResultSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.NonnegativeIntegerLimit;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.RuleContext;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.Socks5RuleArgSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Socks5RuleConditionSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Socks5RuleResultSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.internal.net.BandwidthLimitedSocket;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.RelayServer;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.Rules;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Reply;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;

final class BindCommandWorker extends CommandWorker {

	private static final Logger LOGGER = LoggerFactory.getLogger(
			BindCommandWorker.class);
	
	private Rule applicableRule;
	private final Socket clientFacingSocket;
	private final CommandWorkerContext commandWorkerContext;
	private final String desiredDestinationAddress;
	private final int desiredDestinationPort;
	private final MethodSubnegotiationResults methodSubnegotiationResults;
	private final NetObjectFactory netObjectFactory;
	private final Rules rules;
	private final Settings settings;
	private final Socks5Request socks5Request;
		
	public BindCommandWorker(final CommandWorkerContext context) {
		super(context);
		Rule applicableRl = context.getApplicableRule();
		Socket clientFacingSock = context.getClientFacingSocket();
		String desiredDestinationAddr =	context.getDesiredDestinationAddress();
		int desiredDestinationPrt = context.getDesiredDestinationPort();
		MethodSubnegotiationResults methSubnegotiationResults =
				context.getMethodSubnegotiationResults();
		NetObjectFactory netObjFactory = 
				context.getSelectedRoute().getNetObjectFactory();
		Rules rls = context.getRules();
		Settings sttngs = context.getSettings();
		Socks5Request socks5Req = context.getSocks5Request();
		this.applicableRule = applicableRl;
		this.clientFacingSocket = clientFacingSock;
		this.commandWorkerContext = context;
		this.desiredDestinationAddress = desiredDestinationAddr;
		this.desiredDestinationPort = desiredDestinationPrt;
		this.methodSubnegotiationResults = methSubnegotiationResults;
		this.netObjectFactory = netObjFactory;
		this.rules = rls;
		this.settings = sttngs;
		this.socks5Request = socks5Req;
	}
	
	private Socket acceptInboundSocketFrom(final ServerSocket listenSocket) {
		Socks5Reply socks5Rep = null;
		Socket inboundSocket = null;
		try {
			inboundSocket = listenSocket.accept();
		} catch (IOException e) {
			LOGGER.error( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in waiting for an inbound socket"), 
					e);
			socks5Rep = Socks5Reply.newFailureInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.commandWorkerContext.sendSocks5Reply(this, socks5Rep, LOGGER);
			return null;
		}
		return inboundSocket;
	}
	
	private boolean bindListenSocket(final ServerSocket listenSocket) {
		Socks5Reply socks5Rep = null;
		HostResolver hostResolver =	this.netObjectFactory.newHostResolver();		
		try {
			listenSocket.bind(new InetSocketAddress(
					hostResolver.resolve(this.desiredDestinationAddress),
					this.desiredDestinationPort));
		} catch (IOException e) {
			LOGGER.error( 
					ObjectLogMessageHelper.objectLogMessage(
							this, "Error in binding the listen socket"), 
					e);
			socks5Rep = Socks5Reply.newFailureInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.commandWorkerContext.sendSocks5Reply(this, socks5Rep, LOGGER);
			return false;
		}
		return true;
	}
	
	private boolean canAllowSecondSocks5Reply(
			final Rule applicableRule,
			final RuleContext secondSocks5ReplyRuleContext) {
		if (applicableRule == null) {
			Socks5Reply rep = Socks5Reply.newFailureInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			this.commandWorkerContext.sendSocks5Reply(this, rep, LOGGER);
			return false;
		}
		boolean hasSecondServerBoundAddressRuleCondition =
				applicableRule.hasRuleCondition(
						Socks5RuleConditionSpecConstants.SOCKS5_SECOND_SERVER_BOUND_ADDRESS);
		boolean hasSecondServerBoundPortRuleCondition =
				applicableRule.hasRuleCondition(
						Socks5RuleConditionSpecConstants.SOCKS5_SECOND_SERVER_BOUND_PORT);
		if (!hasSecondServerBoundAddressRuleCondition
				&& !hasSecondServerBoundPortRuleCondition) {
			return true;
		}
		FirewallAction firewallAction = applicableRule.getLastRuleResultValue(
				GeneralRuleResultSpecConstants.FIREWALL_ACTION);
		if (firewallAction == null) {
			Socks5Reply rep = Socks5Reply.newFailureInstance(
					Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
			this.commandWorkerContext.sendSocks5Reply(this, rep, LOGGER);
			return false;
		}
		LogAction firewallActionLogAction = 
				applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.FIREWALL_ACTION_LOG_ACTION);
		String secondServerBoundAddress =
				secondSocks5ReplyRuleContext.getRuleArgValue(
						Socks5RuleArgSpecConstants.SOCKS5_SECOND_SERVER_BOUND_ADDRESS);
		Port secondServerBoundPort =
				secondSocks5ReplyRuleContext.getRuleArgValue(
						Socks5RuleArgSpecConstants.SOCKS5_SECOND_SERVER_BOUND_PORT);
		if (firewallAction.equals(FirewallAction.ALLOW)) {
			if (!this.canAllowSecondSocks5ReplyWithinLimit(
					applicableRule, secondSocks5ReplyRuleContext)) {
				return false;
			}
			if (firewallActionLogAction != null) {
				firewallActionLogAction.invoke(
						LOGGER, 
						ObjectLogMessageHelper.objectLogMessage(
								this,
								"Second server bound address and port (%s) "
								+ "allowed based on the following rule and "
								+ "context: rule: %s context: %s",
								AddressAndPortHelper.toString(
										secondServerBoundAddress,
										secondServerBoundPort.intValue()),
								applicableRule,
								secondSocks5ReplyRuleContext));					
			}
		} else if (firewallAction.equals(FirewallAction.DENY)
				&& firewallActionLogAction != null) {
			firewallActionLogAction.invoke(
					LOGGER, 
					ObjectLogMessageHelper.objectLogMessage(
							this,
							"Second server bound address and port (%s) denied "
							+ "based on the following rule and context: rule: "
							+ "%s context: %s",
							AddressAndPortHelper.toString(
									secondServerBoundAddress,
									secondServerBoundPort.intValue()),
							applicableRule,
							secondSocks5ReplyRuleContext));				
		}
		if (FirewallAction.ALLOW.equals(firewallAction)) {
			return true;
		}
		Socks5Reply rep = Socks5Reply.newFailureInstance(
				Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
		this.commandWorkerContext.sendSocks5Reply(this, rep, LOGGER);
		return false;
	}
	
	private boolean canAllowSecondSocks5ReplyWithinLimit(
			final Rule applicableRule,
			final RuleContext secondSocks5ReplyRuleContext) {
		NonnegativeIntegerLimit firewallActionAllowLimit =
				applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.FIREWALL_ACTION_ALLOW_LIMIT);
		LogAction firewallActionAllowLimitReachedLogAction =
				applicableRule.getLastRuleResultValue(
						GeneralRuleResultSpecConstants.FIREWALL_ACTION_ALLOW_LIMIT_REACHED_LOG_ACTION);
		if (firewallActionAllowLimit != null) {
			if (firewallActionAllowLimit.hasBeenReached()) {
				if (firewallActionAllowLimitReachedLogAction != null) {
					firewallActionAllowLimitReachedLogAction.invoke(
							LOGGER, 
							ObjectLogMessageHelper.objectLogMessage(
									this,
									"Allowed limit has been reached based on "
									+ "the following rule and context: rule: "
									+ "%s context: %s",
									applicableRule,
									secondSocks5ReplyRuleContext));
				}
				Socks5Reply rep = Socks5Reply.newFailureInstance(
						Reply.CONNECTION_NOT_ALLOWED_BY_RULESET);
				this.commandWorkerContext.sendSocks5Reply(this, rep, LOGGER);
				return false;				
			}
			firewallActionAllowLimit.incrementCurrentCount();
			this.commandWorkerContext.addBelowAllowLimitRule(applicableRule);
		}
		return true;
	}

	private boolean configureInboundSocket(final Socket inboundSocket) {
		SocketSettings socketSettings = this.getInboundSocketSettings();
		try {
			socketSettings.applyTo(inboundSocket);
		} catch (SocketException e) {
			LOGGER.error( 
					ObjectLogMessageHelper.objectLogMessage(
							this, "Error in setting the inbound socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.commandWorkerContext.sendSocks5Reply(this, socks5Rep, LOGGER);
			return false;
		}
		return true;
	}

	private boolean configureListenSocket(final ServerSocket listenSocket) {
		SocketSettings socketSettings = this.getListenSocketSettings();
		try {
			socketSettings.applyTo(listenSocket);
		} catch (SocketException e) {
			LOGGER.error( 
					ObjectLogMessageHelper.objectLogMessage(
							this, "Error in setting the listen socket"), 
					e);
			Socks5Reply socks5Rep = Socks5Reply.newFailureInstance(
					Reply.GENERAL_SOCKS_SERVER_FAILURE);
			this.commandWorkerContext.sendSocks5Reply(this, socks5Rep, LOGGER);
			return false;
		}
		return true;
	}
	
	private SocketSettings getInboundSocketSettings() {
		List<SocketSetting<Object>> socketSettings = 
				this.applicableRule.getRuleResultValues(
						Socks5RuleResultSpecConstants.SOCKS5_ON_BIND_INBOUND_SOCKET_SETTING);
		if (socketSettings.size() > 0) {
			List<SocketSetting<? extends Object>> socketSttngs = 
					new ArrayList<SocketSetting<? extends Object>>();
			return SocketSettings.newInstance(socketSttngs);
		}
		return this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_BIND_INBOUND_SOCKET_SETTINGS);
	}
	
	private SocketSettings getListenSocketSettings() {
		List<SocketSetting<Object>> socketSettings = 
				this.applicableRule.getRuleResultValues(
						Socks5RuleResultSpecConstants.SOCKS5_ON_BIND_LISTEN_SOCKET_SETTING);
		if (socketSettings.size() > 0) {
			List<SocketSetting<? extends Object>> socketSttngs = 
					new ArrayList<SocketSetting<? extends Object>>();
			return SocketSettings.newInstance(socketSttngs);
		}
		return this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_BIND_LISTEN_SOCKET_SETTINGS);
	}
	
	private int getRelayBufferSize() {
		PositiveInteger relayBufferSize = 
				this.applicableRule.getLastRuleResultValue(
						Socks5RuleResultSpecConstants.SOCKS5_ON_BIND_RELAY_BUFFER_SIZE);
		if (relayBufferSize != null) {
			return relayBufferSize.intValue();
		}
		relayBufferSize = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_BIND_RELAY_BUFFER_SIZE);
		return relayBufferSize.intValue();
	}
	
	private int getRelayIdleTimeout() {
		PositiveInteger relayIdleTimeout =
				this.applicableRule.getLastRuleResultValue(
						Socks5RuleResultSpecConstants.SOCKS5_ON_BIND_RELAY_IDLE_TIMEOUT);
		if (relayIdleTimeout != null) {
			return relayIdleTimeout.intValue();
		}
		relayIdleTimeout = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_BIND_RELAY_IDLE_TIMEOUT);
		return relayIdleTimeout.intValue();
	}
	
	private Integer getRelayInboundBandwidthLimit() {
		PositiveInteger relayInboundBandwidthLimit =
				this.applicableRule.getLastRuleResultValue(
						Socks5RuleResultSpecConstants.SOCKS5_ON_BIND_RELAY_INBOUND_BANDWIDTH_LIMIT);
		if (relayInboundBandwidthLimit != null) {
			return Integer.valueOf(relayInboundBandwidthLimit.intValue());
		}
		relayInboundBandwidthLimit = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_BIND_RELAY_INBOUND_BANDWIDTH_LIMIT);
		if (relayInboundBandwidthLimit != null) {
			return Integer.valueOf(relayInboundBandwidthLimit.intValue());
		}
		return null;
	}
	
	private Integer getRelayOutboundBandwidthLimit() {
		PositiveInteger relayOutboundBandwidthLimit =
				this.applicableRule.getLastRuleResultValue(
						Socks5RuleResultSpecConstants.SOCKS5_ON_BIND_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
		if (relayOutboundBandwidthLimit != null) {
			return Integer.valueOf(relayOutboundBandwidthLimit.intValue());
		}
		relayOutboundBandwidthLimit = this.settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_BIND_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
		if (relayOutboundBandwidthLimit != null) {
			return Integer.valueOf(relayOutboundBandwidthLimit.intValue());
		}
		return null;
	}
	
	private RuleContext newSecondSocks5ReplyRuleContext(
			final Socks5Reply socks5Rep,
			final Socks5Reply secondSocks5Rep) {
		RuleContext secondSocks5ReplyRuleContext = new RuleContext();
		secondSocks5ReplyRuleContext.putRuleArgValue(
				GeneralRuleArgSpecConstants.CLIENT_ADDRESS, 
				this.clientFacingSocket.getInetAddress().getHostAddress());
		secondSocks5ReplyRuleContext.putRuleArgValue(
				GeneralRuleArgSpecConstants.SOCKS_SERVER_ADDRESS, 
				this.clientFacingSocket.getLocalAddress().getHostAddress());
		secondSocks5ReplyRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_METHOD, 
				this.methodSubnegotiationResults.getMethod());
		secondSocks5ReplyRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_USER, 
				this.methodSubnegotiationResults.getUser());
		secondSocks5ReplyRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_COMMAND, 
				this.socks5Request.getCommand());
		secondSocks5ReplyRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_DESIRED_DESTINATION_ADDRESS, 
				this.socks5Request.getDesiredDestinationAddress());
		secondSocks5ReplyRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_DESIRED_DESTINATION_PORT, 
				Port.newInstance(this.socks5Request.getDesiredDestinationPort()));		
		secondSocks5ReplyRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_SERVER_BOUND_ADDRESS, 
				socks5Rep.getServerBoundAddress());
		secondSocks5ReplyRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_SERVER_BOUND_PORT, 
				Port.newInstance(socks5Rep.getServerBoundPort()));
		secondSocks5ReplyRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_SECOND_SERVER_BOUND_ADDRESS, 
				secondSocks5Rep.getServerBoundAddress());
		secondSocks5ReplyRuleContext.putRuleArgValue(
				Socks5RuleArgSpecConstants.SOCKS5_SECOND_SERVER_BOUND_PORT, 
				Port.newInstance(secondSocks5Rep.getServerBoundPort()));		
		return secondSocks5ReplyRuleContext;
	}
	
	@Override
	public void run() throws IOException {
		ServerSocket listenSocket = null;
		Socks5Reply socks5Rep = null;
		Socket inboundSocket = null;
		Socks5Reply secondSocks5Rep = null;
		try {
			listenSocket = this.netObjectFactory.newServerSocket();
			if (!this.configureListenSocket(listenSocket)) {
				return;
			}
			if (!this.bindListenSocket(listenSocket)) {
				return;
			}
			InetAddress inetAddress = listenSocket.getInetAddress();
			String serverBoundAddress =	inetAddress.getHostAddress();
			int serverBoundPort = listenSocket.getLocalPort();
			socks5Rep = Socks5Reply.newInstance(
					Reply.SUCCEEDED, 
					serverBoundAddress, 
					serverBoundPort);
			RuleContext socks5ReplyRuleContext = 
					this.commandWorkerContext.newSocks5ReplyRuleContext(
							socks5Rep);
			this.applicableRule = this.rules.firstAppliesTo(
					socks5ReplyRuleContext);
			if (!this.commandWorkerContext.canAllowSocks5Reply(
					this, 
					this.applicableRule, 
					socks5ReplyRuleContext, 
					LOGGER)) {
				return;
			}
			if (!this.commandWorkerContext.sendSocks5Reply(
					this, socks5Rep, LOGGER)) {
				return;
			}
			inboundSocket = this.acceptInboundSocketFrom(listenSocket);
			listenSocket.close();
			if (inboundSocket == null) {
				return;
			}
			if (!this.configureInboundSocket(inboundSocket)) {
				return;
			}
			serverBoundAddress = 
					inboundSocket.getInetAddress().getHostAddress();
			serverBoundPort = inboundSocket.getPort();
			secondSocks5Rep = Socks5Reply.newInstance(
					Reply.SUCCEEDED, 
					serverBoundAddress, 
					serverBoundPort);
			RuleContext secondSocks5ReplyRuleContext = 
					this.newSecondSocks5ReplyRuleContext(
							socks5Rep, secondSocks5Rep);
			this.applicableRule = this.rules.firstAppliesTo(
					secondSocks5ReplyRuleContext);
			if (!this.canAllowSecondSocks5Reply(
					this.applicableRule, secondSocks5ReplyRuleContext)) {
				return;
			}
			if (!this.commandWorkerContext.sendSocks5Reply(
					this, socks5Rep, LOGGER)) {
				return;
			}
			Integer inboundBandwidthLimit = this.getRelayInboundBandwidthLimit();
			Integer outboundBandwidthLimit = this.getRelayOutboundBandwidthLimit();
			Socket clientFacingSock = this.clientFacingSocket;
			Socket inboundSock = inboundSocket;
			if (outboundBandwidthLimit != null) {
				clientFacingSock = new BandwidthLimitedSocket(
						clientFacingSock, outboundBandwidthLimit.intValue());
			}
			if (inboundBandwidthLimit != null) {
				inboundSock = new BandwidthLimitedSocket(
						inboundSock, inboundBandwidthLimit.intValue());
			}
			RelayServer.Builder builder = new RelayServer.Builder(
					clientFacingSock, inboundSock);
			builder.bufferSize(this.getRelayBufferSize());
			builder.idleTimeout(this.getRelayIdleTimeout());
			try {
				TcpBasedCommandWorkerHelper.passData(builder);
			} catch (IOException e) {
				LOGGER.error( 
						ObjectLogMessageHelper.objectLogMessage(
								this, "Error in starting to pass data"), 
						e);				
			}
		} finally {
			if (inboundSocket != null && !inboundSocket.isClosed()) {
				inboundSocket.close();
			}
			if (listenSocket != null && !listenSocket.isClosed()) {
				listenSocket.close();
			}
		}
	}

}
