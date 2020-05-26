package com.dangkhoa.socialnetwork.entities.notification

import org.springframework.data.mongodb.core.mapping.Document

@Document("notifications")
class Notification {
    String receiverId
    String postId
    String message
    String type
    Boolean isSeen
    Long createdAt

    static class Type {
        public static String ADD_COMMENT = "ADD_COMMENT"
        public static String ADD_CHILD_COMMENT = "ADD_CHILD_COMMENT"
    }
}
