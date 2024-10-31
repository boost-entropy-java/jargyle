package com.github.jh3nd3rs0n.jargyle.performance.test;

import com.github.jh3nd3rs0n.jargyle.integration.test.EchoDatagramClient;
import com.github.jh3nd3rs0n.jargyle.integration.test.EchoDatagramServerHelper;
import com.github.jh3nd3rs0n.jargyle.integration.test.EchoClient;
import com.github.jh3nd3rs0n.jargyle.integration.test.EchoServerHelper;
import com.github.jh3nd3rs0n.jargyle.test.help.string.TestStringConstants;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.nio.file.Path;

public class EchoIT {

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

    @Test
    public void testEchoDatagramServer() throws IOException {
        LoadTestRunnerResults results = new EchoDatagramServerLoadTestRunner(
                new EchoDatagramServerInterfaceImpl(
                        EchoDatagramServerHelper.newEchoDatagramServer(0)),
                null,
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
    public void testEchoServer() throws IOException {
        LoadTestRunnerResults results = new EchoServerLoadTestRunner(
                new EchoServerInterfaceImpl(
                        EchoServerHelper.newEchoServer(0)),
                null,
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
            try {
                new EchoDatagramClient().echo(
                        TestStringConstants.STRING_05,
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
            try {
                new EchoClient().echo(
                        TestStringConstants.STRING_05,
                        this.echoServerInetAddress,
                        this.echoServerPort);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

    }

}
