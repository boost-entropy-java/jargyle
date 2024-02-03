package com.github.jh3nd3rs0n.echo.integration.test;

import com.github.jh3nd3rs0n.echo.DatagramEchoClient;
import com.github.jh3nd3rs0n.echo.DatagramEchoServer;
import com.github.jh3nd3rs0n.echo.EchoClient;
import com.github.jh3nd3rs0n.echo.EchoServer;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod.UserRepositorySpecConstants;
import com.github.jh3nd3rs0n.test.help.TestStringConstants;
import com.github.jh3nd3rs0n.test.help.ThreadHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.github.jh3nd3rs0n.echo.integration.test.SocksServersHelper.newStartedSocksServers;
import static com.github.jh3nd3rs0n.echo.integration.test.SocksServersHelper.stopSocksServers;
import static org.junit.Assert.assertEquals;

public class EchoThroughChainedSocks5ClientToSocksServersUsingSocks5UserpassMethodIT {

    private static final int SOCKS_SERVER_PORT_1_USING_SOCKS5_USERPASSMETHOD = 6100;
    private static final int SOCKS_SERVER_PORT_2_USING_SOCKS5_USERPASSMETHOD = 6200;
    private static final int SOCKS_SERVER_PORT_3_USING_SOCKS5_USERPASSMETHOD = 6300;

    private static DatagramEchoServer datagramEchoServer;
    private static EchoServer echoServer;

    private static List<SocksServer> socksServersUsingSocks5UserpassMethod;

    @Rule
    public Timeout globalTimeout = Timeout.builder()
            .withTimeout(5, TimeUnit.MINUTES)
            .withLookingForStuckThread(true)
            .build();

