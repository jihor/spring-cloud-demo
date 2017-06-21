package ru.rgs.openshift.test.exceptions;

import com.netflix.hystrix.exception.HystrixBadRequestException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;

/**
 * Created by jihor on 6/21/17.
 */
@Getter
@Setter
public class FeignBadResponseWrapper extends HystrixBadRequestException {
    private final int status;
    private final HttpHeaders headers;
    private final String body;

    public FeignBadResponseWrapper(int status, HttpHeaders headers, String body) {
        super("Bad request");
        this.status = status;
        this.headers = headers;
        this.body = body;
    }
}