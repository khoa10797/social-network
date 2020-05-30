package com.dangkhoa.socialnetwork.event.publisher


import com.dangkhoa.socialnetwork.event.event.PostEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class PostEventPublisher {

    @Autowired
    ApplicationEventPublisher applicationEventPublisher

    void likePost(String publisherId, String postId) {
        PostEvent event = new PostEvent(this, publisherId, postId, PostEvent.Type.LIKE)
        applicationEventPublisher.publishEvent(event)
    }
}
