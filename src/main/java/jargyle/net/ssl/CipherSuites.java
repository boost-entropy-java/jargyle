package jargyle.net.ssl;

import java.util.Arrays;
import java.util.Iterator;

public final class CipherSuites {

	public static CipherSuites newInstance(final String s) {
		if (s.isEmpty()) {
			return newInstance(new String[] { });
		}
		return newInstance(s.split(" "));
	}
	
	public static CipherSuites newInstance(final String[] suites) {
		return new CipherSuites(suites);
	}
	
	private final String[] cipherSuites;
	
	private CipherSuites(final String[] suites) {
		this.cipherSuites = Arrays.copyOf(suites, suites.length);
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
		CipherSuites other = (CipherSuites) obj;
		if (!Arrays.equals(this.cipherSuites, other.cipherSuites)) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(this.cipherSuites);
		return result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Iterator<String> iterator = Arrays.asList(this.cipherSuites).iterator(); 
				iterator.hasNext();) {
			String cipherSuite = iterator.next();
			builder.append(cipherSuite);
			if (iterator.hasNext()) {
				builder.append(' ');
			}
		}
		return builder.toString();		
	}

	public String[] toStringArray() {
		return Arrays.copyOf(this.cipherSuites, this.cipherSuites.length);
	}
	
}