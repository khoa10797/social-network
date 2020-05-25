package com.dangkhoa.socialnetwork.entities.notification

import org.springframework.data.mongodb.core.mapping.Document

@Document("notifications")
class Notification {
    String userId
    String postId
    String message
    Boolean isViewed
    Long createdAt
}
