package com.xiaolee.async.core;

import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author: xiao
 * @date: 2020/3/29
 */
public class DefaultTaskPromise implements TaskPromise {
    CopyOnWriteArrayList<TaskPromiseListener> listeners;
    Object result;
    Callable<Object> task;

    public DefaultTaskPromise(Runnable task) {

    }

    public TaskPromise sync() throws InterruptedException {
        wait();
        return this;
    }

    public TaskPromise addListener(TaskPromiseListener listener) {
        listeners.add(listener);
        return self();
    }

    public Callable<Object> getTask() {
        return task;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    /**
     * notify all waiters that invoke sync method
     */
    private void notifyWaiters() {
        notifyAll();
    }

    /**
     * call all the listeners callback
     */
    private void notifyListeners() {
        for (TaskPromiseListener listener : listeners) {
            listener.onComplete(this);
        }
    }

    /**
     * return
     *
     * @return
     */
    private DefaultTaskPromise self() {
        return this;
    }

    public TaskWrapper newWrapper() {
        return new TaskWrapper();
    }


    class TaskWrapper implements Runnable {
        public void run() {
            try {
                Object result = getTask().call();
                setResult(result);
                notifyWaiters();
                notifyListeners();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
