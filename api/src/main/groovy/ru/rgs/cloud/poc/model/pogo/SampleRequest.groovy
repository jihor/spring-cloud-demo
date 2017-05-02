package ru.rgs.cloud.poc.model.pogo

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import groovy.transform.CompileStatic
import groovy.transform.ToString

import java.time.LocalDate

/**
 * @author jihor (jihor@ya.ru)
 *         Created on 2017-05-02
 */
@CompileStatic
@ToString
class SampleRequest implements Serializable {

    final SampleRequestTechData techData = new SampleRequestTechData()
    final SampleRequestBusinessData businessData = new SampleRequestBusinessData()

    @ToString
    static class SampleRequestTechData implements Serializable {
        String correlationId
    }

    @ToString
    static class SampleRequestBusinessData implements Serializable {
        String name
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonSerialize(using = LocalDateSerializer.class)
        LocalDate dateOfBirth
    }
}
