package net.leirahjelle.playground;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

abstract public class NatasLevel {

    protected final ThreadPoolExecutor executor;

    public NatasLevel(int threads) {
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threads);
    }

    abstract String findPassword() throws InterruptedException, ExecutionException;

    protected String fromCharCode(int... codePoints) {
        return new String(codePoints, 0, codePoints.length);
    }
}
