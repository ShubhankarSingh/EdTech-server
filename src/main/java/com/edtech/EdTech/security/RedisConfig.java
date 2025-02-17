package com.edtech.EdTech.security;

import com.edtech.EdTech.dto.CourseDto;
import com.edtech.EdTech.dto.EnrollmentDto;
import com.edtech.EdTech.dto.UserDisplayDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, UserDisplayDto> redisTemplate(RedisConnectionFactory connectionFactory){

        RedisTemplate<String, UserDisplayDto> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }
    @Bean
    public RedisTemplate<String, List<EnrollmentDto>> enrollmentCacheDtoRedisTemplate(RedisConnectionFactory connectionFactory){

        RedisTemplate<String, List<EnrollmentDto>> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    @Bean
    public RedisTemplate<String, CourseDto> recentlyViewedCourseCache(RedisConnectionFactory connectionFactory){

        RedisTemplate<String, CourseDto> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());

        // Set Value Serializer to JSON for human-readable storage
        Jackson2JsonRedisSerializer<CourseDto> serializer = new Jackson2JsonRedisSerializer<>(CourseDto.class);
        template.setValueSerializer(serializer);
        return template;
    }

}
