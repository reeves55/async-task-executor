package com.xiaolee.async.core;

/**
 * @author: xiao
 * @date: 2020/3/29
 */
public interface TaskPromise {
    TaskPromise sync() throws InterruptedException;

}
