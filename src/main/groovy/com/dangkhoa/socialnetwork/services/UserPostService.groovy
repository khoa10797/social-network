package com.dangkhoa.socialnetwork.services

import com.dangkhoa.socialnetwork.entities.userpost.UserPost
import com.dangkhoa.socialnetwork.repositories.UserPostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service

@Service
class UserPostService {

    @Autowired
    UserPostRepository userPostRepository
    @Autowired
    PostService postService

    UserPost save(UserPost userPost) {
        UserPost savedUserPost = userPostRepository.save(userPost)
        Integer updatedNumberLike = UserPost.LikeStatus.LIKE == userPost.likeStatus ? 1 : -1
        postService.updateNumberLike(userPost.postId, updatedNumberLike)
        return savedUserPost
    }

    List<UserPost> findByUserIdAndPostIds(String userId, List<String> postIds) {
        return userPostRepository.findByUserIdAndPostId(userId, postIds)
    }

}
