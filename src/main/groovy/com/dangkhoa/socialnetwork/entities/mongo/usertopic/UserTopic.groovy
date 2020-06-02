package com.dangkhoa.socialnetwork.entities.mongo.usertopic

import org.springframework.data.mongodb.core.mapping.Document

@Document("user_topic")
class UserTopic {

    String id
    String userId
    String topicId
    String userStatus
    Long createdAt
    Long updatedAt

    static class UserStatus {
        static final String FOLLOW = "follow"
        static final String UNFOLLOW = "unfollow"
    }
}
