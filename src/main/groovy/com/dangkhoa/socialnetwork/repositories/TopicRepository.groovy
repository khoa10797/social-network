package com.dangkhoa.socialnetwork.repositories

import com.dangkhoa.socialnetwork.common.Constant
import com.dangkhoa.socialnetwork.entities.topic.Topic
import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class TopicRepository {

    @Autowired
    MongoTemplate mongoTemplate

    Topic findByTopicId(String topicId) {
        Query query = new Query().addCriteria(Criteria.where("topic_id").is(topicId))
        return mongoTemplate.findOne(query, Topic.class)
    }

    List<Topic> findTopics(Integer page, Integer limit) {
        Query query = new Query(
                limit: limit,
                skip: limit * (page - 1 < 0 ? 0 : page - 1)
        )
        return mongoTemplate.find(query, Topic.class)
    }

    Topic save(Topic topic) {
        return mongoTemplate.save(topic)
    }

    Long remove(String topicId) {
        Query query = new Query(Criteria.where("topic_id").is(topicId))
        return mongoTemplate.remove(query, Topic.class).deletedCount
    }

    void updateNumberPost(String topicId, Integer quantity) {
        Document filter = [topic_id: topicId]
        Document update = [
                $inc: [number_post: quantity]
        ]

        mongoTemplate.getCollection(Constant.TOPICS).updateOne(filter, update)
    }

    void updateNumberFollow(String topicId, Integer quantity) {
        Document filter = [topic_id: topicId]
        Document update = [
                $inc: [number_follow: quantity]
        ]

        mongoTemplate.getCollection(Constant.TOPICS).updateOne(filter, update)
    }
}
