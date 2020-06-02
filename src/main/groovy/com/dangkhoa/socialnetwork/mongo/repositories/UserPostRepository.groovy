package com.dangkhoa.socialnetwork.mongo.repositories

import com.dangkhoa.socialnetwork.common.Constant
import com.dangkhoa.socialnetwork.entities.mongo.userpost.UserPost
import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class UserPostRepository {

    @Autowired
    MongoTemplate mongoTemplate

    UserPost save(UserPost userPost) {
        return mongoTemplate.save(userPost)
    }

    List<UserPost> findByUserIdAndPostIds(String userId, List<String> postIds) {
        Query query = new Query()
                .addCriteria(Criteria.where("user_id").is(userId))
                .addCriteria(Criteria.where("post_id").in(postIds))
        return mongoTemplate.find(query, UserPost.class)
    }

    UserPost findByUserIdAndPostId(String userId, String postId) {
        Query query = new Query()
                .addCriteria(Criteria.where("user_id").is(userId))
                .addCriteria(Criteria.where("post_id").is(postId))
        return mongoTemplate.findOne(query, UserPost.class)
    }

    Long removeByPostId(String postId) {
        Query query = new Query(Criteria.where("post_id").is(postId))
        return mongoTemplate.remove(query, UserPost.class).deletedCount
    }

    List<String> findUserIdByPostId(String postId) {
        List<String> result = new ArrayList<>()
        mongoTemplate.getCollection(Constant.USER_POST).find()
                .filter([post_id: postId] as Document)
                .projection([user_id: 1] as Document)
                .iterator()
                .forEachRemaining { item -> result.add(item?.user_id as String) }
        return result
    }
}
