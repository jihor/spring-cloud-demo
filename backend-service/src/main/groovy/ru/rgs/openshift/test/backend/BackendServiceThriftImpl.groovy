package ru.rgs.openshift.test.backend

import org.apache.thrift.TException
import org.springframework.beans.factory.annotation.Value
import ru.rgs.cloud.poc.model.thrift.TBackendException
import ru.rgs.cloud.poc.model.thrift.TBackendReq
import ru.rgs.cloud.poc.model.thrift.TBackendResp
import ru.rgs.cloud.poc.model.thrift.TBackendService
import ru.trylogic.spring.boot.thrift.annotation.ThriftController;

/**
 * @author jihor (dmitriy_zhikharev@rgs.ru)
 * (С) RGS Group, http://www.rgs.ru
 * Created on 2016-07-06
 */
@ThriftController("/backend")
class BackendServiceThriftImpl implements TBackendService.Iface {
    @Value('${spring.application.name:undefined}')
    String appname

    @Override
    TBackendResp greet(TBackendReq request) throws TBackendException, TException {
        new TBackendResp().setHeaders(request.headers).setMessage("My name is $appname. Your name is $request.lastname")
    }

}