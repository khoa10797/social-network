package com.dangkhoa.socialnetwork.entities.post


import org.springframework.data.mongodb.core.mapping.Document

@Document("posts")
class Post {

    String id
    String postId
    String userOwnerId
    String topicId
    String postType
    String title
    String content
    List<String> images
    Integer numberLike
    Integer numberDislike
    Integer numberComment
    Long createdAt
    Long updatedAt

}
