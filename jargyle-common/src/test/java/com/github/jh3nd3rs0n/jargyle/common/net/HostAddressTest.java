package com.github.jh3nd3rs0n.jargyle.common.net;

import org.junit.Assert;
import org.junit.Test;

public class HostAddressTest {

    @Test
    public void testIsAllZerosHostAddressString01() {
        Assert.assertTrue(HostAddress.isAllZerosHostAddress("0"));
    }

    @Test
    public void testIsAllZerosHostAddressString02() {
        Assert.assertTrue(HostAddress.isAllZerosHostAddress("0.0"));
    }

    @Test
    public void testIsAllZerosHostAddressString03() {
        Assert.assertTrue(HostAddress.isAllZerosHostAddress("0.0.0"));
    }

    @Test
    public void testIsAllZerosHostAddressString04() {
        Assert.assertTrue(HostAddress.isAllZerosHostAddress("0.0.0.0"));
    }

    @Test
    public void testIsAllZerosHostAddressString05() {
        Assert.assertTrue(HostAddress.isAllZerosHostAddress("::"));
    }

    @Test
    public void testIsAllZerosHostAddressString06() {
        Assert.assertTrue(HostAddress.isAllZerosHostAddress(
                "0:0:0:0:0:0:0:0"));
    }

    @Test
    public void testIsAllZerosHostAddressString07() {
        Assert.assertFalse(HostAddress.isAllZerosHostAddress(
                "zero zero zero"));
    }

    @Test
    public void testIsAllZerosHostAddressString08() {
        Assert.assertFalse(HostAddress.isAllZerosHostAddress(
                "nada nada nada"));
    }

    @Test
    public void testIsAllZerosHostAddressString09() {
        Assert.assertFalse(HostAddress.isAllZerosHostAddress(
                "zilch zilch zilch"));
    }

    @Test
    public void testNewHostAddressOfString01() {
        Assert.assertNotNull(HostAddress.newHostAddress("127.0.0.1"));
    }

    @Test
    public void testNewHostAddressOfString02() {
        Assert.assertNotNull(HostAddress.newHostAddress("::1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostAddressOfStringForIllegalArgumentException01() {
        HostAddress.newHostAddress("localhost");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewHostAddressOfStringForIllegalArgumentException02() {
        HostAddress.newHostAddress("999.999.999.999");
    }

}