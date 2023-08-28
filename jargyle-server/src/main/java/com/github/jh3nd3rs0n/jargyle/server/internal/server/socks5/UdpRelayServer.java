package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jh3nd3rs0n.jargyle.client.HostResolver;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.internal.lang.ThrowableHelper;
import com.github.jh3nd3rs0n.jargyle.internal.logging.ObjectLogMessageHelper;
import com.github.jh3nd3rs0n.jargyle.internal.net.AddressHelper;
import com.github.jh3nd3rs0n.jargyle.server.FirewallAction;
import com.github.jh3nd3rs0n.jargyle.server.GeneralRuleResultSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.LogAction;
import com.github.jh3nd3rs0n.jargyle.server.Rule;
import com.github.jh3nd3rs0n.jargyle.server.RuleContext;
import com.github.jh3nd3rs0n.jargyle.server.Socks5RuleArgSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Socks5RuleConditionSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.internal.concurrent.ExecutorHelper;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.Rules;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.UdpRequestHeader;

final class UdpRelayServer {
	
	public static final class Builder {

		public static final int DEFAULT_BUFFER_SIZE = 32768;
		public static final HostResolver DEFAULT_HOST_RESOLVER = new HostResolver();
		public static final int DEFAULT_IDLE_TIMEOUT = 60000;
		public static final RuleContext DEFAULT_RULE_CONTEXT = new RuleContext();
		public static final Rules DEFAULT_RULES = Rules.newInstance(Rule.getDefault());
		
		private int bufferSize;
		private final String clientAddress;
		private final DatagramSocket clientFacingDatagramSocket;
		private final int clientPort;
		private HostResolver hostResolver;
		private int idleTimeout;
		private final DatagramSocket peerFacingDatagramSocket;
		private RuleContext ruleContext;
		private Rules rules;
		
		public Builder(
				final String clientAddr,
				final int clientPrt,
				final DatagramSocket clientFacingDatagramSock,
				final DatagramSocket peerFacingDatagramSock) {
			Objects.requireNonNull(clientAddr);
			Objects.requireNonNull(clientFacingDatagramSock);
			Objects.requireNonNull(peerFacingDatagramSock);
			if (clientPrt < 0 || clientPrt > Port.MAX_INT_VALUE) {
				throw new IllegalArgumentException(
						"client port is out of range");
			}
			this.bufferSize = DEFAULT_BUFFER_SIZE;
			this.clientAddress = clientAddr;
			this.clientFacingDatagramSocket = clientFacingDatagramSock;
			this.clientPort = clientPrt;
			this.hostResolver = DEFAULT_HOST_RESOLVER;
			this.idleTimeout = DEFAULT_IDLE_TIMEOUT;
			this.peerFacingDatagramSocket = peerFacingDatagramSock;
			this.ruleContext = new RuleContext(DEFAULT_RULE_CONTEXT);
			this.rules = DEFAULT_RULES;
		}
		
		public Builder bufferSize(final int bffrSize) {
			if (bffrSize < 0) {
				throw new IllegalArgumentException(
						"buffer size must be greater than 0");
			}
			this.bufferSize = bffrSize;
			return this;
		}
		
		public UdpRelayServer build() {
			return new UdpRelayServer(this);
		}
		
		public Builder hostResolver(final HostResolver resolver) {
			this.hostResolver = Objects.requireNonNull(resolver);
			return this;
		}
		
		public Builder idleTimeout(final int idleTmt) {
			if (idleTmt < 0) {
				throw new IllegalArgumentException(
						"idle timeout must be greater than 0");
			}
			this.idleTimeout = idleTmt;
			return this;
		}
		
		public Builder ruleContext(final RuleContext context) {
			this.ruleContext = context;
			return this;
		}
		
		public Builder rules(final Rules rls) {
			this.rules = rls;
			return this;
		}
		
	}
	
	private static final class InboundPacketsWorker	extends PacketsWorker {
		
		private static final Logger LOGGER = LoggerFactory.getLogger(
				InboundPacketsWorker.class);
		
