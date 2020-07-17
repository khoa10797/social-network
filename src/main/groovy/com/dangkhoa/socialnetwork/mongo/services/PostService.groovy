package com.dangkhoa.socialnetwork.mongo.services

import com.dangkhoa.socialnetwork.base.SocialNetworkContext
import com.dangkhoa.socialnetwork.common.Constant
import com.dangkhoa.socialnetwork.entities.mongo.post.Post
import com.dangkhoa.socialnetwork.entities.mongo.post.PostResponse
import com.dangkhoa.socialnetwork.entities.mongo.user.UserAccount
import com.dangkhoa.socialnetwork.entities.mongo.user.UserResponse
import com.dangkhoa.socialnetwork.entities.mongo.userpost.UserPost
import com.dangkhoa.socialnetwork.exception.InValidObjectException
import com.dangkhoa.socialnetwork.mongo.repositories.PostRepository
import ma.glasnost.orika.MapperFacade
import org.apache.commons.lang3.StringUtils
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PostService {

    @Autowired
    SocialNetworkContext socialNetworkContext
    @Autowired
    MapperFacade postMapperFacade
    @Autowired
    PostRepository postRepository
    @Autowired
    UserService userService
    @Autowired
    UserPostService userPostService
    @Autowired
    CommentService commentService
    @Autowired
    TopicService topicService

    Post findByPostId(String id) {
        return postRepository.findByPostId(id)
    }

    PostResponse getByPostId(String postId) {
        Post post = findByPostId(postId)
        UserResponse userOwner = userService.getByUserId(post.userOwnerId)
        PostResponse postResponse = postMapperFacade.map(post, PostResponse.class)

        UserAccount currentUser = socialNetworkContext.getCurrentUser()
        if (currentUser != null) {
            UserPost userPost = userPostService.findByUserIdAndPostId(currentUser.userId, postId)
            if (userPost != null) {
                postResponse.userStatus = userPost.userStatus
            }
        }

        postResponse.userOwner = userOwner
        return postResponse
    }

    List<Post> findByUserOwnerId(String userOwnerId, Integer page, Integer limit) {
        return postRepository.findByUserOwnerId(userOwnerId, page ?: 1, limit ?: Constant.DEFAULT_PAGE_SIZE)
    }

    List<Post> findByUserOwnerId(String userOwnerId, Integer page) {
        return findByUserOwnerId(userOwnerId, page, Constant.DEFAULT_PAGE_SIZE)
    }

    List<PostResponse> getByUserOwnerId(String userOwnerId, Integer page) {
        List<Post> posts = findByUserOwnerId(userOwnerId, page)
        List<PostResponse> postResponses = postMapperFacade.mapAsList(posts, PostResponse.class)
        fillUserOwner(postResponses)
        fillUserStatus(postResponses)

        return postResponses
    }

    private void fillUserOwner(List<PostResponse> postResponses) {
        List<String> userOwnerIds = postResponses.collect { postResponse -> postResponse.userOwnerId }
        List<UserResponse> userResponses = userService.getByUserIds(userOwnerIds)

        postResponses.each { item ->
            item.userOwner = userResponses.find { it.userId == item.userOwnerId }
        }
    }

    private void fillUserStatus(List<PostResponse> postResponses) {
        List<String> postIds = postResponses.collect { it.postId }
        UserAccount currentUser = socialNetworkContext.getCurrentUser()
        if (currentUser == null) {
            return
        }

        List<UserPost> userPosts = userPostService.findByUserIdAndPostIds(currentUser.userId, postIds)
        postResponses.each { postResponse ->
            userPosts.each { userPost ->
                if (postResponse.postId == userPost.postId) {
                    postResponse.userStatus = userPost.userStatus
                    postResponse.bookmark = userPost.bookmark
                }
            }
        }
    }

    Post save(Post post) {
        if (StringUtils.isBlank(post.postId)) {
            return addPost(post)
        }
        Post existPost = findByPostId(post.postId)
        if (existPost == null)
            throw new InValidObjectException("post not found")

        setUpdateValue(existPost, post)
        return postRepository.save(existPost)
    }

    private Post addPost(Post post) {
        post.postId = new ObjectId().toString()
        long time = new Date().getTime()
        post.createdAt = time
        post.updatedAt = time
        post.numberComment = 0
        post.numberLike = 0
        post.isNew = true
        if (StringUtils.isNotBlank(post.topicId)) {
            topicService.updateNumberPost(post.topicId, 1)
        }

        return postRepository.save(post)
    }

    Long remove(String id) {
        Post post = findByPostId(id)
        if (StringUtils.isNotBlank()) {
            topicService.updateNumberPost(post.topicId, -1)
        }

        userPostService.removeByPostId(id)
        commentService.removeByPostId(id)
        return postRepository.remove(id)
    }

    static void setUpdateValue(Post oldPost, Post newPost) {
        oldPost.content = StringUtils.isBlank(newPost.content) ? oldPost.content : newPost.content
        oldPost.numberComment = newPost.numberComment == null ? oldPost.numberComment : newPost.numberComment
        oldPost.numberLike = newPost.numberLike == null ? oldPost.numberLike : newPost.numberLike
        oldPost.numberDislike = newPost.numberDislike == null ? oldPost.numberDislike : newPost.numberDislike
        oldPost.images = newPost.images == null ? oldPost.images : newPost.images
        oldPost.updatedAt = new Date().getTime()
    }

    void updateNumberComment(String postId, Integer quantity) {
        Post post = postRepository.findByPostId(postId)
        if (post.numberComment <= 0 && quantity < 0) {
            return
        }
        postRepository.updateNumberComment(postId, quantity)
    }

    void updateNumberLike(String postId, Integer quantity) {
        Post post = postRepository.findByPostId(postId)
        if (post.numberLike <= 0 && quantity < 0) {
            return
        }
        postRepository.updateNumberLike(postId, quantity)
    }

    List<Post> findTopByNumberLike(Integer limit, boolean desc) {
        return postRepository.findTopByNumberLike(limit, desc);
    }

    List<Post> findByPostIds(List<String> postIds) {
        return postRepository.findByPostIds(postIds)
    }

    List<PostResponse> getByPostIds(List<String> postIds) {
        List<Post> posts = findByPostIds(postIds)
        List<PostResponse> postResponses = postMapperFacade.mapAsList(posts, PostResponse.class)
        fillUserOwner(postResponses)
        fillUserStatus(postResponses)

        return postResponses
    }

    List<Post> findByTopicId(String topicId, Integer page, Integer limit) {
        return postRepository.findByTopicId(topicId, page ?: 1, limit ?: Constant.DEFAULT_PAGE_SIZE)
    }

    List<PostResponse> getByTopicId(String topicId, Integer page, Integer limit) {
        List<Post> posts = findByTopicId(topicId, page, limit)
        List<PostResponse> postResponses = postMapperFacade.mapAsList(posts, PostResponse.class)
        fillUserOwner(postResponses)
        fillUserStatus(postResponses)

        return postResponses
    }

    Long countByUserOwnerId(String userId) {
        return postRepository.countByUserOwnerId(userId)
    }

    List<PostResponse> getBookmarkPost(String userId) {
        List<UserPost> userPosts = userPostService.findBookmarkByUserId(userId)
        List<String> postIds = userPosts.collect { it.postId }
        List<PostResponse> postResponses = getByPostIds(postIds)
        postResponses.each { item -> item.bookmark = true }

        return postResponses
    }

    List<Post> findNewPost() {
        return postRepository.findNewPost()
    }

    Long convertNewPostToOld(List<String> postIds) {
        return postRepository.convertNewPostToOld(postIds)
    }

    List<Post> findNewPost(Integer limit) {
        return postRepository.findNewPost(limit)
    }

    List<PostResponse> getNewPost(Integer limit) {
        List<Post> posts = postRepository.findNewPost(limit)
        List<PostResponse> postResponses = postMapperFacade.mapAsList(posts, PostResponse.class)
        fillUserOwner(postResponses)
        fillUserStatus(postResponses)

        return postResponses
    }

    Post updateLock(String postId, Boolean lock) {
        Post post = findByPostId(postId)
        post.lock = lock
        return postRepository.save(post)
    }
}
