package com.dangkhoa.socialnetwork.event.listener

import com.dangkhoa.socialnetwork.entities.notification.Notification
import com.dangkhoa.socialnetwork.entities.post.Post
import com.dangkhoa.socialnetwork.entities.user.UserResponse
import com.dangkhoa.socialnetwork.event.event.AddCommentEvent
import com.dangkhoa.socialnetwork.services.*
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
    void notification(AddCommentEvent event) {
        Post post = postService.findByPostId(event.postId)
        UserResponse ownerPost = userService.getByUserId(post.userOwnerId)
        UserResponse publisher = userService.getByUserId(event.userOwnerId)
        List<String> subscriberIds = userPostService.findUserIdByPostId(event.postId)

        List<UserResponse> subscribers = userService.getByUserIds(subscriberIds)
        if (!subscriberIds.contains(ownerPost.userId)) {
            subscribers.add(ownerPost)
        }

        subscribers.each { user ->
            if (user.userId != publisher.userId) {
                Notification notification = new Notification(
                        subscriberId: user.userId,
                        postId: event.postId,
                        type: Notification.Type.ADD_COMMENT,
                        isSeen: false,
                        publisher: publisher,
                        ownerPost: ownerPost,
                        createdAt: new Date().getTime()
                )
                notificationService.save(notification)
            }
        }
    }
}
