package com.dangkhoa.socialnetwork.mongo.services

import com.dangkhoa.socialnetwork.entities.mongo.userpost.UserPost
import com.dangkhoa.socialnetwork.mongo.repositories.UserPostRepository
import org.apache.commons.lang3.StringUtils
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

        Integer updatedNumberLike = 0
        if (StringUtils.isNotBlank(userPost.userStatus))
            updatedNumberLike = UserPost.UserStatus.LIKE == userPost.userStatus ? 1 : -1

        if (existUserPost == null) {
            postService.updateNumberLike(userPost.postId, updatedNumberLike)
            return userPostRepository.save(userPost)
        }

        if (existUserPost != null && existUserPost.userStatus == userPost.userStatus)
            updatedNumberLike = 0

        if (updatedNumberLike != 0)
            postService.updateNumberLike(userPost.postId, updatedNumberLike)


        if (StringUtils.isNotBlank(userPost.userStatus))
            existUserPost.userStatus = userPost.userStatus
        if (userPost.bookmark != null)
            existUserPost.bookmark = userPost.bookmark

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

    List<UserPost> findBookmarkByUserId(String userId) {
        return userPostRepository.findBookmarkByUserId(userId)
    }
}
