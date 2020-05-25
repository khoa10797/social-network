package com.dangkhoa.socialnetwork.services

import com.dangkhoa.socialnetwork.entities.userpost.UserPost
import com.dangkhoa.socialnetwork.repositories.UserPostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserPostService {

    @Autowired
    UserPostRepository userPostRepository
    @Autowired
    PostService postService

    UserPost save(UserPost userPost) {
        UserPost existUserPost = userPostRepository.findByUserIdAndPostId(userPost.userId, userPost.postId)

        Integer updatedNumberLike = UserPost.UserStatus.LIKE == userPost.userStatus ? 1 : -1
        if (existUserPost == null) {
            return userPostRepository.save(userPost)
        }

        if (existUserPost != null && existUserPost.userStatus == userPost.userStatus) {
            updatedNumberLike = 0
        }
        postService.updateNumberLike(userPost.postId, updatedNumberLike)

        existUserPost.userStatus = userPost.userStatus
        return userPostRepository.save(existUserPost)
    }

    List<UserPost> findByUserIdAndPostIds(String userId, List<String> postIds) {
        return userPostRepository.findByUserIdAndPostIds(userId, postIds)
    }

    UserPost findByUserIdAndPostId(String userId, String postId) {
        return userPostRepository.findByUserIdAndPostId(userId, postId)
    }

    Long removeByPostId(String postId) {
        return userPostRepository.removeByPostId(postId)
    }

    List<String> findUserIdByPostId(String postId) {
        return userPostRepository.findUserIdByPostId(postId)
    }
}
