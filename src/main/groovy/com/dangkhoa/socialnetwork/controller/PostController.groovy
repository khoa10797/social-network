package com.dangkhoa.socialnetwork.controller

import com.dangkhoa.socialnetwork.base.BaseController
import com.dangkhoa.socialnetwork.base.response.BaseResponse
import com.dangkhoa.socialnetwork.base.response.ResponseData
import com.dangkhoa.socialnetwork.base.response.ResponseError
import com.dangkhoa.socialnetwork.entities.post.Post
import com.dangkhoa.socialnetwork.entities.post.PostRequest
import com.dangkhoa.socialnetwork.entities.post.PostResponse
import com.dangkhoa.socialnetwork.entities.trendingpost.TrendingPost
import com.dangkhoa.socialnetwork.entities.user.UserAccount
import com.dangkhoa.socialnetwork.entities.user.UserResponse
import com.dangkhoa.socialnetwork.entities.userpost.UserPost
import com.dangkhoa.socialnetwork.event.publisher.PostEventPublisher
import com.dangkhoa.socialnetwork.services.PostService
import com.dangkhoa.socialnetwork.services.TrendingPostService
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
    @Autowired
    TrendingPostService trendingPostService
    @Autowired
    PostEventPublisher postEventPublisher

    @GetMapping("/trending")
    ResponseEntity<BaseResponse> getTrendingPost() {
        TrendingPost trendingPost = trendingPostService.getLast()
        List<PostResponse> postResponses = postService.getByPostIds(trendingPost.postIds)
        ResponseData data = new ResponseData(
                statusCode: 200,
                data: postResponses
        )

        return new ResponseEntity<>(data, HttpStatus.OK)
    }

    @GetMapping("/{id}")
    ResponseEntity<BaseResponse> findById(@PathVariable String id) {
        PostResponse postResponse = postService.getByPostId(id)
        ResponseData data = new ResponseData(
                statusCode: 200,
                data: postResponse
        )

        return new ResponseEntity<>(data, HttpStatus.OK)
    }

    @GetMapping("/user/{userOwnerId}")
    ResponseEntity<BaseResponse> findByUserOwnerId(@PathVariable String userOwnerId,
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
    ResponseEntity<BaseResponse> add(@RequestBody @Valid PostRequest postRequest) {
        Post post = postMapperFacade.map(postRequest, Post.class)
        Post insertedPost = postService.save(post)

        PostResponse postResponse = postMapperFacade.map(insertedPost, PostResponse.class)
        UserResponse userOwner = userService.getByUserId(postResponse.userOwnerId)
        postResponse.userOwner = userOwner
        ResponseData data = new ResponseData(data: postResponse)

        return new ResponseEntity(data, HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    ResponseEntity<BaseResponse> update(@PathVariable String id, @RequestBody @Valid PostRequest postRequest) {
        Post post = postMapperFacade.map(postRequest, Post.class)
        post.postId = id

        UserAccount currentUser = getCurrentUser()
        if (currentUser.userId != post.userOwnerId) {
            return new ResponseEntity(
                    new ResponseError(
                            statusCode: 403,
                            error: "Bạn không có quyền thực hiện chức năng này"
                    )
                    , HttpStatus.FORBIDDEN)
        }

        Post updatedPost = postService.save(post)
        PostResponse postResponse = postMapperFacade.map(updatedPost, PostResponse.class)
        ResponseData data = new ResponseData(data: postResponse)

        return new ResponseEntity(data, HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    ResponseEntity remove(@PathVariable String id) {
        UserAccount currentUser = getCurrentUser()
        Post post = postService.findByPostId(id)

        if (post.userOwnerId != currentUser.userId) {
            return new ResponseEntity(
                    new ResponseError(
                            statusCode: 403,
                            error: "Bạn không có quyền thực hiện chức năng này"
                    )
                    , HttpStatus.FORBIDDEN)
        }

        postService.remove(id)
        return new ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @PutMapping("/user_status")
    ResponseEntity<BaseResponse> updateUserStatus(@RequestBody UserPost userPost) {
        UserPost savedUserPost = userPostService.save(userPost)
        ResponseData data = new ResponseData(data: savedUserPost)
        postEventPublisher.likePost(userPost.userId, userPost.postId)

        return new ResponseEntity<>(data, HttpStatus.OK)
    }

    @GetMapping("/topic/{topicId}")
    ResponseEntity<BaseResponse> getByTopicId(@PathVariable String topicId,
                                              @RequestParam(required = false) Integer page,
                                              @RequestParam(required = false) Integer pageSize) {
        List<PostResponse> postResponses = postService.getByTopicId(topicId, page, pageSize)
        ResponseData data = new ResponseData(
                statusCode: 200,
                meta: buildMetaResponse(page, pageSize),
                data: postResponses
        )

        return new ResponseEntity<>(data, HttpStatus.OK)
    }
}
