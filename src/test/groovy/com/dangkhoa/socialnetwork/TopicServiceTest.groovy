package com.dangkhoa.socialnetwork

import com.dangkhoa.socialnetwork.entities.mongo.topic.Topic
import com.dangkhoa.socialnetwork.mongo.services.TopicService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class TopicServiceTest {

    @Autowired
    TopicService topicService

    @Test
    void createTopicTest() {
        topicService.save(
                new Topic(
                        name: "Khoa học",
                        numberFollow: 0
                )
        )
    }
}
