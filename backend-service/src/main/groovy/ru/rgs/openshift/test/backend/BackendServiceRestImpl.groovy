package ru.rgs.openshift.test.backend

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.rgs.cloud.poc.model.pogo.BackendServiceRest
import ru.rgs.cloud.poc.model.pogo.SampleRequest
import ru.rgs.cloud.poc.model.pogo.SampleResponse

import java.time.LocalDate

import static org.springframework.web.bind.annotation.RequestMethod.POST

/**
 * @author jihor (jihor@ya.ru)
 *         Created on 2017-05-02
 */
@CompileStatic
@RestController
@Slf4j
class BackendServiceRestImpl implements BackendServiceRest {
    @RequestMapping(method = POST, value = "/hello", produces = MediaType.APPLICATION_JSON_VALUE)
    SampleResponse greet(@RequestBody SampleRequest request){
        log.info("Received REST request: $request")
        SampleResponse response = new SampleResponse()
        response.techData.resultCode = 0
        response.techData.correlationId = request.techData.correlationId
        response.businessData.message = "Hello ${request.businessData.name}${request.businessData.dateOfBirth == LocalDate.now() ? '. Happy birthday!' : ''}"
        response
    }
}
