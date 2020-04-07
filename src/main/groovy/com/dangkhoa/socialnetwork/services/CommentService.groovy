package com.dangkhoa.socialnetwork.services

import com.dangkhoa.socialnetwork.common.Constant
import com.dangkhoa.socialnetwork.entities.comment.Comment
import com.dangkhoa.socialnetwork.entities.comment.CommentResponse
import com.dangkhoa.socialnetwork.entities.user.UserResponse
import com.dangkhoa.socialnetwork.exception.InValidObjectException
import com.dangkhoa.socialnetwork.repositories.CommentRepository
import ma.glasnost.orika.MapperFacade
import org.apache.commons.lang3.StringUtils
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CommentService {

    @Autowired
    MapperFacade commentMapperFacade
    @Autowired
    CommentRepository commentRepository
    @Autowired
    UserService userService
    @Autowired
    PostService postService
    @Autowired
    UserCommentService userCommentService

    Comment findByCommentId(String commentId) {
        return commentRepository.findByCommentId(commentId)
    }

    CommentResponse getByCommentId(String commentId) {
        Comment comment = findByCommentId(commentId)
        UserResponse userResponse = userService.getByUserId(comment.userId)
        CommentResponse commentResponse = commentMapperFacade.map(comment, CommentResponse.class)
        commentResponse.user = userResponse
        return commentResponse
    }

    List<Comment> findByPostId(String postId, Integer page, Integer limit) {
        return commentRepository.findByPostId(postId, page ?: 1, limit ?: Constant.DEFAULT_COMMENT_SIZE)
    }

    List<Comment> findByPostId(String postId, Integer page) {
        return findByPostId(postId, page, Constant.DEFAULT_COMMENT_SIZE)
    }

    List<CommentResponse> getByPostId(String postId, Integer page) {
        List<Comment> comments = findByPostId(postId, page)
        List<CommentResponse> commentResponses = commentMapperFacade.mapAsList(comments, CommentResponse.class)
        fillUserResponses(commentResponses)
        commentResponses.each { item ->
            item.childComments = getByCommentParentId(item.commentId)
        }

        return commentResponses
    }

    List<Comment> findByUserId(String userId, Integer page, Integer limit) {
        return commentRepository.findByUserId(userId, page ?: 1, limit ?: Constant.DEFAULT_COMMENT_SIZE)
    }

    List<Comment> findByUserId(String userId, Integer page) {
        return findByUserId(userId, page, Constant.DEFAULT_COMMENT_SIZE)
    }

    List<CommentResponse> getByUserId(String userId, Integer page) {
        List<Comment> comments = findByUserId(userId, page)
        UserResponse userResponse = userService.getByUserId(userId)
        List<CommentResponse> commentResponses = commentMapperFacade.mapAsList(comments, CommentResponse.class)
        commentResponses.each { it.user = userResponse }
        return commentResponses
    }

    Comment save(Comment comment) {
        if (StringUtils.isBlank(comment.commentId)) {
            return addComment(comment)
        }
        Comment existComment = findByCommentId(comment.commentId)
        if (existComment == null)
            throw new InValidObjectException("comment not found")

        setUpdateValue(existComment, comment)
        return commentRepository.save(existComment)
    }

    private Comment addComment(Comment comment) {
        comment.commentId = new ObjectId().toString()
        long time = new Date().getTime()
        comment.createdAt = time
        comment.updatedAt = time
        postService.updateNumberComment(comment.postId, 1)

        return commentRepository.save(comment)
    }

    Comment remove(String commentId) {
        Comment comment = commentRepository.findByCommentId(commentId)
        postService.updateNumberComment(comment.postId, -1)
        userCommentService.removeByCommentId(commentId)
        commentRepository.remove(commentId)

        return comment
    }

    static void setUpdateValue(Comment oldComment, Comment newComment) {
        oldComment.content = StringUtils.isBlank(newComment.content) ? oldComment.content : newComment.content
        oldComment.numberLike = newComment.numberLike == null ? oldComment.numberLike : newComment.numberLike
        oldComment.numberDislike = newComment.numberDislike == null ? oldComment.numberDislike : newComment.numberDislike
        oldComment.updatedAt = new Date().getTime()
    }

    Long countByPostId(String postId) {
        return commentRepository.countByPostId(postId)
    }

    List<Comment> findByCommentParentId(String commentParentId) {
        return commentRepository.findByCommentParentId(commentParentId)
    }

    List<CommentResponse> getByCommentParentId(String commentParentId) {
        List<Comment> comments = findByCommentParentId(commentParentId)
        List<CommentResponse> commentResponses = commentMapperFacade.mapAsList(comments, CommentResponse.class)
        fillUserResponses(commentResponses)
        return commentResponses
    }

    private void fillUserResponses(List<CommentResponse> commentResponses) {
        Set<String> userIds = commentResponses.collect { it.userId }.toSet()
        List<UserResponse> userResponses = userService.getByUserId(userIds.toList())
        commentResponses.each { commentResponse ->
            commentResponse.user = userResponses.find { it.userId == commentResponse.userId }
        }
    }

    Long removeByPostId(String postId) {
        return commentRepository.removeByPostId(postId)
    }

    void updateNumberLike(String commentId, Integer quantity) {
        commentRepository.updateNumberLike(commentId, quantity)
    }
}
