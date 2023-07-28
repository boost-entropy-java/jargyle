package com.github.jh3nd3rs0n.jargyle.clientserver;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.TestResourceConstants;
import com.github.jh3nd3rs0n.jargyle.TestStringConstants;
import com.github.jh3nd3rs0n.jargyle.ThreadHelper;
import com.github.jh3nd3rs0n.jargyle.client.DtlsPropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.client.SslPropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.server.ChainingDtlsSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.ChainingGeneralSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.ChainingSocks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.ChainingSslSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.DtlsSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.GeneralSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;
import com.github.jh3nd3rs0n.jargyle.server.SslSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.userrepo.impl.StringSourceUserRepository;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Methods;

public class EchoThroughSocksServerChainingIT {
	
	private static final int CHAINED_SOCKS_SERVER_PORT_1 = 2100;
	private static final int CHAINED_SOCKS_SERVER_PORT_2 = 2200;
	private static final int CHAINED_SOCKS_SERVER_PORT_3 = 2300;
	
	private static final int CHAINED_SOCKS_SERVER_PORT_1_USING_SOCKS5_USERPASSAUTH = 3100;
	private static final int CHAINED_SOCKS_SERVER_PORT_2_USING_SOCKS5_USERPASSAUTH = 3200;
	private static final int CHAINED_SOCKS_SERVER_PORT_3_USING_SOCKS5_USERPASSAUTH = 3300;
	
	private static final int CHAINED_SOCKS_SERVER_PORT_1_USING_SSL = 4100;
	private static final int CHAINED_SOCKS_SERVER_PORT_2_USING_SSL = 4200;

	private static final int SOCKS_SERVER_PORT_1 = 5100;
	private static final int SOCKS_SERVER_PORT_2 = 5200;
	private static final int SOCKS_SERVER_PORT_3 = 5300;
	
	private static final int SOCKS_SERVER_PORT_1_USING_SOCKS5_USERPASSAUTH = 6100;
	private static final int SOCKS_SERVER_PORT_2_USING_SOCKS5_USERPASSAUTH = 6200;
	private static final int SOCKS_SERVER_PORT_3_USING_SOCKS5_USERPASSAUTH = 6300;
	
	private static final int SOCKS_SERVER_PORT_1_USING_SSL = 7100;
	private static final int SOCKS_SERVER_PORT_2_USING_SSL = 7200;

	private static List<SocksServer> chainedSocksServers;
	private static List<SocksServer> chainedSocksServersUsingSocks5Userpassauth;
	private static List<SocksServer> chainedSocksServersUsingSsl;
	
	private static DatagramEchoServer datagramEchoServer;
	private static EchoServer echoServer;
	
	private static List<SocksServer> socksServers;
	private static List<SocksServer> socksServersUsingSocks5Userpassauth;
	private static List<SocksServer> socksServersUsingSsl;
	
	private static List<Configuration> newChainedConfigurations() {
		return Arrays.asList(
				Configuration.newUnmodifiableInstance(Settings.newInstance(
						GeneralSettingSpecConstants.PORT.newSetting(
								Port.newInstance(CHAINED_SOCKS_SERVER_PORT_1)),
						ChainingGeneralSettingSpecConstants.CHAINING_SOCKS_SERVER_URI.newSetting(
								Scheme.SOCKS5.newSocksServerUri(
										InetAddress.getLoopbackAddress().getHostAddress(), 
										Integer.valueOf(CHAINED_SOCKS_SERVER_PORT_2))))),
				Configuration.newUnmodifiableInstance(Settings.newInstance(
						GeneralSettingSpecConstants.PORT.newSetting(
								Port.newInstance(CHAINED_SOCKS_SERVER_PORT_2)),
						ChainingGeneralSettingSpecConstants.CHAINING_SOCKS_SERVER_URI.newSetting(
								Scheme.SOCKS5.newSocksServerUri(
										InetAddress.getLoopbackAddress().getHostAddress(), 
										Integer.valueOf(CHAINED_SOCKS_SERVER_PORT_3))))),
				Configuration.newUnmodifiableInstance(Settings.newInstance(
						GeneralSettingSpecConstants.PORT.newSetting(
								Port.newInstance(CHAINED_SOCKS_SERVER_PORT_3)))));
	}
	
