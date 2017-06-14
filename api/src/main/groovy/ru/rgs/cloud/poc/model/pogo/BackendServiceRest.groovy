package ru.rgs.cloud.poc.model.pogo

import groovy.transform.CompileStatic
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping

import static org.springframework.web.bind.annotation.RequestMethod.POST

/**
 * @author jihor (jihor@ya.ru)
 *         Created on 2017-05-02
 */
@CompileStatic
interface BackendServiceRest {
    @RequestMapping(method = POST, value = "/hello", produces = MediaType.APPLICATION_JSON_VALUE)
    SampleResponse greet(SampleRequest request)

    @RequestMapping(method = POST, value = "/do-a-task", produces = MediaType.APPLICATION_JSON_VALUE)
    Integer doATask()
}
