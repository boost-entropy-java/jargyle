package jargyle.net.socks.transport.v5;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import jargyle.util.UnsignedByte;

public final class Address {

	public static Address newInstance(final String string) {
		AddressType addressType = AddressType.valueForString(string);
		return addressType.newAddress(string);
	}
	
	public static Address newInstanceFrom(
			final InputStream in) throws IOException {
		int b = in.read();
		AddressType addressType = null;
		try {
			addressType = AddressType.valueOfByte(
					(byte) UnsignedByte.newInstance(b).intValue());
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
		return addressType.newAddressFrom(in);
	}
	
	private final AddressType addressType;
	private final byte[] byteArray;
	private final String string;
	
	Address(final AddressType type, final byte[] bytes, final String str) {
		this.addressType = type;
		this.byteArray = Arrays.copyOf(bytes, bytes.length);
		this.string = str;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Address other = (Address) obj;
		if (this.addressType != other.addressType) {
			return false;
		}
		if (!Arrays.equals(this.byteArray, other.byteArray)) {
			return false;
		}
		return true;
	}

	public AddressType getAddressType() {
		return this.addressType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.addressType == null) ? 
				0 : this.addressType.hashCode());
		result = prime * result + Arrays.hashCode(this.byteArray);
		return result;
	}
	
	public byte[] toByteArray() {
		return Arrays.copyOf(this.byteArray, this.byteArray.length);
	}
	
	@Override
	public String toString() {
		return this.string;
	}
	
}
