package com.github.jh3nd3rs0n.jargyle.common.net;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class StandardSocketSettingSpecSoReuseaddrTest {

    @Test
    public void testApplyValueDatagramSocket() throws SocketException {
        DatagramSocket datagramSocket = new DatagramSocket(null);
        StandardSocketSettingSpecConstants.SO_REUSEADDR.apply(
                Boolean.TRUE, datagramSocket);
        Assert.assertTrue(datagramSocket.getReuseAddress());
    }

    @Test
    public void testApplyValueServerSocket() throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        StandardSocketSettingSpecConstants.SO_REUSEADDR.apply(
                Boolean.TRUE, serverSocket);
        Assert.assertTrue(serverSocket.getReuseAddress());
    }

    @Test
    public void testApplyValueSocket() throws SocketException {
        Socket socket = new Socket();
        StandardSocketSettingSpecConstants.SO_REUSEADDR.apply(
                Boolean.TRUE, socket);
        Assert.assertTrue(socket.getReuseAddress());
    }

    @Test
    public void testExtractDatagramSocket() throws SocketException {
        DatagramSocket datagramSocket = new DatagramSocket(null);
        datagramSocket.setReuseAddress(true);
        Assert.assertTrue(
                StandardSocketSettingSpecConstants.SO_REUSEADDR.extract(
                        datagramSocket));
    }

    @Test
    public void testExtractServerSocket() throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.setReuseAddress(true);
        Assert.assertTrue(
                StandardSocketSettingSpecConstants.SO_REUSEADDR.extract(
                        serverSocket));
    }

    @Test
    public void testExtractSocket() throws SocketException {
        Socket socket = new Socket();
        socket.setReuseAddress(true);
        Assert.assertTrue(
                StandardSocketSettingSpecConstants.SO_REUSEADDR.extract(
                        socket));
    }

    @Test
    public void testNewSocketSettingWithParsedValueString() {
        Assert.assertNotNull(
                StandardSocketSettingSpecConstants.SO_REUSEADDR.newSocketSettingWithParsedValue(
                        "true"));
    }

}
