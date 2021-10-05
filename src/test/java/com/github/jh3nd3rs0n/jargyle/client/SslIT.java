package com.github.jh3nd3rs0n.jargyle.client;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.InetAddress;

import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.ResourceHelper;
import com.github.jh3nd3rs0n.jargyle.ResourceNameConstants;
import com.github.jh3nd3rs0n.jargyle.TestStringConstants;
import com.github.jh3nd3rs0n.jargyle.client.socks5.Socks5ServerUri;
import com.github.jh3nd3rs0n.jargyle.common.net.DatagramSocketHelper;
import com.github.jh3nd3rs0n.jargyle.common.net.ServerSocketHelper;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketHelper;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.DtlsSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.ImmutableConfiguration;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.SslSettingSpecConstants;

public class SslIT {

/*	
	@org.junit.BeforeClass
	public static void setUp() {
		System.setProperty("javax.net.debug", "ssl,handshake");
	}
	
	@org.junit.AfterClass
	public static void tearDown() {
		System.clearProperty("javax.net.debug");
	}
*/
	
	private static Configuration newConfigurationUsingSsl() {
		return ImmutableConfiguration.newInstance(Settings.newInstance(
				DtlsSettingSpecConstants.DTLS_ENABLED.newSetting(Boolean.TRUE),
				DtlsSettingSpecConstants.DTLS_KEY_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_FILE)),
				DtlsSettingSpecConstants.DTLS_KEY_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),				
				SslSettingSpecConstants.SSL_ENABLED.newSetting(Boolean.TRUE),
				SslSettingSpecConstants.SSL_KEY_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_FILE)),
				SslSettingSpecConstants.SSL_KEY_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE))));
	}

	private static Configuration newConfigurationUsingSslAndRequestedClientAuth() {
		return ImmutableConfiguration.newInstance(Settings.newInstance(
				DtlsSettingSpecConstants.DTLS_ENABLED.newSetting(Boolean.TRUE),
				DtlsSettingSpecConstants.DTLS_KEY_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_FILE)),
				DtlsSettingSpecConstants.DTLS_KEY_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),
				DtlsSettingSpecConstants.DTLS_TRUST_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_CLIENT_KEY_STORE_FILE)),
				DtlsSettingSpecConstants.DTLS_TRUST_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_CLIENT_KEY_STORE_PASSWORD_FILE)),
				DtlsSettingSpecConstants.DTLS_WANT_CLIENT_AUTH.newSetting(
						Boolean.TRUE),				
				SslSettingSpecConstants.SSL_ENABLED.newSetting(Boolean.TRUE),
				SslSettingSpecConstants.SSL_KEY_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_FILE)),
				SslSettingSpecConstants.SSL_KEY_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),
				SslSettingSpecConstants.SSL_TRUST_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_CLIENT_KEY_STORE_FILE)),
				SslSettingSpecConstants.SSL_TRUST_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_CLIENT_KEY_STORE_PASSWORD_FILE)),
				SslSettingSpecConstants.SSL_WANT_CLIENT_AUTH.newSetting(
						Boolean.TRUE)));
	}

	private static Configuration newConfigurationUsingSslAndRequiredClientAuth() {
		return ImmutableConfiguration.newInstance(Settings.newInstance(
				DtlsSettingSpecConstants.DTLS_ENABLED.newSetting(Boolean.TRUE),
				DtlsSettingSpecConstants.DTLS_KEY_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_FILE)),
				DtlsSettingSpecConstants.DTLS_KEY_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),
				DtlsSettingSpecConstants.DTLS_NEED_CLIENT_AUTH.newSetting(
						Boolean.TRUE),
				DtlsSettingSpecConstants.DTLS_TRUST_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_CLIENT_KEY_STORE_FILE)),
				DtlsSettingSpecConstants.DTLS_TRUST_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_CLIENT_KEY_STORE_PASSWORD_FILE)),				
				SslSettingSpecConstants.SSL_ENABLED.newSetting(Boolean.TRUE),
				SslSettingSpecConstants.SSL_KEY_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_FILE)),
				SslSettingSpecConstants.SSL_KEY_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),
				SslSettingSpecConstants.SSL_NEED_CLIENT_AUTH.newSetting(
						Boolean.TRUE),
				SslSettingSpecConstants.SSL_TRUST_STORE_FILE.newSetting(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_CLIENT_KEY_STORE_FILE)),
				SslSettingSpecConstants.SSL_TRUST_STORE_PASSWORD.newSettingOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_CLIENT_KEY_STORE_PASSWORD_FILE))));
	}
	
	private static SocksClient newSocks5ClientUsingSsl(
			final String host, 
			final Integer port) {
		Properties properties = Properties.newInstance(
				DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(
						Boolean.TRUE),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_FILE)),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newPropertyOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),				
				SslPropertySpecConstants.SSL_ENABLED.newProperty(
						Boolean.TRUE),
				SslPropertySpecConstants.SSL_TRUST_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_FILE)),
				SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newPropertyOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)));
		return new Socks5ServerUri(host, port).newSocksClient(properties);
	}
	
	private static SocksClient newSocks5ClientUsingSslAndClientAuth(
			final String host, 
			final Integer port) {
		Properties properties = Properties.newInstance(
				DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(
						Boolean.TRUE),
				DtlsPropertySpecConstants.DTLS_KEY_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_CLIENT_KEY_STORE_FILE)),
				DtlsPropertySpecConstants.DTLS_KEY_STORE_PASSWORD.newPropertyOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_CLIENT_KEY_STORE_PASSWORD_FILE)),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_FILE)),
				DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newPropertyOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)),
				SslPropertySpecConstants.SSL_ENABLED.newProperty(Boolean.TRUE),
				SslPropertySpecConstants.SSL_KEY_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_CLIENT_KEY_STORE_FILE)),
				SslPropertySpecConstants.SSL_KEY_STORE_PASSWORD.newPropertyOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_CLIENT_KEY_STORE_PASSWORD_FILE)),
				SslPropertySpecConstants.SSL_TRUST_STORE_FILE.newProperty(
						ResourceHelper.getResourceAsFile(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_FILE)),
				SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newPropertyOfParsableValue(
						ResourceHelper.getResourceAsString(
								ResourceNameConstants.JARGYLE_COMMON_SECURITY_SERVER_KEY_STORE_PASSWORD_FILE)));
		return new Socks5ServerUri(host, port).newSocksClient(properties);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSsl01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSsl());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSsl02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSsl());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSsl03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSsl());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSslAndRequestedClientAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSslAndRequestedClientAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSslAndRequestedClientAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSslAndRequiredClientAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndClientAuth(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSslAndRequiredClientAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndClientAuth(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingSslAndRequiredClientAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketHelper.echoThroughDatagramSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndClientAuth(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingSsl01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSsl());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSsl02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSsl());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSsl03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSsl());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSslAndRequestedClientAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSslAndRequestedClientAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSslAndRequestedClientAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSslAndRequiredClientAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndClientAuth(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSslAndRequiredClientAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndClientAuth(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingSslAndRequiredClientAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketHelper.echoThroughServerSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndClientAuth(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingSsl01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketHelper.echoThroughSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSsl());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5SocketUsingSsl02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketHelper.echoThroughSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSsl());
		assertEquals(string, returningString);		
	}
	
	@Test
	public void testThroughSocks5SocketUsingSsl03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketHelper.echoThroughSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSsl());
		assertEquals(string, returningString);		
	}
	
	@Test
	public void testThroughSocks5SocketUsingSslAndRequestedClientAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketHelper.echoThroughSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingSslAndRequestedClientAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketHelper.echoThroughSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);		
	}

	@Test
	public void testThroughSocks5SocketUsingSslAndRequestedClientAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketHelper.echoThroughSocket(
				string,
				SslIT.newSocks5ClientUsingSsl(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequestedClientAuth());
		assertEquals(string, returningString);		
	}

	@Test
	public void testThroughSocks5SocketUsingSslAndRequiredClientAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketHelper.echoThroughSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndClientAuth(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingSslAndRequiredClientAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketHelper.echoThroughSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndClientAuth(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);		
	}

	@Test
	public void testThroughSocks5SocketUsingSslAndRequiredClientAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketHelper.echoThroughSocket(
				string,
				SslIT.newSocks5ClientUsingSslAndClientAuth(
						InetAddress.getLoopbackAddress().getHostAddress(), 
						null),
				SslIT.newConfigurationUsingSslAndRequiredClientAuth());
		assertEquals(string, returningString);		
	}
	
}
