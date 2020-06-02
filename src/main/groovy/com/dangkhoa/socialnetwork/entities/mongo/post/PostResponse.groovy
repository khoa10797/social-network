package com.dangkhoa.socialnetwork.entities.mongo.post

class PostResponse {

    String postId
    String userOwnerId
    String topicId
    String postType
    String title
    String content
    List<String> images
    Integer numberLike
    Integer numberDislike
    com.dangkhoa.socialnetwork.entities.mongo.user.UserResponse userOwner
    Long numberComment
    String userStatus
    Long createdAt
    Long updatedAt

}
