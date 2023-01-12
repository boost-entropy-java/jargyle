package com.github.jh3nd3rs0n.jargyle.server;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.StringSourceUserRepository;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Methods;

public final class ConfigurationHelper {

	public static Configuration newConfiguration(final int port) {
		return ImmutableConfiguration.newInstance(Settings.newInstance(
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.newInstance(port))));
	}
	
	public static Configuration newConfigurationUsingSocks5Userpassauth(
			final int port) {
		StringBuilder sb = new StringBuilder();
		sb.append("Aladdin:opensesame ");
		sb.append("Jasmine:mission%3Aimpossible ");
		sb.append("Abu:safeDriversSave40%25");
		return ImmutableConfiguration.newInstance(Settings.newInstance(
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.newInstance(port)),
				Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
						Methods.newInstance(Method.USERNAME_PASSWORD)),
				Socks5SettingSpecConstants.SOCKS5_USERPASSAUTH_USER_REPOSITORY.newSetting(
						new StringSourceUserRepository(sb.toString()))));
	}
	
	private ConfigurationHelper() { }
}
