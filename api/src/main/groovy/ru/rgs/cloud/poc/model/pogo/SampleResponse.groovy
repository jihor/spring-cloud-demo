package ru.rgs.cloud.poc.model.pogo

import groovy.transform.CompileStatic
import groovy.transform.ToString

/**
 * @author jihor (jihor@ya.ru)
 *         Created on 2017-05-02
 */
@CompileStatic
@ToString
class SampleResponse implements Serializable {

    final SampleResponseTechData techData = new SampleResponseTechData()
    final SampleResponseBusinessData businessData = new SampleResponseBusinessData()

    @ToString
    static class SampleResponseTechData implements Serializable {
        String correlationId
        Integer resultCode
        String errorString
    }

    @ToString
    static class SampleResponseBusinessData implements Serializable {
        String message
    }
}
