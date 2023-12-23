package com.github.jh3nd3rs0n.jargyle.internal.net.ssl;

import com.github.jh3nd3rs0n.jargyle.internal.security.KeyStoreHelper;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * Helper class for {@code TrustManager}s.
 */
public final class TrustManagerHelper {

    /**
     * Prevents the construction of unnecessary instances.
     */
    private TrustManagerHelper() {
    }

    /**
     * Returns an array of {@code TrustManager}s from the provided {@code File}
     * of the trust store, the provided password of the trust store, and the
     * type of trust store.
     *
     * @param trustStoreFile     the provided {@code File} of the trust store
     * @param trustStorePassword the provided password of the trust store
     * @param trustStoreType     the type of trust store (can be {@code null})
     * @return an array of {@code TrustManager}s from the provided {@code File}
     * of the trust store, the provided password of the trust store, and the
     * type of trust store
     * @throws IOException if an I/O error occurs when reading the provided
     *                     {@code File}
     */
    public static TrustManager[] getTrustManagers(
            final File trustStoreFile,
            final char[] trustStorePassword,
            final String trustStoreType) throws IOException {
        TrustManagerFactory trustManagerFactory;
        try {
            trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
        KeyStore trustStore = KeyStoreHelper.getKeyStore(
                trustStoreFile, trustStorePassword, trustStoreType);
        try {
            trustManagerFactory.init(trustStore);
        } catch (KeyStoreException e) {
            throw new IOException(e);
        }
        return trustManagerFactory.getTrustManagers();
    }

}
