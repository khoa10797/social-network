package com.dangkhoa.socialnetwork.entities.mongo.userpost

import org.springframework.data.mongodb.core.mapping.Document

@Document("user_post")
class UserPost {

    String id
    String userId
    String postId
    String userStatus

    static class UserStatus {
        static final String LIKE = "like"
        static final String NORMAL = "normal"
        static final String DISLIKE = "dislike"
    }

}
