package jargyle.server;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import jargyle.client.socks5.UsernamePassword;
import jargyle.server.socks5.Socks5RequestCriteria;
import jargyle.server.socks5.UsernamePasswordAuthenticator;

public final class ImmutableConfiguration implements Configuration {

	public static final class Builder {
		
		private Criteria allowedClientAddressCriteria;
		private Criteria allowedIncomingTcpAddressCriteria;
		private Criteria allowedIncomingUdpAddressCriteria;
		private Socks5RequestCriteria allowedSocks5RequestCriteria;
		private Criteria blockedClientAddressCriteria;
		private Criteria blockedIncomingTcpAddressCriteria;
		private Criteria blockedIncomingUdpAddressCriteria;
		private Socks5RequestCriteria blockedSocks5RequestCriteria;
		private UsernamePassword externalClientSocks5UsernamePassword;
		private Settings settings;
		private UsernamePasswordAuthenticator socks5UsernamePasswordAuthenticator;
				
		public Builder() {
			this.allowedClientAddressCriteria = null;
			this.allowedIncomingTcpAddressCriteria = null;
			this.allowedIncomingUdpAddressCriteria = null;
			this.allowedSocks5RequestCriteria = null;
			this.blockedClientAddressCriteria = null;
			this.blockedIncomingTcpAddressCriteria = null;
			this.blockedIncomingUdpAddressCriteria = null;
			this.blockedSocks5RequestCriteria = null;
			this.externalClientSocks5UsernamePassword = null;
			this.settings = null;
			this.socks5UsernamePasswordAuthenticator = null;
		}
		
		public Builder allowedClientAddressCriteria(
				final Criteria allowedClientAddrCriteria) {
			this.allowedClientAddressCriteria = allowedClientAddrCriteria;
			return this;
		}
		
		public Builder allowedIncomingTcpAddressCriteria(
				final Criteria allowedIncomingTcpAddrCriteria) {
			this.allowedIncomingTcpAddressCriteria = 
					allowedIncomingTcpAddrCriteria;
			return this;
		}
		
		public Builder allowedIncomingUdpAddressCriteria(
				final Criteria allowedIncomingUdpAddrCriteria) {
			this.allowedIncomingUdpAddressCriteria =
					allowedIncomingUdpAddrCriteria;
			return this;
		}
		
		public Builder allowedSocks5RequestCriteria(
				final Socks5RequestCriteria allowedSocks5ReqCriteria) {
			this.allowedSocks5RequestCriteria = allowedSocks5ReqCriteria;
			return this;
		}
		
		public Builder blockedClientAddressCriteria(
				final Criteria blockedClientAddrCriteria) {
			this.blockedClientAddressCriteria = blockedClientAddrCriteria;
			return this;
		}
		
		public Builder blockedIncomingTcpAddressCriteria(
				final Criteria blockedIncomingTcpAddrCriteria) {
			this.blockedIncomingTcpAddressCriteria =
					blockedIncomingTcpAddrCriteria;
			return this;
		}
		
		public Builder blockedIncomingUdpAddressCriteria(
				final Criteria blockedIncomingUdpAddrCriteria) {
			this.blockedIncomingUdpAddressCriteria = 
					blockedIncomingUdpAddrCriteria;
			return this;
		}
		
		public Builder blockedSocks5RequestCriteria(
				final Socks5RequestCriteria blockedSocks5ReqCriteria) {
			this.blockedSocks5RequestCriteria = blockedSocks5ReqCriteria;
			return this;
		}
		
		public ImmutableConfiguration build() {
			return new ImmutableConfiguration(this);
		}
		
		public Builder externalClientSocks5UsernamePassword(
				final UsernamePassword externalClientSocks5UsrnmPsswrd) {
			this.externalClientSocks5UsernamePassword = 
					externalClientSocks5UsrnmPsswrd;
			return this;
		}
		
		public Builder settings(final Settings sttngs) {
			this.settings = sttngs;
			return this;
		}
		
		public Builder socks5UsernamePasswordAuthenticator(
				final UsernamePasswordAuthenticator socks5UsrnmPsswrdAuthenticator) {
			this.socks5UsernamePasswordAuthenticator = 
					socks5UsrnmPsswrdAuthenticator;
			return this;
		}
	}
	
