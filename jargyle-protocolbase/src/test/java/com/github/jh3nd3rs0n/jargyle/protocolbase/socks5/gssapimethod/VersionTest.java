package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod;

import org.junit.Assert;
import org.junit.Test;

public class VersionTest {

    @Test
    public void testValueOfByteByte01() {
        Assert.assertEquals(
                Version.V1,
                Version.valueOfByte((byte) 0x01));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfByteByteForIllegalArgumentException01() {
        Version.valueOfByte((byte) 0x02);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfByteByteForIllegalArgumentException02() {
        Version.valueOfByte((byte) 0xff);
    }

}