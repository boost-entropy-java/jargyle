package com.github.jh3nd3rs0n.jargyle.client.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;

public final class BooleanPropertySpec extends PropertySpec<Boolean> {

	public BooleanPropertySpec(
			final Object permission, 
			final String s, 
			final Boolean defaultVal) {
		super(permission, s, Boolean.class, defaultVal);
	}

	@Override
	public Property<Boolean> newPropertyOfParsableValue(final String value) {
		return super.newProperty(Boolean.valueOf(value));
	}

}