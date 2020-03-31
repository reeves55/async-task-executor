package com.xiaolee.async.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: xiao
 * @date: 2020/3/29
 */
public class DefaultTaskPromise<T> implements TaskPromise<T>, Runnable {
    private static final Integer INIT = 0;
    private static final Integer RUNNING = 1;
    private static final Integer SUCCESS = 2;
    private static final Integer REJECTED = 3;
    private static final Integer FAILED = 4;

    private List<TaskPromiseListener> listeners;
    private T result;
    private Thread thread;
    private Callable<T> task;
    private Exception cause;
    private AtomicInteger waiters;
    private AtomicInteger status;
    private boolean isRejected;

    DefaultTaskPromise() {
        this.listeners = new ArrayList<>(8);
        this.waiters = new AtomicInteger(0);
        this.status = new AtomicInteger(INIT);
        this.isRejected = false;
        this.cause = null;
    }

    public DefaultTaskPromise(Runnable task) {
        this();
        this.task = new RunnableAdapter<T>(task,null);

    }

    public DefaultTaskPromise(Callable<T> task) {
        this();
        this.task = task;
    }

    @Override
    public TaskPromise sync() throws InterruptedException {
        synchronized (this) {
            waiters.getAndIncrement();
            wait();

            // waked up by another thread
            waiters.decrementAndGet();
        }

        return this;
    }

    @Override
    public TaskPromise addListener(TaskPromiseListener listener) {
        synchronized (this) {
            listeners.add(listener);
        }

        if (status.get() > RUNNING) {
            notifyListeners();
        }

        return self();
    }

    @Override
    public Exception cause() {
        return cause;
    }

    @Override
    public boolean isRejected() {
        return isRejected;
    }

    @Override
    public T getResult() {
        return result;
    }

    public Callable<T> getTask() {
        return task;
    }

    public void setResult(T result) {
        this.result = result;
    }

    private void notifyWaiters() {
        synchronized (this) {
            if (waiters.get() > 0) {
                notifyAll();
            }
        }
    }

    private void notifyListeners() {
        for (TaskPromiseListener listener : listeners) {
            listener.onComplete(this);
        }
    }

    public void tryRejected() {
        isRejected = true;
        status.set(REJECTED);
    }

    private DefaultTaskPromise self() {
        return this;
    }


    @Override
    public void run() {
        thread = Thread.currentThread();

        try {
            status.compareAndSet(INIT, RUNNING);
            setResult(getTask().call());
        } catch (Exception e) {
            status.compareAndSet(RUNNING, FAILED);
            cause = e;
        } finally {
            if (status.get() == RUNNING) {
                status.compareAndSet(RUNNING, SUCCESS);
            }

            notifyWaiters();
            notifyListeners();
        }
    }


    class RunnableAdapter<T> implements Callable<T> {
        Runnable r;
        T result;

        public RunnableAdapter(Runnable r, T result) {
            this.r = r;
            this.result = result;
        }

        @Override
        public T call() throws Exception {
            r.run();
            return result;
        }
    }
}
