package com.dangkhoa.socialnetwork.mongo.repositories

import com.dangkhoa.socialnetwork.entities.mongo.userfollow.UserFollow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class UserFollowRepository {

    @Autowired
    MongoTemplate mongoTemplate

    UserFollow save(UserFollow userFollow) {
        return mongoTemplate.save(userFollow)
    }

    UserFollow findByUserIdAndFollowedUserId(String userId, String followedUserId) {
        Query query = new Query()
                .addCriteria(Criteria.where("user_id").is(userId))
                .addCriteria(Criteria.where("followed_user_id").in(followedUserId))
        return mongoTemplate.findOne(query, UserFollow.class)
    }

    List<UserFollow> getUserFollowedByUserId(String userId) {
        Query query = new Query()
                .addCriteria(Criteria.where("user_id").is(userId))
        return mongoTemplate.find(query, UserFollow.class)
    }
}
