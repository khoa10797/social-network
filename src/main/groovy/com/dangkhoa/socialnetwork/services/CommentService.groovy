package com.dangkhoa.socialnetwork.services

import com.dangkhoa.socialnetwork.common.Constant
import com.dangkhoa.socialnetwork.entities.comment.Comment
import com.dangkhoa.socialnetwork.exception.InValidObjectException
import com.dangkhoa.socialnetwork.repositories.CommentRepository
import org.apache.commons.lang3.StringUtils
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CommentService {

    @Autowired
    CommentRepository commentRepository

    Comment findByCommentId(String commentId) {
        return commentRepository.findByCommentId(commentId)
    }

    List<Comment> findByPostId(String postId, Integer page, Integer limit) {
        return commentRepository.findByPostId(postId, page ?: 1, limit ?: Constant.DEFAULT_PAGE_SIZE)
    }

    List<Comment> findByPostId(String postId, Integer page) {
        return findByUserId(postId, page, Constant.DEFAULT_PAGE_SIZE)
    }

    List<Comment> findByUserId(String userId, Integer page, Integer limit) {
        return commentRepository.findByUserId(userId, page ?: 1, limit ?: Constant.DEFAULT_PAGE_SIZE)
    }

    List<Comment> findByUserId(String userId, Integer page) {
        return findByUserId(userId, page, Constant.DEFAULT_PAGE_SIZE)
    }

    Comment save(Comment comment) {
        if (StringUtils.isBlank(comment.commentId)) {
            comment.commentId = new ObjectId().toString()
            comment.createdAt = new Date().getTime()
            comment.updatedAt = new Date().getTime()
            return commentRepository.save(comment)
        }
        Comment existComment = findByCommentId(comment.commentId)
        if (existComment == null)
            throw new InValidObjectException("comment not found")

        setUpdateValue(existComment, comment)
        return commentRepository.save(existComment)
    }

    Long remove(String commentId) {
        return commentRepository.remove(commentId)
    }

    @SuppressWarnings("Duplicates")
    static void setUpdateValue(Comment oldComment, Comment newComment) {
        oldComment.content = StringUtils.isBlank(newComment.content) ? oldComment.content : newComment.content
        oldComment.numberLike = newComment.numberLike == null ? oldComment.numberLike : newComment.numberLike
        oldComment.numberDislike = newComment.numberDislike == null ? oldComment.numberDislike : newComment.numberDislike
        oldComment.updatedAt = new Date().getTime()
    }
}
