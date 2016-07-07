package ru.rgs.openshift.test

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

/**
 *
 *
 * @author jihor (dmitriy_zhikharev@rgs.ru)
 * (С) RGS Group, http://www.rgs.ru
 * Created on 2016-07-06
 */
@SpringBootApplication
@EnableDiscoveryClient
class Application {
    static void main(String[] args) {
        SpringApplication.run(Application.class, args)
    }
}