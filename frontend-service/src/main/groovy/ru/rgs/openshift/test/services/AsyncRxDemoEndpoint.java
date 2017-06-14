package ru.rgs.openshift.test.services;

import groovy.util.logging.Slf4j;
import io.reactivex.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.rgs.openshift.test.client.RestClientWithFeign;

import java.util.concurrent.CompletableFuture;

/**
 * @author jihor (dmitriy_zhikharev@rgs.ru)
 *         Created on 2017-06-14
 */
@RestController
@Slf4j
public class AsyncRxDemoEndpoint {

    @Autowired
    RestClientWithFeign restClientWithFeign;

    @RequestMapping(value = "/do-tasks-rx", method = RequestMethod.GET)
    public String doTasksRx() {
        Observable<Integer> o1 = doTaskInObservable();
        Observable<Integer> o2 = doTaskInObservable();
        return Observable.zip(o1, o2, this::combine)
                .map(this::toResult)
                .onErrorReturn(e -> "Failed with " + e.getClass().getSimpleName() + " [" + e.getMessage() + "], caused by " + e.getCause())
                .blockingSingle();
    }

    Observable<Integer> doTaskInObservable() {
        return Observable.fromFuture(CompletableFuture.supplyAsync(restClientWithFeign::doATask));
    }

    public Long combine(Integer v1, Integer v2) {
        return (long) (v1 + v2);
    }

    public String toResult(Long l) {
        return "Tasks took " + l + " to complete";
    }
}
