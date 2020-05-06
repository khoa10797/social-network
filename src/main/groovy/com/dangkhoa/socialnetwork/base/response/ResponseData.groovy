package com.dangkhoa.socialnetwork.base.response

class ResponseData extends BaseResponse{

    Meta meta
    Object data

    static class Meta {
        String message
        Integer page
        Integer pageSize
    }

}
