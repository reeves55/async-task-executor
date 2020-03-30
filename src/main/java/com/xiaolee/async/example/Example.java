package com.xiaolee.async.example;

import com.xiaolee.async.core.AsyncTaskExecutor;
import com.xiaolee.async.core.TaskPromise;
import com.xiaolee.async.core.TaskPromiseListener;

import java.util.concurrent.Callable;

/**
 * @author: xiao
 * @date: 2020/3/29
 */
public class Example {
    public static void main(String[] args) {
        AsyncTaskExecutor executor = new AsyncTaskExecutor();
        // 执行 Runnable 任务
        executor.execute(() -> {
            System.out.println("my runnable task");
        }).addListener(promise -> System.out.println("my runnable task execute complete"));

        // 执行 Callable<T> 任务
        executor.execute(() -> {
            return 1;
        }).addListener(promise -> {
            Integer result = (Integer) promise.getResult();
            System.out.println(result);
        });
    }
}
