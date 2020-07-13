package com.dangkhoa.socialnetwork.entities.mongo.userfollow

import org.springframework.data.mongodb.core.mapping.Document

@Document("user_follow")
class UserFollow {

    String id
    String userId
    String followedUserId
    String userStatus

    static class UserStatus {
        static final String FOLLOW = "follow"
        static final String UNFOLLOW = "unfollow"
    }
}
