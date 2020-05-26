package com.dangkhoa.socialnetwork.event.listener

import com.dangkhoa.socialnetwork.entities.notification.Notification
import com.dangkhoa.socialnetwork.entities.post.Post
import com.dangkhoa.socialnetwork.entities.user.User
import com.dangkhoa.socialnetwork.event.event.AddCommentEvent
import com.dangkhoa.socialnetwork.services.NotificationService
import com.dangkhoa.socialnetwork.services.PostService
import com.dangkhoa.socialnetwork.services.UserCommentService
import com.dangkhoa.socialnetwork.services.UserPostService
import com.dangkhoa.socialnetwork.services.UserService
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
        User ownerComment = userService.findByUserId(event.userOwnerId)

        List<String> userIds = userPostService.findUserIdByPostId(event.postId)
        List<User> userResponses = userService.findByUserId(userIds)
        userResponses.each { user ->
            if (ownerPost.userId != ownerComment.userId) {
                Notification notification = new Notification(
                        receiverId: user.userId,
                        postId: event.postId,
                        type: Notification.Type.ADD_COMMENT,
                        message: "${ownerComment.name} đã bình luận về bài viết của ${ownerPost.name}",
                        isSeen: false,
                        createdAt: new Date().getTime()
                )
                notificationService.save(notification)
            }
        }
    }
}
