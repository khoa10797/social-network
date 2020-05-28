package com.dangkhoa.socialnetwork.controller

import com.dangkhoa.socialnetwork.base.BaseController
import com.dangkhoa.socialnetwork.base.response.BaseResponse
import com.dangkhoa.socialnetwork.base.response.ResponseData
import com.dangkhoa.socialnetwork.entities.notification.Notification
import com.dangkhoa.socialnetwork.services.NotificationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/notification")
class NotificationController extends BaseController {

    @Autowired
    NotificationService notificationService

    @GetMapping("/count/not_seen/{userId}")
    ResponseEntity<BaseResponse> countNotSeenByUserId(@PathVariable String userId) {
        long numberNotification = notificationService.countNotSeenByUserId(userId)
        ResponseData data = new ResponseData(data: [count: numberNotification])
        return new ResponseEntity<>(data, HttpStatus.OK)
    }

    @GetMapping("/{userId}")
    ResponseEntity<BaseResponse> getByUserId(@PathVariable String userId,
                                             @RequestParam(required = false, name = "page") Integer page) {
        if (page == null)
            page = 1
        List<Notification> notifications = notificationService.findByUserId(userId, page)
        ResponseData data = new ResponseData(data: notifications)
        return new ResponseEntity<>(data, HttpStatus.OK)
    }
}
