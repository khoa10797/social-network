package com.dangkhoa.socialnetwork.entities.user

import org.springframework.data.mongodb.core.mapping.Document

@Document("users")
class User {

    String id
    String userId
    String userName
    String password
    List<String> roleIds
    String email
    String name
    Integer age
    Integer sex
    List<String> postIds
    Boolean active
    Long createdAt
    Long updatedAt

}
