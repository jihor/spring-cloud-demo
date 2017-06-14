package ru.rgs.openshift.test.services

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.EnableCaching
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import ru.rgs.cloud.poc.model.pogo.SampleRequest
import ru.rgs.cloud.poc.model.thrift.TBackendReq
import ru.rgs.cloud.poc.model.thrift.THeaders
import ru.rgs.openshift.test.client.RestClientWithFeign
import ru.rgs.openshift.test.config.Constants
import ru.rgs.openshift.test.config.ThriftClientConfiguration.ThriftClientWrapper

import java.time.LocalDate
import java.util.concurrent.CompletableFuture
import java.util.function.Supplier

/**
 * @author jihor (dmitriy_zhikharev@rgs.ru)
 *         (С) RGS Group, http://www.rgs.ru
 *         Created on 2016-07-06
 */
@RestController
@Slf4j
@CompileStatic
@EnableCaching
class CachingDemoEndpoint {
    // can't inject ThriftClientsMap here directly, because once @Cacheable is used, whole class is proxied
    @Autowired
    ThriftClientWrapper thriftClientWrapper

    @RequestMapping(value = "/hello/{name}/thrift", method = RequestMethod.GET)
    @Cacheable(cacheNames = Constants.DEMO_CACHE, key = "'thrift-'.concat(#name)", cacheManager = "redisCacheManager")
    String helloThrift(@PathVariable String name) {
        String backendAorB = name.length() % 2 == 0 ? "a" : "b"
        log.info "Using backend $backendAorB for name $name..."
        def response = thriftClientWrapper.serviceClients.get(backendAorB)
                                          .greet(new TBackendReq()
                                                         .setHeaders(new THeaders().setCorrelationid(UUID.randomUUID().toString()))
                                                         .setLastname(name))
        // NX - set key only if it doesn't exist
        // EX - expiration time is set in seconds
        // see http://redis.io/commands/SET
//        jedis.set(name, response.message, "NX", "EX", 180)

        response.message
    }

    @Autowired
    RestClientWithFeign restClientWithFeign

    @RequestMapping(value = "/hello/{name}/rest-r", method = RequestMethod.GET)
    @Cacheable(cacheNames = Constants.DEMO_CACHE, key = "'rest-'.concat(#name)", cacheManager = "redisCacheManager")
    String helloRestRedis(@PathVariable String name) {
        def request = new SampleRequest()
        request.techData.correlationId = UUID.randomUUID()
        request.businessData.name = name
        request.businessData.dateOfBirth = LocalDate.now()
        log.info("Sending REST request: $request")
        restClientWithFeign.greet(request).businessData.message
    }

    @RequestMapping(value = "/hello/{name}/rest-c", method = RequestMethod.GET)
    @Cacheable(cacheNames = Constants.DEMO_CACHE, key = "'rest-'.concat(#name)", cacheManager = "couchbaseCacheManager")
    String helloRestCouchbase(@PathVariable String name) {
        def request = new SampleRequest()
        request.techData.correlationId = UUID.randomUUID()
        request.businessData.name = name
        request.businessData.dateOfBirth = LocalDate.now()
        log.info("Sending REST request: $request")
        restClientWithFeign.greet(request).businessData.message
    }
}
