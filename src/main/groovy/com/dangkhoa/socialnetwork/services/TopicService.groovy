package com.dangkhoa.socialnetwork.services

import com.dangkhoa.socialnetwork.common.Constant
import com.dangkhoa.socialnetwork.entities.topic.Topic
import com.dangkhoa.socialnetwork.exception.InValidObjectException
import com.dangkhoa.socialnetwork.repositories.TopicRepository
import org.apache.commons.lang3.StringUtils
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TopicService {

    @Autowired
    TopicRepository topicRepository

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
}