	@XmlAccessorType(XmlAccessType.NONE)
	@XmlType(name = "configuration", propOrder = { })
	@XmlRootElement(name = "configuration")
	public static class ConfigurationXml {
		@XmlElement(name = "allowedClientAddressCriteria")
		protected Criteria allowedClientAddressCriteria;
		@XmlElement(name = "allowedIncomingTcpAddressCriteria")
		protected Criteria allowedIncomingTcpAddressCriteria;
		@XmlElement(name = "allowedIncomingUdpAddressCriteria")
		protected Criteria allowedIncomingUdpAddressCriteria;
		@XmlElement(name = "allowedSocks5RequestCriteria")
		protected Socks5RequestCriteria allowedSocks5RequestCriteria;
		@XmlElement(name = "blockedClientAddressCriteria")
		protected Criteria blockedClientAddressCriteria;
		@XmlElement(name = "blockedIncomingTcpAddressCriteria")
		protected Criteria blockedIncomingTcpAddressCriteria;
		@XmlElement(name = "blockedIncomingUdpAddressCriteria")
		protected Criteria blockedIncomingUdpAddressCriteria;		
		@XmlElement(name = "blockedSocks5RequestCriteria")
		protected Socks5RequestCriteria blockedSocks5RequestCriteria;
		@XmlElement(name = "externalClientSocks5UsernamePassword")
		protected UsernamePassword externalClientSocks5UsernamePassword;
		@XmlElement(name = "settings")
		protected Settings settings;
		@XmlElement(name = "socks5UsernamePasswordAuthenticator")
		protected UsernamePasswordAuthenticator socks5UsernamePasswordAuthenticator;
	}
	
	public static Configuration newInstance(
			final ConfigurationXml configurationXml) {
		ImmutableConfiguration.Builder builder = 
				new ImmutableConfiguration.Builder();
		if (configurationXml.allowedClientAddressCriteria != null) {
			builder.allowedClientAddressCriteria(
					configurationXml.allowedClientAddressCriteria);
		}
		if (configurationXml.allowedIncomingTcpAddressCriteria != null) {
			builder.allowedIncomingTcpAddressCriteria(
					configurationXml.allowedIncomingTcpAddressCriteria);
		}
		if (configurationXml.allowedIncomingUdpAddressCriteria != null) {
			builder.allowedIncomingUdpAddressCriteria(
					configurationXml.allowedIncomingUdpAddressCriteria);
		}
		if (configurationXml.allowedSocks5RequestCriteria != null) {
			builder.allowedSocks5RequestCriteria(
					configurationXml.allowedSocks5RequestCriteria);
		}
		if (configurationXml.blockedClientAddressCriteria != null) {
			builder.blockedClientAddressCriteria(
					configurationXml.blockedClientAddressCriteria);
		}
		if (configurationXml.blockedIncomingTcpAddressCriteria != null) {
			builder.blockedIncomingTcpAddressCriteria(
					configurationXml.blockedIncomingTcpAddressCriteria);
		}
		if (configurationXml.blockedIncomingUdpAddressCriteria != null) {
			builder.blockedIncomingUdpAddressCriteria(
					configurationXml.blockedIncomingUdpAddressCriteria);
		}		
		if (configurationXml.blockedSocks5RequestCriteria != null) {
			builder.blockedSocks5RequestCriteria(
					configurationXml.blockedSocks5RequestCriteria);
		}
		if (configurationXml.externalClientSocks5UsernamePassword != null) {
			builder.externalClientSocks5UsernamePassword(
					configurationXml.externalClientSocks5UsernamePassword);
		}
		if (configurationXml.settings != null) {
			builder.settings(configurationXml.settings);
		}
		if (configurationXml.socks5UsernamePasswordAuthenticator != null) {
			builder.socks5UsernamePasswordAuthenticator(
					configurationXml.socks5UsernamePasswordAuthenticator);
		}
		return builder.build();
	}
	
	private final Criteria allowedClientAddressCriteria;
	private final Criteria allowedIncomingTcpAddressCriteria;
	private final Criteria allowedIncomingUdpAddressCriteria;
	private final Socks5RequestCriteria allowedSocks5RequestCriteria;
	private final Criteria blockedClientAddressCriteria;
	private final Criteria blockedIncomingTcpAddressCriteria;
	private final Criteria blockedIncomingUdpAddressCriteria;
	private final Socks5RequestCriteria blockedSocks5RequestCriteria;
	private final UsernamePassword externalClientSocks5UsernamePassword;
	private final Settings settings;
	private final UsernamePasswordAuthenticator socks5UsernamePasswordAuthenticator;
	
