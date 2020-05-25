package com.dangkhoa.socialnetwork.event.publisher

import com.dangkhoa.socialnetwork.entities.comment.Comment
import com.dangkhoa.socialnetwork.event.event.AddCommentEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class CommentEventPublisher {

    @Autowired
    ApplicationEventPublisher applicationEventPublisher

    void addComment(Comment comment) {
        AddCommentEvent event = new AddCommentEvent(this, comment.userOwnerId, comment.postId, comment.commentId, comment.parentId)
        applicationEventPublisher.publishEvent(event)
    }
}
