package com.dangkhoa.socialnetwork.services

import com.dangkhoa.socialnetwork.common.Constant
import com.dangkhoa.socialnetwork.entities.comment.Comment
import com.dangkhoa.socialnetwork.entities.comment.CommentResponse
import com.dangkhoa.socialnetwork.entities.user.UserResponse
import com.dangkhoa.socialnetwork.entities.usercomment.UserComment
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
        UserResponse userResponse = userService.getByUserId(comment.userOwnerId)
        CommentResponse commentResponse = commentMapperFacade.map(comment, CommentResponse.class)
        UserComment userComment = userCommentService.findByUserIdAndCommentId(userResponse.userId, commentId)

        commentResponse.userOwner = userResponse
        commentResponse.userStatus = userComment.userStatus
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
        fillUserOwner(commentResponses)
        fillUserStatus(commentResponses)
        commentResponses.each { item ->
            item.childComments = getByCommentParentId(item.commentId)
        }

        return commentResponses
    }

    List<Comment> findByUserOwnerId(String userOwnerId, Integer page, Integer limit) {
        return commentRepository.findByUserOwnerId(userOwnerId, page ?: 1, limit ?: Constant.DEFAULT_COMMENT_SIZE)
    }

    List<Comment> findByUserOwnerId(String userOwnerId, Integer page) {
        return findByUserOwnerId(userOwnerId, page, Constant.DEFAULT_COMMENT_SIZE)
    }

    List<CommentResponse> getByUserOwnerId(String userOwnerId, Integer page) {
        List<Comment> comments = findByUserOwnerId(userOwnerId, page)
        UserResponse userOwner = userService.getByUserId(userOwnerId)
        List<CommentResponse> commentResponses = commentMapperFacade.mapAsList(comments, CommentResponse.class)
        commentResponses.each { it.userOwner = userOwner }
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
        fillUserOwner(commentResponses)
        return commentResponses
    }

    private void fillUserOwner(List<CommentResponse> commentResponses) {
        Set<String> userOwnerIds = commentResponses.collect { it.userOwnerId }.toSet()
        List<UserResponse> userOwners = userService.getByUserId(userOwnerIds.toList())
        commentResponses.each { commentResponse ->
            commentResponse.userOwner = userOwners.find { it.userId == commentResponse.userOwnerId }
        }
    }

    private void fillUserStatus(List<CommentResponse> commentResponses) {
        List<String> commentIds = commentResponses.collect { it.commentId }
        commentResponses.each { commentResponse ->

        }
    }

    Long removeByPostId(String postId) {
        return commentRepository.removeByPostId(postId)
    }

    void updateNumberLike(String commentId, Integer quantity) {
        commentRepository.updateNumberLike(commentId, quantity)
    }
}
