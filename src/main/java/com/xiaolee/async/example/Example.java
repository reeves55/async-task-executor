package com.xiaolee.async.example;

import com.xiaolee.async.core.AsyncTaskExecutor;
import com.xiaolee.async.core.TaskPromise;
import com.xiaolee.async.core.TaskPromiseListener;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: xiao
 * @date: 2020/3/29
 */
public class Example {
    public static void main(String[] args) {
        AsyncTaskExecutor executor = new AsyncTaskExecutor(300);
        final AtomicInteger count = new AtomicInteger(0);

        // 执行 Runnable 任务
        executor.execute(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("my runnable task");
        }).addListener(promise -> System.out.println("my runnable task execute complete"));

        for (int i=0; i<1000; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 执行 Callable<T> 任务
            executor.execute(() -> {
                Thread.sleep(50);
                return count.getAndIncrement();
            }).addListener(promise -> {
                if (promise.isRejected()) {
                    System.out.println("任务被拒绝执行...");
                } else {
                    Integer result = (Integer) promise.getResult();
                    System.out.println(result);
                }
            });
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
