package ru.rgs.openshift.test.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;

/**
 * Created by jihor on 6/21/17.
 */
@Getter
@Setter
public class FeignBadResponseWrapper extends Throwable {
    private final int status;
    private final HttpHeaders headers;
    private final String body;

    public FeignBadResponseWrapper(int status, HttpHeaders headers, String body) {
        this.status = status;
        this.headers = headers;
        this.body = body;
    }
}