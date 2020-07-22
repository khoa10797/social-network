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
                        name: "Ti vi",
                        numberPost: 0,
                        numberFollow: 0,
                        intro: "Một món đồ không thể thiếu đối với mọi gia đình",
                        description: "Nơi tập trung các bài viết liên, đánh giá của người dùng về những chiếc Ti vi mà bạn đang dùng, chuẩn bị dùng và sẽ dùng",
                        backgroundImage: "https://firebasestorage.googleapis.com/v0/b/social-network-66b92.appspot.com/o/app_images%2Fbackground_topic_tivi.jpg?alt=media&token=ae93e1c7-383c-4ab8-a664-3c63ec3c3747",
                        avatar: "https://firebasestorage.googleapis.com/v0/b/social-network-66b92.appspot.com/o/app_images%2Favatar-tivi-topic.jpg?alt=media&token=437db900-9538-4f53-bc0b-5bd4e7c280db",
                        icon: "https://firebasestorage.googleapis.com/v0/b/social-network-66b92.appspot.com/o/app_images%2Fsmartwatch-pngrepo-com.png?alt=media&token=a39c263d-0432-45b6-a05b-af7a958a34b4",
                        lock: false
                )
        )
    }
}
