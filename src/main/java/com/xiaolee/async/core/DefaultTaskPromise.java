package com.xiaolee.async.core;

import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: xiao
 * @date: 2020/3/29
 */
public class DefaultTaskPromise<T> implements TaskPromise<T> {
    private static final Integer INIT = 0;
    private static final Integer RUNNING = 1;
    private static final Integer DONE = 2;

    private CopyOnWriteArrayList<TaskPromiseListener> listeners;
    private T result;
    private Thread thread;
    private Callable<T> task;
    private Exception cause;
    private AtomicInteger waiters;
    private AtomicInteger status;

    DefaultTaskPromise() {
        this.listeners = new CopyOnWriteArrayList<>();
        this.waiters = new AtomicInteger(0);
        this.status = new AtomicInteger(INIT);
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
        waiters.getAndIncrement();
        wait();
        return this;
    }

    @Override
    public TaskPromise addListener(TaskPromiseListener listener) {
        listeners.add(listener);

        if (status.get() >= DONE) {
            notifyListeners();
        }

        return self();
    }

    @Override
    public Exception cause() {
        return cause;
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

    /**
     * notify all waiters that invoke sync method
     */
    private void notifyWaiters() {
        if (waiters.get() > 0) {
            notifyAll();
        }
    }

    /**
     * call all the listeners callback
     */
    private void notifyListeners() {
        for (TaskPromiseListener listener : listeners) {
            listener.onComplete(this);
        }
    }

    private DefaultTaskPromise self() {
        return this;
    }

    public TaskWrapper newWrapper() {
        return new TaskWrapper();
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

    /**
     * 封装到线程池执行的任务
     */
    class TaskWrapper implements Runnable {
        @Override
        public void run() {
            thread = Thread.currentThread();

            try {
                status.compareAndSet(INIT, RUNNING);
                setResult(getTask().call());
            } catch (Exception e) {
                cause = e;
            } finally {
                status.compareAndSet(RUNNING, DONE);
                notifyWaiters();
                notifyListeners();
            }
        }
    }
}
