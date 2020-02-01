package com.dangkhoa.socialnetwork.entities.post

import org.springframework.data.mongodb.core.mapping.Document

@Document("posts")
class Post {

    String id
    String postId
    String userId
    String postType
    String title
    String content
    Integer numberLike
    Integer numberDislike
    Long createdAt
    Long updatedAt

}
