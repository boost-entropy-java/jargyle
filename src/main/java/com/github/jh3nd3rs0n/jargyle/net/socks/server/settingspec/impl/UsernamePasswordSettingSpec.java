package com.github.jh3nd3rs0n.jargyle.net.socks.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.net.socks.client.v5.userpassauth.UsernamePassword;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.Setting;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.SettingSpec;

final class UsernamePasswordSettingSpec 
	extends SettingSpec<UsernamePassword> {

	public UsernamePasswordSettingSpec(
			final String s, final UsernamePassword defaultVal) {
		super(s, UsernamePassword.class, defaultVal);
	}

	@Override
	public Setting<UsernamePassword> newSettingOfParsableValue(
			final String value) {
		return super.newSetting(UsernamePassword.newInstance(value));
	}
	
}