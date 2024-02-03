package com.github.jh3nd3rs0n.echo.integration.test;

import com.github.jh3nd3rs0n.echo.DatagramEchoClient;
import com.github.jh3nd3rs0n.echo.DatagramEchoServer;
import com.github.jh3nd3rs0n.echo.EchoClient;
import com.github.jh3nd3rs0n.echo.EchoServer;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.test.help.TestStringConstants;
import com.github.jh3nd3rs0n.test.help.ThreadHelper;
import com.github.jh3nd3rs0n.test.help.constants.TestResourceConstants;
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

public class EchoThroughSocks5ClientToChainedSocksServersUsingSslIT {

    private static final int CHAINED_SOCKS_SERVER_PORT_1_USING_SSL = 4100;
    private static final int CHAINED_SOCKS_SERVER_PORT_2_USING_SSL = 4200;

    private static List<SocksServer> chainedSocksServersUsingSsl;

    private static DatagramEchoServer datagramEchoServer;
    private static EchoServer echoServer;

    @Rule
    public Timeout globalTimeout = Timeout.builder()
            .withTimeout(5, TimeUnit.MINUTES)
            .withLookingForStuckThread(true)
            .build();

    private static List<Configuration> newChainedConfigurationsUsingSsl() {
        return Arrays.asList(
                Configuration.newUnmodifiableInstance(Settings.of(
                        GeneralSettingSpecConstants.PORT.newSetting(
                                Port.valueOf(CHAINED_SOCKS_SERVER_PORT_1_USING_SSL)),
                        ChainingGeneralSettingSpecConstants.CHAINING_SOCKS_SERVER_URI.newSetting(
                                Scheme.SOCKS5.newSocksServerUri(
                                        InetAddress.getLoopbackAddress().getHostAddress(),
                                        Integer.valueOf(CHAINED_SOCKS_SERVER_PORT_2_USING_SSL))),
                        ChainingDtlsSettingSpecConstants.CHAINING_DTLS_ENABLED.newSetting(Boolean.TRUE),
                        ChainingDtlsSettingSpecConstants.CHAINING_DTLS_TRUST_STORE_FILE.newSetting(
                                TestResourceConstants.ECHO_INTEGRATION_TEST_SOCKS_SERVER_KEY_STORE_FILE.getFile()),
                        ChainingDtlsSettingSpecConstants.CHAINING_DTLS_TRUST_STORE_PASSWORD.newSettingWithParsedValue(
                                TestResourceConstants.ECHO_INTEGRATION_TEST_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()),
                        ChainingSslSettingSpecConstants.CHAINING_SSL_ENABLED.newSetting(Boolean.TRUE),
                        ChainingSslSettingSpecConstants.CHAINING_SSL_TRUST_STORE_FILE.newSetting(
                                TestResourceConstants.ECHO_INTEGRATION_TEST_SOCKS_SERVER_KEY_STORE_FILE.getFile()),
                        ChainingSslSettingSpecConstants.CHAINING_SSL_TRUST_STORE_PASSWORD.newSettingWithParsedValue(
                                TestResourceConstants.ECHO_INTEGRATION_TEST_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()))),
                Configuration.newUnmodifiableInstance(Settings.of(
                        GeneralSettingSpecConstants.PORT.newSetting(
                                Port.valueOf(CHAINED_SOCKS_SERVER_PORT_2_USING_SSL)),
                        DtlsSettingSpecConstants.DTLS_ENABLED.newSetting(Boolean.TRUE),
                        DtlsSettingSpecConstants.DTLS_KEY_STORE_FILE.newSetting(
                                TestResourceConstants.ECHO_INTEGRATION_TEST_SOCKS_SERVER_KEY_STORE_FILE.getFile()),
                        DtlsSettingSpecConstants.DTLS_KEY_STORE_PASSWORD.newSettingWithParsedValue(
                                TestResourceConstants.ECHO_INTEGRATION_TEST_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()),
                        SslSettingSpecConstants.SSL_ENABLED.newSetting(Boolean.TRUE),
                        SslSettingSpecConstants.SSL_KEY_STORE_FILE.newSetting(
                                TestResourceConstants.ECHO_INTEGRATION_TEST_SOCKS_SERVER_KEY_STORE_FILE.getFile()),
                        SslSettingSpecConstants.SSL_KEY_STORE_PASSWORD.newSettingWithParsedValue(
                                TestResourceConstants.ECHO_INTEGRATION_TEST_SOCKS_SERVER_KEY_STORE_PASSWORD_FILE.getContentAsString()))));
    }

    private static SocksClient newSocks5ClientToChainedSocksServersUsingSsl() {
        return Scheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        Integer.valueOf(CHAINED_SOCKS_SERVER_PORT_1_USING_SSL))
                .newSocksClient(Properties.of());
    }

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        datagramEchoServer = new DatagramEchoServer();
        echoServer = new EchoServer();
        datagramEchoServer.start();
        echoServer.start();
        chainedSocksServersUsingSsl = newStartedSocksServers(
                newChainedConfigurationsUsingSsl());
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        if (!datagramEchoServer.getState().equals(DatagramEchoServer.State.STOPPED)) {
            datagramEchoServer.stop();
        }
        if (!echoServer.getState().equals(EchoServer.State.STOPPED)) {
            echoServer.stop();
        }
        stopSocksServers(chainedSocksServersUsingSsl);
        ThreadHelper.sleepForThreeSeconds();
    }

    @Test
    public void testDatagramEchoClientUsingSocks5ClientToChainedSocksServersUsingSsl01() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_01;
        String returningString = datagramEchoClient.echo(string);
        assertEquals(string, returningString);
    }

    @Test
    public void testDatagramEchoClientUsingSocks5ClientToChainedSocksServersUsingSsl02() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_02;
        String returningString = datagramEchoClient.echo(string);
        assertEquals(string, returningString);
    }

    @Test
    public void testDatagramEchoClientUsingSocks5ClientToChainedSocksServersUsingSsl03() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_03;
        String returningString = datagramEchoClient.echo(string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientToChainedSocksServersUsingSsl01() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_01;
        String returningString = echoClient.echo(string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientToChainedSocksServersUsingSsl02() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_02;
        String returningString = echoClient.echo(string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientToChainedSocksServersUsingSsl03() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_03;
        String returningString = echoClient.echo(string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientToChainedSocksServersUsingSsl01() throws IOException {
        EchoServer echServer = new EchoServer(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_01;
        EchoServerHelper.startThenExecuteThenStop(echServer, () -> {
            String returningString = new EchoClient().echo(
                    string, echServer.getInetAddress(), echServer.getPort());
            assertEquals(string, returningString);
        });
    }

    @Test
    public void testEchoServerUsingSocks5ClientToChainedSocksServersUsingSsl02() throws IOException {
        EchoServer echServer = new EchoServer(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_02;
        EchoServerHelper.startThenExecuteThenStop(echServer, () -> {
            String returningString = new EchoClient().echo(
                    string, echServer.getInetAddress(), echServer.getPort());
            assertEquals(string, returningString);
        });
    }

    @Test
    public void testEchoServerUsingSocks5ClientToChainedSocksServersUsingSsl03() throws IOException {
        EchoServer echServer = new EchoServer(
                newSocks5ClientToChainedSocksServersUsingSsl().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_03;
        EchoServerHelper.startThenExecuteThenStop(echServer, () -> {
            String returningString = new EchoClient().echo(
                    string, echServer.getInetAddress(), echServer.getPort());
            assertEquals(string, returningString);
        });
    }

}
