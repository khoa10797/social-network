package com.dangkhoa.socialnetwork.mongo.repositories

import com.dangkhoa.socialnetwork.common.Constant
import com.dangkhoa.socialnetwork.entities.mongo.post.Post
import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
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

    List<Post> findByUserOwnerId(String userOwnerId, Integer page, Integer limit) {
        Query query = new Query(
                limit: limit,
                skip: limit * (page - 1 < 0 ? 0 : page - 1)
        ).addCriteria(Criteria.where("user_owner_id").is(userOwnerId))
        query.with(Sort.by(Sort.Direction.DESC, "created_at"))
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

    void updateNumberLike(String postId, Integer quantity) {
        Document filter = [post_id: postId]
        Document update = [
                $inc: [number_like: quantity]
        ]

        mongoTemplate.getCollection(Constant.POSTS).updateOne(filter, update)
    }

    List<Post> findTopByNumberLike(Integer limit, boolean desc) {
        Query query = new Query(
                limit: limit
        )

        Sort.Direction direction = Sort.Direction.DESC
        if (!desc) {
            direction = Sort.Direction.ASC
        }
        query.with(Sort.by(direction, "number_like"))

        return mongoTemplate.find(query, Post.class)
    }

    List<Post> findByPostIds(List<String> postIds) {
        Query query = new Query(Criteria.where("post_id").in(postIds))
        return mongoTemplate.find(query, Post.class)
    }

    List<Post> findByTopicId(String topicId, Integer page, Integer limit) {
        Query query = new Query(
                limit: limit,
                skip: limit * (page - 1 < 0 ? 0 : page - 1)
        ).addCriteria(Criteria.where("topic_id").is(topicId))
        query.with(Sort.by(Sort.Direction.DESC, "created_at"))
        return mongoTemplate.find(query, Post.class)
    }

    Long countByUserOwnerId(String userId) {
        Query query = new Query()
        Criteria criteria = Criteria.where("user_owner_id").is(userId)
        query.addCriteria(criteria)
        return mongoTemplate.count(query, Post.class)
    }

    List<Post> findNewPost() {
        Query query = new Query()
                .addCriteria(Criteria.where("is_new").is(true))

        return mongoTemplate.find(query, Post.class)
    }

    List<Post> findNewPost(Integer limit) {
        Query query = new Query(
                limit: limit
        ).addCriteria(Criteria.where("is_new").is(true))

        return mongoTemplate.find(query, Post.class)
    }

    Long convertNewPostToOld(List<String> postIds) {
        Query query = new Query().addCriteria(Criteria.where("post_id").in(postIds))
        Update update = new Update().set("is_new", false)

        return mongoTemplate.updateMulti(query, update, Post.class).modifiedCount
    }
}
