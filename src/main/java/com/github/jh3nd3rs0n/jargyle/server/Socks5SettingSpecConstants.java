package com.github.jh3nd3rs0n.jargyle.server;

import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.common.number.impl.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.common.text.Criteria;
import com.github.jh3nd3rs0n.jargyle.common.text.Criterion;
import com.github.jh3nd3rs0n.jargyle.common.text.CriterionMethod;
import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;
import com.github.jh3nd3rs0n.jargyle.server.settingspec.impl.BooleanSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.settingspec.impl.CriteriaSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.settingspec.impl.HostSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.settingspec.impl.MethodsSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.settingspec.impl.PositiveIntegerSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.settingspec.impl.ProtectionLevelsSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.settingspec.impl.SocketSettingsSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.settingspec.impl.Socks5RequestCriteriaSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.settingspec.impl.Socks5RequestWorkerFactorySettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.settingspec.impl.UsernamePasswordAuthenticatorSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5RequestCriteria;
import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5RequestCriterion;
import com.github.jh3nd3rs0n.jargyle.server.socks5.Socks5RequestWorkerFactory;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.UsernamePasswordAuthenticator;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.ProtectionLevels;

public final class Socks5SettingSpecConstants {

	private static final SettingSpecs SETTING_SPECS = new SettingSpecs();
	
	public static final SettingSpec<Socks5RequestCriteria> SOCKS5_ALLOWED_SOCKS5_REQUEST_CRITERIA =	
			SETTING_SPECS.addThenGet(new Socks5RequestCriteriaSettingSpec(
					NewSettingSpecPermission.INSTANCE,
					"socks5.allowedSocks5RequestCriteria",
					Socks5RequestCriteria.newInstance(
							new Socks5RequestCriterion.Builder().build())));

	public static final SettingSpec<Socks5RequestCriteria> SOCKS5_BLOCKED_SOCKS5_REQUEST_CRITERIA =	
			SETTING_SPECS.addThenGet(new Socks5RequestCriteriaSettingSpec(
					NewSettingSpecPermission.INSTANCE,
					"socks5.blockedSocks5RequestCriteria",
					Socks5RequestCriteria.getEmptyInstance()));
	
