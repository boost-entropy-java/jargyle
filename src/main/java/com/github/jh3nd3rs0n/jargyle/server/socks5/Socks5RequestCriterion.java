package com.github.jh3nd3rs0n.jargyle.server.socks5;

import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRanges;
import com.github.jh3nd3rs0n.jargyle.common.text.Criterion;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Socks5Request;

public final class Socks5RequestCriterion {

	public static final class Builder {
		
		private Criterion clientAddressCriterion;
		private Criterion commandCriterion;
		private Criterion desiredDestinationAddressCriterion;
		private PortRanges desiredDestinationPortRanges;
		private String doc;		
		
		public Builder() {
			this.clientAddressCriterion = null;
			this.commandCriterion = null;
			this.desiredDestinationAddressCriterion = null;
			this.desiredDestinationPortRanges = null;
			this.doc = null;			
		}
		
		public Socks5RequestCriterion build() {
			return new Socks5RequestCriterion(this);
		}
		
		public Builder clientAddressCriterion(
				final Criterion clientAddrCriterion) {
			this.clientAddressCriterion = clientAddrCriterion;
			return this;
		}
		
		public Builder commandCriterion(final Criterion cmdCriterion) {
			this.commandCriterion = cmdCriterion;
			return this;
		}
		
		public Builder desiredDestinationAddressCriterion(
				final Criterion desiredDestinationAddrCriterion) {
			this.desiredDestinationAddressCriterion = 
					desiredDestinationAddrCriterion;
			return this;
		}
		
		public Builder desiredDestinationPortRanges(
				final PortRanges desiredDestinationPrtRanges) {
			this.desiredDestinationPortRanges = desiredDestinationPrtRanges;
			return this;
		}
		
		public Builder doc(final String d) {
			this.doc = d;
			return this;
		}
	}
	
	private final Criterion clientAddressCriterion;
	private final Criterion commandCriterion;
	private final Criterion desiredDestinationAddressCriterion;
	private final PortRanges desiredDestinationPortRanges;
	private final String doc;
	
	private Socks5RequestCriterion(final Builder builder) {
		Criterion clientAddrCriterion = builder.clientAddressCriterion;
		Criterion cmdCriterion = builder.commandCriterion;
		Criterion desiredDestinationAddrCriterion = 
				builder.desiredDestinationAddressCriterion;
		PortRanges desiredDestinationPrtRange = 
				builder.desiredDestinationPortRanges;
		String d = builder.doc;		
		this.clientAddressCriterion = clientAddrCriterion;
		this.commandCriterion = cmdCriterion;
		this.desiredDestinationAddressCriterion = 
				desiredDestinationAddrCriterion;
		this.desiredDestinationPortRanges = desiredDestinationPrtRange;
		this.doc = d;		
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
		Socks5RequestCriterion other = (Socks5RequestCriterion) obj;
		if (this.clientAddressCriterion == null) {
			if (other.clientAddressCriterion != null) {
				return false;
			}
		} else if (!this.clientAddressCriterion.equals(
				other.clientAddressCriterion)) {
			return false;
		}
		if (this.commandCriterion == null) {
			if (other.commandCriterion != null) {
				return false;
			}
		} else if (!this.commandCriterion.equals(other.commandCriterion)) {
			return false;
		}
		if (this.desiredDestinationAddressCriterion == null) {
			if (other.desiredDestinationAddressCriterion != null) {
				return false;
			}
		} else if (!this.desiredDestinationAddressCriterion.equals(
				other.desiredDestinationAddressCriterion)) {
			return false;
		}
		if (this.desiredDestinationPortRanges == null) {
			if (other.desiredDestinationPortRanges != null) {
				return false;
			}
		} else if (!this.desiredDestinationPortRanges.equals(
				other.desiredDestinationPortRanges)) {
			return false;
		}
		return true;
	}
	
	public boolean evaluatesTrue(
			final String clientAddress, 
			final Socks5Request socks5Req) {
		if (this.clientAddressCriterion != null 
				&& !this.clientAddressCriterion.evaluatesTrue(clientAddress)) {
			return false;
		}
		if (this.commandCriterion != null
				&& !this.commandCriterion.evaluatesTrue(
						socks5Req.getCommand().toString())) {
			return false;
		}
		if (this.desiredDestinationAddressCriterion != null
				&& !this.desiredDestinationAddressCriterion.evaluatesTrue(
						socks5Req.getDesiredDestinationAddress())) {
			return false;
		}
		if (this.desiredDestinationPortRanges != null
				&& !this.desiredDestinationPortRanges.contains(Port.newInstance(
						socks5Req.getDesiredDestinationPort()))) {
			return false;
		}
		return true;
	}

	public Criterion getClientAddressCriterion() {
		return this.clientAddressCriterion;
	}

	public Criterion getCommandCriterion() {
		return this.commandCriterion;
	}

	public Criterion getDesiredDestinationAddressCriterion() {
		return this.desiredDestinationAddressCriterion;
	}
	
	public PortRanges getDesiredDestinationPortRanges() {
		return this.desiredDestinationPortRanges;
	}
	
	public String getDoc() {
		return this.doc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.clientAddressCriterion == null) ? 
				0 : this.clientAddressCriterion.hashCode());
		result = prime * result + ((this.commandCriterion == null) ? 
				0 : this.commandCriterion.hashCode());
		result = prime * result
				+ ((this.desiredDestinationAddressCriterion == null) ? 
						0 : this.desiredDestinationAddressCriterion.hashCode());
		result = prime * result
				+ ((this.desiredDestinationPortRanges == null) ? 
						0 : this.desiredDestinationPortRanges.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [clientAddressCriterion=")
			.append(this.clientAddressCriterion)
			.append(", commandCriterion=")
			.append(this.commandCriterion)
			.append(", desiredDestinationAddressCriterion=")
			.append(this.desiredDestinationAddressCriterion)
			.append(", desiredDestinationPortRanges=")
			.append(this.desiredDestinationPortRanges)
			.append("]");
		return builder.toString();
	}
	
}
