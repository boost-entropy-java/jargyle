package com.github.jh3nd3rs0n.jargyle.transport.socks5;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ClientMethodSelectionMessageTest {

	@Test
	public void testNewInstanceMethods01() {
		ClientMethodSelectionMessage cmsm1 = 
				ClientMethodSelectionMessage.newInstance(
						Methods.newInstance(
								Method.NO_AUTHENTICATION_REQUIRED));
		ClientMethodSelectionMessage cmsm2 =
				ClientMethodSelectionMessage.newInstance(cmsm1.toByteArray());
		assertEquals(cmsm1, cmsm2);
	}

	@Test
	public void testNewInstanceMethods02() {
		ClientMethodSelectionMessage cmsm1 = 
				ClientMethodSelectionMessage.newInstance(
						Methods.newInstance(
								Method.NO_AUTHENTICATION_REQUIRED,
								Method.USERNAME_PASSWORD));
		ClientMethodSelectionMessage cmsm2 =
				ClientMethodSelectionMessage.newInstance(cmsm1.toByteArray());
		assertEquals(cmsm1, cmsm2);
	}

	@Test
	public void testNewInstanceMethods03() {
		ClientMethodSelectionMessage cmsm1 = 
				ClientMethodSelectionMessage.newInstance(
						Methods.newInstance(
								Method.NO_AUTHENTICATION_REQUIRED,
								Method.GSSAPI,
								Method.USERNAME_PASSWORD));
		ClientMethodSelectionMessage cmsm2 =
				ClientMethodSelectionMessage.newInstance(cmsm1.toByteArray());
		assertEquals(cmsm1, cmsm2);
	}
	
	@Test
	public void testNewInstanceMethods04() {
		ClientMethodSelectionMessage cmsm1 = 
				ClientMethodSelectionMessage.newInstance(Methods.newInstance());
		ClientMethodSelectionMessage cmsm2 =
				ClientMethodSelectionMessage.newInstance(cmsm1.toByteArray());
		assertEquals(cmsm1, cmsm2);		
	}
	
	@Test
	public void testNewInstanceMethods05() {
		ClientMethodSelectionMessage cmsm1 = 
				ClientMethodSelectionMessage.newInstance(
						Methods.newInstance(
								Method.NO_AUTHENTICATION_REQUIRED,
								Method.GSSAPI,
								Method.NO_AUTHENTICATION_REQUIRED));
		ClientMethodSelectionMessage cmsm2 =
				ClientMethodSelectionMessage.newInstance(cmsm1.toByteArray());
		assertEquals(cmsm1, cmsm2);		
	}
	
	@Test
	public void testNewInstanceMethods06() {
		ClientMethodSelectionMessage cmsm1 = 
				ClientMethodSelectionMessage.newInstance(
						Methods.newInstance(
								Method.GSSAPI,
								Method.GSSAPI,
								Method.USERNAME_PASSWORD));
		ClientMethodSelectionMessage cmsm2 =
				ClientMethodSelectionMessage.newInstance(cmsm1.toByteArray());
		assertEquals(cmsm1, cmsm2);		
	}
	
	@Test
	public void testNewInstanceMethods07() {
		ClientMethodSelectionMessage cmsm1 = 
				ClientMethodSelectionMessage.newInstance(
						Methods.newInstance(
								Method.NO_AUTHENTICATION_REQUIRED,
								Method.USERNAME_PASSWORD,
								Method.USERNAME_PASSWORD));
		ClientMethodSelectionMessage cmsm2 =
				ClientMethodSelectionMessage.newInstance(cmsm1.toByteArray());
		assertEquals(cmsm1, cmsm2);		
	}
	
	@Test
	public void testNewInstanceMethods08() {
		ClientMethodSelectionMessage cmsm1 = 
				ClientMethodSelectionMessage.newInstance(
						Methods.newInstance(
								Method.NO_AUTHENTICATION_REQUIRED,
								Method.NO_AUTHENTICATION_REQUIRED,
								Method.NO_AUTHENTICATION_REQUIRED));
		ClientMethodSelectionMessage cmsm2 =
				ClientMethodSelectionMessage.newInstance(cmsm1.toByteArray());
		assertEquals(cmsm1, cmsm2);		
	}

}