    private static SocksClient newChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod() {
        SocksClient client1 = Scheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        Integer.valueOf(SOCKS_SERVER_PORT_1_USING_SOCKS5_USERPASSMETHOD))
                .newSocksClient(Properties.of(
                        Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
                                Methods.of(Method.USERNAME_PASSWORD)),
                        Socks5PropertySpecConstants.SOCKS5_USERPASSMETHOD_USERNAME.newProperty(
                                "Aladdin"),
                        Socks5PropertySpecConstants.SOCKS5_USERPASSMETHOD_PASSWORD.newProperty(
                                EncryptedPassword.newInstance(
                                        "opensesame".toCharArray()))));
        SocksClient client2 = Scheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        Integer.valueOf(SOCKS_SERVER_PORT_2_USING_SOCKS5_USERPASSMETHOD))
                .newSocksClient(Properties.of(
                                Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
                                        Methods.of(Method.USERNAME_PASSWORD)),
                                Socks5PropertySpecConstants.SOCKS5_USERPASSMETHOD_USERNAME.newProperty(
                                        "Jasmine"),
                                Socks5PropertySpecConstants.SOCKS5_USERPASSMETHOD_PASSWORD.newProperty(
                                        EncryptedPassword.newInstance(
                                                "mission:impossible".toCharArray()))),
                        client1);
        SocksClient client3 = Scheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        Integer.valueOf(SOCKS_SERVER_PORT_3_USING_SOCKS5_USERPASSMETHOD))
                .newSocksClient(Properties.of(
                                Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
                                        Methods.of(Method.USERNAME_PASSWORD)),
                                Socks5PropertySpecConstants.SOCKS5_USERPASSMETHOD_USERNAME.newProperty(
                                        "Abu"),
                                Socks5PropertySpecConstants.SOCKS5_USERPASSMETHOD_PASSWORD.newProperty(
                                        EncryptedPassword.newInstance(
                                                "safeDriversSave40%".toCharArray()))),
                        client2);
        return client3;
    }

    private static List<Configuration> newConfigurationsUsingSocks5UserpassMethod() {
        return Arrays.asList(
                Configuration.newUnmodifiableInstance(Settings.of(
                        GeneralSettingSpecConstants.PORT.newSetting(
                                Port.valueOf(SOCKS_SERVER_PORT_1_USING_SOCKS5_USERPASSMETHOD)),
                        Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
                                Methods.of(Method.USERNAME_PASSWORD)),
                        Socks5SettingSpecConstants.SOCKS5_USERPASSMETHOD_USER_REPOSITORY.newSetting(
                                UserRepositorySpecConstants.STRING_SOURCE_USER_REPOSITORY.newUserRepository(
                                        "Aladdin:opensesame")))),
                Configuration.newUnmodifiableInstance(Settings.of(
                        GeneralSettingSpecConstants.PORT.newSetting(
                                Port.valueOf(SOCKS_SERVER_PORT_2_USING_SOCKS5_USERPASSMETHOD)),
                        Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
                                Methods.of(Method.USERNAME_PASSWORD)),
                        Socks5SettingSpecConstants.SOCKS5_USERPASSMETHOD_USER_REPOSITORY.newSetting(
                                UserRepositorySpecConstants.STRING_SOURCE_USER_REPOSITORY.newUserRepository(
                                        "Jasmine:mission%3Aimpossible")))),
                Configuration.newUnmodifiableInstance(Settings.of(
                        GeneralSettingSpecConstants.PORT.newSetting(
                                Port.valueOf(SOCKS_SERVER_PORT_3_USING_SOCKS5_USERPASSMETHOD)),
                        Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
                                Methods.of(Method.USERNAME_PASSWORD)),
                        Socks5SettingSpecConstants.SOCKS5_USERPASSMETHOD_USER_REPOSITORY.newSetting(
                                UserRepositorySpecConstants.STRING_SOURCE_USER_REPOSITORY.newUserRepository(
                                        "Abu:safeDriversSave40%25")))));
    }

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        datagramEchoServer = new DatagramEchoServer();
        echoServer = new EchoServer();
        datagramEchoServer.start();
        echoServer.start();
        socksServersUsingSocks5UserpassMethod = newStartedSocksServers(
                newConfigurationsUsingSocks5UserpassMethod());
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        if (!datagramEchoServer.getState().equals(DatagramEchoServer.State.STOPPED)) {
            datagramEchoServer.stop();
        }
        if (!echoServer.getState().equals(EchoServer.State.STOPPED)) {
            echoServer.stop();
        }
        stopSocksServers(socksServersUsingSocks5UserpassMethod);
        ThreadHelper.sleepForThreeSeconds();
    }

    @Test
    public void testDatagramEchoClientUsingChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod01() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_01;
        String returningString = datagramEchoClient.echo(string);
        assertEquals(string, returningString);
    }

    @Test
    public void testDatagramEchoClientUsingChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod02() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_02;
        String returningString = datagramEchoClient.echo(string);
        assertEquals(string, returningString);
    }

    @Test
    public void testDatagramEchoClientUsingChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod03() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_03;
        String returningString = datagramEchoClient.echo(string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod01() throws IOException {
        EchoClient echoClient = new EchoClient(
                newChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_01;
        String returningString = echoClient.echo(string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod02() throws IOException {
        EchoClient echoClient = new EchoClient(
                newChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_02;
        String returningString = echoClient.echo(string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod03() throws IOException {
        EchoClient echoClient = new EchoClient(
                newChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_03;
        String returningString = echoClient.echo(string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod01() throws IOException {
        EchoServer echServer = new EchoServer(
                newChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_01;
        EchoServerHelper.startThenExecuteThenStop(echServer, () -> {
            String returningString = new EchoClient().echo(
                    string, echServer.getInetAddress(), echServer.getPort());
            assertEquals(string, returningString);
        });
    }

    @Test
    public void testEchoServerUsingChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod02() throws IOException {
        EchoServer echServer = new EchoServer(
                newChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_02;
        EchoServerHelper.startThenExecuteThenStop(echServer, () -> {
            String returningString = new EchoClient().echo(
                    string, echServer.getInetAddress(), echServer.getPort());
            assertEquals(string, returningString);
        });
    }

    @Test
    public void testEchoServerUsingChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod03() throws IOException {
        EchoServer echServer = new EchoServer(
                newChainedSocks5ClientToSocksServersUsingSocks5UserpassMethod().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_03;
        EchoServerHelper.startThenExecuteThenStop(echServer, () -> {
            String returningString = new EchoClient().echo(
                    string, echServer.getInetAddress(), echServer.getPort());
            assertEquals(string, returningString);
        });
    }

}
