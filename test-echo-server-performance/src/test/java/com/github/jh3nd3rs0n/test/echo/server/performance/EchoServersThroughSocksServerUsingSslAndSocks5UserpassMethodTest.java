package com.github.jh3nd3rs0n.test.echo.server.performance;

import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.test.echo.EchoDatagramClient;
import com.github.jh3nd3rs0n.test.echo.EchoDatagramServerHelper;
import com.github.jh3nd3rs0n.test.echo.EchoClient;
import com.github.jh3nd3rs0n.jargyle.client.*;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.test.echo.EchoServerHelper;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.server.*;
import com.github.jh3nd3rs0n.jargyle.server.socks5.userpassmethod.UserRepositorySpecConstants;
import com.github.jh3nd3rs0n.test.help.net.DatagramServer;
import com.github.jh3nd3rs0n.test.help.net.Server;
import com.github.jh3nd3rs0n.test.help.security.KeyStoreResourceConstants;
import com.github.jh3nd3rs0n.test.help.string.StringConstants;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.nio.file.Path;

public class EchoServersThroughSocksServerUsingSslAndSocks5UserpassMethodTest {

    private static final long DELAY_BETWEEN_THREADS_STARTING = 500;
    private static final int THREAD_COUNT = 100;
    private static final long TIMEOUT = 60000 * 5;

