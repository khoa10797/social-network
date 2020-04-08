package com.dangkhoa.socialnetwork.services

import com.dangkhoa.socialnetwork.entities.usercomment.UserComment
import com.dangkhoa.socialnetwork.repositories.UserCommentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserCommentService {

    @Autowired
    UserCommentRepository userCommentRepository
    @Autowired
    CommentService commentService

    UserComment save(UserComment userComment) {
        UserComment existUserComment = userCommentRepository.findByUserIdAndCommentId(userComment.userId, userComment.commentId)

        Integer updatedNumberLike = UserComment.UserStatus.LIKE == userComment.userStatus ? 1 : -1
        commentService.updateNumberLike(userComment.commentId, updatedNumberLike)

        if (existUserComment == null) {
            return userCommentRepository.save(userComment)
        }

        existUserComment.userStatus = userComment.userStatus
        return userCommentRepository.save(existUserComment)
    }

    List<UserComment> findByUserIdAndCommentIds(String userId, List<String> commentIds) {
        return userCommentRepository.findByUserIdAndCommentIds(userId, commentIds)
    }

    UserComment findByUserIdAndCommentId(String userId, String commentId) {
        return userCommentRepository.findByUserIdAndCommentId(userId, commentId)
    }

    Long removeByCommentId(String commentId) {
        return userCommentRepository.removeByCommentId(commentId)
    }
}
