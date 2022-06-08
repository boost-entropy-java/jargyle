package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.SocksServerUri;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class SocksServerUriSettingSpec 
	extends SettingSpec<SocksServerUri> {

	public SocksServerUriSettingSpec(
			final String s, final SocksServerUri defaultVal) {
		super(s, SocksServerUri.class, defaultVal);
	}

	@Override
	public Setting<SocksServerUri> newSettingOfParsableValue(
			final String value) {
		return super.newSetting(SocksServerUri.newInstance(value));
	}

}