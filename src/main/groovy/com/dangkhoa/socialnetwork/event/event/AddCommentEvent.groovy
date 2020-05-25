package com.dangkhoa.socialnetwork.event.event

import org.springframework.context.ApplicationEvent

class AddCommentEvent extends ApplicationEvent {

    String userOwnerId
    String postId
    String commentId
    String parentId
    /**
     * Create a new {@code ApplicationEvent}.
     * @param source the object on which the event initially occurred or with
     * which the event is associated (never {@code null})
     */
    AddCommentEvent(Object source, String userOwnerId, String postId, String commentId, String parentId) {
        super(source)
        this.userOwnerId = userOwnerId
        this.postId = postId
        this.commentId = commentId
        this.parentId = parentId
    }
}
