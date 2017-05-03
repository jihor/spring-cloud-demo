package ru.rgs.openshift.test.backend

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.rgs.cloud.poc.model.pogo.BackendServiceRest
import ru.rgs.cloud.poc.model.pogo.SampleRequest
import ru.rgs.cloud.poc.model.pogo.SampleResponse

import java.time.LocalDate

/**
 * @author jihor (jihor@ya.ru)
 *         Created on 2017-05-02
 */
@CompileStatic
@RestController
@Slf4j
class BackendServiceRestImpl implements BackendServiceRest {
    @Value('${spring.application.name:undefined}')
    String appname

    @Value('${spring.application.index:default}')
    String appindex

    @Override
    SampleResponse greet(@RequestBody SampleRequest request){
        log.info("$appname-$appindex received REST request: $request")
        SampleResponse response = new SampleResponse()
        response.techData.resultCode = 0
        response.techData.correlationId = request.techData.correlationId
        response.businessData.message = "Hello ${request.businessData.name}${request.businessData.dateOfBirth == LocalDate.now() ? '. Happy birthday!' : ''}"
        response
    }
}