	private ImmutableConfiguration(final Builder builder) {
		Criteria allowedClientAddrCriteria = 
				builder.allowedClientAddressCriteria;
		Criteria allowedIncomingTcpAddrCriteria = 
				builder.allowedIncomingTcpAddressCriteria;
		Criteria allowedIncomingUdpAddrCriteria =
				builder.allowedIncomingUdpAddressCriteria;
		Socks5RequestCriteria allowedSocks5ReqCriteria = 
				builder.allowedSocks5RequestCriteria;
		Criteria blockedClientAddrCriteria = 
				builder.blockedClientAddressCriteria;
		Criteria blockedIncomingTcpAddrCriteria = 
				builder.blockedIncomingTcpAddressCriteria;
		Criteria blockedIncomingUdpAddrCriteria =
				builder.blockedIncomingUdpAddressCriteria;		
		Socks5RequestCriteria blockedSocks5ReqCriteria =
				builder.blockedSocks5RequestCriteria;
		UsernamePassword externalClientSocks5UsrnmPsswrd = 
				builder.externalClientSocks5UsernamePassword;
		Settings sttngs = builder.settings;
		UsernamePasswordAuthenticator socks5UsrnmPsswrdAuthenticator = 
				builder.socks5UsernamePasswordAuthenticator;
		this.allowedClientAddressCriteria = allowedClientAddrCriteria;
		this.allowedIncomingTcpAddressCriteria = allowedIncomingTcpAddrCriteria;
		this.allowedIncomingUdpAddressCriteria = allowedIncomingUdpAddrCriteria;
		this.allowedSocks5RequestCriteria = allowedSocks5ReqCriteria;
		this.blockedClientAddressCriteria = blockedClientAddrCriteria;
		this.blockedIncomingTcpAddressCriteria = blockedIncomingTcpAddrCriteria;
		this.blockedIncomingUdpAddressCriteria = blockedIncomingUdpAddrCriteria;
		this.blockedSocks5RequestCriteria = blockedSocks5ReqCriteria;
		this.externalClientSocks5UsernamePassword = 
				externalClientSocks5UsrnmPsswrd;
		this.settings = sttngs;
		this.socks5UsernamePasswordAuthenticator = 
				socks5UsrnmPsswrdAuthenticator;
	}
	
	@Override
	public Criteria getAllowedClientAddressCriteria() {
		if (this.allowedClientAddressCriteria == null) {
			return Criteria.EMPTY_INSTANCE;
		}
		return this.allowedClientAddressCriteria;
	}
	
	@Override
	public Criteria getAllowedIncomingTcpAddressCriteria() {
		if (this.allowedIncomingTcpAddressCriteria == null) {
			return Criteria.EMPTY_INSTANCE;
		}
		return this.allowedIncomingTcpAddressCriteria;
	}
	
	@Override
	public Criteria getAllowedIncomingUdpAddressCriteria() {
		if (this.allowedIncomingUdpAddressCriteria == null) {
			return Criteria.EMPTY_INSTANCE;
		}
		return this.allowedIncomingUdpAddressCriteria;
	}

	@Override
	public Socks5RequestCriteria getAllowedSocks5RequestCriteria() {
		if (this.allowedSocks5RequestCriteria == null) {
			return Socks5RequestCriteria.EMPTY_INSTANCE;
		}
		return this.allowedSocks5RequestCriteria;
	}
	
	@Override
	public Criteria getBlockedClientAddressCriteria() {
		if (this.blockedClientAddressCriteria == null) {
			return Criteria.EMPTY_INSTANCE;
		}
		return this.blockedClientAddressCriteria;
	}
	
	@Override
	public Criteria getBlockedIncomingTcpAddressCriteria() {
		if (this.blockedIncomingTcpAddressCriteria == null) {
			return Criteria.EMPTY_INSTANCE;
		}
		return this.blockedIncomingTcpAddressCriteria;
	}
	
