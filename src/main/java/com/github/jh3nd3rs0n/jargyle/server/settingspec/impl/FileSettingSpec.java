package com.github.jh3nd3rs0n.jargyle.server.settingspec.impl;

import java.io.File;

import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;

public final class FileSettingSpec extends SettingSpec<File> {

	public FileSettingSpec(
			final Object permission, 
			final String s, 
			final File defaultVal) {
		super(permission, s, File.class, defaultVal);
	}

	@Override
	public Setting<File> newSetting(final File value) {
		if (!value.exists()) {
			throw new IllegalArgumentException(String.format(
					"file `%s' does not exist", 
					value));
		}
		if (!value.isFile()) {
			throw new IllegalArgumentException(String.format(
					"file `%s' must be a file", 
					value));
		}
		return super.newSetting(value);
	}

	@Override
	public Setting<File> newSettingOfParsableValue(final String value) {
		return newSetting(new File(value));
	}

}
