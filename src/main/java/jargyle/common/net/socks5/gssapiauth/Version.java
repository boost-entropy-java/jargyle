package jargyle.common.net.socks5.gssapiauth;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import jargyle.common.util.UnsignedByte;

public enum Version {

	V1((byte) 0x01);
	
	public static Version valueOf(final byte b) {
		for (Version version : Version.values()) {
			if (version.byteValue() == b) {
				return version;
			}
		}
		StringBuilder sb = new StringBuilder();
		List<Version> list = Arrays.asList(Version.values());
		for (Iterator<Version> iterator = list.iterator();
				iterator.hasNext();) {
			Version value = iterator.next();
			byte byteValue = value.byteValue();
			sb.append(Integer.toHexString(
					UnsignedByte.newInstance(byteValue).intValue()));
			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}
		throw new IllegalArgumentException(
				String.format(
						"expected version must be one of the following "
						+ "values: %s. actual value is %s",
						sb.toString(),
						Integer.toHexString(
								UnsignedByte.newInstance(b).intValue())));
	}
	
	private final byte byteValue;
	
	private Version(final byte bValue) {
		this.byteValue = bValue;
	}
	
	public byte byteValue() {
		return this.byteValue;
	}
	
}
