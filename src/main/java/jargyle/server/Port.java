package jargyle.server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;

public final class Port implements Comparable<Port> {

	public static final int MAX_INT_VALUE = 0xffff;
	public static final int MIN_INT_VALUE = 1;
	
	public static Port newInstance(final int i) {
		if (i < MIN_INT_VALUE || i > MAX_INT_VALUE) {
			throw new IllegalArgumentException(String.format(
					"expected an integer between %s and %s (inclusive). "
					+ "actual value is %s", 
					MIN_INT_VALUE,
					MAX_INT_VALUE,
					i));
		}
		return new Port(i);
	}
	
	public static Port newInstance(final String s) {
		int i;
		try {
			i = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(String.format(
					"expected an integer between %s and %s (inclusive). "
					+ "actual value is %s", 
					MIN_INT_VALUE,
					MAX_INT_VALUE,
					s),
					e);
		}
		return newInstance(i);
	}
	
	private final int intValue;
	
	private Port(final int i) {
		this.intValue = i;
	}
	
	@Override
	public int compareTo(final Port other) {
		return this.intValue - other.intValue;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Port)) {
			return false;
		}
		Port other = (Port) obj;
		if (this.intValue != other.intValue) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.intValue;
		return result;
	}

	public int intValue() {
		return this.intValue;
	}
	
	public boolean isAvailable() {
		ServerSocket serverSocket = null;
		DatagramSocket datagramSocket = null;
		try {
			serverSocket = new ServerSocket(this.intValue);
			serverSocket.setReuseAddress(true);
			datagramSocket = new DatagramSocket(this.intValue);
			datagramSocket.setReuseAddress(true);
			return true;
		} catch (IOException e) {
		} finally {
			if (datagramSocket != null) {
				datagramSocket.close();
			}
			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e) {
				}
			}
		}
		return false;
	}

	public String toString() {
		return Integer.toString(this.intValue);
	}
}
