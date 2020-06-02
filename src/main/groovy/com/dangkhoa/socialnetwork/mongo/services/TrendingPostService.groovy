package com.dangkhoa.socialnetwork.mongo.services

import com.dangkhoa.socialnetwork.entities.mongo.trendingpost.TrendingPost
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TrendingPostService {

    @Autowired
    com.dangkhoa.socialnetwork.mongo.repositories.TrendingPostRepository trendingPostRepository

    TrendingPost save(TrendingPost trendingPost) {
        return trendingPostRepository.save(trendingPost)
    }

    TrendingPost getLast() {
        return trendingPostRepository.getLast()
    }

}
