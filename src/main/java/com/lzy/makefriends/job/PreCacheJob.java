package com.lzy.makefriends.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzy.makefriends.mapper.UserMapper;
import com.lzy.makefriends.model.domain.User;
import com.lzy.makefriends.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class PreCacheJob {
    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private UserService userService;

    @Resource
    private RedissonClient redissonClient;

    // 重点用户
    private List<Long> mainUserList = Arrays.asList(1L);

    // 每天执行，预热推荐用户
    @Scheduled(cron = "0 0 10 * * *")
    public void doCacheRecommendUser(){
        // 获取锁
        RLock rLock = redissonClient.getLock("lzy:precachejob:docache:lock");
        try {
            // 设置锁的等待时间和过期时间
            if (rLock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                System.out.println("lock:" + Thread.currentThread().getId());
                for (Long userId : mainUserList){
                    // 无缓存，查数据库
                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                    Page<User> userPage = userService.page(new Page<>(1, 20) ,queryWrapper);
                    String redisKey = String.format("lzy:user:recommend:%s", userId);
                    ValueOperations valueOperations = redisTemplate.opsForValue();
                    // 写缓存
                    try {
                        valueOperations.set(redisKey, userPage, 10000, TimeUnit.MILLISECONDS);
                    } catch (Exception e) {
                        log.error("redis set key error", e);
                    }
                }
            }
        } catch (InterruptedException e) {
            log.error("doCacheRecommendUser error", e);
        } finally {
            // 释放自己的锁，一定要写在finally中，防止程序执行错误锁不释放
            if (rLock.isHeldByCurrentThread()){
                rLock.unlock();
                System.out.println("unlock:" + Thread.currentThread().getId());
            }
        }


    }
}
