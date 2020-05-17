package jargyle.server;

public final class PortRange {

	public static PortRange newInstance(final Port prt) {
		return newInstance(prt, prt);
	}
	
	public static PortRange newInstance(final Port minPrt, final Port maxPrt) {
		if (minPrt.compareTo(maxPrt) > 0) {
			throw new IllegalArgumentException(String.format(
					"minimum port (%s) must not be greater than maximum port (%s)", 
					minPrt,
					maxPrt));
		}
		return new PortRange(minPrt, maxPrt);
	}
	
	public static PortRange newInstance(final String s) {
		String[] sElements = s.split("-");
		if (sElements.length < 1 || sElements.length > 2 
				|| (sElements.length == 1 && s.indexOf('-') != -1)) {
			throw new IllegalArgumentException(String.format(
					"port range must be either of the following formats: "
					+ "INTEGER_BETWEEN_%1$s_and_%2$s, "
					+ "INTEGER1_BETWEEN_%1$s_and_%2$s-INTEGER2_BETWEEN_%1$s_and_%2$s", 
					Port.MIN_INT_VALUE,
					Port.MAX_INT_VALUE));
		}
		if (sElements.length == 1) {
			String sElement = sElements[0];
			Port prt = null;
			try { 
				prt = Port.newInstance(sElement);
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException(String.format(
						"port range must be either of the following formats: "
						+ "INTEGER_BETWEEN_%1$s_and_%2$s, "
						+ "INTEGER1_BETWEEN_%1$s_and_%2$s-INTEGER2_BETWEEN_%1$s_and_%2$s", 
						Port.MIN_INT_VALUE,
						Port.MAX_INT_VALUE),
						e);
			}
			return newInstance(prt); 
		}
		String sElement0 = sElements[0];
		String sElement1 = sElements[1];
		Port minPrt = null;
		try {
			minPrt = Port.newInstance(sElement0);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(String.format(
					"port range must be either of the following formats: "
					+ "INTEGER_BETWEEN_%1$s_and_%2$s, "
					+ "INTEGER1_BETWEEN_%1$s_and_%2$s-INTEGER2_BETWEEN_%1$s_and_%2$s", 
					Port.MIN_INT_VALUE,
					Port.MAX_INT_VALUE),
					e);
		}
		Port maxPrt = null;
		try { 
			maxPrt = Port.newInstance(sElement1);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(String.format(
					"port range must be either of the following formats: "
					+ "INTEGER_BETWEEN_%1$s_and_%2$s, "
					+ "INTEGER1_BETWEEN_%1$s_and_%2$s-INTEGER2_BETWEEN_%1$s_and_%2$s", 
					Port.MIN_INT_VALUE,
					Port.MAX_INT_VALUE),
					e);
		}
		return newInstance(minPrt, maxPrt);
	}
	
	private final Port minPort;
	private final Port maxPort;
	
	private PortRange(final Port minPrt, final Port maxPrt) {
		this.minPort = minPrt;
		this.maxPort = maxPrt;
	}

	public boolean contains(final Port port) {
		return this.minPort.compareTo(port) <= 0 && this.maxPort.compareTo(port) >= 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof PortRange)) {
			return false;
		}
		PortRange other = (PortRange) obj;
		if (this.maxPort == null) {
			if (other.maxPort != null) {
				return false;
			}
		} else if (!this.maxPort.equals(other.maxPort)) {
			return false;
		}
		if (this.minPort == null) {
			if (other.minPort != null) {
				return false;
			}
		} else if (!this.minPort.equals(other.minPort)) {
			return false;
		}
		return true;
	}

	public Port firstAvailablePort() {
		Port port = null;
		for (int i = this.minPort.intValue(); i <= this.maxPort.intValue(); i++) {
			Port prt = Port.newInstance(i);
			if (prt.isAvailable()) {
				port = prt;
				break; 
			}
		}
		return port;
	}
	
	public Port getMaxPort() {
		return this.maxPort;
	}

	public Port getMinPort() {
		return this.minPort;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.maxPort == null) ? 0 : this.maxPort.hashCode());
		result = prime * result + ((this.minPort == null) ? 0 : this.minPort.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		if (this.minPort.equals(this.maxPort)) {
			return this.minPort.toString();
		}
		return String.format("%s-%s", this.minPort, this.maxPort);
	}
	
}