package com.dangkhoa.socialnetwork.base

import com.dangkhoa.socialnetwork.common.Constant
import com.dangkhoa.socialnetwork.common.ResponseData
import com.dangkhoa.socialnetwork.common.ResponseError
import com.dangkhoa.socialnetwork.exception.InValidObjectException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler

class BaseController {

    ResponseData.Meta buildMetaResponse(String message = "", Integer page, Integer pageSize) {
        return new ResponseData.Meta(
                message: message,
                page: page ?: 1,
                pageSize: pageSize ?: Constant.DEFAULT_PAGE_SIZE
        )
    }

    @ExceptionHandler(InValidObjectException.class)
    ResponseEntity<ResponseError> handleInValidateObjectException(InValidObjectException e) {
        ResponseError data = new ResponseError(
                statusCode: 422,
                message: e.message,
                error: e.stackTrace
        )
        return new ResponseEntity<>(data, HttpStatus.UNPROCESSABLE_ENTITY)
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ResponseError> handleException(Exception e) {
        ResponseError data = new ResponseError(
                statusCode: 500,
                message: e.message,
                error: e.stackTrace
        )
        return new ResponseEntity<>(data, HttpStatus.UNPROCESSABLE_ENTITY)
    }

}
