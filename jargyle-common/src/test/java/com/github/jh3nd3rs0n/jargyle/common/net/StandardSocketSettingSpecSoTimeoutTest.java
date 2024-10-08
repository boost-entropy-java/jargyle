package com.github.jh3nd3rs0n.jargyle.common.net;

import com.github.jh3nd3rs0n.jargyle.common.number.NonNegativeInteger;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class StandardSocketSettingSpecSoTimeoutTest {

    @Test
    public void testApplyValueDatagramSocket() throws SocketException {
        DatagramSocket datagramSocket = new DatagramSocket(null);
        StandardSocketSettingSpecConstants.SO_TIMEOUT.apply(
                NonNegativeInteger.valueOf(1000),
                datagramSocket);
        Assert.assertEquals(1000, datagramSocket.getSoTimeout());
    }

    @Test
    public void testApplyValueServerSocket() throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        StandardSocketSettingSpecConstants.SO_TIMEOUT.apply(
                NonNegativeInteger.valueOf(1000),
                serverSocket);
        Assert.assertEquals(1000, serverSocket.getSoTimeout());
    }

    @Test
    public void testApplyValueSocket() throws SocketException {
        Socket socket = new Socket();
        StandardSocketSettingSpecConstants.SO_TIMEOUT.apply(
                NonNegativeInteger.valueOf(1000),
                socket);
        Assert.assertEquals(1000, socket.getSoTimeout());
    }

    @Test
    public void testExtractDatagramSocket() throws SocketException {
        DatagramSocket datagramSocket = new DatagramSocket(null);
        datagramSocket.setSoTimeout(2000);
        Assert.assertEquals(
                2000,
                StandardSocketSettingSpecConstants.SO_TIMEOUT.extract(
                        datagramSocket).intValue());
    }

    @Test
    public void testExtractServerSocket() throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.setSoTimeout(2000);
        Assert.assertEquals(
                2000,
                StandardSocketSettingSpecConstants.SO_TIMEOUT.extract(
                        serverSocket).intValue());
    }

    @Test
    public void testExtractSocket() throws SocketException {
        Socket socket = new Socket();
        socket.setSoTimeout(2000);
        Assert.assertEquals(
                2000,
                StandardSocketSettingSpecConstants.SO_TIMEOUT.extract(
                        socket).intValue());
    }

    @Test
    public void testNewSocketSettingWithParsedValueString() {
        Assert.assertNotNull(
                StandardSocketSettingSpecConstants.SO_TIMEOUT.newSocketSettingWithParsedValue(
                        "1000"));
    }

}
