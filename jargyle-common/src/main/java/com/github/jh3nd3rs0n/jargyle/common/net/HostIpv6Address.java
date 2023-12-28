package com.github.jh3nd3rs0n.jargyle.common.net;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * An IPv6 address of a node of a network.
 */
public final class HostIpv6Address extends HostAddress {

    /**
     * An all zeros IPv6 address in compressed form.
     */
    public static final String ALL_ZEROS_IPV6_ADDRESS_IN_COMPRESSED_FORM = "::";

    /**
     * An all zeros IPv6 address in full form.
     */
    public static final String ALL_ZEROS_IPV6_ADDRESS_IN_FULL_FORM =
            "0:0:0:0:0:0:0:0";

    /**
     * The regular expression for an IPv6 address in compressed form.
     */
    private static final String IPV6_ADDRESS_IN_COMPRESSED_FORM_REGEX =
            "\\A([a-fA-F0-9]{0,4}:){1,7}(:[a-fA-F0-9]{0,4}){1,7}\\z";

    /**
     * The regular expression for an IPv6 address in full form.
     */
    private static final String IPV6_ADDRESS_IN_FULL_FORM_REGEX =
            "\\A[a-fA-F0-9]{1,4}(:[a-fA-F0-9]{1,4}){7}+\\z";

    /**
     * Constructs a {@code HostIpv6Address} with the provided IPv6 address
     * and the provided {@code InetAddress}.
     *
     * @param str      the provided IPv6 address
     * @param inetAddr the provided {@code InetAddress}
     */
    HostIpv6Address(final String str, final InetAddress inetAddr) {
        super(str, inetAddr);
    }

    /**
     * Returns a {@code boolean} value to indicate if the provided
     * IPv6 address is an IPv6 address of all zeros.
     *
     * @param string the provided IPv6 address
     * @return a {@code boolean} value to indicate if the provided
     * IPv6 address is an IPv6 address of all zeros
     */
    public static boolean isAllZerosIpv6Address(final String string) {
        return ALL_ZEROS_IPV6_ADDRESS_IN_COMPRESSED_FORM.equals(string)
                || ALL_ZEROS_IPV6_ADDRESS_IN_FULL_FORM.equals(string);
    }

    /**
     * Returns a new {@code HostIpv6Address} with the provided IPv6 address.
     * An {@code IllegalArgumentException} is thrown if the provided
     * IPv6 address is invalid.
     *
     * @param string the provided IPv6 address
     * @return a new {@code HostIpv6Address} with the provided IPv6 address
     */
    public static HostIpv6Address newHostIpv6Address(final String string) {
        String message = String.format(
                "invalid IPv6 address: %s",
                string);
        if (!(string.matches(IPV6_ADDRESS_IN_COMPRESSED_FORM_REGEX)
                || string.matches(IPV6_ADDRESS_IN_FULL_FORM_REGEX))) {
            throw new IllegalArgumentException(message);
        }
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(string);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException(message, e);
        }
        if (!(inetAddress instanceof Inet6Address)) {
            throw new IllegalArgumentException(message);
        }
        return new HostIpv6Address(string, inetAddress);
    }

}
