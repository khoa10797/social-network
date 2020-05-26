package com.dangkhoa.socialnetwork.repositories

import com.dangkhoa.socialnetwork.entities.notification.Notification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class NotificationRepository {

    @Autowired
    MongoTemplate mongoTemplate

    Notification save(Notification notification) {
        return mongoTemplate.save(notification)
    }

    List<Notification> findByUserId(String userId) {
        Query query = new Query().addCriteria(Criteria.where("receiver_id").is(userId))
        return mongoTemplate.find(query, Notification.class)
    }

    Long countNotSeenByUserId(String userId) {
        Query query = new Query().addCriteria(Criteria.where("receiver_id").is(userId))
        return mongoTemplate.count(query, Notification.class)
    }
}
