package io.github.skywalkerdarren.simpleaccounting.util;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Executor that runs a task on a new background thread.
 */
public class DiskIOThreadExecutor implements Executor {

    private final Executor mDiskIO;

    public DiskIOThreadExecutor() {
        mDiskIO = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new DiskThreadFactory());
    }

    private class DiskThreadFactory implements ThreadFactory {
        private AtomicInteger threadId = new AtomicInteger(0);

        @Override
        public DiskThread newThread(Runnable r) {
            return new DiskThread(threadId, r);
        }
    }

    private class DiskThread extends Thread {
        private static final String TAG = "DiskThread";
        private AtomicInteger mThreadId;

        public DiskThread(AtomicInteger threadId, Runnable r) {
            super(r);
            threadId.getAndIncrement();
            setName("disk thread NO." + threadId);
        }
    }

    @Override
    public void execute(@NonNull Runnable command) {
        mDiskIO.execute(command);
    }
}
