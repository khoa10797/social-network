package com.dangkhoa.socialnetwork.entities.usertopic

import org.springframework.data.mongodb.core.mapping.Document

@Document("user_topic")
class UserTopic {

    String id
    String userId
    String topicId
    Integer followStatus
    Long createdAt
    Long updatedAt

    static class FollowStatus{
        static final Integer FOLLOW = 1
        static final Integer UNFOLLOW = -1
    }
}
