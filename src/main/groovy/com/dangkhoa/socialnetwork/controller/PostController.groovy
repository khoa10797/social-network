package com.dangkhoa.socialnetwork.controller

import com.dangkhoa.socialnetwork.base.BaseController
import com.dangkhoa.socialnetwork.common.ResponseData
import com.dangkhoa.socialnetwork.entities.post.Post
import com.dangkhoa.socialnetwork.entities.post.PostRequest
import com.dangkhoa.socialnetwork.entities.post.PostResponse
import com.dangkhoa.socialnetwork.services.PostService
import com.dangkhoa.socialnetwork.services.UserService
import ma.glasnost.orika.MapperFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import javax.validation.Valid

@RestController
@RequestMapping("/post")
class PostController extends BaseController {

    @Autowired
    MapperFacade postMapperFacade
    @Autowired
    PostService postService
    @Autowired
    UserService userService

    @GetMapping("/{id}")
    ResponseEntity<ResponseData> findById(@PathVariable String id) {
        PostResponse postResponse = postService.getByPostId(id)
        ResponseData data = new ResponseData(
                statusCode: 200,
                data: postResponse
        )
        return new ResponseEntity<>(data, HttpStatus.OK)
    }

    @GetMapping("/user/{userId}")
    ResponseEntity<ResponseData> findByUserId(@PathVariable String userId,
                                              @RequestParam(required = false) Integer page,
                                              @RequestParam(required = false) Integer pageSize) {
        List<PostResponse> postResponses = postService.getByUserId(userId, page)
        ResponseData data = new ResponseData(
                statusCode: 200,
                meta: buildMetaResponse(page, pageSize),
                data: postResponses
        )
        return new ResponseEntity<>(data, HttpStatus.OK)
    }

    @PostMapping
    ResponseEntity add(@RequestBody @Valid PostRequest postRequest) {
        Post post = postMapperFacade.map(postRequest, Post.class)
        postService.save(post)
        return new ResponseEntity(HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    ResponseEntity update(@PathVariable String id, @RequestBody @Valid PostRequest postRequest) {
        Post post = postMapperFacade.map(postRequest, Post.class)
        post.postId = id
        postService.save(post)
        return new ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @DeleteMapping("/{id}")
    ResponseEntity remove(@PathVariable String id) {
        postService.remove(id)
        return new ResponseEntity(HttpStatus.NO_CONTENT)
    }
}
