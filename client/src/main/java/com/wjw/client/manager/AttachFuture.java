package com.wjw.client.manager;

import com.wjw.proto.AttachRequest;
import com.wjw.proto.AttachResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 此类参考https://github.com/luxiaoxun/NettyRpc
 *
 * @author wjw
 * @description: 结果回调钩子
 * @title: AttachFuture
 * @date 2022/4/6 13:46
 */
public class AttachFuture implements Future<AttachResponse> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AttachFuture.class);

    private Sync sync;
    private AttachRequest request;
    private AttachResponse response;
    private long startTime;
    private long responseTimeThreshold = 5000;

    public AttachFuture(AttachRequest request) {
        sync = new Sync();
        this.request = request;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDone() {
        return sync.isDone();
    }

    @Override
    public AttachResponse get() throws InterruptedException, ExecutionException {
        // 阻塞，知道执行isDone
        sync.acquire(1);
        return response;
    }

    @Override
    public AttachResponse get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        boolean success = sync.tryAcquireNanos(1, unit.toNanos(timeout));
        if (success) {
            return response;
        } else {
            throw new RuntimeException("Timeout exception.");
        }
    }

    public void done(AttachResponse reponse) {
        this.response = reponse;
        sync.release(1);
        // Threshold
        long responseTime = System.currentTimeMillis() - startTime;
        if (responseTime > this.responseTimeThreshold) {
            LOGGER.warn("Service response time is too slow.");
        }
    }

    static class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 1L;

        //future status
        private final int done = 1;
        private final int pending = 0;

        @Override
        protected boolean tryAcquire(int arg) {
            // 默认state为0，所以不用初始化加锁
            return getState() == done;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if (getState() == pending) {
                // 使用cas更新state
                if (compareAndSetState(pending, done)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }

        protected boolean isDone() {
            return getState() == done;
        }
    }
}
