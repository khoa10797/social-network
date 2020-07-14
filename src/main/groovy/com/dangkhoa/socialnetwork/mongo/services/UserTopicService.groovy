package com.dangkhoa.socialnetwork.mongo.services

import com.dangkhoa.socialnetwork.entities.mongo.usertopic.UserTopic
import com.dangkhoa.socialnetwork.mongo.repositories.UserTopicRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserTopicService {

    @Autowired
    UserTopicRepository userTopicRepository
    @Autowired
    TopicService topicService

    List<String> getTopicIdByUserId(String userId) {
        return userTopicRepository.getTopicIdByUserId(userId)
    }

    Long countByTopicId(String topicId) {
        return userTopicRepository.countByTopicId(topicId)
    }

    UserTopic save(UserTopic userTopic) {
        UserTopic existUserTopic = userTopicRepository.findByUserIdAndTopicId(userTopic.userId, userTopic.topicId)

        Integer updatedNumberFollow = UserTopic.UserStatus.FOLLOW == userTopic.userStatus ? 1 : -1
        if (existUserTopic != null && existUserTopic.userStatus == userTopic.userStatus) {
            updatedNumberFollow = 0
        }
        topicService.updateNumberFollow(userTopic.topicId, updatedNumberFollow)

        if (existUserTopic == null) {
            return userTopicRepository.save(userTopic)
        }

        existUserTopic.userStatus = userTopic.userStatus
        return userTopicRepository.save(existUserTopic)
    }

    UserTopic findByUserIdAndTopicId(String userId, String topicId) {
        return userTopicRepository.findByUserIdAndTopicId(userId, topicId)
    }

    List<String> getUserIdByTopicId(String topicId) {
        return userTopicRepository.getUserIdByTopicId(topicId)
    }

    List<UserTopic> findByUserId(String userId) {
        return userTopicRepository.findByUserId(userId)
    }
}
