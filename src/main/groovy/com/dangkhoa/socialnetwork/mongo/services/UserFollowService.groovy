package com.dangkhoa.socialnetwork.mongo.services

import com.dangkhoa.socialnetwork.entities.mongo.userfollow.UserFollow
import com.dangkhoa.socialnetwork.mongo.repositories.UserFollowRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserFollowService {

    @Autowired
    UserFollowRepository userFollowRepository
    @Autowired
    UserService userService

    UserFollow save(UserFollow userFollow) {
        UserFollow existUserFollow = userFollowRepository.findByUserIdAndFollowedUserId(userFollow.userId, userFollow.followedUserId)
        Integer updatedNumberFollow = UserFollow.UserStatus.FOLLOW == userFollow.userStatus ? 1 : -1

        if (existUserFollow == null) {
            userService.updateNumberFollow(userFollow.followedUserId, updatedNumberFollow)
            return userFollowRepository.save(userFollow)
        }

        if (existUserFollow != null && existUserFollow.userStatus == userFollow.userStatus) {
            updatedNumberFollow = 0
        }

        userService.updateNumberFollow(userFollow.followedUserId, updatedNumberFollow)
        existUserFollow.userStatus = userFollow.userStatus
        return userFollowRepository.save(existUserFollow)
    }

    UserFollow findByUserIdAndFollowedUserId(String userId, String followedUserId) {
        return userFollowRepository.findByUserIdAndFollowedUserId(userId, followedUserId)
    }
}
