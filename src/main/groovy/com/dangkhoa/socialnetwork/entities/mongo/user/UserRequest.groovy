package com.dangkhoa.socialnetwork.entities.mongo.user

class UserRequest {

    String userId
    String name
    Long dateOfBirth
    Integer gender
    String userName
    String password
    List<String> roleGroupIds
    String email
    Boolean active
    String avatar
    String intro
    String description

}
