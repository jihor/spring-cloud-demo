package ru.rgs.openshift.test.services;

import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.rgs.openshift.test.client.RestClientWithFeign;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author jihor (dmitriy_zhikharev@rgs.ru)
 *         Created on 2017-06-14
 */
@RestController
@Slf4j
public class  AsyncDemoEndpoint {

    @Autowired
    RestClientWithFeign restClientWithFeign;

    @RequestMapping(value = "/do-tasks", method = RequestMethod.GET)
    public String doTasks() {
        CompletableFuture<Integer> cf1 = doTask();
        CompletableFuture<Integer> cf2 = doTask();
        try {
            return CompletableFuture.allOf(cf1, cf2)
                    .thenApply(dummy -> combine(cf1.join(), cf2.join()))
                    .thenApply(this::toIntermediateResult)
                    .handle(this::toResultOrError)
                    .get();
        } catch (InterruptedException ie) {
            return "Interrupted: " + ie.getMessage();
        } catch (ExecutionException ee) {
            return "Execution failed: " + ee.getMessage();
        }
    }

    CompletableFuture<Integer> doTask() {
        return CompletableFuture.supplyAsync(restClientWithFeign::doATask);
    }

    public Long combine(Integer v1, Integer v2) {
        return (long) (v1 + v2);
    }

    public String toIntermediateResult(Long l) {
        return "Tasks took " + l + " to complete";
    }

    private String toResultOrError(String r, Throwable e) {
        if (e != null) {
            return "Failed with " + e.getClass().getSimpleName() + " [" + e.getMessage() + "], caused by " + e.getCause();
        } else {
            return r;
        }
    }
}
