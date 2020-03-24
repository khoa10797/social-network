package com.dangkhoa.socialnetwork.repositories


import com.dangkhoa.socialnetwork.entities.userpost.UserPost
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

    List<UserPost> findByUserIdAndPostId(String userId, List<String> postIds) {
        Query query = new Query()
                .addCriteria(Criteria.where("user_id").is(userId))
                .addCriteria(Criteria.where("post_id").in(postIds))
        return mongoTemplate.find(query, UserPost.class)
    }
}
