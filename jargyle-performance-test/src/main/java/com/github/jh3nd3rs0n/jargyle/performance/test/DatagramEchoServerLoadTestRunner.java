package com.github.jh3nd3rs0n.jargyle.performance.test;

import com.github.jh3nd3rs0n.jargyle.integration.test.DatagramEchoServer;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.SocksServer;
import com.github.jh3nd3rs0n.jargyle.internal.concurrent.ExecutorsHelper;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

public final class DatagramEchoServerLoadTestRunner {

    private static final int HALF_SECOND = 500;

    private final Configuration configuration;
    private final long delayBetweenThreadsStarting;
    private final DatagramEchoServerTestRunnerFactory datagramEchoServerTestRunnerFactory;
    private final int threadCount;
    private final long timeout;

    public DatagramEchoServerLoadTestRunner(
            final Configuration config,
            final int numberOfThreads,
            final long delayBetweenThreadsStart,
            final DatagramEchoServerTestRunnerFactory datagramEchServerTestRunnerFactory,
            final long tmt) {
        this.configuration = config;
        this.delayBetweenThreadsStarting = delayBetweenThreadsStart;
        this.datagramEchoServerTestRunnerFactory = datagramEchServerTestRunnerFactory;
        this.threadCount = numberOfThreads;
        this.timeout = tmt;
    }

    public LoadTestRunnerResults run() throws IOException {
        SocksServer socksServer = (this.configuration == null) ?
                null : new SocksServer(this.configuration);
        DatagramEchoServer datagramEchoServer = DatagramEchoServerHelper.newDatagramEchoServer();
        LoadTestRunnerResults loadTestRunnerResults = new LoadTestRunnerResults(
                this.threadCount, this.delayBetweenThreadsStarting);
        ExecutorService executor =
                ExecutorsHelper.newVirtualThreadPerTaskExecutorOrDefault(
                        ExecutorsHelper.newCachedThreadPoolBuilder());
        try {
            String socksServerHostAddress = null;
            int socksServerPort = -1;
            if (socksServer != null) {
                socksServer.start();
                socksServerHostAddress = socksServer.getHost().toString();
                socksServerPort = socksServer.getPort().intValue();
            }
            datagramEchoServer.start();
            for (int i = 0; i < this.threadCount; i++) {
                executor.execute(new LoadTestRunnerWorker(
                        i * this.delayBetweenThreadsStarting,
                        this.datagramEchoServerTestRunnerFactory.newDatagramEchoServerTestRunner(
                                datagramEchoServer.getInetAddress(),
                                datagramEchoServer.getPort(),
                                socksServerHostAddress,
                                socksServerPort),
                        loadTestRunnerResults));
            }
            long startWaitTime = System.currentTimeMillis();
            do {
                try {
                    Thread.sleep(HALF_SECOND);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } while (loadTestRunnerResults.getCompletedThreadCount() < this.threadCount
                    && System.currentTimeMillis() - startWaitTime < this.timeout);
        } finally {
            executor.shutdownNow();
            if (!datagramEchoServer.getState().equals(
                    DatagramEchoServer.State.STOPPED)) {
                datagramEchoServer.stop();
            }
            if (socksServer != null && !socksServer.getState().equals(
                    SocksServer.State.STOPPED)) {
                socksServer.stop();
            }
        }
        return loadTestRunnerResults;
    }
    
}
