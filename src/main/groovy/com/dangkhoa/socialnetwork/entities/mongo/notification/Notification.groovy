package com.dangkhoa.socialnetwork.entities.mongo.notification


import org.springframework.data.mongodb.core.mapping.Document

@Document("notifications")
class Notification {
    String id
    String subscriberId
    String postId
    String type
    Boolean isSeen
    com.dangkhoa.socialnetwork.entities.mongo.user.UserResponse publisher
    com.dangkhoa.socialnetwork.entities.mongo.user.UserResponse ownerPost
    Long createdAt

    static class Type {
        public static String ADD_COMMENT = "ADD_COMMENT"
        public static String ADD_CHILD_COMMENT = "ADD_CHILD_COMMENT"
        public static String LIKE_POST = "LIKE_POST"
    }
}
