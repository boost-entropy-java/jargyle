package com.github.jh3nd3rs0n.echo.performance.test;

import java.net.InetAddress;

public abstract class EchoServerTest implements Runnable {

    protected final InetAddress echoServerInetAddress;
    protected final int echoServerPort;

    protected final String socksServerHostAddress;
    protected final int socksServerPort;

    public EchoServerTest(
            final InetAddress echServerInetAddress,
            final int echServerPort,
            final String scksServerHostAddress,
            final int scksServerPort) {
        this.echoServerInetAddress = echServerInetAddress;
        this.echoServerPort = echServerPort;
        this.socksServerHostAddress = scksServerHostAddress;
        this.socksServerPort = scksServerPort;
    }

    @Override
    public abstract void run();

}
