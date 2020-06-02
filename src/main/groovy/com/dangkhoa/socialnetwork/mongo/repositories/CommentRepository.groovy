package com.dangkhoa.socialnetwork.mongo.repositories

import com.dangkhoa.socialnetwork.common.Constant
import com.dangkhoa.socialnetwork.entities.mongo.comment.Comment
import org.bson.Document
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
        )
        Criteria criteria = Criteria.where("post_id").is(postId).and("parent_id").is(null)
        query.addCriteria(criteria)
        return mongoTemplate.find(query, Comment.class)
    }

    List<Comment> findByUserOwnerId(String userOwnerId, Integer page, Integer limit) {
        Query query = new Query(
                limit: limit,
                skip: limit * (page - 1 < 0 ? 0 : page - 1)
        ).addCriteria(Criteria.where("user_owner_id").is(userOwnerId))
        return mongoTemplate.find(query, Comment.class)
    }

    Comment save(Comment comment) {
        return mongoTemplate.save(comment)
    }

    Long remove(String commentId) {
        Query query = new Query(Criteria.where("comment_id").is(commentId))
        return mongoTemplate.remove(query, Comment.class).deletedCount
    }

    Long countByPostId(String postId) {
        Query query = new Query(Criteria.where("post_id").is(postId))
        return mongoTemplate.count(query, Comment.class)
    }

    List<Comment> findByCommentParentId(String commentParentId) {
        Query query = new Query(Criteria.where("parent_id").is(commentParentId))
        return mongoTemplate.find(query, Comment.class)
    }

    Long removeByPostId(String postId) {
        Query query = new Query(Criteria.where("post_id").is(postId))
        return mongoTemplate.remove(query, Comment.class).deletedCount
    }

    void updateNumberLike(String commentId, Integer quantity) {
        Document filter = [comment_id: commentId]
        Document update = [
                $inc: [number_like: quantity]
        ]

        mongoTemplate.getCollection(Constant.COMMENTS).updateOne(filter, update)
    }

    List<Comment> findByParentId(String parentId) {
        Query query = new Query(Criteria.where("parent_id").is(parentId))
        return mongoTemplate.find(query, Comment.class)
    }

    Long removeByParentId(String parentId) {
        Query query = new Query(Criteria.where("parent_id").is(parentId))
        return mongoTemplate.remove(query, Comment.class).deletedCount
    }
}
