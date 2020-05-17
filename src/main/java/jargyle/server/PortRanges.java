package jargyle.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class PortRanges {

	public static final PortRanges DEFAULT_INSTANCE = new PortRanges(
			Arrays.asList(PortRange.newInstance(
					Port.newInstance(Port.MIN_INT_VALUE), 
					Port.newInstance(Port.MAX_INT_VALUE))));
	
	public static PortRanges newInstance(
			final PortRange prtRange, final List<PortRange> prtRanges) {
		List<PortRange> list = new ArrayList<PortRange>();
		list.add(prtRange);
		list.addAll(prtRanges);
		return new PortRanges(list);
	}
	
	public static PortRanges newInstance(
			final PortRange prtRange, final PortRange... prtRanges) {
		return newInstance(prtRange, Arrays.asList(prtRanges));
	}
	
	public static PortRanges newInstance(final String s) {
		List<PortRange> prtRanges = new ArrayList<PortRange>();
		String[] sElements = s.split("\\s");
		for (String sElement : sElements) {
			prtRanges.add(PortRange.newInstance(sElement));
		}
		return new PortRanges(prtRanges);
	}
	
	private final List<PortRange> portRanges;
	
	private PortRanges(final List<PortRange> prtRanges) {
		this.portRanges = new ArrayList<PortRange>(prtRanges);
	}

	public boolean contains(final Port port) {
		for (PortRange portRange : this.portRanges) {
			if (portRange.contains(port)) {
				return true;
			}
		}
		return false;
	}
	
	public Port firstAvailablePort() {
		Port port = null;
		for (PortRange portRange : this.portRanges) {
			Port prt = portRange.firstAvailablePort();
			if (prt != null) {
				port = prt;
				break; 
			} 
		}
		return port;
	}
	
	public List<PortRange> toList() {
		return Collections.unmodifiableList(this.portRanges);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Iterator<PortRange> iterator = this.portRanges.iterator();
				iterator.hasNext();) {
			PortRange portRange = iterator.next();
			builder.append(portRange.toString());
			if (iterator.hasNext()) {
				builder.append(' ');
			}
		}
		return builder.toString();
	}
}