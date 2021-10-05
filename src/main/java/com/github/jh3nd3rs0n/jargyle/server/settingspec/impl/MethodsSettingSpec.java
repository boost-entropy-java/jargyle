package com.github.jh3nd3rs0n.jargyle.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Methods;

public final class MethodsSettingSpec extends SettingSpec<Methods> {

	public MethodsSettingSpec(
			final Object permission, 
			final String s, 
			final Methods defaultVal) {
		super(permission, s, Methods.class, defaultVal);
	}

	@Override
	public Setting<Methods> newSettingOfParsableValue(final String value) {
		return super.newSetting(Methods.newInstance(value));
	}
	
}
