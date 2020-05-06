package com.dangkhoa.socialnetwork.controller

import com.dangkhoa.socialnetwork.base.BaseController
import com.dangkhoa.socialnetwork.base.response.ResponseData
import com.dangkhoa.socialnetwork.base.response.ResponseError
import com.dangkhoa.socialnetwork.entities.comment.Comment
import com.dangkhoa.socialnetwork.entities.comment.CommentRequest
import com.dangkhoa.socialnetwork.entities.comment.CommentResponse
import com.dangkhoa.socialnetwork.entities.user.UserAccount
import com.dangkhoa.socialnetwork.entities.user.UserResponse
import com.dangkhoa.socialnetwork.entities.usercomment.UserComment
import com.dangkhoa.socialnetwork.services.CommentService
import com.dangkhoa.socialnetwork.services.UserCommentService
import com.dangkhoa.socialnetwork.services.UserService
import ma.glasnost.orika.MapperFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import javax.validation.Valid

@RestController
@RequestMapping("/comment")
class CommentController extends BaseController {

    @Autowired
    MapperFacade commentMapperFacade
    @Autowired
    CommentService commentService
    @Autowired
    UserService userService
    @Autowired
    UserCommentService userCommentService

    @GetMapping("/post/{postId}")
    ResponseEntity<ResponseData> findByPostId(@PathVariable String postId,
                                              @RequestParam(required = false) Integer page,
                                              @RequestParam(required = false) Integer pageSize) {
        List<CommentResponse> commentResponses = commentService.getByPostId(postId, page)
        ResponseData data = new ResponseData(
                statusCode: 200,
                meta: buildMetaResponse(page, pageSize),
                data: commentResponses
        )

        return new ResponseEntity<ResponseData>(data, HttpStatus.OK)
    }

    @GetMapping("/user/{userOwnerId}")
    ResponseEntity<ResponseData> findByUserOwnerId(@PathVariable String userOwnerId,
                                              @RequestParam(required = false) Integer page,
                                              @RequestParam(required = false) Integer pageSize) {
        List<CommentResponse> commentResponses = commentService.getByUserOwnerId(userOwnerId, page)
        ResponseData data = new ResponseData(
                statusCode: 200,
                meta: buildMetaResponse(page, pageSize),
                data: commentResponses
        )

        return new ResponseEntity<ResponseData>(data, HttpStatus.OK)
    }

    @PostMapping
    ResponseEntity<ResponseData> add(@RequestBody @Valid CommentRequest commentRequest) {
        Comment comment = commentMapperFacade.map(commentRequest, Comment.class)
        Comment insertedComment = commentService.save(comment)

        CommentResponse commentResponse = commentMapperFacade.map(insertedComment, CommentResponse.class)
        UserResponse userOwnerResponse = userService.getByUserId(commentResponse.userOwnerId)
        commentResponse.userOwner = userOwnerResponse
        ResponseData data = new ResponseData(data: commentResponse)

        return new ResponseEntity(data, HttpStatus.CREATED)
    }

    @PutMapping("/{commentId}")
    ResponseEntity<ResponseData> update(@PathVariable String commentId, @RequestBody @Valid CommentRequest commentRequest) {
        Comment comment = commentMapperFacade.map(commentRequest, Comment.class)
        comment.commentId = commentId

        UserAccount currentUser = getCurrentUser()
        if (currentUser.userId != comment.userOwnerId) {
            return new ResponseEntity(
                    new ResponseError(
                            statusCode: 403,
                            error: "Bạn không có quyền thực hiện chức năng này"
                    )
                    , HttpStatus.FORBIDDEN)
        }

        Comment updatedComment = commentService.save(comment)
        CommentResponse commentResponse = commentMapperFacade.map(updatedComment, CommentResponse.class)
        ResponseData data = new ResponseData(data: commentResponse)

        return new ResponseEntity(data, HttpStatus.OK)
    }

    @DeleteMapping("/{commentId}")
    ResponseEntity remove(@PathVariable String commentId) {
        Comment comment = commentService.findByCommentId(commentId)
        UserAccount currentUser = getCurrentUser()
        if (currentUser.userId != comment.userOwnerId) {
            return new ResponseEntity(
                    new ResponseError(
                            statusCode: 403,
                            error: "Bạn không có quyền thực hiện chức năng này"
                    )
                    , HttpStatus.FORBIDDEN)
        }

        commentService.remove(commentId)
        return new ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @PutMapping("/user_status")
    ResponseEntity<ResponseData> updateUserStatus(@RequestBody UserComment userComment) {
        UserComment savedUserComment = userCommentService.save(userComment)
        ResponseData data = new ResponseData(data: savedUserComment)
        return new ResponseEntity<>(data, HttpStatus.OK)
    }
}
