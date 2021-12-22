package com.github.jh3nd3rs0n.jargyle.server;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.ietf.jgss.Oid;

import com.github.jh3nd3rs0n.jargyle.client.DtlsPropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.GeneralPropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.client.SocksServerUri;
import com.github.jh3nd3rs0n.jargyle.client.SslPropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.socks5.userpassauth.UsernamePassword;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.common.number.impl.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.common.text.Strings;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.ProtectionLevels;

public final class Routes {
	
	private static abstract class SettingConverter {
		
		public abstract Object convert(final Setting<Object> setting);
		
	}
	
	private static final Map<SettingSpec<? extends Object>, SettingConverter> SETTING_CONVERTER_MAP =
			new HashMap<SettingSpec<? extends Object>, SettingConverter>();
	
	private static void fillSettingConverterMapIfEmpty() {
		if (!SETTING_CONVERTER_MAP.isEmpty()) {
			return;
		}
		SETTING_CONVERTER_MAP.put(
				ChainingGeneralSettingSpecConstants.CHAINING_SOCKS_SERVER_URI, 
				new SettingConverter() {

					@Override
					public Object convert(final Setting<Object> setting) {
						SocksServerUri socksServerUri = 
								(SocksServerUri) setting.getValue();
						return socksServerUri;
					}
					
				});
		putChainingDtlsSettingConverters();
		putChainingDtlsKeyStoreSettingConverters();
		putChainingDtlsTrustStoreSettingConverters();
		putChainingGeneralSettingConverters();
		putChainingSocks5SettingConverters();
		putChainingSocks5GssapiAuthSettingConverters();
		putChainingSslSettingConverters();
		putChainingSslKeyStoreSettingConverters();
		putChainingSslTrustStoreSettingConverters();
	}
	
	private static Map<SettingSpec<? extends Object>, SettingConverter> getSettingConverterMap() {
		fillSettingConverterMapIfEmpty();
		return SETTING_CONVERTER_MAP;
	}
	
	public static Routes newInstance(final Configuration configuration) {
		List<Route> routes = new ArrayList<Route>();
		Settings settings = configuration.getSettings();
		Map<SettingSpec<? extends Object>, SettingConverter> settingConverterMap =
				getSettingConverterMap();
		SocksServerUri socksServerUri = null;
		List<Property<? extends Object>> properties = 
				new ArrayList<Property<? extends Object>>();
		SocksClient chainedSocksClient = null;
		String routeId = null;
		for (Setting<Object> setting : settings.toList()) {
			SettingSpec<Object> settingSpec = setting.getSettingSpec();
			if (settingSpec.equals(
					ChainingGeneralSettingSpecConstants.CHAINING_ROUTE_ID)) {
				routeId = (String) setting.getValue();
				continue;
			}
			SettingConverter settingConverter = settingConverterMap.get(
					settingSpec);
			if (settingConverter == null) {
				continue;
			}
			Object obj = settingConverter.convert(setting);
			if (obj instanceof SocksServerUri) {
				SocksServerUri serverUri = (SocksServerUri) obj;
				if (socksServerUri != null) {
					chainedSocksClient = socksServerUri.newSocksClient(
							Properties.newInstance(properties), 
							chainedSocksClient);
					properties.clear();
					if (routeId != null) {
						routes.add(new Route(
								routeId,
								chainedSocksClient.newSocksNetObjectFactory()));
						chainedSocksClient = null;
						routeId = null;
					}
				}
				socksServerUri = serverUri;
				continue;
			}
			if (obj instanceof Property) {
				@SuppressWarnings("unchecked")
				Property<Object> prop = (Property<Object>) obj;
				properties.add(prop);
				continue;
			}
			if (obj instanceof List) {
				@SuppressWarnings("unchecked")
				List<Property<Object>> props = (List<Property<Object>>) obj;
				properties.addAll(props);
			}
		}
		String lastRouteId = settings.getLastValue(
				GeneralSettingSpecConstants.LAST_ROUTE_ID);
		if (socksServerUri == null) {
			routes.add(new Route(lastRouteId, NetObjectFactory.newInstance()));
		} else {
			SocksClient socksClient = socksServerUri.newSocksClient(
					Properties.newInstance(properties), chainedSocksClient);
			if (routeId == null) {
				routes.add(new Route(
						lastRouteId, socksClient.newSocksNetObjectFactory()));
			} else {
				routes.add(new Route(
						routeId, socksClient.newSocksNetObjectFactory()));
				routes.add(new Route(
						lastRouteId, NetObjectFactory.newInstance()));				
			}
		}
		return newInstance(routes);
	}
	
