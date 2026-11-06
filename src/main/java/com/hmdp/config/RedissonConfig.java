package com.hmdp.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class  RedissonConfig {
    @Value("${spring.redis.host}")
    private String REDIS_HOST;
    @Value("${spring.redis.password}")
    private String REDIS_PASSWORD;


    @Bean
    public RedissonClient redissonClient(){
        // 配置
        Config config = new Config();
        String address = "redis://" + REDIS_HOST + ":6379";
        config.useSingleServer().setAddress(address).setPassword(REDIS_PASSWORD);
//        config.useSingleServer().setAddress("redis://192.168.249.128:6379").setPassword("123321");
        // 创建RedissonClient对象
        return Redisson.create(config);
    }
}
