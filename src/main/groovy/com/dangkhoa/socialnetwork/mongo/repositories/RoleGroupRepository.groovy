package com.dangkhoa.socialnetwork.mongo.repositories


import com.dangkhoa.socialnetwork.entities.mongo.rolegroup.RoleGroup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class RoleGroupRepository {

    @Autowired
    MongoTemplate mongoTemplate

    RoleGroup findById(String roleGroupId) {
        Query query = new Query().addCriteria(Criteria.where("role_group_id").is(roleGroupId))
        return mongoTemplate.findOne(query, RoleGroup.class)
    }

    List<RoleGroup> findByIds(List<String> roleGroupIds) {
        Query query = new Query().addCriteria(Criteria.where("role_group_id").in(roleGroupIds))
        return mongoTemplate.find(query, RoleGroup.class)
    }

    RoleGroup save(RoleGroup roleGroup) {
        return mongoTemplate.save(roleGroup)
    }

    Long remove(String roleGroupId) {
        Query query = new Query(Criteria.where("role_group_id").is(roleGroupId))
        return mongoTemplate.remove(query, RoleGroup.class).deletedCount
    }
}
