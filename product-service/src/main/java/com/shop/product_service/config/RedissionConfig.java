package com.shop.product_service.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissionConfig {
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
      config.useSingleServer().setAddress("redis://localhost:6379"); // local
//        config.useSingleServer().setAddress("redis://redis:6379");  // docker
        return Redisson.create(config);
    }
}
