package ru.rgs.openshift.test.config

import info.developerblog.spring.thrift.annotation.ThriftClientsMap
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.rgs.cloud.poc.model.thrift.TBackendService

/**
 *
 *
 * @author jihor (jihor@ya.ru)
 * Created on 2017-05-02
 */
@Configuration
class ThriftClientConfiguration {
    @Bean
    DemoMapper demoMapper(){
        new DemoMapper()
    }

    @ThriftClientsMap(mapperClass = DemoMapper.class)
    Map<String, TBackendService.Client> serviceClients

    @Bean
    ThriftClientWrapper thriftClientWrapper() {
        return new ThriftClientWrapper(serviceClients)
    }

    public static class ThriftClientWrapper {

        Map<String, TBackendService.Client> serviceClients

        ThriftClientWrapper(Map<String, TBackendService.Client> serviceClients) {
            this.serviceClients = serviceClients
        }
    }
}
