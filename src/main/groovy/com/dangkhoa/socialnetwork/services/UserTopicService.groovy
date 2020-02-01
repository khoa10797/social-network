package com.dangkhoa.socialnetwork.services

import com.dangkhoa.socialnetwork.entities.usertopic.UserTopic
import com.dangkhoa.socialnetwork.repositories.UserTopicRepository
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserTopicService {

    @Autowired
    UserTopicRepository userTopicRepository

    List<String> getTopicIdByUserId(String userId){
        return userTopicRepository.getTopicIdByUserId(userId)
    }

    Long countUserByTopicId(String topicId){
        return userTopicRepository.countUserByTopicId(topicId)
    }

    UserTopic save(UserTopic userTopic){
        return userTopicRepository.save(userTopic)
    }
}
