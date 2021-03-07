package jargyle.net.socks.server.settingspec;

import jargyle.net.socks.client.v5.UsernamePassword;
import jargyle.net.socks.server.Setting;
import jargyle.net.socks.server.SettingSpec;

public final class UsernamePasswordSettingSpec 
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
