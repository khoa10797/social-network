package com.dangkhoa.socialnetwork.entities.mongo.post

class PostRequest {

    String userOwnerId
    String topicId
    String postType
    String title
    String content
    List<String> images
    Integer numberLike
    Integer numberDislike
    Integer numberComment

}
