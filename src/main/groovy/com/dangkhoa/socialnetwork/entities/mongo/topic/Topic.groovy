package com.dangkhoa.socialnetwork.entities.mongo.topic

import org.springframework.data.mongodb.core.mapping.Document

@Document("topics")
class Topic {

    String id
    String topicId
    String name
    Integer numberPost
    Integer numberFollow
    String intro
    String description
    String backgroundImage
    String avatar
    String icon
    Boolean lock
    Long createdAt
    Long updatedAt

}
