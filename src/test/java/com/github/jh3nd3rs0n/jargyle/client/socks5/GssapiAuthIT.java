package com.github.jh3nd3rs0n.jargyle.client.socks5;

import static org.junit.Assert.assertEquals;

import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.apache.kerby.kerberos.kerb.KrbException;
import org.apache.kerby.kerberos.kerb.server.SimpleKdcServer;
import org.apache.kerby.kerberos.kerb.server.impl.DefaultInternalKdcServerImpl;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.jh3nd3rs0n.jargyle.FilesHelper;
import com.github.jh3nd3rs0n.jargyle.TestStringConstants;
import com.github.jh3nd3rs0n.jargyle.ThreadHelper;
import com.github.jh3nd3rs0n.jargyle.client.DatagramSocketEchoHelper;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.ServerSocketEchoHelper;
import com.github.jh3nd3rs0n.jargyle.client.SocketEchoHelper;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.GeneralSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.ImmutableConfiguration;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;
import com.github.jh3nd3rs0n.jargyle.server.SocksServerHelper;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.ProtectionLevel;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.ProtectionLevels;

public class GssapiAuthIT {
	
	private static final String KDC_REALM = "EXAMPLE.COM";
	private static final String KDC_HOST = 
			InetAddress.getLoopbackAddress().getHostAddress();
	private static final int KDC_PORT = 8000;
	
	private static final String ALICE_PRINCIPAL = "alice";
	private static final String RCMD_SERVICE_PRINCIPAL = String.format(
			"rcmd/%s", InetAddress.getLoopbackAddress().getHostAddress());

	private static final String KRB5_CONF_PROPERTY_NAME = 
			"java.security.krb5.conf";
	private static final String USE_SUBJECT_CREDS_ONLY_PROPERTY_NAME = 
			"javax.security.auth.useSubjectCredsOnly";
	private static final String LOGIN_CONFIG_PROPERTY_NAME = 
			"java.security.auth.login.config";
	
	private static final int SERVER_PORT_USING_SOCKS5_GSSAPI_AUTH = 8100;
	private static final int SERVER_PORT_USING_SOCKS5_GSSAPI_AUTH_NEC_REFERENCE_IMPL = 8200;
	
	private static Path baseDir = null;
	private static Path aliceKeytab = null;
	private static Path rcmdKeytab = null;
	private static Path loginConf = null;	
	private static Path krb5Conf = null;
	
	private static SimpleKdcServer kerbyServer = null;

	private static List<SocksServer> socksServersUsingSocks5GssapiAuth;
	private static List<SocksServer> socksServersUsingSocks5GssapiAuthNecReferenceImpl;
	
