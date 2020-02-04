package com.dangkhoa.socialnetwork.controller

import com.dangkhoa.socialnetwork.base.BaseController
import com.dangkhoa.socialnetwork.common.ResponseData
import com.dangkhoa.socialnetwork.entities.comment.Comment
import com.dangkhoa.socialnetwork.entities.comment.CommentRequest
import com.dangkhoa.socialnetwork.entities.comment.CommentResponse
import com.dangkhoa.socialnetwork.services.CommentService
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

    @GetMapping("/user/{userId}")
    ResponseEntity<ResponseData> findByUserId(@PathVariable String userId,
                                              @RequestParam(required = false) Integer page,
                                              @RequestParam(required = false) Integer pageSize) {
        List<CommentResponse> commentResponses = commentService.getByUserId(userId, page)
        ResponseData data = new ResponseData(
                statusCode: 200,
                meta: buildMetaResponse(page, pageSize),
                data: commentResponses
        )
        return new ResponseEntity<ResponseData>(data, HttpStatus.OK)
    }

    @PostMapping
    ResponseEntity add(@RequestBody @Valid CommentRequest commentRequest) {
        Comment comment = commentMapperFacade.map(commentRequest, Comment.class)
        commentService.save(comment)
        return new ResponseEntity(HttpStatus.CREATED)
    }

    @PutMapping("/{commentId}")
    ResponseEntity update(@PathVariable String commentId, @RequestBody @Valid CommentRequest commentRequest) {
        Comment comment = commentMapperFacade.map(commentRequest, Comment.class)
        comment.commentId = commentId
        commentService.save(comment)
        return new ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @DeleteMapping("/{commentId}")
    ResponseEntity remove(@PathVariable String commentId) {
        commentService.remove(commentId)
        return new ResponseEntity(HttpStatus.NO_CONTENT)
    }
}