	@Override
	public Criteria getBlockedIncomingUdpAddressCriteria() {
		if (this.blockedIncomingUdpAddressCriteria == null) {
			return Criteria.EMPTY_INSTANCE;
		}
		return this.blockedIncomingUdpAddressCriteria;
	}

	@Override
	public Socks5RequestCriteria getBlockedSocks5RequestCriteria() {
		if (this.blockedSocks5RequestCriteria == null) {
			return Socks5RequestCriteria.EMPTY_INSTANCE;
		}
		return this.blockedSocks5RequestCriteria;
	}

	@Override
	public UsernamePassword getExternalClientSocks5UsernamePassword() {
		return this.externalClientSocks5UsernamePassword;
	}

	@Override
	public Settings getSettings() {
		if (this.settings == null) {
			return Settings.EMPTY_INSTANCE;
		}
		return this.settings;
	}

	@Override
	public UsernamePasswordAuthenticator getSocks5UsernamePasswordAuthenticator() {
		return this.socks5UsernamePasswordAuthenticator;
	}

	public ConfigurationXml toConfigurationXml() {
		ConfigurationXml configurationXml = new ConfigurationXml();
		if (this.allowedClientAddressCriteria != null) {
			configurationXml.allowedClientAddressCriteria = 
					this.allowedClientAddressCriteria;
		}
		if (this.allowedIncomingTcpAddressCriteria != null) {
			configurationXml.allowedIncomingTcpAddressCriteria =
					this.allowedIncomingTcpAddressCriteria;
		}
		if (this.allowedIncomingUdpAddressCriteria != null) {
			configurationXml.allowedIncomingUdpAddressCriteria =
					this.allowedIncomingUdpAddressCriteria;
		}
		if (this.allowedSocks5RequestCriteria != null) {
			configurationXml.allowedSocks5RequestCriteria =
					this.allowedSocks5RequestCriteria;
		}
		if (this.blockedClientAddressCriteria != null) {
			configurationXml.blockedClientAddressCriteria = 
					this.blockedClientAddressCriteria;
		}
		if (this.blockedIncomingTcpAddressCriteria != null) {
			configurationXml.blockedIncomingTcpAddressCriteria =
					this.blockedIncomingTcpAddressCriteria;
		}
		if (this.blockedIncomingUdpAddressCriteria != null) {
			configurationXml.blockedIncomingUdpAddressCriteria =
					this.blockedIncomingUdpAddressCriteria;
		}		
		if (this.blockedSocks5RequestCriteria != null) {
			configurationXml.blockedSocks5RequestCriteria = 
					this.blockedSocks5RequestCriteria;
		}
		if (this.externalClientSocks5UsernamePassword != null) {
			configurationXml.externalClientSocks5UsernamePassword = 
					this.externalClientSocks5UsernamePassword;
		}
		if (this.settings != null) {
			configurationXml.settings = this.settings;
		}
		if (this.socks5UsernamePasswordAuthenticator != null) {
			configurationXml.socks5UsernamePasswordAuthenticator = 
					this.socks5UsernamePasswordAuthenticator;
		}
		return configurationXml;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [allowedClientAddressCriteria=")
			.append(this.allowedClientAddressCriteria)
			.append(", allowedIncomingTcpAddressCriteria=")
			.append(this.allowedIncomingTcpAddressCriteria)
			.append(", allowedIncomingUdpAddressCriteria=")
			.append(this.allowedIncomingUdpAddressCriteria)
			.append(", allowedSocks5RequestCriteria=")
			.append(this.allowedSocks5RequestCriteria)			
			.append(", blockedClientAddressCriteria=")
			.append(this.blockedClientAddressCriteria)
			.append(", blockedIncomingTcpAddressCriteria=")
			.append(this.blockedIncomingTcpAddressCriteria)
			.append(", blockedIncomingUdpAddressCriteria=")
			.append(this.blockedIncomingUdpAddressCriteria)			
			.append(", blockedSocks5RequestCriteria=")
			.append(this.blockedSocks5RequestCriteria)
			.append(", externalClientSocks5UsernamePassword=")
			.append(this.externalClientSocks5UsernamePassword)
			.append(", settings=")
			.append(this.settings)
			.append(", socks5UsernamePasswordAuthenticator=")
			.append(this.socks5UsernamePasswordAuthenticator)
			.append("]");
		return builder.toString();
	}
	
}
