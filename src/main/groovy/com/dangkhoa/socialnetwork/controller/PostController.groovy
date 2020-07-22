package com.dangkhoa.socialnetwork.controller

import com.dangkhoa.socialnetwork.base.BaseController
import com.dangkhoa.socialnetwork.base.response.BaseResponse
import com.dangkhoa.socialnetwork.base.response.ResponseData
import com.dangkhoa.socialnetwork.base.response.ResponseError
import com.dangkhoa.socialnetwork.elasticsearch.services.EsPostService
import com.dangkhoa.socialnetwork.entities.elasticsearch.EsPost
import com.dangkhoa.socialnetwork.entities.mongo.post.Post
import com.dangkhoa.socialnetwork.entities.mongo.post.PostRequest
import com.dangkhoa.socialnetwork.entities.mongo.post.PostResponse
import com.dangkhoa.socialnetwork.entities.mongo.topic.Topic
import com.dangkhoa.socialnetwork.entities.mongo.trendingpost.TrendingPost
import com.dangkhoa.socialnetwork.entities.mongo.user.UserAccount
import com.dangkhoa.socialnetwork.entities.mongo.user.UserResponse
import com.dangkhoa.socialnetwork.entities.mongo.userpost.UserPost
import com.dangkhoa.socialnetwork.event.publisher.PostEventPublisher
import com.dangkhoa.socialnetwork.mongo.services.PostService
import com.dangkhoa.socialnetwork.mongo.services.TopicService
import com.dangkhoa.socialnetwork.mongo.services.TrendingPostService
import com.dangkhoa.socialnetwork.mongo.services.UserPostService
import com.dangkhoa.socialnetwork.mongo.services.UserService
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
    EsPostService esPostService
    @Autowired
    UserService userService
    @Autowired
    UserPostService userPostService
    @Autowired
    TrendingPostService trendingPostService
    @Autowired
    PostEventPublisher postEventPublisher
    @Autowired
    TopicService topicService

    @GetMapping("/suggest")
    ResponseEntity<BaseResponse> getSuggestPost() {
        TrendingPost trendingPost = trendingPostService.getLast()
        List<String> trendingPostIds = postService.getByPostIds(trendingPost.postIds).collect { it.postId }
        List<String> newPostIds = postService.findNewPost(50).collect { it.postId }

        List<String> postIds = (trendingPostIds + newPostIds).toSet().toList()
        List<PostResponse> postResponses = postService.getByPostIds(postIds)
        Collections.shuffle(postResponses)
        if (postResponses.size() > 20) {
            postResponses = postResponses.subList(0, 49)
        }

        ResponseData data = new ResponseData(
                statusCode: 200,
                data: postResponses
        )

        return new ResponseEntity<>(data, HttpStatus.OK)
    }

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
        Topic topic = topicService.findByTopicId(postRequest.topicId)
        if (topic.lock) {
            return new ResponseEntity<>(new ResponseError(message: "Chủ đề đã bị khoá"), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Post post = postMapperFacade.map(postRequest, Post.class)
        Post insertedPost = postService.save(post)
        EsPost esPost = postMapperFacade.map(insertedPost, EsPost.class)
        esPostService.indexDocument(esPost)

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
        EsPost esPost = postMapperFacade.map(updatedPost, EsPost.class)
        esPostService.update(id, esPost)

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
        esPostService.deleteById(id);

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

    @GetMapping("/filter")
    ResponseEntity<ResponseData> filter(EsPost esPost) {
        List<EsPost> esPosts = esPostService.filter(esPost)
        List<String> postIds = esPosts.collect { item -> item.postId }
        List<PostResponse> postResponses = postService.getByPostIds(postIds)

        ResponseData data = new ResponseData(
                statusCode: 200,
                data: postResponses
        )

        return new ResponseEntity<>(data, HttpStatus.OK)
    }

    @GetMapping("/count_by_user/{userOwnerId}")
    ResponseEntity<ResponseData> countByUser(@PathVariable("userOwnerId") String userOwnerId) {
        long numberPostByUserOwner = postService.countByUserOwnerId(userOwnerId)

        ResponseData data = new ResponseData(
                statusCode: 200,
                data: numberPostByUserOwner
        )

        return new ResponseEntity<>(data, HttpStatus.OK)
    }

    @PostMapping("/bookmark")
    ResponseEntity<ResponseData> bookmarkPost(@RequestBody UserPost userPost) {
        UserPost savedUserPost = userPostService.save(userPost)
        ResponseData data = new ResponseData(
                statusCode: 200,
                data: savedUserPost
        )

        return new ResponseEntity<>(data, HttpStatus.OK)
    }

    @GetMapping("/bookmark/user/{userId}")
    ResponseEntity<ResponseData> getBookmarkPost(@PathVariable("userId") String userId) {
        List<PostResponse> postResponses = postService.getBookmarkPost(userId)

        ResponseData data = new ResponseData(
                statusCode: 200,
                data: postResponses
        )

        return new ResponseEntity<>(data, HttpStatus.OK)
    }

    @PutMapping("/lock/{postId}")
    ResponseEntity<BaseResponse> lockTopic(@PathVariable String postId, @RequestParam("lock") boolean lock) {
        postService.updateLock(postId, lock)
        PostResponse postResponse = postService.getByPostId(postId)

        ResponseData data = new ResponseData(
                statusCode: 200,
                data: postResponse
        )
        return new ResponseEntity<BaseResponse>(data, HttpStatus.OK)
    }
}
