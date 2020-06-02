package com.dangkhoa.socialnetwork.mongo.repositories

import com.dangkhoa.socialnetwork.common.Constant
import com.dangkhoa.socialnetwork.entities.mongo.usertopic.UserTopic
import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class UserTopicRepository {

    @Autowired
    MongoTemplate mongoTemplate

    List<String> getTopicIdByUserId(String userId) {
        List<String> result = new ArrayList<>()
        mongoTemplate.getCollection(Constant.USER_TOPIC).find()
                .filter([user_id: userId] as Document)
                .projection([topic_id: 1] as Document)
                .iterator()
                .forEachRemaining { item -> result.add(item?.topic_id as String) }
        return result
    }

    Long countByTopicId(String topicId) {
        Query query = new Query().addCriteria(Criteria.where("topic_id").is(topicId))
        return mongoTemplate.count(query, UserTopic.class)
    }

    UserTopic save(UserTopic userTopic) {
        return mongoTemplate.save(userTopic)
    }

    UserTopic findByUserIdAndTopicId(String userId, String topicId) {
        Query query = new Query()
                .addCriteria(Criteria.where("user_id").is(userId))
                .addCriteria(Criteria.where("topic_id").is(topicId))
        return mongoTemplate.findOne(query, UserTopic.class)
    }

    List<String> getUserIdByTopicId(String topicId) {
        List<String> result = new ArrayList<>()
        mongoTemplate.getCollection(Constant.USER_TOPIC).find()
                .filter([topic_id: topicId] as Document)
                .projection([user_id: 1] as Document)
                .iterator()
                .forEachRemaining { item -> result.add(item?.user_id as String) }
        return result
    }
}
