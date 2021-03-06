package com.dangkhoa.socialnetwork.entities.mongo.comment

import org.springframework.data.mongodb.core.mapping.Document

@Document("comments")
class Comment {

    String id
    String commentId
    String postId
    String userOwnerId
    String parentId
    String content
    Integer numberLike
    Integer numberDislike
    Long createdAt
    Long updatedAt

}