	private static List<Configuration> newChainedConfigurationsUsingSocks5Userpassauth() {
		return Arrays.asList(
				Configuration.newUnmodifiableInstance(Settings.newInstance(
						GeneralSettingSpecConstants.PORT.newSetting(
								Port.newInstance(CHAINED_SOCKS_SERVER_PORT_1_USING_SOCKS5_USERPASSAUTH)),
						Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						Socks5SettingSpecConstants.SOCKS5_USERPASSAUTH_USER_REPOSITORY.newSetting(
								new StringSourceUserRepository(
										"Aladdin:opensesame")),
						ChainingGeneralSettingSpecConstants.CHAINING_SOCKS_SERVER_URI.newSetting(
								Scheme.SOCKS5.newSocksServerUri(
										InetAddress.getLoopbackAddress().getHostAddress(), 
										Integer.valueOf(CHAINED_SOCKS_SERVER_PORT_2_USING_SOCKS5_USERPASSAUTH))),
						ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_METHODS.newSetting(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_USERPASSAUTH_USERNAME.newSetting(
								"Jasmine"),
						ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_USERPASSAUTH_PASSWORD.newSetting(
								EncryptedPassword.newInstance("mission:impossible".toCharArray())))),
				Configuration.newUnmodifiableInstance(Settings.newInstance(
						GeneralSettingSpecConstants.PORT.newSetting(
								Port.newInstance(CHAINED_SOCKS_SERVER_PORT_2_USING_SOCKS5_USERPASSAUTH)),
						Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						Socks5SettingSpecConstants.SOCKS5_USERPASSAUTH_USER_REPOSITORY.newSetting(
								new StringSourceUserRepository(
										"Jasmine:mission%3Aimpossible")),
						ChainingGeneralSettingSpecConstants.CHAINING_SOCKS_SERVER_URI.newSetting(
								Scheme.SOCKS5.newSocksServerUri(
										InetAddress.getLoopbackAddress().getHostAddress(), 
										Integer.valueOf(CHAINED_SOCKS_SERVER_PORT_3_USING_SOCKS5_USERPASSAUTH))),
						ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_METHODS.newSetting(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_USERPASSAUTH_USERNAME.newSetting(
								"Abu"),
						ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_USERPASSAUTH_PASSWORD.newSetting(
								EncryptedPassword.newInstance("safeDriversSave40%".toCharArray())))),
				Configuration.newUnmodifiableInstance(Settings.newInstance(
						GeneralSettingSpecConstants.PORT.newSetting(
								Port.newInstance(CHAINED_SOCKS_SERVER_PORT_3_USING_SOCKS5_USERPASSAUTH)),
						Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						Socks5SettingSpecConstants.SOCKS5_USERPASSAUTH_USER_REPOSITORY.newSetting(
								new StringSourceUserRepository(
										"Abu:safeDriversSave40%25")))));
	}
	
	private static List<Configuration> newChainedConfigurationsUsingSsl() {
		return Arrays.asList(
				Configuration.newUnmodifiableInstance(Settings.newInstance(
						GeneralSettingSpecConstants.PORT.newSetting(
								Port.newInstance(CHAINED_SOCKS_SERVER_PORT_1_USING_SSL)),
						ChainingGeneralSettingSpecConstants.CHAINING_SOCKS_SERVER_URI.newSetting(
								Scheme.SOCKS5.newSocksServerUri(
										InetAddress.getLoopbackAddress().getHostAddress(), 
										Integer.valueOf(CHAINED_SOCKS_SERVER_PORT_2_USING_SSL))),
						ChainingDtlsSettingSpecConstants.CHAINING_DTLS_ENABLED.newSetting(Boolean.TRUE),
						ChainingDtlsSettingSpecConstants.CHAINING_DTLS_TRUST_STORE_FILE.newSetting(
								TestResourceConstants.JARGYLE_CLIENTSERVER_SERVER_KEY_STORE_FILE.getFile()),
						ChainingDtlsSettingSpecConstants.CHAINING_DTLS_TRUST_STORE_PASSWORD.newSettingOfParsableValue(
								TestResourceConstants.JARGYLE_CLIENTSERVER_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()),				
						ChainingSslSettingSpecConstants.CHAINING_SSL_ENABLED.newSetting(Boolean.TRUE),
						ChainingSslSettingSpecConstants.CHAINING_SSL_TRUST_STORE_FILE.newSetting(
								TestResourceConstants.JARGYLE_CLIENTSERVER_SERVER_KEY_STORE_FILE.getFile()),
						ChainingSslSettingSpecConstants.CHAINING_SSL_TRUST_STORE_PASSWORD.newSettingOfParsableValue(
								TestResourceConstants.JARGYLE_CLIENTSERVER_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()))),
				Configuration.newUnmodifiableInstance(Settings.newInstance(
						GeneralSettingSpecConstants.PORT.newSetting(
								Port.newInstance(CHAINED_SOCKS_SERVER_PORT_2_USING_SSL)),
						DtlsSettingSpecConstants.DTLS_ENABLED.newSetting(Boolean.TRUE),
						DtlsSettingSpecConstants.DTLS_KEY_STORE_FILE.newSetting(
								TestResourceConstants.JARGYLE_CLIENTSERVER_SERVER_KEY_STORE_FILE.getFile()),
						DtlsSettingSpecConstants.DTLS_KEY_STORE_PASSWORD.newSettingOfParsableValue(
								TestResourceConstants.JARGYLE_CLIENTSERVER_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()),				
						SslSettingSpecConstants.SSL_ENABLED.newSetting(Boolean.TRUE),
						SslSettingSpecConstants.SSL_KEY_STORE_FILE.newSetting(
								TestResourceConstants.JARGYLE_CLIENTSERVER_SERVER_KEY_STORE_FILE.getFile()),
						SslSettingSpecConstants.SSL_KEY_STORE_PASSWORD.newSettingOfParsableValue(
								TestResourceConstants.JARGYLE_CLIENTSERVER_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()))));		
	}
	
	private static SocksClient newChainedSocks5ClientToConfigurations() {
		SocksClient client1 = Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(SOCKS_SERVER_PORT_1))
				.newSocksClient(Properties.newInstance());
		SocksClient client2 = Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(SOCKS_SERVER_PORT_2))
				.newSocksClient(Properties.newInstance(), client1);
		SocksClient client3 = Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(SOCKS_SERVER_PORT_3))
				.newSocksClient(Properties.newInstance(), client2);
		return client3;
	}
	
	private static SocksClient newChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth() {
		SocksClient client1 = Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(SOCKS_SERVER_PORT_1_USING_SOCKS5_USERPASSAUTH))
				.newSocksClient(Properties.newInstance(
						Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						Socks5PropertySpecConstants.SOCKS5_USERPASSAUTH_USERNAME.newProperty(
								"Aladdin"),
						Socks5PropertySpecConstants.SOCKS5_USERPASSAUTH_PASSWORD.newProperty(
								EncryptedPassword.newInstance(
										"opensesame".toCharArray()))));
		SocksClient client2 = Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(SOCKS_SERVER_PORT_2_USING_SOCKS5_USERPASSAUTH))
				.newSocksClient(Properties.newInstance(
						Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						Socks5PropertySpecConstants.SOCKS5_USERPASSAUTH_USERNAME.newProperty(
								"Jasmine"),
						Socks5PropertySpecConstants.SOCKS5_USERPASSAUTH_PASSWORD.newProperty(
								EncryptedPassword.newInstance(
										"mission:impossible".toCharArray()))), 
						client1);
		SocksClient client3 = Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(SOCKS_SERVER_PORT_3_USING_SOCKS5_USERPASSAUTH))
				.newSocksClient(Properties.newInstance(
						Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						Socks5PropertySpecConstants.SOCKS5_USERPASSAUTH_USERNAME.newProperty(
								"Abu"),
						Socks5PropertySpecConstants.SOCKS5_USERPASSAUTH_PASSWORD.newProperty(
								EncryptedPassword.newInstance(
										"safeDriversSave40%".toCharArray()))), 
						client2);
		return client3;
	}
	
	private static SocksClient newChainedSocks5ClientToConfigurationsUsingSsl() {
		SocksClient client1 = Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(SOCKS_SERVER_PORT_1_USING_SSL))
				.newSocksClient(Properties.newInstance());
		SocksClient client2 = Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(SOCKS_SERVER_PORT_2_USING_SSL))
				.newSocksClient(Properties.newInstance(
						DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(
								Boolean.TRUE),
						DtlsPropertySpecConstants.DTLS_TRUST_STORE_FILE.newProperty(
								TestResourceConstants.JARGYLE_CLIENTSERVER_SERVER_KEY_STORE_FILE.getFile()),
						DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newPropertyOfParsableValue(
								TestResourceConstants.JARGYLE_CLIENTSERVER_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()),				
						SslPropertySpecConstants.SSL_ENABLED.newProperty(
								Boolean.TRUE),
						SslPropertySpecConstants.SSL_TRUST_STORE_FILE.newProperty(
								TestResourceConstants.JARGYLE_CLIENTSERVER_SERVER_KEY_STORE_FILE.getFile()),
						SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newPropertyOfParsableValue(
								TestResourceConstants.JARGYLE_CLIENTSERVER_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString())), 
						client1);
		return client2;
	}
	
	private static List<Configuration> newConfigurations() {
		return Arrays.asList(
				Configuration.newUnmodifiableInstance(Settings.newInstance(
						GeneralSettingSpecConstants.PORT.newSetting(
								Port.newInstance(SOCKS_SERVER_PORT_1)))),
				Configuration.newUnmodifiableInstance(Settings.newInstance(
						GeneralSettingSpecConstants.PORT.newSetting(
								Port.newInstance(SOCKS_SERVER_PORT_2)))),
				Configuration.newUnmodifiableInstance(Settings.newInstance(
						GeneralSettingSpecConstants.PORT.newSetting(
								Port.newInstance(SOCKS_SERVER_PORT_3)))));
	}
	
	private static List<Configuration> newConfigurationsUsingSocks5Userpassauth() {
		return Arrays.asList(
				Configuration.newUnmodifiableInstance(Settings.newInstance(
						GeneralSettingSpecConstants.PORT.newSetting(
								Port.newInstance(SOCKS_SERVER_PORT_1_USING_SOCKS5_USERPASSAUTH)),
						Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						Socks5SettingSpecConstants.SOCKS5_USERPASSAUTH_USER_REPOSITORY.newSetting(
								new StringSourceUserRepository(
										"Aladdin:opensesame")))),
				Configuration.newUnmodifiableInstance(Settings.newInstance(
						GeneralSettingSpecConstants.PORT.newSetting(
								Port.newInstance(SOCKS_SERVER_PORT_2_USING_SOCKS5_USERPASSAUTH)),
						Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						Socks5SettingSpecConstants.SOCKS5_USERPASSAUTH_USER_REPOSITORY.newSetting(
								new StringSourceUserRepository(
										"Jasmine:mission%3Aimpossible")))),
				Configuration.newUnmodifiableInstance(Settings.newInstance(
						GeneralSettingSpecConstants.PORT.newSetting(
								Port.newInstance(SOCKS_SERVER_PORT_3_USING_SOCKS5_USERPASSAUTH)),
						Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						Socks5SettingSpecConstants.SOCKS5_USERPASSAUTH_USER_REPOSITORY.newSetting(
								new StringSourceUserRepository(
										"Abu:safeDriversSave40%25")))));
	}
	
	private static List<Configuration> newConfigurationsUsingSsl() {
		return Arrays.asList(
				Configuration.newUnmodifiableInstance(Settings.newInstance(
						GeneralSettingSpecConstants.PORT.newSetting(
								Port.newInstance(SOCKS_SERVER_PORT_1_USING_SSL)))),
				Configuration.newUnmodifiableInstance(Settings.newInstance(
						GeneralSettingSpecConstants.PORT.newSetting(
								Port.newInstance(SOCKS_SERVER_PORT_2_USING_SSL)),
						DtlsSettingSpecConstants.DTLS_ENABLED.newSetting(Boolean.TRUE),
						DtlsSettingSpecConstants.DTLS_KEY_STORE_FILE.newSetting(
								TestResourceConstants.JARGYLE_CLIENTSERVER_SERVER_KEY_STORE_FILE.getFile()),
						DtlsSettingSpecConstants.DTLS_KEY_STORE_PASSWORD.newSettingOfParsableValue(
								TestResourceConstants.JARGYLE_CLIENTSERVER_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()),				
						SslSettingSpecConstants.SSL_ENABLED.newSetting(Boolean.TRUE),
						SslSettingSpecConstants.SSL_KEY_STORE_FILE.newSetting(
								TestResourceConstants.JARGYLE_CLIENTSERVER_SERVER_KEY_STORE_FILE.getFile()),
						SslSettingSpecConstants.SSL_KEY_STORE_PASSWORD.newSettingOfParsableValue(
								TestResourceConstants.JARGYLE_CLIENTSERVER_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()))));
	}
	
	private static SocksClient newSocks5ClientToChainedConfigurations() {
		return Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(CHAINED_SOCKS_SERVER_PORT_1))
				.newSocksClient(Properties.newInstance());
	}
	
	private static SocksClient newSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth() {
		return Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(CHAINED_SOCKS_SERVER_PORT_1_USING_SOCKS5_USERPASSAUTH))
				.newSocksClient(Properties.newInstance(
						Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
								Methods.newInstance(Method.USERNAME_PASSWORD)),
						Socks5PropertySpecConstants.SOCKS5_USERPASSAUTH_USERNAME.newProperty(
								"Aladdin"),
						Socks5PropertySpecConstants.SOCKS5_USERPASSAUTH_PASSWORD.newProperty(
								EncryptedPassword.newInstance(
										"opensesame".toCharArray()))));
	}
	
	private static SocksClient newSocks5ClientToChainedConfigurationsUsingSsl() {
		return Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				Integer.valueOf(CHAINED_SOCKS_SERVER_PORT_1_USING_SSL))
				.newSocksClient(Properties.newInstance());
	}

	private static List<SocksServer> newStartedSocksServers(
			final List<Configuration> configurations) throws IOException {
		int configurationsSize = configurations.size();
		List<SocksServer> socksServers = new ArrayList<SocksServer>();
		if (configurationsSize > 0) {
			for (int i = configurationsSize - 1; i > -1; i--) {
				Configuration configuration = configurations.get(i);
				SocksServer socksServer = new SocksServer(configuration);
				socksServers.add(0, socksServer);
				socksServer.start();
			}
		}
		return Collections.unmodifiableList(socksServers);
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		datagramEchoServer = new DatagramEchoServer();
		echoServer = new EchoServer();
		datagramEchoServer.start();
		echoServer.start();
		chainedSocksServers = newStartedSocksServers(newChainedConfigurations());
		chainedSocksServersUsingSocks5Userpassauth = newStartedSocksServers(
				newChainedConfigurationsUsingSocks5Userpassauth());
		chainedSocksServersUsingSsl = newStartedSocksServers(
				newChainedConfigurationsUsingSsl());
		socksServers = newStartedSocksServers(newConfigurations());
		socksServersUsingSocks5Userpassauth = newStartedSocksServers(
				newConfigurationsUsingSocks5Userpassauth());
		socksServersUsingSsl = newStartedSocksServers(
				newConfigurationsUsingSsl());		
	}
	
	private static void stopSocksServers(
			final List<SocksServer> socksServers) throws IOException {
		for (SocksServer socksServer : socksServers) {
			if (!socksServer.getState().equals(SocksServer.State.STOPPED)) {
				socksServer.stop();
			}
		}
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		if (!datagramEchoServer.getState().equals(DatagramEchoServer.State.STOPPED)) {
			datagramEchoServer.stop();
		}
		if (!echoServer.getState().equals(EchoServer.State.STOPPED)) {
			echoServer.stop();
		}
		stopSocksServers(chainedSocksServers);
		stopSocksServers(chainedSocksServersUsingSocks5Userpassauth);
		stopSocksServers(chainedSocksServersUsingSsl);
		stopSocksServers(socksServers);
		stopSocksServers(socksServersUsingSocks5Userpassauth);
		stopSocksServers(socksServersUsingSsl);
		// ThreadHelper.sleepForThreeSeconds();
	}
	
	@Test
	public void testDatagramEchoClientBehindSocks5ServerChainingUsingChainedSocks5ClientToConfigurations01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newChainedSocks5ClientToConfigurations().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testDatagramEchoClientBehindSocks5ServerChainingUsingChainedSocks5ClientToConfigurations02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newChainedSocks5ClientToConfigurations().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testDatagramEchoClientBehindSocks5ServerChainingUsingChainedSocks5ClientToConfigurations03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newChainedSocks5ClientToConfigurations().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testDatagramEchoClientBehindSocks5ServerChainingUsingChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testDatagramEchoClientBehindSocks5ServerChainingUsingChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testDatagramEchoClientBehindSocks5ServerChainingUsingChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testDatagramEchoClientBehindSocks5ServerChainingUsingChainedSocks5ClientToConfigurationsUsingSsl01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newChainedSocks5ClientToConfigurationsUsingSsl().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testDatagramEchoClientBehindSocks5ServerChainingUsingChainedSocks5ClientToConfigurationsUsingSsl02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newChainedSocks5ClientToConfigurationsUsingSsl().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testDatagramEchoClientBehindSocks5ServerChainingUsingChainedSocks5ClientToConfigurationsUsingSsl03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newChainedSocks5ClientToConfigurationsUsingSsl().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientBehindSocks5ServerChainingUsingSocks5ClientToChainedConfigurations01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientToChainedConfigurations().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}

	@Test
	public void testDatagramEchoClientBehindSocks5ServerChainingUsingSocks5ClientToChainedConfigurations02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientToChainedConfigurations().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testDatagramEchoClientBehindSocks5ServerChainingUsingSocks5ClientToChainedConfigurations03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientToChainedConfigurations().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testDatagramEchoClientBehindSocks5ServerChainingUsingSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testDatagramEchoClientBehindSocks5ServerChainingUsingSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testDatagramEchoClientBehindSocks5ServerChainingUsingSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testDatagramEchoClientBehindSocks5ServerChainingUsingSocks5ClientToChainedConfigurationsUsingSsl01() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientToChainedConfigurationsUsingSsl().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testDatagramEchoClientBehindSocks5ServerChainingUsingSocks5ClientToChainedConfigurationsUsingSsl02() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientToChainedConfigurationsUsingSsl().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testDatagramEchoClientBehindSocks5ServerChainingUsingSocks5ClientToChainedConfigurationsUsingSsl03() throws IOException {
		DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
				newSocks5ClientToChainedConfigurationsUsingSsl().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = datagramEchoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientBehindSocks5ServerChainingUsingChainedSocks5ClientToConfigurations01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newChainedSocks5ClientToConfigurations().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientBehindSocks5ServerChainingUsingChainedSocks5ClientToConfigurations02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newChainedSocks5ClientToConfigurations().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientBehindSocks5ServerChainingUsingChainedSocks5ClientToConfigurations03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newChainedSocks5ClientToConfigurations().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientBehindSocks5ServerChainingUsingChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientBehindSocks5ServerChainingUsingChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientBehindSocks5ServerChainingUsingChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}
	@Test
	public void testEchoClientBehindSocks5ServerChainingUsingChainedSocks5ClientToConfigurationsUsingSsl01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newChainedSocks5ClientToConfigurationsUsingSsl().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientBehindSocks5ServerChainingUsingChainedSocks5ClientToConfigurationsUsingSsl02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newChainedSocks5ClientToConfigurationsUsingSsl().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientBehindSocks5ServerChainingUsingChainedSocks5ClientToConfigurationsUsingSsl03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newChainedSocks5ClientToConfigurationsUsingSsl().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientBehindSocks5ServerChainingUsingSocks5ClientToChainedConfigurations01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientToChainedConfigurations().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientBehindSocks5ServerChainingUsingSocks5ClientToChainedConfigurations02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientToChainedConfigurations().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientBehindSocks5ServerChainingUsingSocks5ClientToChainedConfigurations03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientToChainedConfigurations().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientBehindSocks5ServerChainingUsingSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientBehindSocks5ServerChainingUsingSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientBehindSocks5ServerChainingUsingSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientBehindSocks5ServerChainingUsingSocks5ClientToChainedConfigurationsUsingSsl01() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientToChainedConfigurationsUsingSsl().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_01;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientBehindSocks5ServerChainingUsingSocks5ClientToChainedConfigurationsUsingSsl02() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientToChainedConfigurationsUsingSsl().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_02;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoClientBehindSocks5ServerChainingUsingSocks5ClientToChainedConfigurationsUsingSsl03() throws IOException {
		EchoClient echoClient = new EchoClient(
				newSocks5ClientToChainedConfigurationsUsingSsl().newSocksNetObjectFactory()); 
		String string = TestStringConstants.STRING_03;
		String returningString = echoClient.echo(string);
		assertEquals(string, returningString);
	}
	
	@Test
	public void testEchoServerBehindSocks5ServerChainingUsingChainedSocks5ClientToConfigurations01() throws IOException {
		EchoServer echServer = new EchoServer(
				newChainedSocks5ClientToConfigurations().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_01;
		echServer.startThenExecuteThenStop(() -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}
	
	@Test
	public void testEchoServerBehindSocks5ServerChainingUsingChainedSocks5ClientToConfigurations02() throws IOException {
		EchoServer echServer = new EchoServer(
				newChainedSocks5ClientToConfigurations().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_02;
		echServer.startThenExecuteThenStop(() -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}
	
	@Test
	public void testEchoServerBehindSocks5ServerChainingUsingChainedSocks5ClientToConfigurations03() throws IOException {
		EchoServer echServer = new EchoServer(
				newChainedSocks5ClientToConfigurations().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_03;
		echServer.startThenExecuteThenStop(() -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}
	
	@Test
	public void testEchoServerBehindSocks5ServerChainingUsingChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth01() throws IOException {
		EchoServer echServer = new EchoServer(
				newChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_01;
		echServer.startThenExecuteThenStop(() -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}
	
	@Test
	public void testEchoServerBehindSocks5ServerChainingUsingChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth02() throws IOException {
		EchoServer echServer = new EchoServer(
				newChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_02;
		echServer.startThenExecuteThenStop(() -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}
	
	@Test
	public void testEchoServerBehindSocks5ServerChainingUsingChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth03() throws IOException {
		EchoServer echServer = new EchoServer(
				newChainedSocks5ClientToConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_03;
		echServer.startThenExecuteThenStop(() -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}
	
	@Test
	public void testEchoServerBehindSocks5ServerChainingUsingChainedSocks5ClientToConfigurationsUsingSsl01() throws IOException {
		EchoServer echServer = new EchoServer(
				newChainedSocks5ClientToConfigurationsUsingSsl().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_01;
		echServer.startThenExecuteThenStop(() -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}
	
	@Test
	public void testEchoServerBehindSocks5ServerChainingUsingChainedSocks5ClientToConfigurationsUsingSsl02() throws IOException {
		EchoServer echServer = new EchoServer(
				newChainedSocks5ClientToConfigurationsUsingSsl().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_02;
		echServer.startThenExecuteThenStop(() -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}
	
	@Test
	public void testEchoServerBehindSocks5ServerChainingUsingChainedSocks5ClientToConfigurationsUsingSsl03() throws IOException {
		EchoServer echServer = new EchoServer(
				newChainedSocks5ClientToConfigurationsUsingSsl().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_03;
		echServer.startThenExecuteThenStop(() -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}
	
	@Test
	public void testEchoServerBehindSocks5ServerChainingUsingSocks5ClientToChainedConfigurations01() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientToChainedConfigurations().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_01;
		echServer.startThenExecuteThenStop(() -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}
	
	@Test
	public void testEchoServerBehindSocks5ServerChainingUsingSocks5ClientToChainedConfigurations02() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientToChainedConfigurations().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_02;
		echServer.startThenExecuteThenStop(() -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}
	
	@Test
	public void testEchoServerBehindSocks5ServerChainingUsingSocks5ClientToChainedConfigurations03() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientToChainedConfigurations().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_03;
		echServer.startThenExecuteThenStop(() -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}
	
	@Test
	public void testEchoServerBehindSocks5ServerChainingUsingSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth01() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_01;
		echServer.startThenExecuteThenStop(() -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}
	
	@Test
	public void testEchoServerBehindSocks5ServerChainingUsingSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth02() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_02;
		echServer.startThenExecuteThenStop(() -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}
	
	@Test
	public void testEchoServerBehindSocks5ServerChainingUsingSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth03() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientToChainedConfigurationsUsingSocks5Userpassauth().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_03;
		echServer.startThenExecuteThenStop(() -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}
	
	@Test
	public void testEchoServerBehindSocks5ServerChainingUsingSocks5ClientToChainedConfigurationsUsingSsl01() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientToChainedConfigurationsUsingSsl().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_01;
		echServer.startThenExecuteThenStop(() -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}
	
	@Test
	public void testEchoServerBehindSocks5ServerChainingUsingSocks5ClientToChainedConfigurationsUsingSsl02() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientToChainedConfigurationsUsingSsl().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_02;
		echServer.startThenExecuteThenStop(() -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}
	
	@Test
	public void testEchoServerBehindSocks5ServerChainingUsingSocks5ClientToChainedConfigurationsUsingSsl03() throws IOException {
		EchoServer echServer = new EchoServer(
				newSocks5ClientToChainedConfigurationsUsingSsl().newSocksNetObjectFactory(), 0); 
		String string = TestStringConstants.STRING_03;
		echServer.startThenExecuteThenStop(() -> {
			String returningString = new EchoClient().echo(
					string, echServer.getInetAddress(), echServer.getPort());
			assertEquals(string, returningString);
		});
	}
	
}
