package com.dangkhoa.socialnetwork.common

class ResponseData {

    Integer statusCode
    Meta meta
    Object data

    static class Meta {
        String message
        Integer page
        Integer pageSize
    }

}