	public static Routes newInstance(final List<Route> rtes) {
		return new Routes(rtes);
	}
	
	public static Routes newInstance(final Routes other) {
		return new Routes(other);
	}
	
	private static void putChainingDtlsKeyStoreSettingConverters() {
		SETTING_CONVERTER_MAP.put(
				ChainingDtlsSettingSpecConstants.CHAINING_DTLS_KEY_STORE_FILE, 
				new SettingConverter() {

					@Override
					public Object convert(final Setting<Object> setting) {
						File dtlsKeyStoreFile = (File) setting.getValue();
						return DtlsPropertySpecConstants.DTLS_KEY_STORE_FILE.newProperty(
								dtlsKeyStoreFile);
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				ChainingDtlsSettingSpecConstants.CHAINING_DTLS_KEY_STORE_PASSWORD, 
				new SettingConverter() {

					@Override
					public Object convert(final Setting<Object> setting) {
						EncryptedPassword dtlsKeyStorePassword = 
								(EncryptedPassword) setting.getValue();
						return DtlsPropertySpecConstants.DTLS_KEY_STORE_PASSWORD.newProperty(
								dtlsKeyStorePassword);
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				ChainingDtlsSettingSpecConstants.CHAINING_DTLS_KEY_STORE_TYPE, 
				new SettingConverter() {

					@Override
					public Object convert(final Setting<Object> setting) {
						String dtlsKeyStoreType = (String) setting.getValue();
						return DtlsPropertySpecConstants.DTLS_KEY_STORE_TYPE.newProperty(
								dtlsKeyStoreType);
					}
					
				});
	}
	
	private static void putChainingDtlsSettingConverters() {
		SETTING_CONVERTER_MAP.put(
				ChainingDtlsSettingSpecConstants.CHAINING_DTLS_ENABLED, 
				new SettingConverter() {

					@Override
					public Object convert(final Setting<Object> setting) {
						Boolean dtlsEnabled = (Boolean) setting.getValue();
						return DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(
								dtlsEnabled);
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				ChainingDtlsSettingSpecConstants.CHAINING_DTLS_ENABLED_CIPHER_SUITES, 
				new SettingConverter() {

					@Override
					public Object convert(final Setting<Object> setting) {
						Strings dtlsEnabledCipherSuites = 
								(Strings) setting.getValue();
						return DtlsPropertySpecConstants.DTLS_ENABLED_CIPHER_SUITES.newProperty(
								dtlsEnabledCipherSuites);
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				ChainingDtlsSettingSpecConstants.CHAINING_DTLS_ENABLED_PROTOCOLS, 
				new SettingConverter() {

					@Override
					public Object convert(final Setting<Object> setting) {
						Strings dtlsEnabledProtocols = 
								(Strings) setting.getValue();
						return DtlsPropertySpecConstants.DTLS_ENABLED_PROTOCOLS.newProperty(
								dtlsEnabledProtocols);
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				ChainingDtlsSettingSpecConstants.CHAINING_DTLS_MAX_PACKET_SIZE, 
				new SettingConverter() {

					@Override
					public Object convert(final Setting<Object> setting) {
						PositiveInteger dtlsMaxPacketSize = 
								(PositiveInteger) setting.getValue();
						return DtlsPropertySpecConstants.DTLS_MAX_PACKET_SIZE.newProperty(
								dtlsMaxPacketSize);
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				ChainingDtlsSettingSpecConstants.CHAINING_DTLS_PROTOCOL, 
				new SettingConverter() {

					@Override
					public Object convert(final Setting<Object> setting) {
						String dtlsProtocol = (String) setting.getValue();
						return DtlsPropertySpecConstants.DTLS_PROTOCOL.newProperty(
								dtlsProtocol);
					}
					
				});
	}
	
	private static void putChainingDtlsTrustStoreSettingConverters() {
		SETTING_CONVERTER_MAP.put(
				ChainingDtlsSettingSpecConstants.CHAINING_DTLS_TRUST_STORE_FILE, 
				new SettingConverter() {

					@Override
					public Object convert(final Setting<Object> setting) {
						File dtlsTrustStoreFile = (File) setting.getValue();
						return DtlsPropertySpecConstants.DTLS_TRUST_STORE_FILE.newProperty(
								dtlsTrustStoreFile);
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				ChainingDtlsSettingSpecConstants.CHAINING_DTLS_TRUST_STORE_PASSWORD, 
				new SettingConverter() {

					@Override
					public Object convert(final Setting<Object> setting) {
						EncryptedPassword dtlsTrustStorePassword = 
								(EncryptedPassword) setting.getValue();
						return DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newProperty(
								dtlsTrustStorePassword);
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				ChainingDtlsSettingSpecConstants.CHAINING_DTLS_TRUST_STORE_TYPE, 
				new SettingConverter() {

					@Override
					public Object convert(final Setting<Object> setting) {
						String dtlsTrustStoreType = (String) setting.getValue();
						return DtlsPropertySpecConstants.DTLS_TRUST_STORE_TYPE.newProperty(
								dtlsTrustStoreType);
					}
					
				});		
	}
	
	private static void putChainingGeneralSettingConverters() {
		SETTING_CONVERTER_MAP.put(
				ChainingGeneralSettingSpecConstants.CHAINING_INTERNAL_BIND_HOST, 
				new SettingConverter() {

					@Override
					public Object convert(final Setting<Object> setting) {
						Host internalBindHost = (Host) setting.getValue();
						return GeneralPropertySpecConstants.INTERNAL_BIND_HOST.newProperty(
								internalBindHost);
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				ChainingGeneralSettingSpecConstants.CHAINING_INTERNAL_CONNECT_TIMEOUT, 
				new SettingConverter() {

					@Override
					public Object convert(final Setting<Object> setting) {
						PositiveInteger internalConnectTimeout = 
								(PositiveInteger) setting.getValue();
						return GeneralPropertySpecConstants.INTERNAL_CONNECT_TIMEOUT.newProperty(
								internalConnectTimeout);
					} 
					
				});
		SETTING_CONVERTER_MAP.put(
				ChainingGeneralSettingSpecConstants.CHAINING_INTERNAL_SOCKET_SETTINGS, 
				new SettingConverter() {

					@Override
					public Object convert(final Setting<Object> setting) {
						SocketSettings internalSocketSettings = 
								(SocketSettings) setting.getValue();
						return GeneralPropertySpecConstants.INTERNAL_SOCKET_SETTINGS.newProperty(
								internalSocketSettings);
					}
					
				});
	}
	
	private static void putChainingSocks5GssapiAuthSettingConverters() {
		SETTING_CONVERTER_MAP.put(
				ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_GSSAPIAUTH_MECHANISM_OID, 
				new SettingConverter() {

					@Override
					public Object convert(final Setting<Object> setting) {
						Oid mechanismOid = (Oid) setting.getValue();
						return Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTH_MECHANISM_OID.newProperty(
								mechanismOid);
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_GSSAPIAUTH_NEC_REFERENCE_IMPL, 
				new SettingConverter() {

					@Override
					public Object convert(final Setting<Object> setting) {
						Boolean necReferenceImpl = (Boolean) setting.getValue();
						return Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTH_NEC_REFERENCE_IMPL.newProperty(
								necReferenceImpl);
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_GSSAPIAUTH_PROTECTION_LEVELS, 
				new SettingConverter() {

					@Override
					public Object convert(final Setting<Object> setting) {
						ProtectionLevels protectionLevels =	
								(ProtectionLevels) setting.getValue();
						return Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTH_PROTECTION_LEVELS.newProperty(
								protectionLevels);
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_GSSAPIAUTH_SERVICE_NAME, 
				new SettingConverter() {

					@Override
					public Object convert(final Setting<Object> setting) {
						String serviceName = (String) setting.getValue();
						return Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTH_SERVICE_NAME.newProperty(
								serviceName);
					}
					
				});
	}
	
	private static void putChainingSocks5SettingConverters() {
		SETTING_CONVERTER_MAP.put(
				ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_METHODS, 
				new SettingConverter() {

					@Override
					public Object convert(final Setting<Object> setting) {
						Methods methods = (Methods) setting.getValue();
						return Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(methods);
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_RESOLVE_USE_RESOLVE_COMMAND, 
				new SettingConverter() {

					@Override
					public Object convert(final Setting<Object> setting) {
						Boolean useResolveCommand =
								(Boolean) setting.getValue();
						return Socks5PropertySpecConstants.SOCKS5_RESOLVE_USE_RESOLVE_COMMAND.newProperty(
								useResolveCommand);
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_USERPASSAUTH_USERNAME_PASSWORD, 
				new SettingConverter() {

					@Override
					public Object convert(final Setting<Object> setting) {
						List<Property<Object>> properties = 
								new ArrayList<Property<Object>>();
						UsernamePassword usernamePassword = 
								(UsernamePassword) setting.getValue();
						Property<? extends Object> username = 
								Socks5PropertySpecConstants.SOCKS5_USERPASSAUTH_USERNAME.newProperty(
										usernamePassword.getUsername());
						Property<? extends Object> password =
								Socks5PropertySpecConstants.SOCKS5_USERPASSAUTH_PASSWORD.newProperty(
										usernamePassword.getEncryptedPassword());
						@SuppressWarnings("unchecked")
						Property<Object> usrnm = (Property<Object>) username;
						@SuppressWarnings("unchecked")
						Property<Object> psswrd = (Property<Object>) password;
						properties.add(usrnm);
						properties.add(psswrd);
						return properties;
					}
					
				});
	}
	
	private static void putChainingSslKeyStoreSettingConverters() {
		SETTING_CONVERTER_MAP.put(
				ChainingSslSettingSpecConstants.CHAINING_SSL_KEY_STORE_FILE, 
				new SettingConverter() {

					@Override
					public Object convert(final Setting<Object> setting) {
						File sslKeyStoreFile = (File) setting.getValue();
						return SslPropertySpecConstants.SSL_KEY_STORE_FILE.newProperty(
								sslKeyStoreFile);
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				ChainingSslSettingSpecConstants.CHAINING_SSL_KEY_STORE_PASSWORD, 
				new SettingConverter() {

					@Override
					public Object convert(final Setting<Object> setting) {
						EncryptedPassword sslKeyStorePassword = 
								(EncryptedPassword) setting.getValue();
						return SslPropertySpecConstants.SSL_KEY_STORE_PASSWORD.newProperty(
								sslKeyStorePassword);
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				ChainingSslSettingSpecConstants.CHAINING_SSL_KEY_STORE_TYPE, 
				new SettingConverter() {

					@Override
					public Object convert(final Setting<Object> setting) {
						String sslKeyStoreType = (String) setting.getValue();
						return SslPropertySpecConstants.SSL_KEY_STORE_TYPE.newProperty(
								sslKeyStoreType);
					}
					
				});
	}
	
	private static void putChainingSslSettingConverters() {
		SETTING_CONVERTER_MAP.put(
				ChainingSslSettingSpecConstants.CHAINING_SSL_ENABLED, 
				new SettingConverter() {

					@Override
					public Object convert(final Setting<Object> setting) {
						Boolean sslEnabled = (Boolean) setting.getValue();
						return SslPropertySpecConstants.SSL_ENABLED.newProperty(
								sslEnabled);
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				ChainingSslSettingSpecConstants.CHAINING_SSL_ENABLED_CIPHER_SUITES, 
				new SettingConverter() {

					@Override
					public Object convert(final Setting<Object> setting) {
						Strings sslEnabledCipherSuites =
								(Strings) setting.getValue();
						return SslPropertySpecConstants.SSL_ENABLED_CIPHER_SUITES.newProperty(
								sslEnabledCipherSuites);
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				ChainingSslSettingSpecConstants.CHAINING_SSL_ENABLED_PROTOCOLS, 
				new SettingConverter() {

					@Override
					public Object convert(final Setting<Object> setting) {
						Strings sslEnabledProtocols = 
								(Strings) setting.getValue();
						return SslPropertySpecConstants.SSL_ENABLED_PROTOCOLS.newProperty(
								sslEnabledProtocols);
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				ChainingSslSettingSpecConstants.CHAINING_SSL_PROTOCOL, 
				new SettingConverter() {

					@Override
					public Object convert(final Setting<Object> setting) {
						String sslProtocol = (String) setting.getValue();
						return SslPropertySpecConstants.SSL_PROTOCOL.newProperty(
								sslProtocol);
					}
					
				});
	}
	
	private static void putChainingSslTrustStoreSettingConverters() {
		SETTING_CONVERTER_MAP.put(
				ChainingSslSettingSpecConstants.CHAINING_SSL_TRUST_STORE_FILE, 
				new SettingConverter() {

					@Override
					public Object convert(final Setting<Object> setting) {
						File sslTrustStoreFile = (File) setting.getValue();
						return SslPropertySpecConstants.SSL_TRUST_STORE_FILE.newProperty(
								sslTrustStoreFile);
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				ChainingSslSettingSpecConstants.CHAINING_SSL_TRUST_STORE_PASSWORD, 
				new SettingConverter() {

					@Override
					public Object convert(final Setting<Object> setting) {
						EncryptedPassword sslTrustStorePassword = 
								(EncryptedPassword) setting.getValue();
						return SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newProperty(
								sslTrustStorePassword);
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				ChainingSslSettingSpecConstants.CHAINING_SSL_TRUST_STORE_TYPE, 
				new SettingConverter() {

					@Override
					public Object convert(final Setting<Object> setting) {
						String sslTrustStoreType = (String) setting.getValue();
						return SslPropertySpecConstants.SSL_TRUST_STORE_TYPE.newProperty(
								sslTrustStoreType);
					}
					
				});		
	}
	
	private final Map<String, Route> routes;
	
	private Routes(final List<Route> rtes) {
		Map<String, Route> map = new LinkedHashMap<String, Route>();
		for (Route rte : rtes) {
			String rteId = rte.getRouteId();
			if (map.containsKey(rteId)) {
				map.remove(rteId);
			}
			map.put(rteId, rte);
		}
		this.routes = map;
	}
	
	private Routes(final Routes other) {
		this.routes = new LinkedHashMap<String, Route>(other.routes); 
	}

	public Route get(final String routeId) {
		return this.routes.get(routeId);
	}

	public Map<String, Route> toMap() {
		return Collections.unmodifiableMap(this.routes);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [routes=")
			.append(this.routes)
			.append("]");
		return builder.toString();
	}
	
}
