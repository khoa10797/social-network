package com.dangkhoa.socialnetwork.repositories

import com.dangkhoa.socialnetwork.common.Constant
import com.dangkhoa.socialnetwork.entities.post.Post
import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository

@Repository
class PostRepository {

    @Autowired
    MongoTemplate mongoTemplate

    Post findByPostId(String postId) {
        Query query = new Query().addCriteria(Criteria.where("post_id").is(postId))
        return mongoTemplate.findOne(query, Post.class)
    }

    List<Post> findByUserId(String userId, Integer page, Integer limit) {
        Query query = new Query(
                limit: limit,
                skip: limit * (page - 1 < 0 ? 0 : page - 1)
        ).addCriteria(Criteria.where("user_id").is(userId))
        return mongoTemplate.find(query, Post.class)
    }

    Post save(Post post) {
        return mongoTemplate.save(post)
    }

    Long remove(String postId) {
        Query query = new Query(Criteria.where("post_id").is(postId))
        return mongoTemplate.remove(query, Post.class).deletedCount
    }

    void updateNumberComment(String postId, Integer quantity) {
        Document filter = [post_id: postId]
        Document update = [
                $inc: [number_comment: quantity]
        ]

        mongoTemplate.getCollection(Constant.POSTS).updateOne(filter, update)
    }
}
