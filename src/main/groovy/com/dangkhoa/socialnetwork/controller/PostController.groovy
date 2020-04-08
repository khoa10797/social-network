package com.dangkhoa.socialnetwork.controller

import com.dangkhoa.socialnetwork.base.BaseController
import com.dangkhoa.socialnetwork.common.ResponseData
import com.dangkhoa.socialnetwork.entities.post.Post
import com.dangkhoa.socialnetwork.entities.post.PostRequest
import com.dangkhoa.socialnetwork.entities.post.PostResponse
import com.dangkhoa.socialnetwork.entities.user.UserResponse
import com.dangkhoa.socialnetwork.entities.userpost.UserPost
import com.dangkhoa.socialnetwork.services.PostService
import com.dangkhoa.socialnetwork.services.UserPostService
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
    @Autowired
    UserPostService userPostService

    @GetMapping("/{id}")
    ResponseEntity<ResponseData> findById(@PathVariable String id) {
        PostResponse postResponse = postService.getByPostId(id)
        ResponseData data = new ResponseData(
                statusCode: 200,
                data: postResponse
        )

        return new ResponseEntity<>(data, HttpStatus.OK)
    }

    @GetMapping("/user/{userOwnerId}")
    ResponseEntity<ResponseData> findByUserOwnerId(@PathVariable String userOwnerId,
                                              @RequestParam(required = false) Integer page,
                                              @RequestParam(required = false) Integer pageSize) {
        List<PostResponse> postResponses = postService.getByUserOwnerId(userOwnerId, page)
        ResponseData data = new ResponseData(
                statusCode: 200,
                meta: buildMetaResponse(page, pageSize),
                data: postResponses
        )

        return new ResponseEntity<>(data, HttpStatus.OK)
    }

    @PostMapping
    ResponseEntity<ResponseData> add(@RequestBody @Valid PostRequest postRequest) {
        Post post = postMapperFacade.map(postRequest, Post.class)
        Post insertedPost = postService.save(post)

        PostResponse postResponse = postMapperFacade.map(insertedPost, PostResponse.class)
        UserResponse userOwner = userService.getByUserId(postResponse.userOwnerId)
        postResponse.userOwner = userOwner
        ResponseData data = new ResponseData(data: postResponse)

        return new ResponseEntity(data, HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseData> update(@PathVariable String id, @RequestBody @Valid PostRequest postRequest) {
        Post post = postMapperFacade.map(postRequest, Post.class)
        post.postId = id
        Post updatedPost = postService.save(post)
        PostResponse postResponse = postMapperFacade.map(updatedPost, PostResponse.class)
        ResponseData data = new ResponseData(data: postResponse)

        return new ResponseEntity(data, HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    ResponseEntity remove(@PathVariable String id) {
        postService.remove(id)
        return new ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @PutMapping("/user_status")
    ResponseEntity<ResponseData> updateUserStatus(@RequestBody UserPost userPost) {
        UserPost savedUserPost = userPostService.save(userPost)
        ResponseData data = new ResponseData(data: savedUserPost)
        return new ResponseEntity<>(data, HttpStatus.OK)
    }
}
