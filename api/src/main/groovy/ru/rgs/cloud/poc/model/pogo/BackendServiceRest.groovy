package ru.rgs.cloud.poc.model.pogo

import groovy.transform.CompileStatic

/**
 * @author jihor (jihor@ya.ru)
 *         Created on 2017-05-02
 */
@CompileStatic
interface BackendServiceRest {
    SampleResponse greet(SampleRequest request)
}
