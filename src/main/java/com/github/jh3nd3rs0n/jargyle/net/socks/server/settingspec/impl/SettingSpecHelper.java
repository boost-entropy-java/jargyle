package com.github.jh3nd3rs0n.jargyle.net.socks.server.settingspec.impl;

import java.io.File;

import org.ietf.jgss.Oid;

import com.github.jh3nd3rs0n.jargyle.net.Host;
import com.github.jh3nd3rs0n.jargyle.net.Port;
import com.github.jh3nd3rs0n.jargyle.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.SocksServerUri;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.v5.userpassauth.UsernamePassword;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.SettingSpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.v5.Socks5RequestCriteria;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.v5.Socks5RequestWorkerFactory;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.v5.userpassauth.UsernamePasswordAuthenticator;
import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.Methods;
import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.gssapiauth.ProtectionLevels;
import com.github.jh3nd3rs0n.jargyle.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.util.Criteria;
import com.github.jh3nd3rs0n.jargyle.util.NonnegativeInteger;
import com.github.jh3nd3rs0n.jargyle.util.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.util.Strings;

public final class SettingSpecHelper {
	
	public static SettingSpec<Boolean> newBooleanSettingSpec(
			final String s, final Boolean defaultVal) {
		return new BooleanSettingSpec(s, defaultVal);
	}

	public static SettingSpec<Criteria> newCriteriaSettingSpec(
			final String s, final Criteria defaultVal) {
		return new CriteriaSettingSpec(s, defaultVal);
	}

	public static SettingSpec<EncryptedPassword> newEncryptedPasswordSettingSpec(
			final String s, final EncryptedPassword defaultVal) {
		return new EncryptedPasswordSettingSpec(s, defaultVal);
	}

	public static SettingSpec<File> newFileSettingSpec(
			final String s, final File defaultVal) {
		return new FileSettingSpec(s, defaultVal);
	}

	public static SettingSpec<Host> newHostSettingSpec(
			final String s, final Host defaultVal) {
		return new HostSettingSpec(s, defaultVal);
	}

	public static SettingSpec<Methods> newMethodsSettingSpec(
			final String s, final Methods defaultVal) {
		return new MethodsSettingSpec(s, defaultVal);
	}

	public static SettingSpec<NonnegativeInteger> newNonnegativeIntegerSettingSpec(
			final String s, final NonnegativeInteger defaultVal) {
		return new NonnegativeIntegerSettingSpec(s, defaultVal);
	}

	public static SettingSpec<Oid> newOidSettingSpec(
			final String s, final Oid defaultVal) {
		return new OidSettingSpec(s, defaultVal);
	}

	public static SettingSpec<Port> newPortSettingSpec(
			final String s, final Port defaultVal) {
		return new PortSettingSpec(s, defaultVal);
	}

	public static SettingSpec<PositiveInteger> newPositiveIntegerSettingSpec(
			final String s, final PositiveInteger defaultVal) {
		return new PositiveIntegerSettingSpec(s, defaultVal);
	}

	public static SettingSpec<ProtectionLevels> newProtectionLevelsSettingSpec(
			final String s, final ProtectionLevels defaultVal) {
		return new ProtectionLevelsSettingSpec(s, defaultVal);
	}

	public static SettingSpec<SocketSettings> newSocketSettingsSettingSpec(
			final String s, final SocketSettings defaultVal) {
		return new SocketSettingsSettingSpec(s, defaultVal);
	}

	public static SettingSpec<Socks5RequestCriteria> newSocks5RequestCriteriaSettingSpec(
			final String s, final Socks5RequestCriteria defaultVal) {
		return new Socks5RequestCriteriaSettingSpec(s, defaultVal);
	}

	public static SettingSpec<Socks5RequestWorkerFactory> newSocks5RequestWorkerFactorySettingSpec(
			final String s, final Socks5RequestWorkerFactory defaultVal) {
		return new Socks5RequestWorkerFactorySettingSpec(s, defaultVal);
	}

	public static SettingSpec<SocksServerUri> newSocksServerUriSettingSpec(
			final String s, final SocksServerUri defaultVal) {
		return new SocksServerUriSettingSpec(s, defaultVal);
	}

	public static SettingSpec<String> newStringSettingSpec(
			final String s, final String defaultVal) {
		return new StringSettingSpec(s, defaultVal);
	}

	public static SettingSpec<Strings> newStringsSettingSpec(
			final String s, final Strings defaultVal) {
		return new StringsSettingSpec(s, defaultVal);
	}

	public static SettingSpec<UsernamePasswordAuthenticator> newUsernamePasswordAuthenticatorSettingSpec(
			final String s, final UsernamePasswordAuthenticator defaultVal) {
		return new UsernamePasswordAuthenticatorSettingSpec(s, defaultVal);
	}

	public static SettingSpec<UsernamePassword> newUsernamePasswordSettingSpec(
			final String s, final UsernamePassword defaultVal) {
		return new UsernamePasswordSettingSpec(s, defaultVal);
	}
	
	private SettingSpecHelper() { }

}