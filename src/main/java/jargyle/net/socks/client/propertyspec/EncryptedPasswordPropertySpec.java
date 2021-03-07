package jargyle.net.socks.client.propertyspec;

import jargyle.net.socks.client.Property;
import jargyle.net.socks.client.PropertySpec;
import jargyle.security.EncryptedPassword;

public final class EncryptedPasswordPropertySpec 
	extends PropertySpec<EncryptedPassword> {

	public EncryptedPasswordPropertySpec(
			final String s, final EncryptedPassword defaultVal) {
		super(s, EncryptedPassword.class, defaultVal);
	}

	@Override
	public Property<EncryptedPassword> newPropertyOfParsableValue(
			final String value) {
		return super.newProperty(EncryptedPassword.newInstance(
				value.toCharArray()));
	}

}
