package com.dangkhoa.socialnetwork.event.event

import org.springframework.context.ApplicationEvent

class CommentEvent extends ApplicationEvent {

    String publisherId
    String postId
    String commentId
    String parentId
    String type

    /**
     * Create a new {@code ApplicationEvent}.
     * @param source the object on which the event initially occurred or with
     * which the event is associated (never {@code null})
     */
    CommentEvent(Object source, String publisherId, String postId, String commentId, String parentId, String type) {
        super(source)
        this.publisherId = publisherId
        this.postId = postId
        this.commentId = commentId
        this.parentId = parentId
        this.type = type
    }

    static class Type {
        public static final LIKE = "LIKE"
        public static final ADD = "ADD"
    }
}
