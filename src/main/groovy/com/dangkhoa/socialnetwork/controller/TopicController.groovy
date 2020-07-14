package com.dangkhoa.socialnetwork.controller

import com.dangkhoa.socialnetwork.base.BaseController
import com.dangkhoa.socialnetwork.base.response.BaseResponse
import com.dangkhoa.socialnetwork.base.response.ResponseData
import com.dangkhoa.socialnetwork.entities.mongo.topic.Topic
import com.dangkhoa.socialnetwork.entities.mongo.topic.TopicRequest
import com.dangkhoa.socialnetwork.entities.mongo.topic.TopicResponse
import com.dangkhoa.socialnetwork.entities.mongo.user.UserResponse
import com.dangkhoa.socialnetwork.entities.mongo.usertopic.UserTopic
import com.dangkhoa.socialnetwork.mongo.services.TopicService
import com.dangkhoa.socialnetwork.mongo.services.UserService
import com.dangkhoa.socialnetwork.mongo.services.UserTopicService
import ma.glasnost.orika.MapperFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import javax.validation.Valid

@RestController
@RequestMapping("/topic")
class TopicController extends BaseController {

    @Autowired
    MapperFacade topicMapperFacade
    @Autowired
    TopicService topicService
    @Autowired
    UserTopicService userTopicService
    @Autowired
    UserService userService

    @GetMapping
    ResponseEntity<ResponseData> findTopics(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {
        List<Topic> topics = topicService.findTopics(page)
        List<TopicResponse> topicResponses = topicMapperFacade.mapAsList(topics, TopicResponse.class)
        ResponseData data = new ResponseData(
                statusCode: 200,
                meta: buildMetaResponse(page, pageSize),
                data: topicResponses
        )

        return new ResponseEntity<ResponseData>(data, HttpStatus.OK)
    }

    @GetMapping("/{topicId}")
    ResponseEntity<ResponseData> findByTopicId(@PathVariable String topicId) {
        TopicResponse topicResponse = topicService.getByTopicId(topicId)
        ResponseData data = new ResponseData(
                statusCode: 200,
                data: topicResponse
        )

        return new ResponseEntity<ResponseData>(data, HttpStatus.OK)
    }

    @PostMapping
    ResponseEntity add(@RequestBody @Valid TopicRequest topicRequest) {
        Topic topic = topicMapperFacade.map(topicRequest, Topic.class)
        topicService.save(topic)
        return new ResponseEntity(HttpStatus.CREATED)
    }

    @PutMapping("/{topicId}")
    ResponseEntity update(@PathVariable String topicId, @RequestBody @Valid TopicRequest topicRequest) {
        Topic topic = topicMapperFacade.map(topicRequest, Topic.class)
        topic.topicId = topicId
        topicService.save(topic)
        return new ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @DeleteMapping("/{topicId}")
    ResponseEntity remove(@PathVariable String topicId) {
        topicService.remove(topicId)
        return new ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @PutMapping("/user_status")
    ResponseEntity<BaseResponse> updateUserStatus(@RequestBody UserTopic userTopic) {
        UserTopic savedUserTopic = userTopicService.save(userTopic)
        ResponseData data = new ResponseData(data: savedUserTopic)
        return new ResponseEntity<>(data, HttpStatus.OK)
    }

    @GetMapping("/{topicId}/follower")
    ResponseEntity<BaseResponse> getFollowUser(@PathVariable String topicId) {
        List<String> userIds = userTopicService.getUserIdByTopicId(topicId)
        List<UserResponse> userFollows = userService.getByUserIds(userIds)
        ResponseData data = new ResponseData(
                statusCode: 200,
                data: userFollows
        )
        return new ResponseEntity<BaseResponse>(data, HttpStatus.OK)
    }

    @GetMapping("/user/{userId}")
    ResponseEntity<BaseResponse> getByUserFollow(@PathVariable String userId) {
        List<UserTopic> userTopics = userTopicService.findByUserId(userId)
        List<String> topicIds = userTopics.collect { it.topicId }
        List<TopicResponse> topicResponses = topicService.getByTopicIds(topicIds)

        ResponseData data = new ResponseData(
                statusCode: 200,
                data: topicResponses
        )
        return new ResponseEntity<BaseResponse>(data, HttpStatus.OK)
    }
}
