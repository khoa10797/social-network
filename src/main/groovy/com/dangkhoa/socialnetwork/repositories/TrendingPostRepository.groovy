package com.dangkhoa.socialnetwork.repositories

import com.dangkhoa.socialnetwork.entities.trendingpost.TrendingPost
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class TrendingPostRepository {

    @Autowired
    MongoTemplate mongoTemplate

    TrendingPost save(TrendingPost trendingPost) {
        return mongoTemplate.save(trendingPost)
    }

    TrendingPost getLast() {
        Query query = new Query()
        query.with(Sort.by(Sort.Direction.DESC, "created_at"))

        return mongoTemplate.findOne(query, TrendingPost.class)
    }
}
