package com.dangkhoa.socialnetwork.job

import com.dangkhoa.socialnetwork.entities.post.Post
import com.dangkhoa.socialnetwork.entities.trendingpost.TrendingPost
import com.dangkhoa.socialnetwork.services.PostService
import com.dangkhoa.socialnetwork.services.TrendingPostService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class TrendingPostTask {

    @Autowired
    PostService postService
    @Autowired
    TrendingPostService trendingPostService

    @Scheduled(fixedRate = 3600000L)
    void staticsTrendingPost() {
        List<Post> posts = postService.findTopByNumberLike(50, true)
        List<String> postIds = posts.sort { item -> item.numberLike + item.numberComment }
                .collect { item -> item.postId }

        TrendingPost trendingPost = new TrendingPost(
                postIds: postIds,
                createdAt: new Date().getTime()
        )

        trendingPostService.save(trendingPost)
    }

}
