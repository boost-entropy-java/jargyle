package com.github.jh3nd3rs0n.jargyle.net.socks.client;

import com.github.jh3nd3rs0n.jargyle.net.NetObjectFactory;

public abstract class SocksNetObjectFactory extends NetObjectFactory {

	public static SocksNetObjectFactory newInstance() {
		SocksClient socksClient = SocksClient.newInstance();
		SocksNetObjectFactory socksNetObjectFactory = null;
		if (socksClient != null) {
			socksNetObjectFactory = socksClient.newSocksNetObjectFactory();
		}
		return socksNetObjectFactory;
	}
	
	public abstract SocksClient getSocksClient();
	
}
