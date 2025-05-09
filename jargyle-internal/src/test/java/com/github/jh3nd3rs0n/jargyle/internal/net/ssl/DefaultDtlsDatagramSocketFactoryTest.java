package com.github.jh3nd3rs0n.jargyle.internal.net.ssl;

import com.github.jh3nd3rs0n.jargyle.test.help.security.KeyStoreResourceConstants;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class DefaultDtlsDatagramSocketFactoryTest {

    @Test
    public void testGetDatagramSocketDatagramSocket01() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        DefaultDtlsDatagramSocketFactory defaultDtlsDatagramSocketFactory = new DefaultDtlsDatagramSocketFactory(SslContextHelper.getSslContext(
                "DTLSv1.2",
                KeyManagerHelper.getKeyManagers(
                        new ByteArrayInputStream(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_2.getContentAsBytes()),
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_2.getContentAsString().toCharArray(),
                        null),
                TrustManagerHelper.getTrustManagers(
                        new ByteArrayInputStream(KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_FILE_1.getContentAsBytes()),
                        KeyStoreResourceConstants.JARGYLE_TEST_HELP_SECURITY_KEY_STORE_PASSWORD_FILE_1.getContentAsString().toCharArray(),
                        null)));
        Assert.assertNotNull(defaultDtlsDatagramSocketFactory.getDatagramSocket(
                new DatagramSocket(null)));
    }

}