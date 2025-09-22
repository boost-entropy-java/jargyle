package com.github.jh3nd3rs0n.jargyle.test.echo;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.GeneralSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Settings;
import com.github.jh3nd3rs0n.jargyle.server.Socks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod.UserRepositorySpecConstants;
import com.github.jh3nd3rs0n.jargyle.test.help.string.StringConstants;
import com.github.jh3nd3rs0n.jargyle.test.help.thread.ThreadHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class EchoClientUsingProxiedNetObjectFactorySetToSocksServerUsingSocks5UserpassMethodIT {

    private static EchoServer echoServer;
    private static int echoServerPort;

    private static AbstractSocksServer socksServerUsingSocks5UserpassMethod;

    private static Proxy proxy;

    private static Authenticator defaultAuthenticator;

    @Rule
    public Timeout globalTimeout = Timeout.builder()
            .withTimeout(60, TimeUnit.SECONDS)
            .withLookingForStuckThread(true)
            .build();

    private static AbstractSocksServer newSocksServerUsingSocks5UserpassMethod() throws IOException {
        String s = "Aladdin:opensesame," +
                "Jasmine:mission%3Aimpossible," +
                "Abu:safeDriversSave40%25," +
                "Jafar:opensesame";
        AbstractSocksServer socksServer = new JargyleSocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(
                        Port.valueOf(0)),
                Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
                        Methods.of(Method.USERNAME_PASSWORD)),
                Socks5SettingSpecConstants.SOCKS5_USERPASSMETHOD_USER_REPOSITORY.newSetting(
                        UserRepositorySpecConstants.STRING_SOURCE_USER_REPOSITORY.newUserRepository(
                                s)),
                Socks5SettingSpecConstants.SOCKS5_ON_REQUEST_RELAY_IDLE_TIMEOUT.newSetting(
                        PositiveInteger.valueOf(500)),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(EchoDatagramServer.RECEIVE_BUFFER_SIZE)))));
        socksServer.start();
        proxy = new Proxy(
                Proxy.Type.SOCKS,
                new InetSocketAddress(
                        socksServer.getInetAddress(),
                        socksServer.getPort()));
        return socksServer;
    }

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        echoServer = EchoServer.newInstance(0);
        echoServer.start();
        echoServerPort = echoServer.getPort();
        socksServerUsingSocks5UserpassMethod = newSocksServerUsingSocks5UserpassMethod();
        defaultAuthenticator = Authenticator.getDefault();
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        if (echoServer != null
                && !echoServer.getState().equals(EchoServer.State.STOPPED)) {
            echoServer.stop();
        }
        if (socksServerUsingSocks5UserpassMethod != null
                && !socksServerUsingSocks5UserpassMethod.getState().equals(
                AbstractSocksServer.State.STOPPED)) {
            socksServerUsingSocks5UserpassMethod.stop();
        }
        ThreadHelper.interruptibleSleepForThreeSeconds();
    }

    @Test(expected = IOException.class)
    public void testEchoClientUsingProxiedNetObjectFactorySetToSocksServerUsingSocks5UserpassMethodForIOException01() throws IOException {
        Authenticator.setDefault(new AuthenticatorForBogusUser01());
        try {
            EchoClient echoClient = new EchoClient(
                    new ProxiedNetObjectFactory(proxy));
            String string = StringConstants.STRING_01;
            String returningString = echoClient.echo(string, echoServerPort);
            assertEquals(string, returningString);
        } finally {
            Authenticator.setDefault(defaultAuthenticator);
        }
    }

    @Test(expected = IOException.class)
    public void testEchoClientUsingProxiedNetObjectFactorySetToSocksServerUsingSocks5UserpassMethodForIOException02() throws IOException {
        Authenticator.setDefault(new AuthenticatorForBogusUser02());
        try {
            EchoClient echoClient = new EchoClient(
                    new ProxiedNetObjectFactory(proxy));
            String string = StringConstants.STRING_02;
            String returningString = echoClient.echo(string, echoServerPort);
            assertEquals(string, returningString);
        } finally {
            Authenticator.setDefault(defaultAuthenticator);
        }
    }

    @Test
    public void testEchoClientUsingProxiedNetObjectFactorySetToSocksServerUsingSocks5UserpassMethod01() throws IOException {
        Authenticator.setDefault(new AuthenticatorForUser01());
        try {
            EchoClient echoClient = new EchoClient(
                    new ProxiedNetObjectFactory(proxy));
            String string = StringConstants.STRING_01;
            String returningString = echoClient.echo(string, echoServerPort);
            assertEquals(string, returningString);
        } finally {
            Authenticator.setDefault(defaultAuthenticator);
        }
    }

    @Test
    public void testEchoClientUsingProxiedNetObjectFactorySetToSocksServerUsingSocks5UserpassMethod02() throws IOException {
        Authenticator.setDefault(new AuthenticatorForUser02());
        try {
            EchoClient echoClient = new EchoClient(
                    new ProxiedNetObjectFactory(proxy));
            String string = StringConstants.STRING_02;
            String returningString = echoClient.echo(string, echoServerPort);
            assertEquals(string, returningString);
        } finally {
            Authenticator.setDefault(defaultAuthenticator);
        }
    }

    @Test
    public void testEchoClientUsingProxiedNetObjectFactorySetToSocksServerUsingSocks5UserpassMethod03() throws IOException {
        Authenticator.setDefault(new AuthenticatorForUser03());
        try {
            EchoClient echoClient = new EchoClient(
                    new ProxiedNetObjectFactory(proxy));
            String string = StringConstants.STRING_03;
            String returningString = echoClient.echo(string, echoServerPort);
            assertEquals(string, returningString);
        } finally {
            Authenticator.setDefault(defaultAuthenticator);
        }
    }

    @Test
    public void testEchoClientUsingProxiedNetObjectFactorySetToSocksServerUsingSocks5UserpassMethod04() throws IOException {
        Authenticator.setDefault(new AuthenticatorForUser04());
        try {
            EchoClient echoClient = new EchoClient(
                    new ProxiedNetObjectFactory(proxy));
            String string = StringConstants.STRING_04;
            String returningString = echoClient.echo(string, echoServerPort);
            assertEquals(string, returningString);
        } finally {
            Authenticator.setDefault(defaultAuthenticator);
        }
    }

    @Test
    public void testEchoClientUsingProxiedNetObjectFactorySetToSocksServerUsingSocks5UserpassMethod05() throws IOException {
        Authenticator.setDefault(new AuthenticatorForUser04());
        try {
            EchoClient echoClient = new EchoClient(
                    new ProxiedNetObjectFactory(proxy));
            String string = StringConstants.STRING_05;
            String returningString = echoClient.echo(string, echoServerPort);
            assertEquals(string, returningString);
        } finally {
            Authenticator.setDefault(defaultAuthenticator);
        }
    }

    private static final class AuthenticatorForBogusUser01 extends Authenticator {

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(
                    "Bogus", "12345678".toCharArray());
        }

    }

    private static final class AuthenticatorForBogusUser02 extends Authenticator {

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(
                    "Aladdin", "12345678".toCharArray());
        }

    }

    private static final class AuthenticatorForUser01 extends Authenticator {

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(
                    "Aladdin", "opensesame".toCharArray());
        }

    }

    private static final class AuthenticatorForUser02 extends Authenticator {

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(
                    "Jasmine", "mission:impossible".toCharArray());
        }

    }

    private static final class AuthenticatorForUser03 extends Authenticator {

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(
                    "Abu", "safeDriversSave40%".toCharArray());
        }

    }

    private static final class AuthenticatorForUser04 extends Authenticator {

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(
                    "Jafar", "opensesame".toCharArray());
        }

    }

}
