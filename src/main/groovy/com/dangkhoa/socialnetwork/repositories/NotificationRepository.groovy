package com.dangkhoa.socialnetwork.repositories

import com.dangkhoa.socialnetwork.common.Constant
import com.dangkhoa.socialnetwork.entities.notification.Notification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository

@Repository
class NotificationRepository {

    @Autowired
    MongoTemplate mongoTemplate

    Notification save(Notification notification) {
        return mongoTemplate.save(notification)
    }

    List<Notification> findBySubscriberId(String userId, Integer page) {
        Query query = new Query(
                limit: Constant.DEFAULT_NOTIFICATION_SIZE,
                skip: Constant.DEFAULT_NOTIFICATION_SIZE * (page - 1 < 0 ? 0 : page - 1)
        )
        query.with(Sort.by(Sort.Direction.DESC, "created_at"))
        query.addCriteria(Criteria.where("subscriber_id").is(userId))
        return mongoTemplate.find(query, Notification.class)
    }

    Long countNotSeenByUserId(String userId) {
        Query query = new Query()
        Criteria criteria = Criteria.where("subscriber_id").is(userId).and("is_seen").is(false)
        query.addCriteria(criteria)
        return mongoTemplate.count(query, Notification.class)
    }

    Notification findByPostIdAndSubscriberId(String postId, String subscriberId) {
        Query query = new Query()
        Criteria criteria = Criteria.where("post_id").is(postId).and("subscriber_id").is(subscriberId)
        query.addCriteria(criteria)
        return mongoTemplate.findOne(query, Notification.class)
    }

    List<Notification> findByPostIdAndSubscriberIds(String postId, List<String> subscriberIds) {
        Query query = new Query()
        Criteria criteria = Criteria.where("post_id").is(postId).and("subscriber_id").in(subscriberIds)
        query.addCriteria(criteria)
        return mongoTemplate.find(query, Notification.class)
    }

    List<Notification> findByNotification(String postId, String publisherId, String type) {
        Query query = new Query()
        Criteria criteria = Criteria.where("post_id").is(postId)
                .and("publisher_id").is(publisherId)
                .and("type").is(type)
        query.addCriteria(criteria)
        return mongoTemplate.find(query, Notification.class)
    }

    Long updateSeenStatusBySubscriberId(String subscriberId) {
        Query query = new Query().addCriteria(Criteria.where("subscriber_id").is(subscriberId))
        Update update = new Update()
        update.set("is_seen", true)
        return mongoTemplate.updateMulti(query, update, Notification.class).modifiedCount
    }
}
