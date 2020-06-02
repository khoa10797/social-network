package com.dangkhoa.socialnetwork.entities.mongo.user

import org.springframework.data.mongodb.core.mapping.Document

@Document("users")
class User {

    String id
    String userId
    String userName
    String password
    List<String> roleGroupIds
    String email
    String name
    Integer age
    Integer sex
    Boolean active
    String avatar
    String intro
    String description
    Long createdAt
    Long updatedAt

}
