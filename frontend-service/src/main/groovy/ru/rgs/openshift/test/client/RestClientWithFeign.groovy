package ru.rgs.openshift.test.client

import groovy.transform.CompileStatic
import org.springframework.cloud.netflix.feign.FeignClient
import ru.rgs.cloud.poc.model.pogo.BackendServiceRest

/**
 * @author jihor (jihor@ya.ru)
 *         Created on 2017-05-02
 */
@CompileStatic
@FeignClient("backend-service-a")
interface RestClientWithFeign extends BackendServiceRest {}
