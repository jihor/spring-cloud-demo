package ru.rgs.cloud.poc.model.pojo;

import java.time.LocalDate;

/**
 * @author jihor (jihor@ya.ru)
 *         Created on 2017-05-02
 */

public class SampleRequest {

    public final SampleRequestTechData techData = new SampleRequestTechData();
    public final SampleRequestBusinessData businessData = new SampleRequestBusinessData();

    public static class SampleRequestTechData {
        public String correlationId;
    }

    public static class SampleRequestBusinessData {
        public String name;
        public LocalDate dateOfBirth;
    }
}