		public InboundPacketsWorker(final PacketsWorkerContext context) {
			super(context);
		}
		
		private boolean canAllowDatagramPacket(
				final Rule applicableRule,
				final RuleContext inboundRuleContext) {
			if (applicableRule == null) {
				return false;
			}
			if (!this.canApplyRule(applicableRule)) {
				return true;
			}
			FirewallAction firewallAction = 
					applicableRule.getLastRuleResultValue(
							GeneralRuleResultSpecConstants.FIREWALL_ACTION);
			if (firewallAction == null) {
				return false;
			}
			LogAction firewallActionLogAction = 
					applicableRule.getLastRuleResultValue(
							GeneralRuleResultSpecConstants.FIREWALL_ACTION_LOG_ACTION);
			if (firewallAction.equals(FirewallAction.ALLOW)
					&& firewallActionLogAction != null) {
				firewallActionLogAction.invoke(
						LOGGER, 
						ObjectLogMessageHelper.objectLogMessage(
								this,
								"Inbound UDP packet allowed based on the "
								+ "following rule and context: rule: %s "
								+ "context: %s",
								applicableRule,
								inboundRuleContext));				
			} else if (firewallAction.equals(FirewallAction.DENY)
					&& firewallActionLogAction != null) {
				firewallActionLogAction.invoke(
						LOGGER, 
						ObjectLogMessageHelper.objectLogMessage(
								this,
								"Inbound UDP packet denied based on the "
								+ "following rule and context: rule: %s "
								+ "context: %s",
								applicableRule,
								inboundRuleContext));				
			}
			return FirewallAction.ALLOW.equals(firewallAction);
		}
		
		private boolean canApplyRule(final Rule applicableRule) {
			if (applicableRule.hasRuleCondition(
					Socks5RuleConditionSpecConstants.SOCKS5_UDP_INBOUND_DESIRED_DESTINATION_ADDRESS)) {
				return true;
			}
			if (applicableRule.hasRuleCondition(
					Socks5RuleConditionSpecConstants.SOCKS5_UDP_INBOUND_DESIRED_DESTINATION_PORT)) {
				return true;
			}
			if (applicableRule.hasRuleCondition(
					Socks5RuleConditionSpecConstants.SOCKS5_UDP_INBOUND_SOURCE_ADDRESS)) {
				return true;
			}
			if (applicableRule.hasRuleCondition(
					Socks5RuleConditionSpecConstants.SOCKS5_UDP_INBOUND_SOURCE_PORT)) {
				return true;
			}
			return false;
		}

		private boolean canSendDatagramPacket() {
			return !AddressHelper.isAllZerosAddress(
					this.packetsWorkerContext.getClientAddress())
					&& this.packetsWorkerContext.getClientPort() != 0;
		}
		
		private DatagramPacket newDatagramPacket(
				final UdpRequestHeader header) {
			byte[] headerBytes = header.toByteArray();
			InetAddress inetAddress = null;
			try {
				inetAddress = InetAddress.getByName(
						this.packetsWorkerContext.getClientAddress());
			} catch (IOException e) {
				LOGGER.error( 
						ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Error in determining the IP address from the "
								+ "client"), 
						e);
				return null;
			}
			int inetPort = this.packetsWorkerContext.getClientPort();
			return new DatagramPacket(
					headerBytes, headerBytes.length, inetAddress, inetPort);
		}
		
		private RuleContext newInboundRuleContext(
				final String peerAddr,
				final int peerPrt,
				final String clientAddr,
				final int clientPrt) {
			RuleContext inboundRuleContext = new RuleContext(this.ruleContext);
			inboundRuleContext.putRuleArgValue(
					Socks5RuleArgSpecConstants.SOCKS5_UDP_INBOUND_DESIRED_DESTINATION_ADDRESS, 
					clientAddr);
			inboundRuleContext.putRuleArgValue(
					Socks5RuleArgSpecConstants.SOCKS5_UDP_INBOUND_DESIRED_DESTINATION_PORT, 
					Port.newInstance(clientPrt));
			inboundRuleContext.putRuleArgValue(
					Socks5RuleArgSpecConstants.SOCKS5_UDP_INBOUND_SOURCE_ADDRESS, 
					peerAddr);
			inboundRuleContext.putRuleArgValue(
					Socks5RuleArgSpecConstants.SOCKS5_UDP_INBOUND_SOURCE_PORT, 
					Port.newInstance(peerPrt));
			return inboundRuleContext;
		}
		
