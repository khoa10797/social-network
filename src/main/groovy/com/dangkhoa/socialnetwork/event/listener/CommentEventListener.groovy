package com.dangkhoa.socialnetwork.event.listener

import com.dangkhoa.socialnetwork.entities.mongo.notification.Notification
import com.dangkhoa.socialnetwork.entities.mongo.post.Post
import com.dangkhoa.socialnetwork.entities.mongo.user.UserResponse
import com.dangkhoa.socialnetwork.event.event.CommentEvent
import com.dangkhoa.socialnetwork.mongo.services.NotificationService
import com.dangkhoa.socialnetwork.mongo.services.PostService
import com.dangkhoa.socialnetwork.mongo.services.UserCommentService
import com.dangkhoa.socialnetwork.mongo.services.UserPostService
import com.dangkhoa.socialnetwork.mongo.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class CommentEventListener {

    @Autowired
    UserPostService userPostService
    @Autowired
    UserCommentService userCommentService
    @Autowired
    UserService userService
    @Autowired
    PostService postService
    @Autowired
    NotificationService notificationService

    @Async
    @EventListener
    void notification(CommentEvent event) {
        Post post = postService.findByPostId(event.postId)
        UserResponse ownerPost = userService.getByUserId(post.userOwnerId)
        UserResponse publisher = userService.getByUserId(event.publisherId)

        List<String> subscriberIds = userPostService.findUserIdByPostId(event.postId)
        List<UserResponse> subscribers = userService.getByUserIds(subscriberIds)
        if (!subscriberIds.contains(ownerPost.userId)) {
            subscribers.add(ownerPost)
        }

        String notificationType = getNotificationType(event.type)
        subscribers.each { user ->
            if (user.userId != publisher.userId) {
                Notification notification = new Notification(
                        subscriberId: user.userId,
                        postId: event.postId,
                        type: notificationType,
                        isSeen: false,
                        publisher: publisher,
                        ownerPost: ownerPost,
                        createdAt: new Date().getTime()
                )
                notificationService.save(notification)
            }
        }
    }

    String getNotificationType(String type) {
        switch (type) {
            case CommentEvent.Type.ADD:
                return Notification.Type.ADD_COMMENT
        }
        return ""
    }
}
