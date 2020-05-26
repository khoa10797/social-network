package com.dangkhoa.socialnetwork.services

import com.dangkhoa.socialnetwork.entities.notification.Notification
import com.dangkhoa.socialnetwork.repositories.NotificationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class NotificationService {

    @Autowired
    NotificationRepository notificationRepository

    Notification save(Notification notification) {
        return notificationRepository.save(notification)
    }

    List<Notification> findByUserId(String userId) {
        return notificationRepository.findByUserId(userId)
    }

    Long countNotSeenByUserId(String userId) {
        return notificationRepository.countNotSeenByUserId(userId)
    }
}