	private static Configuration newConfigurationUsingSocks5GssapiAuth() {
		return ImmutableConfiguration.newInstance(Settings.newInstance(
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.newInstance(SERVER_PORT_USING_SOCKS5_GSSAPI_AUTH)),
				Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
						Methods.newInstance(Method.GSSAPI))));
	}
	
	private static Configuration newConfigurationUsingSocks5GssapiAuthNecReferenceImpl() {
		return ImmutableConfiguration.newInstance(Settings.newInstance(
				GeneralSettingSpecConstants.PORT.newSetting(
						Port.newInstance(
								SERVER_PORT_USING_SOCKS5_GSSAPI_AUTH_NEC_REFERENCE_IMPL)),
				Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
						Methods.newInstance(Method.GSSAPI)),
				Socks5SettingSpecConstants.SOCKS5_GSSAPIAUTH_NEC_REFERENCE_IMPL.newSetting(
						Boolean.TRUE)));
	}
	
	private static SocksClient newSocks5ClientUsingSocks5GssapiAuth(
			final ProtectionLevels protectionLevels) {
		Properties properties = Properties.newInstance(
				Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
						Methods.newInstance(Method.GSSAPI)),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTH_SERVICE_NAME.newProperty(
						RCMD_SERVICE_PRINCIPAL),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTH_PROTECTION_LEVELS.newProperty(
						protectionLevels),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTH_NEC_REFERENCE_IMPL.newProperty(
						Boolean.valueOf(false)));
		return Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				SERVER_PORT_USING_SOCKS5_GSSAPI_AUTH)
				.newSocksClient(properties);		
	}
	
	private static SocksClient newSocks5ClientUsingSocks5GssapiAuthNecReferenceImpl(
			final ProtectionLevels protectionLevels) {
		Properties properties = Properties.newInstance(
				Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
						Methods.newInstance(Method.GSSAPI)),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTH_SERVICE_NAME.newProperty(
						RCMD_SERVICE_PRINCIPAL),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTH_PROTECTION_LEVELS.newProperty(
						protectionLevels),
				Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTH_NEC_REFERENCE_IMPL.newProperty(
						Boolean.valueOf(true)));
		return Scheme.SOCKS5.newSocksServerUri(
				InetAddress.getLoopbackAddress().getHostAddress(), 
				SERVER_PORT_USING_SOCKS5_GSSAPI_AUTH_NEC_REFERENCE_IMPL)
				.newSocksClient(properties);		
	}
	@BeforeClass
	public static void setUpBeforeClass() throws IOException, KrbException {
		
		baseDir = Files.createTempDirectory("com.github.jh3nd3rs0n.jargyle-");
		loginConf = Files.createFile(baseDir.resolve("login.conf"));
		aliceKeytab = baseDir.resolve("alice.keytab");
		rcmdKeytab = baseDir.resolve("rmcd.keytab");
		krb5Conf = baseDir.resolve("krb5.conf");
		
		SimpleKdcServer kerbyServer = new SimpleKdcServer();
		kerbyServer.setKdcRealm(KDC_REALM);
		kerbyServer.setKdcHost(KDC_HOST);
		kerbyServer.setKdcPort(KDC_PORT);
		kerbyServer.setWorkDir(baseDir.toFile());
		kerbyServer.setInnerKdcImpl(new DefaultInternalKdcServerImpl(
				kerbyServer.getKdcSetting()));
		kerbyServer.init();
		kerbyServer.createPrincipal(ALICE_PRINCIPAL, "alice");
		kerbyServer.exportPrincipal(ALICE_PRINCIPAL, aliceKeytab.toFile());
		kerbyServer.createPrincipal(RCMD_SERVICE_PRINCIPAL, "rcmd");
		kerbyServer.exportPrincipal(RCMD_SERVICE_PRINCIPAL, rcmdKeytab.toFile());
		kerbyServer.start();
		
		String aliceKeytabUriString = aliceKeytab.toUri().toString();
		String rcmdKeytabUriString = rcmdKeytab.toUri().toString();
		
		FileWriter w = new FileWriter(loginConf.toFile());
		w.write("com.sun.security.jgss.initiate {\n");
		w.write("  com.sun.security.auth.module.Krb5LoginModule required\n");
		w.write(String.format(
				"  principal=\"%s\"\n", ALICE_PRINCIPAL));		
		w.write("  useKeyTab=true\n");
		w.write(String.format(
				"  keyTab=\"%s\"\n", aliceKeytabUriString));
		w.write("  storeKey=true;\n");
		w.write("};\n");
		w.write("com.sun.security.jgss.accept {\n");
		w.write("  com.sun.security.auth.module.Krb5LoginModule required\n");
		w.write(String.format(
				"  principal=\"%s\"\n", RCMD_SERVICE_PRINCIPAL));		
		w.write("  useKeyTab=true\n");
		w.write(String.format(
				"  keyTab=\"%s\"\n", rcmdKeytabUriString));
		w.write("  storeKey=true;\n");
		w.write("};\n");
		w.flush();
		w.close();
		
		System.setProperty(
				KRB5_CONF_PROPERTY_NAME, krb5Conf.toAbsolutePath().toString());
		System.setProperty(USE_SUBJECT_CREDS_ONLY_PROPERTY_NAME, "false");
		System.setProperty(
				LOGIN_CONFIG_PROPERTY_NAME, loginConf.toAbsolutePath().toString());
		
		DatagramSocketEchoHelper.startEchoServer();
		SocketEchoHelper.startEchoServer();
		socksServersUsingSocks5GssapiAuth = 
				SocksServerHelper.newStartedSocksServers(Arrays.asList(
						newConfigurationUsingSocks5GssapiAuth()));
		socksServersUsingSocks5GssapiAuthNecReferenceImpl =
				SocksServerHelper.newStartedSocksServers(Arrays.asList(
						newConfigurationUsingSocks5GssapiAuthNecReferenceImpl()));
	}

	@AfterClass
	public static void tearDownAfterClass() throws IOException, KrbException {
		if (kerbyServer != null) {
			kerbyServer.stop();
			kerbyServer = null;
		}
		if (krb5Conf != null) {
			FilesHelper.attemptsToDeleteIfExists(krb5Conf);
			krb5Conf = null;
		}
		if (aliceKeytab != null) {
			FilesHelper.attemptsToDeleteIfExists(aliceKeytab);
			aliceKeytab = null;
		}
		if (rcmdKeytab != null) {
			FilesHelper.attemptsToDeleteIfExists(rcmdKeytab);
			rcmdKeytab = null;
		}
		if (loginConf != null) {
			FilesHelper.attemptsToDeleteIfExists(loginConf);
			loginConf = null;
		}
		if (baseDir != null) {
			FilesHelper.attemptsToDeleteIfExists(baseDir);
			baseDir = null;
		}
		System.clearProperty(KRB5_CONF_PROPERTY_NAME);
		System.clearProperty(USE_SUBJECT_CREDS_ONLY_PROPERTY_NAME);
		System.clearProperty(LOGIN_CONFIG_PROPERTY_NAME);
		DatagramSocketEchoHelper.stopEchoServer();
		SocketEchoHelper.stopEchoServer();
		SocksServerHelper.stopSocksServers(socksServersUsingSocks5GssapiAuth);
		SocksServerHelper.stopSocksServers(
				socksServersUsingSocks5GssapiAuthNecReferenceImpl);
		ThreadHelper.sleepForThreeSeconds();
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5GssapiAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuth(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5GssapiAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuth(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5GssapiAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuth(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5GssapiAuthNecReferenceImpl01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuthNecReferenceImpl(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5GssapiAuthNecReferenceImpl02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuthNecReferenceImpl(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5GssapiAuthNecReferenceImpl03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuthNecReferenceImpl(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5GssapiAuthNecReferenceImplWithIntegAndConfProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5GssapiAuthNecReferenceImplWithIntegAndConfProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5GssapiAuthNecReferenceImplWithIntegAndConfProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5GssapiAuthNecReferenceImplWithIntegProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5GssapiAuthNecReferenceImplWithIntegProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5GssapiAuthNecReferenceImplWithIntegProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5GssapiAuthWithIntegAndConfProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5GssapiAuthWithIntegAndConfProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5GssapiAuthWithIntegAndConfProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5GssapiAuthWithIntegProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5GssapiAuthWithIntegProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5DatagramSocketUsingSocks5GssapiAuthWithIntegProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = DatagramSocketEchoHelper.echoThroughDatagramSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSocks5GssapiAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuth(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingSocks5GssapiAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuth(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingSocks5GssapiAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuth(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingSocks5GssapiAuthNecReferenceImpl01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuthNecReferenceImpl(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingSocks5GssapiAuthNecReferenceImpl02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuthNecReferenceImpl(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingSocks5GssapiAuthNecReferenceImpl03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuthNecReferenceImpl(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
	@Test
	public void testThroughSocks5ServerSocketUsingSocks5GssapiAuthNecReferenceImplWithIntegAndConfProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingSocks5GssapiAuthNecReferenceImplWithIntegAndConfProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingSocks5GssapiAuthNecReferenceImplWithIntegAndConfProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingSocks5GssapiAuthNecReferenceImplWithIntegProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingSocks5GssapiAuthNecReferenceImplWithIntegProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingSocks5GssapiAuthNecReferenceImplWithIntegProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingSocks5GssapiAuthWithIntegAndConfProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingSocks5GssapiAuthWithIntegAndConfProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingSocks5GssapiAuthWithIntegAndConfProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingSocks5GssapiAuthWithIntegProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingSocks5GssapiAuthWithIntegProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5ServerSocketUsingSocks5GssapiAuthWithIntegProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = ServerSocketEchoHelper.echoThroughServerSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingSocks5GssapiAuth01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuth(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingSocks5GssapiAuth02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuth(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingSocks5GssapiAuth03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuth(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingSocks5GssapiAuthNecReferenceImpl01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuthNecReferenceImpl(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingSocks5GssapiAuthNecReferenceImpl02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuthNecReferenceImpl(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingSocks5GssapiAuthNecReferenceImpl03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuthNecReferenceImpl(
						ProtectionLevels.newInstance(ProtectionLevel.NONE)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingSocks5GssapiAuthNecReferenceImplWithIntegAndConfProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingSocks5GssapiAuthNecReferenceImplWithIntegAndConfProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingSocks5GssapiAuthNecReferenceImplWithIntegAndConfProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingSocks5GssapiAuthNecReferenceImplWithIntegProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingSocks5GssapiAuthNecReferenceImplWithIntegProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingSocks5GssapiAuthNecReferenceImplWithIntegProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuthNecReferenceImpl(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingSocks5GssapiAuthWithIntegAndConfProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingSocks5GssapiAuthWithIntegAndConfProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingSocks5GssapiAuthWithIntegAndConfProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG_AND_CONF)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingSocks5GssapiAuthWithIntegProtection01() throws IOException {
		String string = TestStringConstants.STRING_01;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingSocks5GssapiAuthWithIntegProtection02() throws IOException {
		String string = TestStringConstants.STRING_02;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}

	@Test
	public void testThroughSocks5SocketUsingSocks5GssapiAuthWithIntegProtection03() throws IOException {
		String string = TestStringConstants.STRING_03;
		String returningString = SocketEchoHelper.echoThroughSocket(
				string, 
				GssapiAuthIT.newSocks5ClientUsingSocks5GssapiAuth(
						ProtectionLevels.newInstance(
								ProtectionLevel.REQUIRED_INTEG)).newSocksNetObjectFactory());
		assertEquals(string, returningString);
	}
	
}
