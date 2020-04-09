package com.xiaolee.async.promise;

/**
 * @author: xiao
 * @date: 2020/3/29
 */
public interface TaskPromise<T> {
    /**
     * 同步等待任务执行完毕
     * @return
     * @throws InterruptedException
     */
    TaskPromise<T> sync() throws InterruptedException;

    /**
     * 添加事件完成监听器
     * @param listener
     * @return
     */
    TaskPromise<T> addListener(TaskPromiseListener listener);

    /**
     * 获取任务执行异常信息
     * @return
     */
    Exception cause();

    /**
     * 是否执行完毕
     * @return
     */
    boolean isDone();

    /**
     * 取消执行
     * @return
     */
    boolean cancel();

    /**
     * 是否已被取消执行
     * @return
     */
    boolean isCanceled();

    /**
     * 任务是否被拒绝执行
     * @return
     */
    boolean isRejected();

    /**
     * 获取执行结果
     * @return
     */
    T getResult();
}
