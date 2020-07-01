package com.dangkhoa.socialnetwork.entities.mongo.user

class UserResponse {

    String userId
    String name
    Long dateOfBirth
    Integer gender
    String userName
    String email
    Boolean active
    String avatar
    List<String> roleGroupIds
    String intro
    String description
    Long createdAt
    Long updatedAt

}
