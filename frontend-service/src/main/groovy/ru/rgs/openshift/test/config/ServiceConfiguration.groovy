package ru.rgs.openshift.test.config

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import info.developerblog.spring.thrift.annotation.ThriftClientsMap
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import ru.rgs.cloud.poc.model.pogo.SampleRequest
import ru.rgs.cloud.poc.model.thrift.TBackendReq
import ru.rgs.cloud.poc.model.thrift.TBackendService
import ru.rgs.cloud.poc.model.thrift.THeaders
import ru.rgs.openshift.test.client.RestClientWithFeign

import java.time.LocalDate

/**
 * @author jihor (dmitriy_zhikharev@rgs.ru)
 *         (С) RGS Group, http://www.rgs.ru
 *         Created on 2016-07-06
 */
@RestController
@Slf4j
@CompileStatic
@Import(RedisConfiguration)
@EnableCaching
class ServiceConfiguration {
    @Bean
    DemoMapper demoMapper(){
        new DemoMapper()
    }

    @ThriftClientsMap(mapperClass = DemoMapper.class)
    Map<String, TBackendService.Client> serviceClients

    @RequestMapping(value = "/hello/{name}/thrift", method = RequestMethod.GET)
    @Cacheable(cacheNames = "demoCache", key = "'thrift-'.concat(#name)")
    String helloThrift(@PathVariable String name) {
        String backendAorB = name.length() % 2 == 0 ? "a" : "b"
        log.info "Using backend $backendAorB for name $name..."
        def response = serviceClients.get(backendAorB)
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

    @RequestMapping(value = "/hello/{name}/rest", method = RequestMethod.GET)
    @Cacheable(cacheNames = "demoCache", key = "'rest-'.concat(#name)")
    String helloRest(@PathVariable String name) {
        def request = new SampleRequest()
        request.techData.correlationId = UUID.randomUUID()
        request.businessData.name = name
        request.businessData.dateOfBirth = LocalDate.now()
        log.info("Sending REST request: $request")
        restClientWithFeign.greet(request).businessData.message
    }

}