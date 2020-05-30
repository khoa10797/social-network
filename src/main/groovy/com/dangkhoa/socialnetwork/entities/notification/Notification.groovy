package com.dangkhoa.socialnetwork.entities.notification

import com.dangkhoa.socialnetwork.entities.user.UserResponse
import org.springframework.data.mongodb.core.mapping.Document

@Document("notifications")
class Notification {
    String id
    String subscriberId
    String postId
    String type
    Boolean isSeen
    UserResponse publisher
    UserResponse ownerPost
    Long createdAt

    static class Type {
        public static String ADD_COMMENT = "ADD_COMMENT"
        public static String ADD_CHILD_COMMENT = "ADD_CHILD_COMMENT"
        public static String LIKE_POST = "LIKE_POST"
    }
}
