package com.dangkhoa.socialnetwork.event.listener

import com.dangkhoa.socialnetwork.entities.mongo.notification.Notification
import com.dangkhoa.socialnetwork.entities.mongo.post.Post
import com.dangkhoa.socialnetwork.entities.mongo.user.UserResponse
import com.dangkhoa.socialnetwork.event.event.PostEvent
import com.dangkhoa.socialnetwork.mongo.services.NotificationService
import com.dangkhoa.socialnetwork.mongo.services.PostService
import com.dangkhoa.socialnetwork.mongo.services.UserPostService
import com.dangkhoa.socialnetwork.mongo.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class PostEventListener {

    @Autowired
    UserService userService
    @Autowired
    UserPostService userPostService
    @Autowired
    PostService postService
    @Autowired
    NotificationService notificationService

    @Async
    @EventListener
    void notification(PostEvent event) {
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
            case PostEvent.Type.LIKE:
                return Notification.Type.LIKE_POST
        }
        return ""
    }
}
