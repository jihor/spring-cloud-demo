package ru.rgs.cloud.poc.model.pojo;

/**
 * @author jihor (jihor@ya.ru)
 *         Created on 2017-05-02
 */
public class SampleResponse {

    public final SampleResponse.SampleResponseTechData techData = new SampleResponse.SampleResponseTechData();
    public final SampleResponse.SampleResponseBusinessData businessData = new SampleResponse.SampleResponseBusinessData();
    
    public static class SampleResponseTechData {
        public String correlationId;
        public Integer resultCode;
        public String errorString;
    }

    public static class SampleResponseBusinessData {
        public String message;
    }
}
