package com.dangkhoa.socialnetwork.services

import com.dangkhoa.socialnetwork.entities.usertopic.UserTopic
import com.dangkhoa.socialnetwork.repositories.UserTopicRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserTopicService {

    @Autowired
    UserTopicRepository userTopicRepository

    List<String> getTopicIdByUserId(String userId){
        return userTopicRepository.getTopicIdByUserId(userId)
    }

    Long countByTopicId(String topicId){
        return userTopicRepository.countByTopicId(topicId)
    }

    UserTopic save(UserTopic userTopic){
        return userTopicRepository.save(userTopic)
    }
}
