package jargyle.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class Host {
	
	public static final Host INET_4_ALL_ZEROS_INSTANCE = Host.newInstance(
			InetAddressHelper.getInet4AllZerosAddress());
	
	public static final Host INET_6_ALL_ZEROS_INSTANCE = Host.newInstance(
			InetAddressHelper.getInet6AllZerosAddress());
	
	private static Host newInstance(final InetAddress inetAddress) {
		return new Host(inetAddress, inetAddress.getHostAddress());
	}
	
	public static Host newInstance(final String s) throws UnknownHostException {
		InetAddress inetAddress = InetAddress.getByName(s);
		return new Host(inetAddress, s);
	}

	private final InetAddress inetAddress;

	private final String string;
	
	private Host(final InetAddress inetAddr, final String str) {
		this.inetAddress = inetAddr;
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
		Host other = (Host) obj;
		if (this.string == null) {
			if (other.string != null) {
				return false;
			}
		} else if (!this.string.equals(other.string)) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.string == null) ? 0 : this.string.hashCode());
		return result;
	}
	
	public InetAddress toInetAddress() {
		return this.inetAddress;
	}
	
	@Override
	public String toString() {
		return this.string;
	}
	
}
