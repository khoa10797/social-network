package com.dangkhoa.socialnetwork.entities.mongo.comment

class CommentRequest {

    String postId
    String userOwnerId
    String parentId
    String content
    Integer numberLike
    Integer numberDislike

}
