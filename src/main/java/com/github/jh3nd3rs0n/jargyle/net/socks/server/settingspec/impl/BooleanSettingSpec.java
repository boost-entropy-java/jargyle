package com.github.jh3nd3rs0n.jargyle.net.socks.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.net.socks.server.Setting;
import com.github.jh3nd3rs0n.jargyle.net.socks.server.SettingSpec;

public final class BooleanSettingSpec extends SettingSpec<Boolean> {

	public BooleanSettingSpec(
			final Object permissionObj, 
			final String s, 
			final Boolean defaultVal) {
		super(permissionObj, s, Boolean.class, defaultVal);
	}

	@Override
	public Setting<Boolean> newSettingOfParsableValue(final String value) {
		return super.newSetting(Boolean.valueOf(value));
	}	

}
