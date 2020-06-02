package com.dangkhoa.socialnetwork.entities.elasticsearch.post

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "social-network", type = "posts")
class EsPost {
    @Id
    String id
    String postId
    String userOwnerId
    String topicId
    String title
    String content
    List<String> images
    Integer numberLike
    Integer numberDislike
    Integer numberComment
    Long createdAt
    Long updatedAt
}
