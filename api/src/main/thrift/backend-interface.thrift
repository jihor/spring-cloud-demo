namespace java ru.rgs.openshift.test.backend
include "common.thrift"

struct TBackendReq {
    1: required common.THeaders headers
    2: required string lastname
}

struct TBackendResp {
    1: required common.THeaders headers
    2: required string message
}

exception TBackendException {
    1: required string message
}

service TBackendService {
    TBackendResp greet(1: required TBackendReq request) throws (1: TBackendException e)
}



