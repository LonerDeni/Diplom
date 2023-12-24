package com.example.diplom.utile;

import cn.hutool.core.lang.Snowflake;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SnowFlakeConfig {

    @Value("${snowflake.workerId}")
    private Long workerId;
    @Value("${snowflake.dataCenterId}")
    private Long dataCenterId;

    @Bean
    public Snowflake snowflake() {
         return new Snowflake(workerId,dataCenterId);
    }
}