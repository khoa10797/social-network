package com.dangkhoa.socialnetwork.repositories

import com.dangkhoa.socialnetwork.entities.usercomment.UserComment
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class UserCommentRepository {

    @Autowired
    MongoTemplate mongoTemplate

    UserComment save(UserComment userComment) {
        return mongoTemplate.save(userComment)
    }

    UserComment findByUserIdAndCommentId(String userId, String commentId) {
        Query query = new Query()
                .addCriteria(Criteria.where("user_id").is(userId))
                .addCriteria(Criteria.where("comment_id").is(commentId))
        return mongoTemplate.findOne(query, UserComment.class)
    }

    List<UserComment> findByUserIdAndCommentIds(String userId, List<String> commentIds) {
        Query query = new Query()
                .addCriteria(Criteria.where("user_id").is(userId))
                .addCriteria(Criteria.where("comment_id").in(commentIds))
        return mongoTemplate.find(query, UserComment.class)
    }

    Long removeByCommentId(String commentId) {
        Query query = new Query(Criteria.where("comment_id").is(commentId))
        return mongoTemplate.remove(query, UserComment.class).deletedCount
    }

}
