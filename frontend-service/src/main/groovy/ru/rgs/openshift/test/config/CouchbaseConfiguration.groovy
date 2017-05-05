package ru.rgs.openshift.test.config

import com.couchbase.client.java.Bucket
import com.couchbase.client.java.Cluster
import com.couchbase.client.spring.cache.CacheBuilder
import com.couchbase.client.spring.cache.CouchbaseCacheManager
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.support.AbstractCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author jihor (jihor@ya.ru)
 *         Created on 2017-05-05
 *         <p>
 *         Couchbase cache configuration.
 *         See: https://spring.io/blog/2016/04/14/couchbase-as-a-first-class-citizen-of-spring-boot-1-4
 *         and  https://github.com/couchbaselabs/couchbase-spring-cache
 *         Guide: https://github.com/couchbase-guides/couchbase-docker
 */
@Configuration
@CompileStatic
class CouchbaseConfiguration {

    private final Cluster cluster

    CouchbaseConfiguration(Cluster cluster) {
        this.cluster = cluster
    }

    @Bean
    Bucket bucket() {
        return cluster.openBucket("default", "")
    }

    @Bean
    @Qualifier("couchbaseCacheManager")
    AbstractCacheManager couchbaseCacheManager() {
        new CouchbaseCacheManager(CacheBuilder.newInstance(bucket()), Constants.DEMO_CACHE)
    }
}
