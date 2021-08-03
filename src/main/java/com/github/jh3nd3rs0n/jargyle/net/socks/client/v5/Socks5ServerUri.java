package com.github.jh3nd3rs0n.jargyle.net.socks.client.v5;

import com.github.jh3nd3rs0n.jargyle.net.socks.client.Properties;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.SocksServerUri;

public final class Socks5ServerUri extends SocksServerUri {

	public Socks5ServerUri(final String hst, final Integer prt) {
		super(Scheme.SOCKS5, hst, prt);
	}

	@Override
	public SocksClient newSocksClient(final Properties properties) {
		return new Socks5Client(this, properties);
	}

	@Override
	public SocksClient newSocksClient(
			final Properties properties, final SocksClient chainedSocksClient) {
		return new Socks5Client(this, properties, chainedSocksClient);
	}

}
