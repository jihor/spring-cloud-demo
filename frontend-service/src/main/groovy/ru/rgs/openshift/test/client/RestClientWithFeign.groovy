package ru.rgs.openshift.test.client

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.cloud.netflix.feign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import ru.rgs.cloud.poc.model.pogo.BackendServiceRest
import ru.rgs.cloud.poc.model.pogo.SampleRequest
import ru.rgs.cloud.poc.model.pogo.SampleResponse

import static org.springframework.web.bind.annotation.RequestMethod.POST

/**
 * @author jihor (jihor@ya.ru)
 *         Created on 2017-05-02
 */
@CompileStatic
@FeignClient("backend-service-a")
interface RestClientWithFeign extends BackendServiceRest {
    @Override
    @RequestMapping(method = POST, value = "/hello", produces = MediaType.APPLICATION_JSON_VALUE)
    SampleResponse greet(SampleRequest request)
}
