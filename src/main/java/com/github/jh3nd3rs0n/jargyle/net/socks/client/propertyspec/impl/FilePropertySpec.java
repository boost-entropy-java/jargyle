package com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl;

import java.io.File;

import com.github.jh3nd3rs0n.jargyle.net.socks.client.Property;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.PropertySpec;

public final class FilePropertySpec extends PropertySpec<File> {

	public FilePropertySpec(
			final Object permissionObj, 
			final String s, 
			final File defaultVal) {
		super(permissionObj, s, File.class, defaultVal);
	}

	@Override
	public Property<File> newProperty(final File value) {
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
		return super.newProperty(value);
	}

	@Override
	public Property<File> newPropertyOfParsableValue(final String value) {
		return newProperty(new File(value));
	}

}
