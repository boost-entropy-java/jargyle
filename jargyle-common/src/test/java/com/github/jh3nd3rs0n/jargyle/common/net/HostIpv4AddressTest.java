package com.github.jh3nd3rs0n.jargyle.common.net;

import org.junit.Assert;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostIpv4AddressTest {

    @Test
    public void testEqualsObject01() {
        HostIpv4Address hostIpv4Address = HostIpv4Address.newHostIpv4Address(
                "127.0.0.1");
        Assert.assertEquals(hostIpv4Address, hostIpv4Address);
    }

    @Test
    public void testEqualsObject02() {
        HostIpv4Address hostIpv4Address = HostIpv4Address.newHostIpv4Address(
                "127.0.0.1");
        Assert.assertNotEquals(hostIpv4Address, null);
    }

    @Test
    public void testEqualsObject03() {
        HostAddress hostIpv4Address = HostIpv4Address.newHostIpv4Address(
                "127.0.0.1");
        HostAddress hostIpv6Address = HostIpv6Address.newHostIpv6Address(
                "::1");
        Assert.assertNotEquals(hostIpv4Address, hostIpv6Address);
    }

    @Test
    public void testEqualsObject04() {
        HostAddress hostIpv4Address1 = HostIpv4Address.newHostIpv4Address(
                "127.0.0.1");
        HostAddress hostIpv4Address2 = HostIpv4Address.newHostIpv4Address(
                "0.0.0.0");
        Assert.assertNotEquals(hostIpv4Address1, hostIpv4Address2);
    }

    @Test
    public void testEqualsObject05() {
        HostAddress hostIpv4Address1 = HostIpv4Address.newHostIpv4Address(
                "127.0.0.1");
        HostAddress hostIpv4Address2 = HostIpv4Address.newHostIpv4Address(
                "127.0.0.1");
        Assert.assertEquals(hostIpv4Address1, hostIpv4Address2);
    }

    @Test
    public void testToInetAddress() throws UnknownHostException {
        InetAddress inetAddress1 = HostIpv4Address.newHostIpv4Address(
                "127.0.0.1").toInetAddress();
        InetAddress inetAddress2 = InetAddress.getByName("127.0.0.1");
        Assert.assertEquals(inetAddress1, inetAddress2);
    }

    @Test
    public void testGetAllZerosInstance() {
        HostIpv4Address hostIpv4Address = HostIpv4Address.newHostIpv4Address(
                "0.0.0.0");
        Assert.assertEquals(
                hostIpv4Address, HostIpv4Address.getAllZerosInstance());
    }

    @Test
    public void testGetAllZerosInet4Address() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getByName("0.0.0.0");
        Assert.assertEquals(
                inetAddress, HostIpv4Address.getAllZerosInet4Address());
    }

    @Test
    public void testHashCode01() {
        HostIpv4Address hostIpv4Address1 = HostIpv4Address.newHostIpv4Address(
                "127.0.0.1");
        HostIpv4Address hostIpv4Address2 = HostIpv4Address.newHostIpv4Address(
                "127.0.0.1");
        Assert.assertEquals(
                hostIpv4Address1.hashCode(), hostIpv4Address2.hashCode());
    }

    @Test
    public void testHashCode02() {
        HostIpv4Address hostIpv4Address1 = HostIpv4Address.newHostIpv4Address(
                "127.0.0.1");
        HostIpv4Address hostIpv4Address2 = HostIpv4Address.newHostIpv4Address(
                "0.0.0.0");
        Assert.assertNotEquals(
                hostIpv4Address1.hashCode(), hostIpv4Address2.hashCode());
    }

    @Test
    public void testIsAllZerosIpv4AddressString01() {
        Assert.assertTrue(HostIpv4Address.isAllZerosIpv4Address("0"));
    }

    @Test
    public void testIsAllZerosIpv4AddressString02() {
        Assert.assertTrue(HostIpv4Address.isAllZerosIpv4Address("0.0"));
    }

    @Test
    public void testIsAllZerosIpv4AddressString03() {
        Assert.assertTrue(HostIpv4Address.isAllZerosIpv4Address("0.0.0"));
    }

    @Test
    public void testIsAllZerosIpv4AddressString04() {
        Assert.assertTrue(HostIpv4Address.isAllZerosIpv4Address("0.0.0.0"));
    }

    @Test
    public void testIsAllZerosIpv4AddressString05() {
        Assert.assertTrue(HostIpv4Address.isAllZerosIpv4Address(
                "00.00.00.00"));
    }

    @Test
    public void testIsAllZerosIpv4AddressString06() {
        Assert.assertTrue(HostIpv4Address.isAllZerosIpv4Address(
                "000.000.000.000"));
    }

    @Test
    public void testIsAllZerosIpv4AddressString07() {
        Assert.assertTrue(HostIpv4Address.isAllZerosIpv4Address(
                "0000"));
    }

    @Test
    public void testIsAllZerosIpv4AddressString08() {
        Assert.assertFalse(HostIpv4Address.isAllZerosIpv4Address(
                "zero.zero.zero.zero"));
    }

    @Test
    public void testNewHostIpv4AddressString01() {
        Assert.assertNotNull(HostIpv4Address.newHostIpv4Address("127.0.0.1"));
    }

    @Test
    public void testNewHostIpv4AddressString02() {
        Assert.assertNotNull(HostIpv4Address.newHostIpv4Address("127.0.0"));
    }

    @Test
    public void testNewHostIpv4AddressString03() {
        Assert.assertNotNull(HostIpv4Address.newHostIpv4Address("127.0"));
    }

    @Test
    public void testNewHostIpv4AddressString04() {
        Assert.assertNotNull(HostIpv4Address.newHostIpv4Address("127"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv4AddressStringForIllegalArgumentException01() {
        HostIpv4Address.newHostIpv4Address("localhost");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv4AddressStringForIllegalArgumentException02() {
        HostIpv4Address.newHostIpv4Address("::1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv4AddressStringForIllegalArgumentException03() {
        HostIpv4Address.newHostIpv4Address("127.127.127.127.127");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv4AddressStringForIllegalArgumentException04() {
        HostIpv4Address.newHostIpv4Address("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv4AddressStringForIllegalArgumentException05() {
        HostIpv4Address.newHostIpv4Address(" ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv4AddressStringForIllegalArgumentException06() {
        HostIpv4Address.newHostIpv4Address(".");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv4AddressStringForIllegalArgumentException07() {
        HostIpv4Address.newHostIpv4Address(".255.255.255");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv4AddressStringForIllegalArgumentException08() {
        HostIpv4Address.newHostIpv4Address("255.255.255.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv4AddressStringForIllegalArgumentException09() {
        HostIpv4Address.newHostIpv4Address("255.255.255.256");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv4AddressStringForIllegalArgumentException10() {
        HostIpv4Address.newHostIpv4Address("255.255.65536");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv4AddressStringForIllegalArgumentException11() {
        HostIpv4Address.newHostIpv4Address("255.16777216");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostIpv4AddressStringForIllegalArgumentException12() {
        HostIpv4Address.newHostIpv4Address("4294967296");
    }

    @Test
    public void testToInetAddress01() throws UnknownHostException {
        HostIpv4Address hostIpv4Address = HostIpv4Address.newHostIpv4Address(
                "255.255.255.255");
        InetAddress inetAddress = InetAddress.getByName("255.255.255.255");
        Assert.assertEquals(inetAddress, hostIpv4Address.toInetAddress());
    }

    @Test
    public void testToInetAddress02() throws UnknownHostException {
        HostIpv4Address hostIpv4Address = HostIpv4Address.newHostIpv4Address(
                "255.255.65535");
        InetAddress inetAddress = InetAddress.getByName("255.255.65535");
        Assert.assertEquals(inetAddress, hostIpv4Address.toInetAddress());
    }

    @Test
    public void testToInetAddress03() throws UnknownHostException {
        HostIpv4Address hostIpv4Address = HostIpv4Address.newHostIpv4Address(
                "255.16777215");
        InetAddress inetAddress = InetAddress.getByName("255.16777215");
        Assert.assertEquals(inetAddress, hostIpv4Address.toInetAddress());
    }

    @Test
    public void testToInetAddress04() throws UnknownHostException {
        HostIpv4Address hostIpv4Address = HostIpv4Address.newHostIpv4Address(
                "4294967295");
        InetAddress inetAddress = InetAddress.getByName("4294967295");
        Assert.assertEquals(inetAddress, hostIpv4Address.toInetAddress());
    }

    @Test
    public void testToInetAddress05() throws UnknownHostException {
        HostIpv4Address hostIpv4Address = HostIpv4Address.newHostIpv4Address(
                "127.0.0.1");
        InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
        Assert.assertEquals(inetAddress, hostIpv4Address.toInetAddress());
    }

    @Test
    public void testToInetAddress06() throws UnknownHostException {
        HostIpv4Address hostIpv4Address = HostIpv4Address.newHostIpv4Address(
                "127.0.0");
        InetAddress inetAddress = InetAddress.getByName("127.0.0");
        Assert.assertEquals(inetAddress, hostIpv4Address.toInetAddress());
    }

    @Test
    public void testToInetAddress07() throws UnknownHostException {
        HostIpv4Address hostIpv4Address = HostIpv4Address.newHostIpv4Address(
                "127.0");
        InetAddress inetAddress = InetAddress.getByName("127.0");
        Assert.assertEquals(inetAddress, hostIpv4Address.toInetAddress());
    }

    @Test
    public void testToInetAddress08() throws UnknownHostException {
        HostIpv4Address hostIpv4Address = HostIpv4Address.newHostIpv4Address(
                "127");
        InetAddress inetAddress = InetAddress.getByName("127");
        Assert.assertEquals(inetAddress, hostIpv4Address.toInetAddress());
    }

    @Test
    public void testToInetAddress09() throws UnknownHostException {
        HostIpv4Address hostIpv4Address = HostIpv4Address.newHostIpv4Address(
                "255.255.0.6");
        InetAddress inetAddress = InetAddress.getByName("255.255.0.6");
        Assert.assertEquals(inetAddress, hostIpv4Address.toInetAddress());
    }

}