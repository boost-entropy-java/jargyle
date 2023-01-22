package com.github.jh3nd3rs0n.jargyle.client.socks5;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.TestStringConstants;
import com.github.jh3nd3rs0n.jargyle.ThreadHelper;
import com.github.jh3nd3rs0n.jargyle.client.EchoClientHelper;
import com.github.jh3nd3rs0n.jargyle.client.SocksClientHelper;
import com.github.jh3nd3rs0n.jargyle.server.ConfigurationHelper;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;
import com.github.jh3nd3rs0n.jargyle.server.SocksServerHelper;

public class Socks5ServerSocketIT {

	private static final int SOCKS_SERVER_PORT = 20100;
	private static final int SOCKS_SERVER_PORT_USING_SOCKS5_USERPASSAUTH = 20200;
	
	private static List<SocksServer> socksServers;
	private static List<SocksServer> socksServersUsingSocks5Userpassauth;
	
	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		socksServers = SocksServerHelper.newStartedSocksServers(Arrays.asList(
				ConfigurationHelper.newConfiguration(SOCKS_SERVER_PORT)));
		socksServersUsingSocks5Userpassauth = 
				SocksServerHelper.newStartedSocksServers(Arrays.asList(
						ConfigurationHelper.newConfigurationUsingSocks5Userpassauth(
								SOCKS_SERVER_PORT_USING_SOCKS5_USERPASSAUTH)));
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		/*
		echoServer.stop();
		*/
		SocksServerHelper.stopSocksServers(socksServers);
		SocksServerHelper.stopSocksServers(socksServersUsingSocks5Userpassauth);
		ThreadHelper.sleepForThreeSeconds();
	}
	
	@Test
	public void testThroughSocks5ServerSocket01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = EchoClientHelper.echoThroughNewServerSocket(
				string, 
				SocksClientHelper.newSocks5Client(SOCKS_SERVER_PORT).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocket02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = EchoClientHelper.echoThroughNewServerSocket(
				string, 
				SocksClientHelper.newSocks5Client(SOCKS_SERVER_PORT).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocket03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = EchoClientHelper.echoThroughNewServerSocket(
				string, 
				SocksClientHelper.newSocks5Client(SOCKS_SERVER_PORT).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSocks5Userpassauth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = EchoClientHelper.echoThroughNewServerSocket(
				string, 
				SocksClientHelper.newSocks5ClientUsingSocks5Userpassauth(
						SOCKS_SERVER_PORT_USING_SOCKS5_USERPASSAUTH, 
						"Aladdin",
						"opensesame".toCharArray()).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingSocks5Userpassauth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = EchoClientHelper.echoThroughNewServerSocket(
				string, 
				SocksClientHelper.newSocks5ClientUsingSocks5Userpassauth(
						SOCKS_SERVER_PORT_USING_SOCKS5_USERPASSAUTH, 
						"Jasmine",
						"mission:impossible".toCharArray()).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSocks5Userpassauth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = EchoClientHelper.echoThroughNewServerSocket(
				string, 
				SocksClientHelper.newSocks5ClientUsingSocks5Userpassauth(
						SOCKS_SERVER_PORT_USING_SOCKS5_USERPASSAUTH, 
						"Abu",
						"safeDriversSave40%".toCharArray()).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
}