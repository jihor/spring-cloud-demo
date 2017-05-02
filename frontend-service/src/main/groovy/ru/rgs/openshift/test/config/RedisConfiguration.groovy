package ru.rgs.openshift.test.config

import groovy.transform.CompileStatic
import lombok.Getter
import lombok.Setter
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer

/**
 * @author jihor (jihor@ya.ru)
 *         Created on 2017-05-02
 */
@Configuration
// will work since Spring Boot 1.4
// @ConfigurationProperties(prefix = "redis")
@Getter
@Setter
@CompileStatic
class RedisConfiguration {
    @Value('${redis.host}')
    public String host

    @Value('${redis.port}')
    public Integer port

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        def factory = new JedisConnectionFactory()
        factory.hostName = host
        factory.port = port.toInteger()
        factory
    }

    @Bean
    StringRedisSerializer stringRedisSerializer(){
        new StringRedisSerializer()
    }

    @Bean
    // Watch out for incorrect Redis keys when using spring-data-redis with Jedis
    // http://stackoverflow.com/questions/13215024/weird-redis-key-with-spring-data-jedis
    RedisTemplate redisTemplate() {
        new RedisTemplate(connectionFactory: jedisConnectionFactory(), keySerializer: stringRedisSerializer(), hashKeySerializer: stringRedisSerializer())

    }

    @Bean
    CacheManager cacheManager() {
        new RedisCacheManager(redisTemplate())
    }
}
