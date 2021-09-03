package com.github.jh3nd3rs0n.jargyle.net.socks.server;

import java.io.File;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.settingspec.impl.BooleanSettingSpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.settingspec.impl.EncryptedPasswordSettingSpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.settingspec.impl.FileSettingSpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.settingspec.impl.StringSettingSpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.settingspec.impl.StringsSettingSpec;
import com.github.jh3nd3rs0n.jargyle.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.util.Strings;

public final class SslSettingSpecConstants {

	private static final SettingSpecs SETTING_SPECS = new SettingSpecs();
	
	@HelpText(
			doc = "The boolean value to indicate if SSL/TLS connections to "
					+ "the SOCKS server are enabled (default is false)",
			usage = "ssl.enabled=true|false"
	)	
	public static final SettingSpec<Boolean> SSL_ENABLED = 
			SETTING_SPECS.putThenGet(new BooleanSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"ssl.enabled",
					Boolean.FALSE));

	@HelpText(
			doc = "The space separated list of acceptable cipher suites "
					+ "enabled for SSL/TLS connections to the SOCKS server",
			usage = "ssl.enabledCipherSuites=[SSL_CIPHER_SUITE1[ SSL_CIPHER_SUITE2[...]]]"
	)	
	public static final SettingSpec<Strings> SSL_ENABLED_CIPHER_SUITES = 
			SETTING_SPECS.putThenGet(new StringsSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"ssl.enabledCipherSuites",
					Strings.newInstance(new String[] { })));
	
	@HelpText(
			doc = "The space separated list of acceptable protocol versions "
					+ "enabled for SSL/TLS connections to the SOCKS server",
			usage = "ssl.enabledProtocols=[SSL_PROTOCOL1[ SSL_PROTOCOL2[...]]]"
	)	
	public static final SettingSpec<Strings> SSL_ENABLED_PROTOCOLS = 
			SETTING_SPECS.putThenGet(new StringsSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"ssl.enabledProtocols",
					Strings.newInstance(new String[] { })));
	
	@HelpText(
			doc = "The key store file for the SSL/TLS connections to the SOCKS "
					+ "server",
			usage = "ssl.keyStoreFile=FILE"
	)	
	public static final SettingSpec<File> SSL_KEY_STORE_FILE = 
			SETTING_SPECS.putThenGet(new FileSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"ssl.keyStoreFile",
					null));
	
	@HelpText(
			doc = "The password for the key store for the SSL/TLS connections "
					+ "to the SOCKS server",
			usage = "ssl.keyStorePassword=PASSWORD"
	)	
	public static final SettingSpec<EncryptedPassword> SSL_KEY_STORE_PASSWORD = 
			SETTING_SPECS.putThenGet(new EncryptedPasswordSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"ssl.keyStorePassword",
					EncryptedPassword.newInstance(new char[] { })));
	
	@HelpText(
			doc = "The type of key store file for the SSL/TLS connections to "
					+ "the SOCKS server (default is PKCS12)",
			usage = "ssl.keyStoreType=TYPE"
	)	
	public static final SettingSpec<String> SSL_KEY_STORE_TYPE = 
			SETTING_SPECS.putThenGet(new StringSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"ssl.keyStoreType",
					"PKCS12"));
	
	@HelpText(
			doc = "The boolean value to indicate that client authentication is "
					+ "required for SSL/TLS connections to the SOCKS server "
					+ "(default is false)",
			usage = "ssl.needClientAuth=true|false"
	)	
	public static final SettingSpec<Boolean> SSL_NEED_CLIENT_AUTH = 
			SETTING_SPECS.putThenGet(new BooleanSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"ssl.needClientAuth",
					Boolean.FALSE));
	
	@HelpText(
			doc = "The protocol version for the SSL/TLS connections to the "
					+ "SOCKS server (default is TLSv1.2)",
			usage = "ssl.protocol=PROTOCOL"
	)	
	public static final SettingSpec<String> SSL_PROTOCOL = 
			SETTING_SPECS.putThenGet(new StringSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"ssl.protocol",
					"TLSv1.2"));
	
	@HelpText(
			doc = "The trust store file for the SSL/TLS connections to the "
					+ "SOCKS server",
			usage = "ssl.trustStoreFile=FILE"
	)	
	public static final SettingSpec<File> SSL_TRUST_STORE_FILE = 
			SETTING_SPECS.putThenGet(new FileSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"ssl.trustStoreFile",
					null));
	
	@HelpText(
			doc = "The password for the trust store for the SSL/TLS "
					+ "connections to the SOCKS server",
			usage = "ssl.trustStorePassword=PASSWORD"
	)	
	public static final SettingSpec<EncryptedPassword> SSL_TRUST_STORE_PASSWORD = 
			SETTING_SPECS.putThenGet(new EncryptedPasswordSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"ssl.trustStorePassword",
					EncryptedPassword.newInstance(new char[] { })));
	
	@HelpText(
			doc = "The type of trust store file for the SSL/TLS connections to "
					+ "the SOCKS server (default is PKCS12)",
			usage = "ssl.trustStoreType=TYPE"
	)		
	public static final SettingSpec<String> SSL_TRUST_STORE_TYPE = 
			SETTING_SPECS.putThenGet(new StringSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"ssl.trustStoreType",
					"PKCS12"));
	
	@HelpText(
			doc = "The boolean value to indicate that client authentication is "
					+ "requested for SSL/TLS connections to the SOCKS server "
					+ "(default is false)",
			usage = "ssl.wantClientAuth=true|false"
	)	
	public static final SettingSpec<Boolean> SSL_WANT_CLIENT_AUTH = 
			SETTING_SPECS.putThenGet(new BooleanSettingSpec(
					NewSettingSpecPermission.INSTANCE, 
					"ssl.wantClientAuth",
					Boolean.FALSE));
	
	public static Map<String, SettingSpec<Object>> valuesMap() {
		return SETTING_SPECS.toMap();
	}
	
	private SslSettingSpecConstants() { }
	
}