	@HelpText(
			doc = "The boolean value to indicate if the exchange of the "
					+ "GSS-API protection level negotiation must be "
					+ "unprotected according to the NEC reference "
					+ "implementation (default is false)", 
			usage = "socks5.gssapiauth.necReferenceImpl=true|false"
	)
	public static final SettingSpec<Boolean> SOCKS5_GSSAPIAUTH_NEC_REFERENCE_IMPL = 
			SETTING_SPECS.addThenGet(new BooleanSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"socks5.gssapiauth.necReferenceImpl",
					Boolean.FALSE));
	
	@HelpText(
			doc = "The space separated list of acceptable protection levels "
					+ "after GSS-API authentication (The first is preferred "
					+ "if the client does not provide a protection level that "
					+ "is acceptable.) (default is REQUIRED_INTEG_AND_CONF "
					+ "REQUIRED_INTEG NONE)", 
			usage = "socks5.gssapiauth.protectionLevels=SOCKS5_GSSAPIAUTH_PROTECTION_LEVEL1[ SOCKS5_GSSAPIAUTH_PROTECTION_LEVEL2[...]]"
	)
	public static final SettingSpec<ProtectionLevels> SOCKS5_GSSAPIAUTH_PROTECTION_LEVELS = 
			SETTING_SPECS.addThenGet(new ProtectionLevelsSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"socks5.gssapiauth.protectionLevels",
					ProtectionLevels.getDefault()));
	
	@HelpText(
			doc = "The space separated list of acceptable authentication "
					+ "methods in order of preference (default is "
					+ "NO_AUTHENTICATION_REQUIRED)", 
			usage = "socks5.methods=[SOCKS5_METHOD1[ SOCKS5_METHOD2[...]]]"
	)
	public static final SettingSpec<Methods> SOCKS5_METHODS = 
			SETTING_SPECS.addThenGet(new MethodsSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"socks5.methods",
					Methods.newInstance(Method.NO_AUTHENTICATION_REQUIRED)));
	
	@HelpText(
			doc = "The space separated list of allowed inbound address "
					+ "criteria (default is matches:.*)", 
			usage = "socks5.onBind.allowedInboundAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]"
	)
	public static final SettingSpec<Criteria> SOCKS5_ON_BIND_ALLOWED_INBOUND_ADDRESS_CRITERIA = 
			SETTING_SPECS.addThenGet(new CriteriaSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"socks5.onBind.allowedInboundAddressCriteria",
					Criteria.newInstance(Criterion.newInstance(
							CriterionMethod.MATCHES, ".*"))));
	
	@HelpText(
			doc = "The space separated list of blocked inbound address "
					+ "criteria", 
			usage = "socks5.onBind.blockedInboundAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]"
	)
	public static final SettingSpec<Criteria> SOCKS5_ON_BIND_BLOCKED_INBOUND_ADDRESS_CRITERIA = 
			SETTING_SPECS.addThenGet(new CriteriaSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"socks5.onBind.blockedInboundAddressCriteria",
					Criteria.getEmptyInstance()));

	@HelpText(
			doc = "The space separated list of socket settings for the inbound "
					+ "socket", 
			usage = "socks5.onBind.inboundSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_BIND_INBOUND_SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"socks5.onBind.inboundSocketSettings",
					SocketSettings.newInstance()));
	
	@HelpText(
			doc = "The space separated list of socket settings for the listen "
					+ "socket", 
			usage = "socks5.onBind.listenSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_BIND_LISTEN_SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"socks5.onBind.listenSocketSettings",
					SocketSettings.newInstance()));
	
	@HelpText(
			doc = "The buffer size in bytes for relaying the data (default is "
					+ "1024)", 
			usage = "socks5.onBind.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_BIND_RELAY_BUFFER_SIZE = 
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"socks5.onBind.relayBufferSize",
					PositiveInteger.newInstance(1024)));
	
	@HelpText(
			doc = "The timeout in milliseconds on relaying no data (default "
					+ "is 60000)", 
			usage = "socks5.onBind.relayIdleTimeout=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_BIND_RELAY_IDLE_TIMEOUT = 
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"socks5.onBind.relayIdleTimeout",
					PositiveInteger.newInstance(60000))); // 1 minute
	
	@HelpText(
			doc = "The boolean value to indicate if the server-facing socket "
					+ "is to be prepared before connecting (involves applying "
					+ "the specified socket settings, resolving the target "
					+ "host name, and setting the specified timeout on waiting "
					+ "to connect) (default is false)", 
			usage = "socks5.onConnect.prepareServerFacingSocket=true|false"
	)	
	public static final SettingSpec<Boolean> SOCKS5_ON_CONNECT_PREPARE_SERVER_FACING_SOCKET = 
			SETTING_SPECS.addThenGet(new BooleanSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"socks5.onConnect.prepareServerFacingSocket",
					Boolean.FALSE));
	
	@HelpText(
			doc = "The buffer size in bytes for relaying the data (default is "
					+ "1024)", 
			usage = "socks5.onConnect.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_CONNECT_RELAY_BUFFER_SIZE = 
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"socks5.onConnect.relayBufferSize",
					PositiveInteger.newInstance(1024)));
	
	@HelpText(
			doc = "The timeout in milliseconds on relaying no data (default "
					+ "is 60000)", 
			usage = "socks5.onConnect.relayIdleTimeout=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_CONNECT_RELAY_IDLE_TIMEOUT = 
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"socks5.onConnect.relayIdleTimeout",
					PositiveInteger.newInstance(60000))); // 1 minute
	
	@HelpText(
			doc = "The binding host name or address for the server-facing "
					+ "socket (default is 0.0.0.0)", 
			usage = "socks5.onConnect.serverFacingBindHost=HOST"
	)
	public static final SettingSpec<Host> SOCKS5_ON_CONNECT_SERVER_FACING_BIND_HOST = 
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"socks5.onConnect.serverFacingBindHost",
					Host.getAllZerosInet4Instance()));
	
	@HelpText(
			doc = "The timeout in milliseconds on waiting for the "
					+ "server-facing socket to connect (default is 60000)", 
			usage = "socks5.onConnect.serverFacingConnectTimeout=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_CONNECT_SERVER_FACING_CONNECT_TIMEOUT = 
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"socks5.onConnect.serverFacingConnectTimeout",
					PositiveInteger.newInstance(60000))); // 1 minute
	
	@HelpText(
			doc = "The space separated list of socket settings for the "
					+ "server-facing socket", 
			usage = "socks5.onConnect.serverFacingSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_CONNECT_SERVER_FACING_SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"socks5.onConnect.serverFacingSocketSettings",
					SocketSettings.newInstance()));
	
	@HelpText(
			doc = "The space separated list of allowed inbound address "
					+ "criteria (default is matches:.*)", 
			usage = "socks5.onUdpAssociate.allowedInboundAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]"
	)
	public static final SettingSpec<Criteria> SOCKS5_ON_UDP_ASSOCIATE_ALLOWED_INBOUND_ADDRESS_CRITERIA = 
			SETTING_SPECS.addThenGet(new CriteriaSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"socks5.onUdpAssociate.allowedInboundAddressCriteria",
					Criteria.newInstance(Criterion.newInstance(
							CriterionMethod.MATCHES, ".*"))));
	
	@HelpText(
			doc = "The space separated list of allowed outbound address "
					+ "criteria (default is matches:.*)", 
			usage = "socks5.onUdpAssociate.allowedOutboundAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]"
	)
	public static final SettingSpec<Criteria> SOCKS5_ON_UDP_ASSOCIATE_ALLOWED_OUTBOUND_ADDRESS_CRITERIA = 
			SETTING_SPECS.addThenGet(new CriteriaSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"socks5.onUdpAssociate.allowedOutboundAddressCriteria",
					Criteria.newInstance(Criterion.newInstance(
							CriterionMethod.MATCHES, ".*"))));
	
	@HelpText(
			doc = "The space separated list of blocked inbound address "
					+ "criteria", 
			usage = "socks5.onUdpAssociate.blockedInboundAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]"
	)
	public static final SettingSpec<Criteria> SOCKS5_ON_UDP_ASSOCIATE_BLOCKED_INBOUND_ADDRESS_CRITERIA = 
			SETTING_SPECS.addThenGet(new CriteriaSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"socks5.onUdpAssociate.blockedInboundAddressCriteria",
					Criteria.getEmptyInstance()));
	
	@HelpText(
			doc = "The space separated list of blocked outbound address "
					+ "criteria", 
			usage = "socks5.onUdpAssociate.blockedOutboundAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]"
	)
	public static final SettingSpec<Criteria> SOCKS5_ON_UDP_ASSOCIATE_BLOCKED_OUTBOUND_ADDRESS_CRITERIA = 
			SETTING_SPECS.addThenGet(new CriteriaSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"socks5.onUdpAssociate.blockedOutboundAddressCriteria",
					Criteria.getEmptyInstance()));
	
	@HelpText(
			doc = "The binding host name or address for the client-facing UDP "
					+ "socket (default is 0.0.0.0)", 
			usage = "socks5.onUdpAssociate.clientFacingBindHost=HOST"
	)
	public static final SettingSpec<Host> SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_BIND_HOST = 
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"socks5.onUdpAssociate.clientFacingBindHost",
					Host.getAllZerosInet4Instance()));
	
	@HelpText(
			doc = "The space separated list of socket settings for the "
					+ "client-facing UDP socket", 
			usage = "socks5.onUdpAssociate.clientFacingSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"socks5.onUdpAssociate.clientFacingSocketSettings",
					SocketSettings.newInstance()));
	
	@HelpText(
			doc = "The buffer size in bytes for relaying the data (default is "
					+ "32768)", 
			usage = "socks5.onUdpAssociate.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_UDP_ASSOCIATE_RELAY_BUFFER_SIZE = 
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"socks5.onUdpAssociate.relayBufferSize",
					PositiveInteger.newInstance(32768)));
	
	@HelpText(
			doc = "The timeout in milliseconds on relaying no data (default "
					+ "is 60000)", 
			usage = "socks5.onUdpAssociate.relayIdleTimeout=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_UDP_ASSOCIATE_RELAY_IDLE_TIMEOUT = 
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"socks5.onUdpAssociate.relayIdleTimeout",
					PositiveInteger.newInstance(60000))); // 1 minute
	
	@HelpText(
			doc = "The binding host name or address for the server-facing UDP "
					+ "socket (default is 0.0.0.0)", 
			usage = "socks5.onUdpAssociate.serverFacingBindHost=HOST"
	)
	public static final SettingSpec<Host> SOCKS5_ON_UDP_ASSOCIATE_SERVER_FACING_BIND_HOST = 
			SETTING_SPECS.addThenGet(new HostSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"socks5.onUdpAssociate.serverFacingBindHost",
					Host.getAllZerosInet4Instance()));
	
	@HelpText(
			doc = "The space separated list of socket settings for the "
					+ "server-facing UDP socket", 
			usage = "socks5.onUdpAssociate.serverFacingSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_UDP_ASSOCIATE_SERVER_FACING_SOCKET_SETTINGS = 
			SETTING_SPECS.addThenGet(new SocketSettingsSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"socks5.onUdpAssociate.serverFacingSocketSettings",
					SocketSettings.newInstance()));
	
	@HelpText(
			doc = "The SOCKS5 request worker factory for the SOCKS5 server", 
			usage = "socks5.socks5RequestWorkerFactory=CLASSNAME[:VALUE]"
	)	
	public static final SettingSpec<Socks5RequestWorkerFactory> SOCKS5_SOCKS5_REQUEST_WORKER_FACTORY = 
			SETTING_SPECS.addThenGet(new Socks5RequestWorkerFactorySettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"socks5.socks5RequestWorkerFactory",
					null));
	
	@HelpText(
			doc = "The username password authenticator for the SOCKS5 server", 
			usage = "socks5.userpassauth.usernamePasswordAuthenticator=CLASSNAME[:VALUE]"
	)
	public static final SettingSpec<UsernamePasswordAuthenticator> SOCKS5_USERPASSAUTH_USERNAME_PASSWORD_AUTHENTICATOR = 
			SETTING_SPECS.addThenGet(new UsernamePasswordAuthenticatorSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"socks5.userpassauth.usernamePasswordAuthenticator",
					null));
	
	public static List<SettingSpec<Object>> values() {
		return SETTING_SPECS.toList();
	}
	
	public static Map<String, SettingSpec<Object>> valuesMap() {
		return SETTING_SPECS.toMap();
	}
	
	private Socks5SettingSpecConstants() { }
}
