package com.dangkhoa.socialnetwork.repositories

import com.dangkhoa.socialnetwork.common.Constant
import com.dangkhoa.socialnetwork.entities.user.User
import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class UserRepository {

    @Autowired
    MongoTemplate mongoTemplate

    List<User> findUsers(Integer limit, Integer page) {
        Query query = new Query(
                limit: limit,
                skip: limit * (page - 1 < 0 ? 0 : page - 1)
        )
        return mongoTemplate.find(query, User.class)
    }

    User save(User user) {
        return mongoTemplate.save(user)
    }

    User findByUserId(String userId) {
        Query query = new Query().addCriteria(Criteria.where("user_id").is(userId))
        return mongoTemplate.findOne(query, User.class)
    }

    Long remove(String userId) {
        Query query = new Query(Criteria.where("user_id").is(userId))
        return mongoTemplate.remove(query, User.class).deletedCount
    }

    User findByUserName(String userName) {
        Query query = new Query().addCriteria(Criteria.where("user_name").is(userName))
        return mongoTemplate.findOne(query, User.class)
    }

    User findByEmail(String email) {
        Query query = new Query().addCriteria(Criteria.where("email").is(email))
        return mongoTemplate.findOne(query, User.class)
    }

    List<String> findRoleByUserId(String userId) {
        List<String> result = new ArrayList<>()
        mongoTemplate.getCollection(Constant.USERS).find()
                .filter([user_id: userId] as Document)
                .projection([role_ids: 1] as Document)
                .iterator()
                .forEachRemaining { item -> result.add(item?.role_ids as String) }
        return result
    }

    List<User> findByUserId(List<String> userIds) {
        Query query = new Query(Criteria.where("user_id").in(userIds))
        return mongoTemplate.find(query, User.class)
    }
}
