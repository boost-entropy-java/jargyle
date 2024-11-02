package com.github.jh3nd3rs0n.test.echo;

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Scheme;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.test.help.net.DatagramServer;
import com.github.jh3nd3rs0n.test.help.net.Server;
import com.github.jh3nd3rs0n.test.help.string.TestStringConstants;
import com.github.jh3nd3rs0n.test.help.thread.ThreadHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.github.jh3nd3rs0n.test.echo.SocksServersHelper.stopSocksServers;
import static org.junit.Assert.assertEquals;

public class EchoObjectsUsingSocks5ClientToChainedSocksServersTest {

    private static List<SocksServer> chainedSocksServers;
    private static int chainedSocksServerPort1;

    private static DatagramServer datagramEchoServer;
    private static int datagramEchoServerPort;
    private static Server echoServer;
    private static int echoServerPort;

    @Rule
    public Timeout globalTimeout = Timeout.builder()
            .withTimeout(60, TimeUnit.SECONDS)
            .withLookingForStuckThread(true)
            .build();

    private static List<SocksServer> newChainedSocksServers() throws IOException {
        List<SocksServer> socksServerList = new ArrayList<>();
        SocksServer socksServer3 = new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(
                        Port.valueOf(0)),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(DatagramServer.RECEIVE_BUFFER_SIZE)))));
        socksServer3.start();
        int chainedSocksServerPort3 = socksServer3.getPort().intValue();
        socksServerList.add(socksServer3);
        SocksServer socksServer2 = new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(
                        Port.valueOf(0)),
                ChainingGeneralSettingSpecConstants.CHAINING_SOCKS_SERVER_URI.newSetting(
                        Scheme.SOCKS5.newSocksServerUri(
                                InetAddress.getLoopbackAddress().getHostAddress(),
                                chainedSocksServerPort3)),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(DatagramServer.RECEIVE_BUFFER_SIZE)))));
        socksServer2.start();
        int chainedSocksServerPort2 = socksServer2.getPort().intValue();
        socksServerList.add(socksServer2);
        SocksServer socksServer1 = new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(
                        Port.valueOf(0)),
                ChainingGeneralSettingSpecConstants.CHAINING_SOCKS_SERVER_URI.newSetting(
                        Scheme.SOCKS5.newSocksServerUri(
                                InetAddress.getLoopbackAddress().getHostAddress(),
                                chainedSocksServerPort2)),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(DatagramServer.RECEIVE_BUFFER_SIZE)))));
        socksServer1.start();
        chainedSocksServerPort1 = socksServer1.getPort().intValue();
        socksServerList.add(socksServer1);
        return socksServerList;
    }

    private static SocksClient newSocks5ClientToChainedSocksServers() {
        return Scheme.SOCKS5.newSocksServerUri(
                        InetAddress.getLoopbackAddress().getHostAddress(),
                        chainedSocksServerPort1)
                .newSocksClient(Properties.of());
    }

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        datagramEchoServer = DatagramEchoServerHelper.newDatagramEchoServer(0);
        datagramEchoServer.start();
        datagramEchoServerPort = datagramEchoServer.getPort();
        echoServer = EchoServerHelper.newEchoServer(0);
        echoServer.start();
        echoServerPort = echoServer.getPort();
        chainedSocksServers = newChainedSocksServers();
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        if (datagramEchoServer != null
                && !datagramEchoServer.getState().equals(DatagramServer.State.STOPPED)) {
            datagramEchoServer.stop();
        }
        if (echoServer != null
                && !echoServer.getState().equals(Server.State.STOPPED)) {
            echoServer.stop();
        }
        if (chainedSocksServers != null) {
            stopSocksServers(chainedSocksServers);
        }
        ThreadHelper.interruptibleSleepForThreeSeconds();
    }

    @Test
    public void testDatagramEchoClientUsingSocks5ClientToChainedSocksServers01() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newSocks5ClientToChainedSocksServers().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_01;
        String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testDatagramEchoClientUsingSocks5ClientToChainedSocksServers02() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newSocks5ClientToChainedSocksServers().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_02;
        String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testDatagramEchoClientUsingSocks5ClientToChainedSocksServers03() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newSocks5ClientToChainedSocksServers().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_03;
        String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testDatagramEchoClientUsingSocks5ClientToChainedSocksServers04() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newSocks5ClientToChainedSocksServers().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_04;
        String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testDatagramEchoClientUsingSocks5ClientToChainedSocksServers05() throws IOException {
        DatagramEchoClient datagramEchoClient = new DatagramEchoClient(
                newSocks5ClientToChainedSocksServers().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_05;
        String returningString = datagramEchoClient.echo(string, datagramEchoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientToChainedSocksServers01() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientToChainedSocksServers().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_01;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientToChainedSocksServers02() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientToChainedSocksServers().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_02;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientToChainedSocksServers03() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientToChainedSocksServers().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_03;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientToChainedSocksServers04() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientToChainedSocksServers().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_04;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoClientUsingSocks5ClientToChainedSocksServers05() throws IOException {
        EchoClient echoClient = new EchoClient(
                newSocks5ClientToChainedSocksServers().newSocksNetObjectFactory());
        String string = TestStringConstants.STRING_05;
        String returningString = echoClient.echo(string, echoServerPort);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientToChainedSocksServers01() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newSocks5ClientToChainedSocksServers().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_01;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientToChainedSocksServers02() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newSocks5ClientToChainedSocksServers().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_02;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientToChainedSocksServers03() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newSocks5ClientToChainedSocksServers().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_03;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientToChainedSocksServers04() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newSocks5ClientToChainedSocksServers().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_04;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

    @Test
    public void testEchoServerUsingSocks5ClientToChainedSocksServers05() throws IOException {
        Server echServer = EchoServerHelper.newEchoServer(
                newSocks5ClientToChainedSocksServers().newSocksNetObjectFactory(), 0);
        String string = TestStringConstants.STRING_05;
        String returningString = EchoServerHelper.startThenEchoThenStop(
                echServer, new EchoClient(), string);
        assertEquals(string, returningString);
    }

}