    private static Path performanceReport = null;

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        String className = Thread.currentThread().getStackTrace()[1].getClassName();
        performanceReport = PerformanceReportHelper.createPerformanceReport(
                className + ".txt", "Class " + className);
    }

    private static SocksClient newSocks5ClientUsingSslAndSocks5UserpassMethod(
            final String socksServerHostAddress,
            final int socksServerPort) {
        Properties properties = Properties.of(
                DtlsPropertySpecConstants.DTLS_ENABLED.newProperty(
                        Boolean.TRUE),
                DtlsPropertySpecConstants.DTLS_TRUST_STORE_INPUT_STREAM.newProperty(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
                DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
                SslPropertySpecConstants.SSL_ENABLED.newProperty(
                        Boolean.TRUE),
                SslPropertySpecConstants.SSL_TRUST_STORE_INPUT_STREAM.newProperty(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
                SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.newPropertyWithParsedValue(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
                Socks5PropertySpecConstants.SOCKS5_METHODS.newProperty(
                        Methods.of(Method.USERNAME_PASSWORD)),
                Socks5PropertySpecConstants.SOCKS5_USERPASSMETHOD_USERNAME.newProperty(
                        "Aladdin"),
                Socks5PropertySpecConstants.SOCKS5_USERPASSMETHOD_PASSWORD.newProperty(
                        EncryptedPassword.newInstance("opensesame".toCharArray())));
        return Scheme.SOCKS5.newSocksServerUri(
                        socksServerHostAddress,
                        socksServerPort)
                .newSocksClient(properties);
    }

    private static SocksServer newSocksServerUsingSslAndSocks5UserpassMethod() {
        return new SocksServer(Configuration.newUnmodifiableInstance(Settings.of(
                GeneralSettingSpecConstants.INTERNAL_FACING_BIND_HOST.newSetting(
                        Host.newInstance(InetAddress.getLoopbackAddress().getHostAddress())),
                GeneralSettingSpecConstants.PORT.newSetting(Port.valueOf(0)),
                GeneralSettingSpecConstants.BACKLOG.newSetting(
                        NonNegativeInteger.valueOf(Server.BACKLOG)),
                DtlsSettingSpecConstants.DTLS_ENABLED.newSetting(Boolean.TRUE),
                DtlsSettingSpecConstants.DTLS_KEY_STORE_INPUT_STREAM.newSetting(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
                DtlsSettingSpecConstants.DTLS_KEY_STORE_PASSWORD.newSettingWithParsedValue(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
                SslSettingSpecConstants.SSL_ENABLED.newSetting(Boolean.TRUE),
                SslSettingSpecConstants.SSL_KEY_STORE_INPUT_STREAM.newSetting(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getInputStream()),
                SslSettingSpecConstants.SSL_KEY_STORE_PASSWORD.newSettingWithParsedValue(
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString()),
                Socks5SettingSpecConstants.SOCKS5_METHODS.newSetting(
                        Methods.of(Method.USERNAME_PASSWORD)),
                Socks5SettingSpecConstants.SOCKS5_USERPASSMETHOD_USER_REPOSITORY.newSetting(
                        UserRepositorySpecConstants.STRING_SOURCE_USER_REPOSITORY.newUserRepository(
                                "Aladdin:opensesame")),
                Socks5SettingSpecConstants.SOCKS5_ON_UDP_ASSOCIATE_REQUEST_RELAY_BUFFER_SIZE.newSetting(
                        PositiveInteger.valueOf(DatagramServer.RECEIVE_BUFFER_SIZE)))));
    }

    @Test
    public void testEchoDatagramServerThroughSocksServerUsingSslAndSocks5UserpassMethod() throws IOException {
        LoadTestRunnerResults results = new EchoDatagramServerLoadTestRunner(
                new EchoDatagramServerInterfaceImpl(
                        EchoDatagramServerHelper.newEchoDatagramServer(0)),
                new SocksServerInterfaceImpl(
                        newSocksServerUsingSslAndSocks5UserpassMethod()),
                THREAD_COUNT,
                DELAY_BETWEEN_THREADS_STARTING,
                new EchoDatagramServerTestRunnerFactoryImpl(),
                TIMEOUT)
                .run();
        String methodName =
                Thread.currentThread().getStackTrace()[1].getMethodName();
        PerformanceReportHelper.writeToPerformanceReport(
                performanceReport,
                "Method " + methodName,
                results);
        Assert.assertNotNull(results);
    }

    @Test
    public void testEchoServerThroughSocksServerUsingSslAndSocks5UserpassMethod() throws IOException {
        LoadTestRunnerResults results = new EchoServerLoadTestRunner(
                new EchoServerInterfaceImpl(
                        EchoServerHelper.newEchoServer(0)),
                new SocksServerInterfaceImpl(
                        newSocksServerUsingSslAndSocks5UserpassMethod()),
                THREAD_COUNT,
                DELAY_BETWEEN_THREADS_STARTING,
                new EchoServerTestRunnerFactoryImpl(),
                TIMEOUT)
                .run();
        String methodName =
                Thread.currentThread().getStackTrace()[1].getMethodName();
        PerformanceReportHelper.writeToPerformanceReport(
                performanceReport,
                "Method " + methodName,
                results);
        Assert.assertNotNull(results);
    }

    private static final class EchoDatagramServerTestRunnerFactoryImpl extends EchoDatagramServerTestRunnerFactory {

        @Override
        public EchoDatagramServerTestRunner newEchoDatagramServerTestRunner(
                InetAddress echDatagramServerInetAddress,
                int echDatagramServerPort,
                String scksServerHostAddress,
                int scksServerPort) {
            return new EchoDatagramServerTestRunnerImpl(
                    echDatagramServerInetAddress,
                    echDatagramServerPort,
                    scksServerHostAddress,
                    scksServerPort);
        }

    }

    private static final class EchoDatagramServerTestRunnerImpl extends EchoDatagramServerTestRunner {

        public EchoDatagramServerTestRunnerImpl(
                InetAddress echDatagramServerInetAddress,
                int echDatagramServerPort,
                String scksServerHostAddress,
                int scksServerPort) {
            super(
                    echDatagramServerInetAddress,
                    echDatagramServerPort,
                    scksServerHostAddress,
                    scksServerPort);
        }

        @Override
        public void run() {
            EchoDatagramClient echoDatagramClient = new EchoDatagramClient(
                    newSocks5ClientUsingSslAndSocks5UserpassMethod(
                            this.socksServerHostAddress,
                            this.socksServerPort)
                            .newSocksNetObjectFactory());
            try {
                echoDatagramClient.echo(
                        StringConstants.STRING_05,
                        this.echoDatagramServerInetAddress,
                        this.echoDatagramServerPort);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

    }

    private static final class EchoServerTestRunnerFactoryImpl extends EchoServerTestRunnerFactory {

        @Override
        public EchoServerTestRunner newEchoServerTestRunner(
                InetAddress echServerInetAddress,
                int echServerPort,
                String scksServerHostAddress,
                int scksServerPort) {
            return new EchoServerTestRunnerImpl(
                    echServerInetAddress,
                    echServerPort,
                    scksServerHostAddress,
                    scksServerPort);
        }

    }

    private static final class EchoServerTestRunnerImpl extends EchoServerTestRunner {

        public EchoServerTestRunnerImpl(
                InetAddress echServerInetAddress,
                int echServerPort,
                String scksServerHostAddress,
                int scksServerPort) {
            super(
                    echServerInetAddress,
                    echServerPort,
                    scksServerHostAddress,
                    scksServerPort);
        }

        @Override
        public void run() {
            EchoClient echoClient = new EchoClient(
                    newSocks5ClientUsingSslAndSocks5UserpassMethod(
                            this.socksServerHostAddress,
                            this.socksServerPort)
                            .newSocksNetObjectFactory());
            try {
                echoClient.echo(
                        StringConstants.STRING_05,
                        this.echoServerInetAddress,
                        this.echoServerPort);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

    }

}
