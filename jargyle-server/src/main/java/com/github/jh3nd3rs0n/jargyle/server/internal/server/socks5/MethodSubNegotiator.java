package com.github.jh3nd3rs0n.jargyle.server.internal.server.socks5;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;

import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

abstract class MethodSubNegotiator {

    private static final Map<Method, MethodSubNegotiator> METHOD_SUB_NEGOTIATORS_MAP =
            new HashMap<>();

    private static final ReentrantLock REENTRANT_LOCK = new ReentrantLock();

    private static boolean initialized = false;

    public static MethodSubNegotiator getInstance(final Method meth) {
        initializeIfNotInitialized();
        MethodSubNegotiator methodSubNegotiator = METHOD_SUB_NEGOTIATORS_MAP.get(
                meth);
        if (methodSubNegotiator != null) {
            return methodSubNegotiator;
        }
        String str = METHOD_SUB_NEGOTIATORS_MAP.keySet().stream()
                .map(Method::toString)
                .collect(Collectors.joining(", "));
        throw new IllegalArgumentException(String.format(
                "expected method must be one of the following values: %s. "
                        + "actual value is %s",
                str,
                meth));
    }

    private static void initializeIfNotInitialized() {
        REENTRANT_LOCK.lock();
        try {
            if (initialized) {
                return;
            }
            MethodSubNegotiators methodSubNegotiators = new MethodSubNegotiators();
            methodSubNegotiators.add(new GssapiMethodSubNegotiator());
            methodSubNegotiators.add(new NoAcceptableMethodsMethodSubNegotiator());
            methodSubNegotiators.add(
                    new NoAuthenticationRequiredMethodSubNegotiator());
            methodSubNegotiators.add(new UsernamePasswordMethodSubNegotiator());
            METHOD_SUB_NEGOTIATORS_MAP.putAll(methodSubNegotiators.toMap());
            initialized = true;
        } finally {
            REENTRANT_LOCK.unlock();
        }
    }
	
	private final Method method;
	
	public MethodSubNegotiator(final Method meth) {
		this.method = meth;
	}
	
	public Method getMethod() {
		return this.method;
	}
	
	public abstract MethodSubNegotiationResults subNegotiate(
			final Socket socket,
			final Configuration configuration) throws IOException;
	
}
