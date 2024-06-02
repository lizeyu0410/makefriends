package com.lzy.makefriends.service;


import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedissonTest {
    @Resource
    private RedissonClient redissonClient;

    @Test
    void testWatchDog(){
        // 获取锁
        RLock rLock = redissonClient.getLock("lzy:precachejob:docache:lock");
        try {
            // 设置锁的等待时间和过期时间
            if (rLock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                Thread.sleep(1000000);
                System.out.println("lock:" + Thread.currentThread().getId());
                }
        } catch (InterruptedException e) {

        } finally {
            // 释放自己的锁，一定要写在finally中，防止程序执行错误锁不释放
            if (rLock.isHeldByCurrentThread()){
                rLock.unlock();
                System.out.println("unlock:" + Thread.currentThread().getId());
            }
        }
    }
}
