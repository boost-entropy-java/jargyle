package com.github.jh3nd3rs0n.jargyle.internal;

import com.github.jh3nd3rs0n.jargyle.internal.concurrent.ExecutorsHelper;
import com.github.jh3nd3rs0n.jargyle.test.help.net.ExecutorFactory;

import java.util.concurrent.ExecutorService;

public final class VirtualThreadPerTaskExecutorOrDoubleThreadPoolFactory
        extends ExecutorFactory {

    public VirtualThreadPerTaskExecutorOrDoubleThreadPoolFactory() {
    }

    @Override
    public ExecutorService newExecutor() {
        return ExecutorsHelper.newVirtualThreadPerTaskExecutorOrDefault(
                ExecutorsHelper.newFixedThreadPoolBuilder(2));
    }

}
