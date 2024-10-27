package com.edtech.EdTech.security;

import com.edtech.EdTech.dto.EnrollmentCacheDto;
import com.edtech.EdTech.dto.UserCacheDto;
import com.edtech.EdTech.dto.UserDisplayDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, UserCacheDto> redisTemplate(RedisConnectionFactory connectionFactory){

        RedisTemplate<String, UserCacheDto> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    @Bean
    public RedisTemplate<String, List<EnrollmentCacheDto>> enrollmentCacheDtoRedisTemplate(RedisConnectionFactory connectionFactory){

        RedisTemplate<String, List<EnrollmentCacheDto>> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

}
