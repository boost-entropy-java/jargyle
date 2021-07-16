package jargyle.internal.net.socks.transport.v5;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jargyle.net.socks.transport.v5.Method;

public class ServerMethodSelectionMessageTest {

	@Test
	public void testNewInstanceMethod01() {
		ServerMethodSelectionMessage smsm1 = 
				ServerMethodSelectionMessage.newInstance(
						Method.NO_AUTHENTICATION_REQUIRED);
		ServerMethodSelectionMessage smsm2 = 
				ServerMethodSelectionMessage.newInstance(smsm1.toByteArray());
		assertEquals(smsm1, smsm2);
	}

	@Test
	public void testNewInstanceMethod02() {
		ServerMethodSelectionMessage smsm1 = 
				ServerMethodSelectionMessage.newInstance(
						Method.NO_ACCEPTABLE_METHODS);
		ServerMethodSelectionMessage smsm2 = 
				ServerMethodSelectionMessage.newInstance(smsm1.toByteArray());
		assertEquals(smsm1, smsm2);
	}

	@Test
	public void testNewInstanceMethod03() {
		ServerMethodSelectionMessage smsm1 = 
				ServerMethodSelectionMessage.newInstance(
						Method.USERNAME_PASSWORD);
		ServerMethodSelectionMessage smsm2 = 
				ServerMethodSelectionMessage.newInstance(smsm1.toByteArray());
		assertEquals(smsm1, smsm2);
	}

}
