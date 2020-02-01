package com.dangkhoa.socialnetwork.entities.comment

import org.springframework.data.mongodb.core.mapping.Document

@Document("comments")
class Comment {

    String id
    String commentId
    String postId
    String userId
    String content
    Integer numberLike
    Integer numberDislike
    Long createdAt
    Long updatedAt

}
