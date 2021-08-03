package com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.userpassauth;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UsernamePasswordResponseTest {

	@Test
	public void testNewInstanceByte01() {
		UsernamePasswordResponse usernamePasswordResponse1 =
				UsernamePasswordResponse.newInstance(
						UsernamePasswordResponse.STATUS_SUCCESS);
		UsernamePasswordResponse usernamePasswordResponse2 = 
				UsernamePasswordResponse.newInstance(
						usernamePasswordResponse1.toByteArray());
		assertEquals(usernamePasswordResponse1, usernamePasswordResponse2);
	}
	
	@Test
	public void testNewInstanceByte02() {
		UsernamePasswordResponse usernamePasswordResponse1 =
				UsernamePasswordResponse.newInstance((byte) 0x01);
		UsernamePasswordResponse usernamePasswordResponse2 = 
				UsernamePasswordResponse.newInstance(
						usernamePasswordResponse1.toByteArray());
		assertEquals(usernamePasswordResponse1, usernamePasswordResponse2);
	}

	@Test
	public void testNewInstanceByte03() {
		UsernamePasswordResponse usernamePasswordResponse1 =
				UsernamePasswordResponse.newInstance((byte) 0xff);
		UsernamePasswordResponse usernamePasswordResponse2 = 
				UsernamePasswordResponse.newInstance(
						usernamePasswordResponse1.toByteArray());
		assertEquals(usernamePasswordResponse1, usernamePasswordResponse2);
	}


}
