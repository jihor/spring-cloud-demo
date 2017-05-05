package ru.rgs.openshift.test.config

import groovy.transform.CompileStatic
import lombok.Getter
import lombok.Setter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.cache.CacheManager
import org.springframework.cache.support.AbstractCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.validation.annotation.Validated

import javax.validation.constraints.NotNull

/**
 * @author jihor (jihor@ya.ru)
 *         Created on 2017-05-02
 */
@Configuration
@Getter
@Setter
@CompileStatic
class RedisConfiguration {

    @ConfigurationProperties(prefix = "redis")
    @Validated
    static class RedisProperties {
        @NotNull
        String host

        @NotNull
        Integer port
    }

    @Bean
    RedisProperties redisProperties(){
        new RedisProperties()
    }

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        def factory = new JedisConnectionFactory()
        factory.hostName = redisProperties().host
        factory.port = redisProperties().port
        factory
    }

    @Bean
    StringRedisSerializer stringRedisSerializer() {
        new StringRedisSerializer()
    }

    @Bean
    // Watch out for incorrect Redis keys when using spring-data-redis with Jedis
    // http://stackoverflow.com/questions/13215024/weird-redis-key-with-spring-data-jedis
    RedisTemplate redisTemplate() {
        new RedisTemplate(
                connectionFactory: jedisConnectionFactory(),
                keySerializer: stringRedisSerializer(),
                hashKeySerializer: stringRedisSerializer())
    }

    @Bean
    @Qualifier("redisCacheManager")
    @Primary
    AbstractCacheManager redisCacheManager() {
        new RedisCacheManager(redisTemplate())
    }
}
