package com.dangkhoa.socialnetwork.entities.mongo.usercomment

import org.springframework.data.mongodb.core.mapping.Document

@Document("user_comment")
class UserComment {

    String id
    String userId
    String commentId
    String postId
    String userStatus

    static class UserStatus {
        static final String LIKE = "like"
        static final String NORMAL = "normal"
        static final String DISLIKE = "dislike"
    }

}
