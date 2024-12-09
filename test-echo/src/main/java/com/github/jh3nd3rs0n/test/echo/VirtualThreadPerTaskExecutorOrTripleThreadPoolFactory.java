package com.github.jh3nd3rs0n.test.echo;

import com.github.jh3nd3rs0n.jargyle.internal.concurrent.ExecutorsHelper;
import com.github.jh3nd3rs0n.test.help.net.ExecutorFactory;

import java.util.concurrent.ExecutorService;

public final class VirtualThreadPerTaskExecutorOrTripleThreadPoolFactory
        extends ExecutorFactory {

    public VirtualThreadPerTaskExecutorOrTripleThreadPoolFactory() {
    }

    @Override
    public ExecutorService newExecutor() {
        return ExecutorsHelper.newVirtualThreadPerTaskExecutorOrDefault(
                ExecutorsHelper.newFixedThreadPoolBuilder(3));
    }

}