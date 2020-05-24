package com.dangkhoa.socialnetwork.services

import com.dangkhoa.socialnetwork.base.SocialNetworkContext
import com.dangkhoa.socialnetwork.common.Constant
import com.dangkhoa.socialnetwork.entities.topic.Topic
import com.dangkhoa.socialnetwork.entities.topic.TopicResponse
import com.dangkhoa.socialnetwork.entities.user.UserAccount
import com.dangkhoa.socialnetwork.entities.usertopic.UserTopic
import com.dangkhoa.socialnetwork.exception.InValidObjectException
import com.dangkhoa.socialnetwork.repositories.TopicRepository
import ma.glasnost.orika.MapperFacade
import org.apache.commons.lang3.StringUtils
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TopicService {

    @Autowired
    SocialNetworkContext socialNetworkContext
    @Autowired
    TopicRepository topicRepository
    @Autowired
    UserTopicService userTopicService
    @Autowired
    MapperFacade topicMapperFacade

    Topic findByTopicId(String topicId) {
        return topicRepository.findByTopicId(topicId)
    }

    List<Topic> findTopics(Integer page, Integer limit) {
        return topicRepository.findTopics(page ?: 1, limit ?: Constant.DEFAULT_PAGE_SIZE)
    }

    List<Topic> findTopics(Integer page) {
        return findTopics(page, Constant.DEFAULT_PAGE_SIZE)
    }

    Long remove(String topicId) {
        return topicRepository.remove(topicId)
    }

    TopicResponse getByTopicId(String topicId) {
        Topic topic = findByTopicId(topicId)
        TopicResponse topicResponse = topicMapperFacade.map(topic, TopicResponse.class)
        fillUserStatus(topicResponse)

        return topicResponse
    }

    Topic save(Topic topic) {
        if (StringUtils.isBlank(topic.topicId)) {
            topic.topicId = new ObjectId().toString()
            topic.createdAt = new Date().getTime()
            topic.updatedAt = new Date().getTime()
            return topicRepository.save(topic)
        }

        Topic existTopic = findByTopicId(topic.topicId)
        if (existTopic == null)
            throw new InValidObjectException("topic not found")

        setUpdateValue(existTopic, topic)
        return topicRepository.save(existTopic)
    }

    static void setUpdateValue(Topic oldTopic, Topic newTopic) {
        oldTopic.name = StringUtils.isBlank(newTopic.name) ? oldTopic.name : newTopic.name
        oldTopic.numberFollow = newTopic.numberFollow == null ? oldTopic.numberFollow : newTopic.numberFollow
        oldTopic.updatedAt = new Date().getTime()
    }

    void updateNumberPost(String topicId, Integer quantity) {
        Topic topic = findByTopicId(topicId)
        if (topic.numberPost <= 0 && quantity < 0) {
            return
        }

        topicRepository.updateNumberPost(topicId, quantity)
    }

    void updateNumberFollow(String topicId, Integer quantity) {
        Topic topic = topicRepository.findByTopicId(topicId)
        if (topic.numberFollow <= 0 && quantity < 0) {
            return
        }
        topicRepository.updateNumberFollow(topicId, quantity)
    }

    private void fillUserStatus(TopicResponse topicResponse) {
        UserAccount currentUser = socialNetworkContext.getCurrentUser()
        if (currentUser == null) {
            return
        }

        UserTopic userTopic = userTopicService.findByUserIdAndTopicId(currentUser.userId, topicResponse.topicId)
        if (userTopic != null) {
            topicResponse.userStatus = userTopic.userStatus
        }
    }
}
