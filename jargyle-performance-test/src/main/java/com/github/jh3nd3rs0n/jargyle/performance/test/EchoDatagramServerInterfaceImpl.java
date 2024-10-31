package com.github.jh3nd3rs0n.jargyle.performance.test;

import com.github.jh3nd3rs0n.jargyle.test.help.net.DatagramServer;

import java.io.IOException;
import java.net.InetAddress;

public final class EchoDatagramServerInterfaceImpl
        extends EchoDatagramServerInterface {

    private final DatagramServer echoDatagramServer;

    public EchoDatagramServerInterfaceImpl(
            final DatagramServer echDatagramServer) {
        this.echoDatagramServer = echDatagramServer;
    }

    @Override
    public InetAddress getInetAddress() {
        return this.echoDatagramServer.getInetAddress();
    }

    @Override
    public int getPort() {
        return this.echoDatagramServer.getPort();
    }

    @Override
    public State getState() {
        State state = null;
        switch (this.echoDatagramServer.getState()) {
            case STARTED:
                state = State.STARTED;
                break;
            case STOPPED:
                state = State.STOPPED;
                break;
        }
        return state;
    }

    @Override
    public void start() throws IOException {
        this.echoDatagramServer.start();
    }

    @Override
    public void stop() throws IOException {
        this.echoDatagramServer.stop();
    }

}