		private UdpRequestHeader newUdpRequestHeader(
				final DatagramPacket packet) {
			String address = packet.getAddress().getHostAddress();
			int port = packet.getPort();
			UdpRequestHeader header = UdpRequestHeader.newInstance(
					0,
					address,
					port,
					Arrays.copyOfRange(
							packet.getData(), 
							packet.getOffset(), 
							packet.getLength()));
			return header;
		}
		
		@Override
		public void run() {
			while (true) {
				try {
					byte[] buffer = new byte[this.bufferSize];
					DatagramPacket packet = new DatagramPacket(
							buffer, buffer.length);
					IOException ioe = null;
					try {
						this.peerFacingDatagramSocket.receive(packet);
						this.packetsWorkerContext.setIdleStartTime(
								System.currentTimeMillis());
					} catch (IOException e) {
						ioe = e;
					}
					if (ioe != null) {
						if (ThrowableHelper.isOrHasInstanceOf(
								ioe, SocketException.class)) {
							// socket closed
							break;
						} else if (ThrowableHelper.isOrHasInstanceOf(
								ioe, SocketTimeoutException.class)) {
							long idleStartTime = 
									this.packetsWorkerContext.getIdleStartTime();
							long timeSinceIdleStartTime = 
									System.currentTimeMillis() - idleStartTime;
							if (timeSinceIdleStartTime >= this.idleTimeout) {
								LOGGER.trace(
										ObjectLogMessageHelper.objectLogMessage(
												this, 
												"Timeout reached for idle relay!"));							
								break;
							}
							continue;
						} else {
							LOGGER.error( 
									ObjectLogMessageHelper.objectLogMessage(
											this, 
											"Error in receiving the packet from "
											+ "the peer"), 
									ioe);
							continue;
						}
					}
					LOGGER.trace(ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Packet data received: %s byte(s)",
							packet.getLength()));
					RuleContext inboundRuleContext = this.newInboundRuleContext(
							packet.getAddress().getHostAddress(),
							packet.getPort(),
							this.packetsWorkerContext.getClientAddress(),
							this.packetsWorkerContext.getClientPort());
					Rule applicableRule = this.rules.firstAppliesTo(
							inboundRuleContext);
					if (!this.canAllowDatagramPacket(
							applicableRule, inboundRuleContext)) {
						continue;
					}
					if (!this.canSendDatagramPacket()) {
						continue;
					}
					UdpRequestHeader header = this.newUdpRequestHeader(packet);
					LOGGER.trace(ObjectLogMessageHelper.objectLogMessage(
							this, header.toString()));
					packet = this.newDatagramPacket(header);
					if (packet == null) {
						continue;
					}
					ioe = null;
					try {
						this.clientFacingDatagramSocket.send(packet);
					} catch (IOException e) {
						ioe = e;
					}
					if (ioe != null) {
						if (ThrowableHelper.isOrHasInstanceOf(
								ioe, SocketException.class)) {
							// socket closed
							break;
						} else {
							LOGGER.error( 
									ObjectLogMessageHelper.objectLogMessage(
											this, 
											"Error in sending the packet to the "
											+ "client"), 
									ioe);
						}
					}
				} catch (Throwable t) {
					LOGGER.error( 
							ObjectLogMessageHelper.objectLogMessage(
									this, 
									"Error occurred in the process of "
									+ "relaying of a packet from the peer to "
									+ "the client"), 
							t);
				}
			}
			this.packetsWorkerContext.stopUdpRelayServerIfNotStopped();				
		}
		
	}
	
	private static final class OutboundPacketsWorker extends PacketsWorker {
		
		private static final Logger LOGGER = LoggerFactory.getLogger(
				OutboundPacketsWorker.class);
		
		public OutboundPacketsWorker(final PacketsWorkerContext context) {
			super(context);
		}
		
		private boolean canAcceptDatagramPacket(final DatagramPacket packet) {
			String address = packet.getAddress().getHostAddress();
			int port = packet.getPort();
			String clientAddr = this.packetsWorkerContext.getClientAddress();
			if (AddressHelper.isAllZerosAddress(clientAddr)) {
				this.packetsWorkerContext.setClientAddress(address);
			} else {
				InetAddress clientInetAddr = null;
				try {
					clientInetAddr = InetAddress.getByName(clientAddr);
				} catch (IOException e) {
					LOGGER.error( 
							ObjectLogMessageHelper.objectLogMessage(
									this, 
									"Error in determining the IP address from "
									+ "the client"), 
							e);
					return false;
				}
				InetAddress inetAddr = null;
				try {
					inetAddr = InetAddress.getByName(address);
				} catch (IOException e) {
					LOGGER.error( 
							ObjectLogMessageHelper.objectLogMessage(
									this, 
									"Error in determining the IP address from "
									+ "the client"), 
							e);
					return false;
				}
				if ((!clientInetAddr.isLoopbackAddress() 
						|| !inetAddr.isLoopbackAddress())
						&& !clientInetAddr.equals(inetAddr)) {
					return false;
				}
			}
			int clientPrt = this.packetsWorkerContext.getClientPort();
			if (clientPrt == 0) {
				this.packetsWorkerContext.setClientPort(port);
			} else {
				if (clientPrt != port) {
					return false;
				}
			}
			return true;
		}
		
		private boolean canAllowDatagramPacket(
				final Rule applicableRule,
				final RuleContext outboundRuleContext) {
			if (applicableRule == null) {
				return false;
			}
			if (!this.canApplyRule(applicableRule)) {
				return true;
			}
			FirewallAction firewallAction = 
					applicableRule.getLastRuleResultValue(
							GeneralRuleResultSpecConstants.FIREWALL_ACTION);
			if (firewallAction == null) {
				return false;
			}
			LogAction firewallActionLogAction = 
					applicableRule.getLastRuleResultValue(
							GeneralRuleResultSpecConstants.FIREWALL_ACTION_LOG_ACTION);
			if (firewallAction.equals(FirewallAction.ALLOW)
					&& firewallActionLogAction != null) {
				firewallActionLogAction.invoke(
						LOGGER, 
						ObjectLogMessageHelper.objectLogMessage(
								this,
								"Outbound UDP packet allowed based on the "
								+ "following rule and context: rule: %s "
								+ "context: %s",
								applicableRule,
								outboundRuleContext));				
			} else if (firewallAction.equals(FirewallAction.DENY)
					&& firewallActionLogAction != null) {
				firewallActionLogAction.invoke(
						LOGGER, 
						ObjectLogMessageHelper.objectLogMessage(
								this,
								"Outbound UDP packet denied based on the "
								+ "following rule and context: rule: %s "
								+ "context: %s",
								applicableRule,
								outboundRuleContext));				
			}
			return FirewallAction.ALLOW.equals(firewallAction);
		}
		
		private boolean canApplyRule(final Rule applicableRule) {
			if (applicableRule.hasRuleCondition(
					Socks5RuleConditionSpecConstants.SOCKS5_UDP_OUTBOUND_DESIRED_DESTINATION_ADDRESS)) {
				return true;
			}
			if (applicableRule.hasRuleCondition(
					Socks5RuleConditionSpecConstants.SOCKS5_UDP_OUTBOUND_DESIRED_DESTINATION_PORT)) {
				return true;
			}
			if (applicableRule.hasRuleCondition(
					Socks5RuleConditionSpecConstants.SOCKS5_UDP_OUTBOUND_SOURCE_ADDRESS)) {
				return true;
			}
			if (applicableRule.hasRuleCondition(
					Socks5RuleConditionSpecConstants.SOCKS5_UDP_OUTBOUND_SOURCE_PORT)) {
				return true;
			}
			return false;
		}
		
		private DatagramPacket newDatagramPacket(
				final UdpRequestHeader header) {
			byte[] userData = header.getUserData();
			InetAddress inetAddress = null;
			try {
				inetAddress = this.hostResolver.resolve(
						header.getDesiredDestinationAddress());
			} catch (IOException e) {
				LOGGER.error( 
						ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Error in determining the IP address from the "
								+ "peer"), 
						e);
				return null;
			}
			int inetPort = header.getDesiredDestinationPort();
			return new DatagramPacket(
					userData, userData.length, inetAddress, inetPort);
		}
		
		private RuleContext newOutboundRuleContext(
				final String clientAddr,
				final int clientPrt,
				final String peerAddr,
				final int peerPrt) {
			RuleContext outboundRuleContext = new RuleContext(this.ruleContext);
			outboundRuleContext.putRuleArgValue(
					Socks5RuleArgSpecConstants.SOCKS5_UDP_OUTBOUND_DESIRED_DESTINATION_ADDRESS, 
					peerAddr);
			outboundRuleContext.putRuleArgValue(
					Socks5RuleArgSpecConstants.SOCKS5_UDP_OUTBOUND_DESIRED_DESTINATION_PORT, 
					Port.newInstance(peerPrt));
			outboundRuleContext.putRuleArgValue(
					Socks5RuleArgSpecConstants.SOCKS5_UDP_OUTBOUND_SOURCE_ADDRESS, 
					clientAddr);
			outboundRuleContext.putRuleArgValue(
					Socks5RuleArgSpecConstants.SOCKS5_UDP_OUTBOUND_SOURCE_PORT, 
					Port.newInstance(clientPrt));
			return outboundRuleContext;
		}
		
		private UdpRequestHeader newUdpRequestHeader(
				final DatagramPacket packet) {
			UdpRequestHeader header = null; 
			try {
				header = UdpRequestHeader.newInstance(Arrays.copyOfRange(
						packet.getData(), 
						packet.getOffset(), 
						packet.getLength()));
			} catch (IllegalArgumentException e) {
				LOGGER.error( 
						ObjectLogMessageHelper.objectLogMessage(
								this, 
								"Error in parsing the UDP header request from "
								+ "the client"), 
						e);
				return null;
			}
			return header;
		}
		
		@Override
		public void run() {
			while (true) {
				try {
					byte[] buffer = new byte[this.bufferSize];
					DatagramPacket packet = new DatagramPacket(
							buffer, buffer.length);
					IOException ioe = null;
					try {
						this.clientFacingDatagramSocket.receive(packet);
						this.packetsWorkerContext.setIdleStartTime(
								System.currentTimeMillis());
					} catch (IOException e) {
						ioe = e;
					}
					if (ioe != null) {
						if (ThrowableHelper.isOrHasInstanceOf(
								ioe, SocketException.class)) {
							// socket closed
							break;							
						} else if (ThrowableHelper.isOrHasInstanceOf(
								ioe, SocketTimeoutException.class)) {
							long idleStartTime = 
									this.packetsWorkerContext.getIdleStartTime();
							long timeSinceIdleStartTime = 
									System.currentTimeMillis() - idleStartTime;
							if (timeSinceIdleStartTime >= this.idleTimeout) {
								LOGGER.trace(
										ObjectLogMessageHelper.objectLogMessage(
												this, 
												"Timeout reached for idle relay!"));
								break;
							}
							continue;							
						} else {
							LOGGER.error( 
									ObjectLogMessageHelper.objectLogMessage(
											this, 
											"Error in receiving packet from the "
											+ "client"), 
									ioe);
							continue;							
						}
					}
					LOGGER.trace(ObjectLogMessageHelper.objectLogMessage(
							this, 
							"Packet data received: %s byte(s)",
							packet.getLength()));					
					if (!this.canAcceptDatagramPacket(packet)) {
						continue;
					}
					UdpRequestHeader header = this.newUdpRequestHeader(packet);
					if (header == null) {
						continue;
					}
					LOGGER.trace(ObjectLogMessageHelper.objectLogMessage(
							this, header.toString(), new Object[]{}));
					if (header.getCurrentFragmentNumber() != 0) {
						continue;
					}
					RuleContext outboundRuleContext =
							this.newOutboundRuleContext(
									this.packetsWorkerContext.getClientAddress(),
									this.packetsWorkerContext.getClientPort(),
									header.getDesiredDestinationAddress(),
									header.getDesiredDestinationPort());
					Rule applicableRule = this.rules.firstAppliesTo(
							outboundRuleContext);
					if (!this.canAllowDatagramPacket(
							applicableRule,	outboundRuleContext)) {
						continue;
					}
					packet = this.newDatagramPacket(header);
					if (packet == null) {
						continue;
					}
					ioe = null;
					try {
						this.peerFacingDatagramSocket.send(packet);
					} catch (IOException e) {
						ioe = e;
					}
					if (ioe != null) {
						if (ThrowableHelper.isOrHasInstanceOf(
								ioe, SocketException.class)) {
							// socket closed
							break;							
						} else {
							LOGGER.error( 
									ObjectLogMessageHelper.objectLogMessage(
											this, 
											"Error in sending the packet to the "
											+ "peer"), 
									ioe);							
						}
					}
				} catch (Throwable t) {
					LOGGER.error( 
							ObjectLogMessageHelper.objectLogMessage(
									this, 
									"Error occurred in the process of "
									+ "relaying of a packet from the client to "
									+ "the peer"), 
							t);
				}
			}
			this.packetsWorkerContext.stopUdpRelayServerIfNotStopped();				
		}

	}
	
	private static abstract class PacketsWorker implements Runnable {
		
		protected final int bufferSize;
		protected final DatagramSocket clientFacingDatagramSocket;
		protected final HostResolver hostResolver;
		protected final int idleTimeout;
		protected final PacketsWorkerContext packetsWorkerContext;
		protected final DatagramSocket peerFacingDatagramSocket;
		protected final RuleContext ruleContext;
		protected final Rules rules;

		public PacketsWorker(final PacketsWorkerContext context) {
			this.bufferSize = context.getBufferSize();
			this.clientFacingDatagramSocket = 
					context.getClientFacingDatagramSocket();
			this.hostResolver = context.getHostResolver();
			this.idleTimeout = context.getIdleTimeout();
			this.packetsWorkerContext = context;
			this.peerFacingDatagramSocket = 
					context.getPeerFacingDatagramSocket();
			this.ruleContext = context.getRuleContext();
			this.rules = context.getRules();
		}

		@Override
		public final String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(this.getClass().getSimpleName())
				.append(" [packetsWorkerContext=")
				.append(this.packetsWorkerContext)
				.append("]");
			return builder.toString();
		}
		
	}
	
	private static final class PacketsWorkerContext {
		
		private final DatagramSocket clientFacingDatagramSocket;
		private final DatagramSocket peerFacingDatagramSocket;
		private final UdpRelayServer udpRelayServer;
		
		public PacketsWorkerContext(final UdpRelayServer server) {
			this.clientFacingDatagramSocket = server.clientFacingDatagramSocket;
			this.peerFacingDatagramSocket = server.peerFacingDatagramSocket;
			this.udpRelayServer = server;
		}
		
		public final int getBufferSize() {
			return this.udpRelayServer.bufferSize;
		}
		
		public final String getClientAddress() {
			return this.udpRelayServer.getClientAddress();
		}
		
		public final DatagramSocket getClientFacingDatagramSocket() {
			return this.clientFacingDatagramSocket;
		}
		
		public final int getClientPort() {
			return this.udpRelayServer.getClientPort();
		}
		
		public final HostResolver getHostResolver() {
			return this.udpRelayServer.hostResolver;
		}
		
		public final long getIdleStartTime() {
			return this.udpRelayServer.getIdleStartTime();
		}
		
		public final int getIdleTimeout() {
			return this.udpRelayServer.idleTimeout;
		}
		
		public final DatagramSocket getPeerFacingDatagramSocket() {
			return this.peerFacingDatagramSocket;
		}
		
		public final RuleContext getRuleContext() {
			return this.udpRelayServer.ruleContext;
		}
		
		public final Rules getRules() {
			return this.udpRelayServer.rules;
		}
		
		public final void setClientAddress(final String address) {
			this.udpRelayServer.setClientAddress(address);
		}
		
		public final void setClientPort(final int port) {
			this.udpRelayServer.setClientPort(port);
		}
		
		public final void setIdleStartTime(final long time) {
			this.udpRelayServer.setIdleStartTime(time);
		}
		
		public final void stopUdpRelayServerIfNotStopped() {
			this.udpRelayServer.stopIfNotStopped();
		}

		@Override
		public final String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(this.getClass().getSimpleName())
				.append(" [clientFacingDatagramSocket=")
				.append(this.clientFacingDatagramSocket)
				.append(", peerFacingDatagramSocket=")
				.append(this.peerFacingDatagramSocket)
				.append("]");
			return builder.toString();
		}
		
	}
	
	public static enum State {
		
		STARTED,
		
		STOPPED
		
	}
	
	private final int bufferSize;	
	private final AtomicReference<String> clientAddress;
	private final DatagramSocket clientFacingDatagramSocket;	
	private final AtomicInteger clientPort;
	private ExecutorService executor;
	private final HostResolver hostResolver;
	private final AtomicLong idleStartTime;
	private final int idleTimeout;
	private final DatagramSocket peerFacingDatagramSocket;
	private final RuleContext ruleContext;
	private final Rules rules;
	private final AtomicReference<State> state;
	
	private UdpRelayServer(final Builder builder) {
		this.bufferSize = builder.bufferSize;
		this.clientAddress = new AtomicReference<String>(builder.clientAddress);
		this.clientFacingDatagramSocket = builder.clientFacingDatagramSocket;
		this.clientPort = new AtomicInteger(builder.clientPort);
		this.executor = null;
		this.hostResolver = builder.hostResolver;
		this.idleStartTime = new AtomicLong(0L);
		this.idleTimeout = builder.idleTimeout;
		this.peerFacingDatagramSocket = builder.peerFacingDatagramSocket;
		this.ruleContext = builder.ruleContext;
		this.rules = builder.rules;
		this.state = new AtomicReference<State>(State.STOPPED);
	}
	
	private String getClientAddress() {
		return this.clientAddress.get();
	}
	
	private int getClientPort() {
		return this.clientPort.get();
	}
	
	private long getIdleStartTime() {
		return this.idleStartTime.get();
	}
	
	public State getState() {
		return this.state.get();
	}
	
	private void setClientAddress(final String address) {
		this.clientAddress.set(address);
	}
	
	private void setClientPort(final int port) {
		this.clientPort.set(port);
	}
	
	private void setIdleStartTime(final long time) {
		this.idleStartTime.set(time);
	}
	
	public void start() throws IOException {
		if (!this.state.compareAndSet(State.STOPPED, State.STARTED)) {
			throw new IllegalStateException("UdpRelayServer already started");
		}
		this.idleStartTime.set(System.currentTimeMillis());
		this.executor = ExecutorHelper.newExecutor();
		this.executor.execute(new InboundPacketsWorker(
				new PacketsWorkerContext(this)));
		this.executor.execute(new OutboundPacketsWorker(
				new PacketsWorkerContext(this)));
	}
	
	public void stop() {
		if (!this.state.compareAndSet(State.STARTED, State.STOPPED)) {
			throw new IllegalStateException("UdpRelayServer already stopped");
		}
		this.idleStartTime.set(0L);
		this.executor.shutdownNow();
		this.executor = null;
	}
	
	private void stopIfNotStopped() {
		if (!this.state.get().equals(State.STOPPED)) {
			try {
				this.stop();
			} catch (IllegalStateException e) {
				// the other thread stopped the UDP relay server
			}
		}
	}
	
}