package com.dangkhoa.socialnetwork.services

import com.dangkhoa.socialnetwork.base.SocialNetworkContext
import com.dangkhoa.socialnetwork.common.Constant
import com.dangkhoa.socialnetwork.entities.post.Post
import com.dangkhoa.socialnetwork.entities.post.PostResponse
import com.dangkhoa.socialnetwork.entities.user.UserAccount
import com.dangkhoa.socialnetwork.entities.user.UserResponse
import com.dangkhoa.socialnetwork.entities.userpost.UserPost
import com.dangkhoa.socialnetwork.exception.InValidObjectException
import com.dangkhoa.socialnetwork.repositories.PostRepository
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
            postResponse.userStatus = userPost.userStatus
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
        List<UserResponse> userResponses = userService.getByUserId(userOwnerIds)

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
                }
            }
        }
    }

    Post save(Post post) {
        if (StringUtils.isBlank(post.postId)) {
            post.postId = new ObjectId().toString()
            long time = new Date().getTime()
            post.createdAt = time
            post.updatedAt = time
            post.numberComment = 0
            post.numberComment = 0
            return postRepository.save(post)
        }
        Post existPost = findByPostId(post.postId)
        if (existPost == null)
            throw new InValidObjectException("post not found")

        setUpdateValue(existPost, post)
        return postRepository.save(existPost)
    }

    Long remove(String id) {
        userPostService.removeByPostId(id)
        commentService.removeByPostId(id)
        return postRepository.remove(id)
    }

    static void setUpdateValue(Post oldPost, Post newPost) {
        oldPost.content = StringUtils.isBlank(newPost.content) ? oldPost.content : newPost.content
        oldPost.postType = StringUtils.isBlank(newPost.postType) ? oldPost.postType : newPost.postType
        oldPost.numberComment = newPost.numberComment == null ? oldPost.numberComment : newPost.numberComment
        oldPost.numberLike = newPost.numberLike == null ? oldPost.numberLike : newPost.numberLike
        oldPost.numberDislike = newPost.numberDislike == null ? oldPost.numberDislike : newPost.numberDislike
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
}
