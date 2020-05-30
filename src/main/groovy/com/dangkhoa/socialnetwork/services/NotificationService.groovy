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

    List<Notification> findByUserId(String userId, Integer page) {
        return notificationRepository.findBySubscriberId(userId, page)
    }

    Long countNotSeenByUserId(String userId) {
        return notificationRepository.countNotSeenByUserId(userId)
    }

    Notification findByPostIdAndSubscriberId(String postId, String subscriberId) {
        return notificationRepository.findByPostIdAndSubscriberId(postId, subscriberId)
    }

    List<Notification> findByPostIdAndSubscriberIds(String postId, List<String> subscriberIds) {
        return notificationRepository.findByPostIdAndSubscriberIds(postId, subscriberIds)
    }

    List<Notification> findNotification(String postId, String publisherId, String type) {
        return notificationRepository.findByNotification(postId, publisherId, type)
    }

    Long updateSeenStatusBySubscriberId(String subscriberId) {
        return notificationRepository.updateSeenStatusBySubscriberId(subscriberId)
    }
}
