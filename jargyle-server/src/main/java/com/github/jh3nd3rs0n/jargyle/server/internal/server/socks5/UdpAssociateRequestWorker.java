package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.ReplyCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRanges;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSetting;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.internal.throwable.ThrowableHelper;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Address;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Reply;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Request;
import com.github.jh3nd3rs0n.jargyle.server.GeneralRuleActionSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.GeneralSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.RuleContext;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.Socks5RuleActionSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.internal.net.BandwidthLimitedDatagramSocket;

final class UdpAssociateRequestWorker extends RequestWorker {

	private static final int HALF_SECOND = 500;

	private final Logger logger;
	
	public UdpAssociateRequestWorker(
			final Socks5Worker socks5Worker, 
			final MethodSubNegotiationResults methSubNegotiationResults, 
			final Request req) {
		super(socks5Worker, methSubNegotiationResults, req);
		this.logger = LoggerFactory.getLogger(UdpAssociateRequestWorker.class);
	}
	
	private boolean configureClientFacingDatagramSocket(
			final DatagramSocket clientFacingDatagramSock) {
		SocketSettings socketSettings = this.getClientFacingSocketSettings();
		try {
			socketSettings.applyTo(clientFacingDatagramSock);
		} catch (UnsupportedOperationException | SocketException e) {
			this.logger.warn( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in setting the client-facing UDP socket"), 
					e);
			this.sendReply(
					Reply.newFailureInstance(ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return false;			
		}
        return true;
	}
	
	private boolean configurePeerFacingDatagramSocket(
			final DatagramSocket peerFacingDatagramSock) {
		SocketSettings socketSettings = this.getPeerFacingSocketSettings();
		try {
			socketSettings.applyTo(peerFacingDatagramSock);
		} catch (UnsupportedOperationException | SocketException e) {
			this.logger.warn( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in setting the peer-facing UDP socket"), 
					e);
			this.sendReply(
					Reply.newFailureInstance(ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return false;			
		}
        return true;
	}
	
	private Host getClientFacingBindHost() {
		Rule applicableRule = this.getApplicableRule();
		Host host = applicableRule.getLastRuleActionValue(
				Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = applicableRule.getLastRuleActionValue(
				Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_INTERNAL_FACING_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = applicableRule.getLastRuleActionValue(
				Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = applicableRule.getLastRuleActionValue(
				GeneralRuleActionSpecConstants.INTERNAL_FACING_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = applicableRule.getLastRuleActionValue(
				GeneralRuleActionSpecConstants.BIND_HOST);
		if (host != null) {
			return host;
		}
		Settings settings = this.getSettings();
		host = settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_INTERNAL_FACING_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = settings.getLastValue(
				GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = settings.getLastValue(
				GeneralSettingSpecConstants.BIND_HOST);
		return host;
	}
	
	private PortRanges getClientFacingBindPortRanges() {
		Rule applicableRule = this.getApplicableRule();
		List<PortRange> portRanges = applicableRule.getRuleActionValues(
				Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_PORT_RANGE);
		if (portRanges.size() > 0) {
			return PortRanges.of(portRanges);
		}
		portRanges = applicableRule.getRuleActionValues(
				Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_INTERNAL_FACING_BIND_UDP_PORT_RANGE);
		if (portRanges.size() > 0) {
			return PortRanges.of(portRanges);
		}
		portRanges = applicableRule.getRuleActionValues(
				Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_BIND_UDP_PORT_RANGE);
		if (portRanges.size() > 0) {
			return PortRanges.of(portRanges);
		}
		portRanges = applicableRule.getRuleActionValues(
				GeneralRuleActionSpecConstants.INTERNAL_FACING_BIND_UDP_PORT_RANGE);
		if (portRanges.size() > 0) {
			return PortRanges.of(portRanges);
		}
		portRanges = applicableRule.getRuleActionValues(
				GeneralRuleActionSpecConstants.BIND_UDP_PORT_RANGE);
		if (portRanges.size() > 0) {
			return PortRanges.of(portRanges);
		}
		Settings settings = this.getSettings();
		PortRanges prtRanges = settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_BIND_PORT_RANGES);
		if (prtRanges.toList().size() > 0) {
			return prtRanges;
		}
		prtRanges = settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_INTERNAL_FACING_BIND_UDP_PORT_RANGES);
		if (prtRanges.toList().size() > 0) {
			return prtRanges;
		}
		prtRanges = settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_BIND_UDP_PORT_RANGES);
		if (prtRanges.toList().size() > 0) {
			return prtRanges;
		}
		prtRanges = settings.getLastValue(
				GeneralSettingSpecConstants.INTERNAL_FACING_BIND_UDP_PORT_RANGES);
		if (prtRanges.toList().size() > 0) {
			return prtRanges;
		}
		prtRanges = settings.getLastValue(
				GeneralSettingSpecConstants.BIND_UDP_PORT_RANGES);
		return prtRanges;
	}
	
	private SocketSettings getClientFacingSocketSettings() {
		Rule applicableRule = this.getApplicableRule();
		List<SocketSetting<Object>> socketSettings =
				applicableRule.getRuleActionValues(
						Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_SOCKET_SETTING);
		if (socketSettings.size() > 0) {
			return SocketSettings.of(
					socketSettings.stream().collect(Collectors.toList()));
		}
		socketSettings = applicableRule.getRuleActionValues(
				Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_INTERNAL_FACING_SOCKET_SETTING);
		if (socketSettings.size() > 0) {
			return SocketSettings.of(
					socketSettings.stream().collect(Collectors.toList()));
		}
		socketSettings = applicableRule.getRuleActionValues(
				Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_SOCKET_SETTING);
		if (socketSettings.size() > 0) {
			return SocketSettings.of(
					socketSettings.stream().collect(Collectors.toList()));
		}
		socketSettings = applicableRule.getRuleActionValues(
				GeneralRuleActionSpecConstants.INTERNAL_FACING_SOCKET_SETTING);
		if (socketSettings.size() > 0) {
			return SocketSettings.of(
					socketSettings.stream().collect(Collectors.toList()));
		}
		socketSettings = applicableRule.getRuleActionValues(
				GeneralRuleActionSpecConstants.SOCKET_SETTING);
		if (socketSettings.size() > 0) {
			return SocketSettings.of(
					socketSettings.stream().collect(Collectors.toList()));
		}
		Settings settings = this.getSettings();
		SocketSettings socketSttngs = settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_CLIENT_FACING_SOCKET_SETTINGS);
		if (socketSttngs.toMap().size() > 0) {
			return socketSttngs;
		}
		socketSttngs = settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_INTERNAL_FACING_SOCKET_SETTINGS);
		if (socketSttngs.toMap().size() > 0) {
			return socketSttngs;
		}
		socketSttngs = settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_SOCKET_SETTINGS);
		if (socketSttngs.toMap().size() > 0) {
			return socketSttngs;
		}
		socketSttngs = settings.getLastValue(
				GeneralSettingSpecConstants.INTERNAL_FACING_SOCKET_SETTINGS);
		if (socketSttngs.toMap().size() > 0) {
			return socketSttngs;
		}
		socketSttngs = settings.getLastValue(
				GeneralSettingSpecConstants.SOCKET_SETTINGS);
		return socketSttngs;
	}
	
	private Host getPeerFacingBindHost() {
		Rule applicableRule = this.getApplicableRule();
		Host host = applicableRule.getLastRuleActionValue(
				Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = applicableRule.getLastRuleActionValue(
				Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = applicableRule.getLastRuleActionValue(
				Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = applicableRule.getLastRuleActionValue(
				GeneralRuleActionSpecConstants.EXTERNAL_FACING_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = applicableRule.getLastRuleActionValue(
				GeneralRuleActionSpecConstants.BIND_HOST);
		if (host != null) {
			return host;
		}
		Settings settings = this.getSettings();
		host = settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = settings.getLastValue(
				GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_HOST);
		if (host != null) {
			return host;
		}
		host = settings.getLastValue(
				GeneralSettingSpecConstants.BIND_HOST);
		return host;
	}
	
	private PortRanges getPeerFacingBindPortRanges() {
		Rule applicableRule = this.getApplicableRule();
		List<PortRange> portRanges = applicableRule.getRuleActionValues(
				Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_PORT_RANGE);
		if (portRanges.size() > 0) {
			return PortRanges.of(portRanges);
		}
		portRanges = applicableRule.getRuleActionValues(
				Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_UDP_PORT_RANGE);
		if (portRanges.size() > 0) {
			return PortRanges.of(portRanges);
		}
		portRanges = applicableRule.getRuleActionValues(
				Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_BIND_UDP_PORT_RANGE);
		if (portRanges.size() > 0) {
			return PortRanges.of(portRanges);
		}
		portRanges = applicableRule.getRuleActionValues(
				GeneralRuleActionSpecConstants.EXTERNAL_FACING_BIND_UDP_PORT_RANGE);
		if (portRanges.size() > 0) {
			return PortRanges.of(portRanges);
		}
		portRanges = applicableRule.getRuleActionValues(
				GeneralRuleActionSpecConstants.BIND_UDP_PORT_RANGE);
		if (portRanges.size() > 0) {
			return PortRanges.of(portRanges);
		}
		Settings settings = this.getSettings();
		PortRanges prtRanges = settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_BIND_PORT_RANGES);
		if (prtRanges.toList().size() > 0) {
			return prtRanges;
		}
		prtRanges = settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_BIND_UDP_PORT_RANGES);
		if (prtRanges.toList().size() > 0) {
			return prtRanges;
		}
		prtRanges = settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_BIND_UDP_PORT_RANGES);
		if (prtRanges.toList().size() > 0) {
			return prtRanges;
		}
		prtRanges = settings.getLastValue(
				GeneralSettingSpecConstants.EXTERNAL_FACING_BIND_UDP_PORT_RANGES);
		if (prtRanges.toList().size() > 0) {
			return prtRanges;
		}
		prtRanges = settings.getLastValue(
				GeneralSettingSpecConstants.BIND_UDP_PORT_RANGES);
		return prtRanges;
	}
	
	private SocketSettings getPeerFacingSocketSettings() {
		Rule applicableRule = this.getApplicableRule();
		List<SocketSetting<Object>> socketSettings =
				applicableRule.getRuleActionValues(
						Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_SOCKET_SETTING);
		if (socketSettings.size() > 0) {
			return SocketSettings.of(
					socketSettings.stream().collect(Collectors.toList()));
		}
		socketSettings = applicableRule.getRuleActionValues(
				Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_SOCKET_SETTING);
		if (socketSettings.size() > 0) {
			return SocketSettings.of(
					socketSettings.stream().collect(Collectors.toList()));
		}
		socketSettings = applicableRule.getRuleActionValues(
				Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_SOCKET_SETTING);
		if (socketSettings.size() > 0) {
			return SocketSettings.of(
					socketSettings.stream().collect(Collectors.toList()));
		}
		socketSettings = applicableRule.getRuleActionValues(
				GeneralRuleActionSpecConstants.EXTERNAL_FACING_SOCKET_SETTING);
		if (socketSettings.size() > 0) {
			return SocketSettings.of(
					socketSettings.stream().collect(Collectors.toList()));
		}
		socketSettings = applicableRule.getRuleActionValues(
				GeneralRuleActionSpecConstants.SOCKET_SETTING);
		if (socketSettings.size() > 0) {
			return SocketSettings.of(
					socketSettings.stream().collect(Collectors.toList()));
		}
		SocketSettings socketSttngs = this.getSettings().getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_PEER_FACING_SOCKET_SETTINGS);
		if (socketSttngs.toMap().size() > 0) {
			return socketSttngs;
		}
		Settings settings = this.getSettings();
		socketSttngs = settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_EXTERNAL_FACING_SOCKET_SETTINGS);
		if (socketSttngs.toMap().size() > 0) {
			return socketSttngs;
		}
		socketSttngs = settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_SOCKET_SETTINGS);
		if (socketSttngs.toMap().size() > 0) {
			return socketSttngs;
		}
		socketSttngs = settings.getLastValue(
				GeneralSettingSpecConstants.EXTERNAL_FACING_SOCKET_SETTINGS);
		if (socketSttngs.toMap().size() > 0) {
			return socketSttngs;
		}
		socketSttngs = settings.getLastValue(
				GeneralSettingSpecConstants.SOCKET_SETTINGS);
		return socketSttngs;
	}
	
	private int getRelayBufferSize() {
		Rule applicableRule = this.getApplicableRule();
		PositiveInteger relayBufferSize =
				applicableRule.getLastRuleActionValue(
						Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE);
		if (relayBufferSize != null) {
			return relayBufferSize.intValue();
		}
		relayBufferSize = applicableRule.getLastRuleActionValue(
				Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_RELAY_BUFFER_SIZE);
		if (relayBufferSize != null) {
			return relayBufferSize.intValue();
		}
		Settings settings = this.getSettings();
		relayBufferSize = settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE);
		if (relayBufferSize != null) {
			return relayBufferSize.intValue();
		}
		relayBufferSize = settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_RELAY_BUFFER_SIZE);
		return relayBufferSize.intValue();
	}
	
	private int getRelayIdleTimeout() {
		Rule applicableRule = this.getApplicableRule();
		PositiveInteger relayIdleTimeout =
				applicableRule.getLastRuleActionValue(
						Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_IDLE_TIMEOUT);
		if (relayIdleTimeout != null) {
			return relayIdleTimeout.intValue();
		}
		relayIdleTimeout = applicableRule.getLastRuleActionValue(
				Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_RELAY_IDLE_TIMEOUT);
		if (relayIdleTimeout != null) {
			return relayIdleTimeout.intValue();
		}
		Settings settings = this.getSettings();
		relayIdleTimeout = settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_IDLE_TIMEOUT);
		if (relayIdleTimeout != null) {
			return relayIdleTimeout.intValue();
		}
		relayIdleTimeout = settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_RELAY_IDLE_TIMEOUT);
		return relayIdleTimeout.intValue();
	}
	
	private Integer getRelayInboundBandwidthLimit() {
		Rule applicableRule = this.getApplicableRule();
		PositiveInteger relayInboundBandwidthLimit =
				applicableRule.getLastRuleActionValue(
						Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT);
		if (relayInboundBandwidthLimit != null) {
			return Integer.valueOf(relayInboundBandwidthLimit.intValue());
		}
		relayInboundBandwidthLimit = applicableRule.getLastRuleActionValue(
				Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT);
		if (relayInboundBandwidthLimit != null) {
			return Integer.valueOf(relayInboundBandwidthLimit.intValue());
		}
		Settings settings = this.getSettings();
		relayInboundBandwidthLimit = settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT);
		if (relayInboundBandwidthLimit != null) {
			return Integer.valueOf(relayInboundBandwidthLimit.intValue());
		}
		relayInboundBandwidthLimit = settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_RELAY_INBOUND_BANDWIDTH_LIMIT);
		if (relayInboundBandwidthLimit != null) {
			return Integer.valueOf(relayInboundBandwidthLimit.intValue());
		}
		return null;
	}
	
	private Integer getRelayOutboundBandwidthLimit() {
		Rule applicableRule = this.getApplicableRule();
		PositiveInteger relayOutboundBandwidthLimit =
				applicableRule.getLastRuleActionValue(
						Socks5RuleActionSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
		if (relayOutboundBandwidthLimit != null) {
			return Integer.valueOf(relayOutboundBandwidthLimit.intValue());
		}
		relayOutboundBandwidthLimit =
				applicableRule.getLastRuleActionValue(
						Socks5RuleActionSpecConstants.SOCKS5_ON_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
		if (relayOutboundBandwidthLimit != null) {
			return Integer.valueOf(relayOutboundBandwidthLimit.intValue());
		}
		Settings settings = this.getSettings();
		relayOutboundBandwidthLimit = settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
		if (relayOutboundBandwidthLimit != null) {
			return Integer.valueOf(relayOutboundBandwidthLimit.intValue());
		}
		relayOutboundBandwidthLimit = settings.getLastValue(
				Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_RELAY_OUTBOUND_BANDWIDTH_LIMIT);
		if (relayOutboundBandwidthLimit != null) {
			return Integer.valueOf(relayOutboundBandwidthLimit.intValue());
		}
		return null;
	}
	
	private DatagramSocket limitClientFacingDatagramSocket(
			final DatagramSocket clientFacingDatagramSock) {
		Integer outboundBandwidthLimit = this.getRelayOutboundBandwidthLimit();
		if (outboundBandwidthLimit != null) {
			try {
				return new BandwidthLimitedDatagramSocket(
						clientFacingDatagramSock, 
						outboundBandwidthLimit.intValue());
			} catch (SocketException e) {
				this.logger.warn( 
						ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Error in creating the bandwidth-limited "
								+ "client-facing UDP socket"), 
						e);
				return null;
			}
		}		
		return clientFacingDatagramSock;
	}
	
	private DatagramSocket limitPeerFacingDatagramSocket(
			final DatagramSocket peerFacingDatagramSock) {
		Integer inboundBandwidthLimit = this.getRelayInboundBandwidthLimit();
		if (inboundBandwidthLimit != null) {
			try {
				return new BandwidthLimitedDatagramSocket(
						peerFacingDatagramSock,
						inboundBandwidthLimit.intValue());
			} catch (SocketException e) {
				this.logger.warn( 
						ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Error in creating the bandwidth-limited "
								+ "peer-facing UDP socket"), 
						e);
				return null;
			}
		}		
		return peerFacingDatagramSock;
	}
	
	private DatagramSocket newClientFacingDatagramSocket() {
		Host clientFacingBindHost = this.getClientFacingBindHost();
		InetAddress bindInetAddress = null;
		try {
			bindInetAddress = clientFacingBindHost.toInetAddress();
		} catch (UnknownHostException e) {
			this.logger.warn( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Unable to bind the client-facing UDP socket to "
							+ "the following host: %s",
							clientFacingBindHost));
			this.sendReply(
					Reply.newFailureInstance(ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return null;
		}
		PortRanges bindPortRanges = this.getClientFacingBindPortRanges();
		DatagramSocket clientFacingDatagramSock = null;
		boolean clientFacingDatagramSockBound = false;
		for (Iterator<PortRange> iterator = bindPortRanges.toList().iterator();
				!clientFacingDatagramSockBound && iterator.hasNext();) {
			PortRange bindPortRange = iterator.next();
			for (Iterator<Port> iter = bindPortRange.iterator();
					!clientFacingDatagramSockBound && iter.hasNext();) {
				Port bindPort = iter.next();
				try {
					clientFacingDatagramSock =
							this.newClientFacingDatagramSocket(
									bindInetAddress, bindPort);
				} catch (BindException e) {
					continue;
				}
				if (clientFacingDatagramSock == null) {
					return null;
				}
				clientFacingDatagramSockBound = true;
			}
		}
		if (!clientFacingDatagramSockBound) {
			this.logger.warn( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Unable to bind the client-facing UDP socket to "
							+ "the following address and port (range(s)): "
							+ "%s %s",
							bindInetAddress,
							bindPortRanges));
			this.sendReply(
					Reply.newFailureInstance(ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return null;			
		}
		return clientFacingDatagramSock;
	}
	
	private DatagramSocket newClientFacingDatagramSocket(
			final InetAddress bindInetAddress,
			final Port bindPort) throws BindException {
		DatagramSocket clientFacingDatagramSock = null;
		try {
			clientFacingDatagramSock = new DatagramSocket(null);
		} catch (SocketException e) {
			this.logger.warn( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in creating the client-facing UDP "
							+ "socket"), 
					e);
			this.sendReply(
					Reply.newFailureInstance(ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return null;
		}
		if (!this.configureClientFacingDatagramSocket(
				clientFacingDatagramSock)) {
			clientFacingDatagramSock.close();
			return null;
		}
		try {
			clientFacingDatagramSock.bind(new InetSocketAddress(
					bindInetAddress, bindPort.intValue()));
		} catch (BindException e) {
			clientFacingDatagramSock.close();
			throw e;
		} catch (SocketException e) {
			if (ThrowableHelper.isOrHasInstanceOf(e, BindException.class)) {
				clientFacingDatagramSock.close();
				throw new BindException();
			}
			this.logger.warn( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in binding the client-facing UDP "
							+ "socket"), 
					e);
			this.sendReply(
					Reply.newFailureInstance(ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			clientFacingDatagramSock.close();
			return null;
		}
		return clientFacingDatagramSock;
	}
	
	private DatagramSocket newPeerFacingDatagramSocket() {
		Host peerFacingBindHost = this.getPeerFacingBindHost();
		InetAddress bindInetAddress = null;
		try {
			bindInetAddress = peerFacingBindHost.toInetAddress();
		} catch (UnknownHostException e) {
			this.logger.warn( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Unable to bind the peer-facing UDP socket to "
							+ "the following host: %s",
							peerFacingBindHost));
			this.sendReply(
					Reply.newFailureInstance(ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return null;
		}
		PortRanges bindPortRanges = this.getPeerFacingBindPortRanges();
		DatagramSocket peerFacingDatagramSock = null;
		boolean peerFacingDatagramSockBound = false;
		for (Iterator<PortRange> iterator = bindPortRanges.toList().iterator();
				!peerFacingDatagramSockBound && iterator.hasNext();) {
			PortRange bindPortRange = iterator.next();
			for (Iterator<Port> iter = bindPortRange.iterator();
					!peerFacingDatagramSockBound && iter.hasNext();) {
				Port bindPort = iter.next();
				try {
					peerFacingDatagramSock = 
							this.newPeerFacingDatagramSocket(
									bindInetAddress, bindPort);
				} catch (BindException e) {
					continue;
				}
				if (peerFacingDatagramSock == null) {
					return null;
				}
				peerFacingDatagramSockBound = true;
			}
		}
		if (!peerFacingDatagramSockBound) {
			this.logger.warn( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Unable to bind the peer-facing UDP socket to the "
							+ "following address and port (range(s)): %s %s",
							bindInetAddress,
							bindPortRanges));
			this.sendReply(
					Reply.newFailureInstance(ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return null;			
		}
		return peerFacingDatagramSock;
	}
	
	private DatagramSocket newPeerFacingDatagramSocket(
			final InetAddress bindInetAddress,
			final Port bindPort) throws BindException {
		NetObjectFactory netObjectFactory = 
				this.getSelectedRoute().getNetObjectFactory();
		DatagramSocket peerFacingDatagramSock = null;
		try {
			peerFacingDatagramSock =
					netObjectFactory.newDatagramSocket(null);
		} catch (SocketException e) {
			this.logger.warn( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in creating the peer-facing UDP "
							+ "socket"), 
					e);
			this.sendReply(
					Reply.newFailureInstance(ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return null;
		}
		if (!this.configurePeerFacingDatagramSocket(
				peerFacingDatagramSock)) {
			peerFacingDatagramSock.close();
			return null;
		}
		try {
			peerFacingDatagramSock.bind(new InetSocketAddress(
					bindInetAddress, bindPort.intValue()));
		} catch (BindException e) {
			peerFacingDatagramSock.close();
			throw e;
		} catch (SocketException e) {
			if (ThrowableHelper.isOrHasInstanceOf(e, BindException.class)) {
				peerFacingDatagramSock.close();
				throw new BindException();
			}
			this.logger.warn( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in binding the peer-facing UDP "
							+ "socket"), 
					e);
			this.sendReply(
					Reply.newFailureInstance(ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			peerFacingDatagramSock.close();
			return null;
		}
		return peerFacingDatagramSock;
	}

	private void passPackets(
			final UdpRelayServer udpRelayServer) throws IOException {
		try {
			udpRelayServer.start();
			try {
				while (this.getClientSocket().getInputStream().read() != -1) {
					try {
						Thread.sleep(HALF_SECOND);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			} catch (IOException e) {
			}
		} finally {
			if (!udpRelayServer.getState().equals(
					UdpRelayServer.State.STOPPED)) {
				udpRelayServer.stop();
			}
		}
	}
	
	@Override
	public void run() {
		DatagramSocket peerFacingDatagramSock = null;
		DatagramSocket clientFacingDatagramSock = null;		
		Reply rep = null;
		try {
			peerFacingDatagramSock = this.newPeerFacingDatagramSocket();
			if (peerFacingDatagramSock == null) {
				return;
			}
			clientFacingDatagramSock = this.newClientFacingDatagramSocket();
			if (clientFacingDatagramSock == null) {
				return;
			}
			/*
			 * Create a temporary variable to avoid the resource-never-closed warning
			 * (The resource will get closed)
			 */
			DatagramSocket clientFacingDatagramSck = 
					this.wrapClientFacingDatagramSocket(
							clientFacingDatagramSock);
			if (clientFacingDatagramSck == null) {
				return;
			}
			clientFacingDatagramSock = clientFacingDatagramSck; 
			InetAddress inetAddress =
					clientFacingDatagramSock.getLocalAddress();
			String serverBoundAddress = inetAddress.getHostAddress();
			int serverBoundPort = clientFacingDatagramSock.getLocalPort();
			rep = Reply.newSuccessInstance(
					Address.newInstanceFrom(serverBoundAddress),
					Port.valueOf(serverBoundPort));
			RuleContext ruleContext = this.newReplyRuleContext(
					rep);
			this.setRuleContext(ruleContext);
			Rule applicableRule = this.getRules().firstAppliesTo(
					this.getRuleContext());
			if (applicableRule == null) {
				this.logger.warn(ObjectLogMessageHelper.objectLogMessage(
						this, 
						"No applicable rule found based on the following "
						+ "context: %s",
						this.getRuleContext()));				
				this.sendReply(
						Reply.newFailureInstance(ReplyCode.CONNECTION_NOT_ALLOWED_BY_RULESET));
				return;
			}			
			this.setApplicableRule(applicableRule);
			if (!this.canAllowReply()) {
				return;
			}			
			if (!this.sendReply(rep)) {
				return;
			}
			/*
			 * Create a temporary variable to avoid the resource-never-closed warning
			 * (The resource will get closed)
			 */
			DatagramSocket peerFacingDatagramSck = 
					this.limitPeerFacingDatagramSocket(
							peerFacingDatagramSock);
			if (peerFacingDatagramSck == null) {
				return;
			}
			peerFacingDatagramSock = peerFacingDatagramSck; 
			clientFacingDatagramSock = this.limitClientFacingDatagramSocket(
					clientFacingDatagramSock);
			if (clientFacingDatagramSock == null) {
				return;
			}
			UdpRelayServer.Builder builder = new UdpRelayServer.Builder(
					this.getDesiredDestinationAddress().toString(), 
					this.getDesiredDestinationPort().intValue(),
					clientFacingDatagramSock, 
					peerFacingDatagramSock);
			builder.bufferSize(this.getRelayBufferSize());
			NetObjectFactory netObjectFactory = 
					this.getSelectedRoute().getNetObjectFactory();
			builder.hostResolver(netObjectFactory.newHostResolver());
			builder.idleTimeout(this.getRelayIdleTimeout());
			builder.ruleContext(this.getRuleContext());
			builder.rules(this.getRules());
			UdpRelayServer udpRelayServer = builder.build();
			try {
				this.passPackets(udpRelayServer);
			} catch (IOException e) {
				this.logger.warn( 
						ObjectLogMessageHelper.objectLogMessage(
								this, "Error in starting the UDP association"), 
						e);
			}
		} finally {
			if (clientFacingDatagramSock != null 
					&& !clientFacingDatagramSock.isClosed()) {
				clientFacingDatagramSock.close();
			}
			if (peerFacingDatagramSock != null 
					&& !peerFacingDatagramSock.isClosed()) {
				peerFacingDatagramSock.close();
			}
		}
	}
	
	private DatagramSocket wrapClientFacingDatagramSocket(
			final DatagramSocket clientFacingDatagramSock) {
		DatagramSocket clientFacingDatagramSck = clientFacingDatagramSock;
		DtlsDatagramSocketFactory clientFacingDtlsDatagramSocketFactory =
				this.getClientFacingDtlsDatagramSocketFactory();
		if (clientFacingDtlsDatagramSocketFactory != null) {
			try {
				clientFacingDatagramSck = 
						clientFacingDtlsDatagramSocketFactory.newDatagramSocket(
								clientFacingDatagramSck);
			} catch (IOException e) {
				this.logger.warn( 
						ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Error in wrapping the client-facing UDP socket"), 
						e);
				this.sendReply(
						Reply.newFailureInstance(ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
				return null;
			}
		}
		try {
			clientFacingDatagramSck = 
					this.getMethodSubNegotiationResults().getDatagramSocket(
							clientFacingDatagramSck);
		} catch (IOException e) {
			this.logger.warn( 
					ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Error in wrapping the client-facing UDP socket"), 
					e);
			this.sendReply(
					Reply.newFailureInstance(ReplyCode.GENERAL_SOCKS_SERVER_FAILURE));
			return null;
		}
		return clientFacingDatagramSck;
	}

}
