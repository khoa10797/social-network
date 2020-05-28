package com.dangkhoa.socialnetwork.event.listener

import com.dangkhoa.socialnetwork.entities.notification.Notification
import com.dangkhoa.socialnetwork.entities.post.Post
import com.dangkhoa.socialnetwork.entities.user.User
import com.dangkhoa.socialnetwork.entities.user.UserResponse
import com.dangkhoa.socialnetwork.event.event.AddCommentEvent
import com.dangkhoa.socialnetwork.services.*
import org.apache.commons.collections4.CollectionUtils
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
        User ownerPost = userService.findByUserId(post.userOwnerId)
        UserResponse publisher = userService.getByUserId(event.userOwnerId)
        List<String> subscriberIds = userPostService.findUserIdByPostId(event.postId)
        List<User> subscribers = userService.findByUserId(subscriberIds)

        List<Notification> notifications = notificationService.findByPostIdAndPublisherId(post.postId, publisher.userId)
        if (CollectionUtils.isNotEmpty(notifications)) {

        }

        subscribers.each { user ->
            if (ownerPost.userId != publisher.userId) {
                Notification notification = new Notification(
                        publisherId: publisher.userId,
                        subscriberId: user.userId,
                        postId: event.postId,
                        type: Notification.Type.ADD_COMMENT,
                        message: "${publisher.name} đã bình luận về bài viết của ${ownerPost.name}",
                        isSeen: false,
                        publisher: publisher,
                        createdAt: new Date().getTime()
                )
                notificationService.save(notification)
            }
        }
    }
}
