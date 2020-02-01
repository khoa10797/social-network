package com.dangkhoa.socialnetwork.repositories

import com.dangkhoa.socialnetwork.entities.comment.Comment
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class CommentRepository {

    @Autowired
    MongoTemplate mongoTemplate

    Comment findByCommentId(String commentId) {
        Query query = new Query().addCriteria(Criteria.where("comment_id").is(commentId))
        return mongoTemplate.findOne(query, Comment.class)
    }

    List<Comment> findByPostId(String postId, Integer page, Integer limit) {
        Query query = new Query(
                limit: limit,
                skip: limit * (page - 1 < 0 ? 0 : page - 1)
        ).addCriteria(Criteria.where("post_id").is(postId))
        return mongoTemplate.find(query, Comment.class)
    }

    List<Comment> findByUserId(String postId, Integer page, Integer limit) {
        Query query = new Query(
                limit: limit,
                skip: limit * (page - 1 < 0 ? 0 : page - 1)
        ).addCriteria(Criteria.where("user_id").is(postId))
        return mongoTemplate.find(query, Comment.class)
    }

    Comment save(Comment comment) {
        return mongoTemplate.save(comment)
    }

    Long remove(String commentId) {
        Query query = new Query(Criteria.where("comment_id").is(commentId))
        return mongoTemplate.remove(query, Comment.class).deletedCount
    }
}
