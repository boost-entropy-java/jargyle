package jargyle.net.socks.client.propertyspec;

import jargyle.net.SocketSettings;
import jargyle.net.socks.client.Property;
import jargyle.net.socks.client.PropertySpec;

public final class SocketSettingsPropertySpec 
	extends PropertySpec<SocketSettings> {

	public SocketSettingsPropertySpec(
			final String s, final SocketSettings defaultVal) {
		super(s, SocketSettings.class, defaultVal);
	}

	@Override
	public Property<SocketSettings> newPropertyOfParsableValue(
			final String value) {
		return super.newProperty(SocketSettings.newInstance(value));
	}

}
