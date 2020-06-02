package com.dangkhoa.socialnetwork.event.publisher

import com.dangkhoa.socialnetwork.entities.mongo.comment.Comment
import com.dangkhoa.socialnetwork.event.event.CommentEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class CommentEventPublisher {

    @Autowired
    ApplicationEventPublisher applicationEventPublisher

    void addComment(Comment comment) {
        CommentEvent event = new CommentEvent(this, comment.userOwnerId, comment.postId, comment.commentId, comment.parentId, CommentEvent.Type.ADD)
        applicationEventPublisher.publishEvent(event)
    }
}
