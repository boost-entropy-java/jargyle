package jargyle.net.socks.client.v5.userpassauth;

import jargyle.net.socks.client.v5.Socks5ServerUri;

public abstract class UsernamePasswordRequestor {

	private static UsernamePasswordRequestor defaultInstance;
	
	public static UsernamePasswordRequestor getDefault() {
		return defaultInstance;
	}
	
	public static void setDefault(final UsernamePasswordRequestor requestor) {
		defaultInstance = requestor;
	}
	
	public UsernamePasswordRequestor() { }
	
	public abstract UsernamePassword requestUsernamePassword(
			final Socks5ServerUri socks5ServerUri,
			final String prompt);
	
}