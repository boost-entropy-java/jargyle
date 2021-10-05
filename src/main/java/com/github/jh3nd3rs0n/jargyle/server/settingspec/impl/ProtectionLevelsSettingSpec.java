package com.github.jh3nd3rs0n.jargyle.server.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.ProtectionLevels;

public final class ProtectionLevelsSettingSpec 
	extends SettingSpec<ProtectionLevels> {

	public ProtectionLevelsSettingSpec(
			final Object permission, 
			final String s, 
			final ProtectionLevels defaultVal) {
		super(permission, s, ProtectionLevels.class, defaultVal);
	}

	@Override
	public Setting<ProtectionLevels> newSettingOfParsableValue(
			final String value) {
		return super.newSetting(ProtectionLevels.newInstance(value));
	}

}