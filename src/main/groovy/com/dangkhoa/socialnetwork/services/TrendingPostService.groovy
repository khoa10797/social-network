package com.dangkhoa.socialnetwork.services

import com.dangkhoa.socialnetwork.entities.trendingpost.TrendingPost
import com.dangkhoa.socialnetwork.repositories.TrendingPostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TrendingPostService {

    @Autowired
    TrendingPostRepository trendingPostRepository

    TrendingPost save(TrendingPost trendingPost) {
        return trendingPostRepository.save(trendingPost)
    }

    TrendingPost getLast() {
        return trendingPostRepository.getLast()
    }

}
