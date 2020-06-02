package com.dangkhoa.socialnetwork.entities.mongo.trendingpost

import org.springframework.data.mongodb.core.mapping.Document

@Document("trending_post")
class TrendingPost {
    String id
    List<String> postIds
    Long createdAt
}
