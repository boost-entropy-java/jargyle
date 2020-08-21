package jargyle.server.socks5;
import static jargyle.server.ServerSocketIT.LOOPBACK_ADDRESS;
import static jargyle.server.ServerSocketIT.echoThroughServerSocket;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import jargyle.TestStringConstants;
import jargyle.client.socks5.UsernamePassword;
import jargyle.server.ConfigurationFactory;

public class Socks5ServerSocketIT {
	
	@Test
	public void testThroughSocks5ServerSocket01() throws IOException {
		System.out.println("Testing through Socks5ServerSocket...");
		String string = TestStringConstants.STRING_01;
		String returningString = echoThroughServerSocket(
				string, 
				Socks5ClientFactory.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), null), 
				ConfigurationFactory.newConfiguration());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocket02() throws IOException {
		System.out.println("Testing through Socks5ServerSocket...");
		String string = TestStringConstants.STRING_02;
		String returningString = echoThroughServerSocket(
				string, 
				Socks5ClientFactory.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), null), 
				ConfigurationFactory.newConfiguration());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocket03() throws IOException {
		System.out.println("Testing through Socks5ServerSocket...");
		String string = TestStringConstants.STRING_03;
		String returningString = echoThroughServerSocket(
				string, 
				Socks5ClientFactory.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), null), 
				ConfigurationFactory.newConfiguration());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingUsernamePasswordAuth01() throws IOException {
		System.out.println("Testing through Socks5ServerSocket using username password authentication...");
		String string = TestStringConstants.STRING_01;
		String returningString = echoThroughServerSocket(
				string, 
				Socks5ClientFactory.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						UsernamePassword.newInstance("Aladdin", "opensesame".toCharArray())),
				ConfigurationFactory.newConfigurationUsingSocks5UsernamePasswordAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingUsernamePasswordAuth02() throws IOException {
		System.out.println("Testing through Socks5ServerSocket using username password authentication...");
		String string = TestStringConstants.STRING_02;
		String returningString = echoThroughServerSocket(
				string, 
				Socks5ClientFactory.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						UsernamePassword.newInstance("Jasmine", "mission:impossible".toCharArray())),
				ConfigurationFactory.newConfigurationUsingSocks5UsernamePasswordAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingUsernamePasswordAuth03() throws IOException {
		System.out.println("Testing through Socks5ServerSocket using username password authentication...");
		String string = TestStringConstants.STRING_03;
		String returningString = echoThroughServerSocket(
				string, 
				Socks5ClientFactory.newSocks5Client(
						LOOPBACK_ADDRESS.getHostAddress(), 
						null,
						UsernamePassword.newInstance("Abu", "safeDriversSave40%".toCharArray())),
				ConfigurationFactory.newConfigurationUsingSocks5UsernamePasswordAuth());
		assertEquals(string, returningString);
	}
	
}