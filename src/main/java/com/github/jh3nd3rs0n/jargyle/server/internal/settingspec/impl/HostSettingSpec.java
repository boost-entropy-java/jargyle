package com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl;

import java.net.UnknownHostException;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class HostSettingSpec extends SettingSpec<Host> {

	public HostSettingSpec(final String s, final Host defaultVal) {
		super(s, Host.class, defaultVal);
	}

	@Override
	public Setting<Host> newSettingOfParsableValue(final String value) {
		Host host = null;
		try {
			host = Host.newInstance(value);
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException(e);
		}
		return super.newSetting(host);
	}

}