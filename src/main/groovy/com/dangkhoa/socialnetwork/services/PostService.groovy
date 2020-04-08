package com.dangkhoa.socialnetwork.services

import com.dangkhoa.socialnetwork.common.Constant
import com.dangkhoa.socialnetwork.entities.post.Post
import com.dangkhoa.socialnetwork.entities.post.PostResponse
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
        UserPost userPost = userPostService.findByUserIdAndPostId(userOwner.userId, postId)

        postResponse.userOwner = userOwner
        postResponse.userStatus = userPost.userStatus
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
        UserResponse userOwner = userService.getByUserId(userOwnerId)
        List<PostResponse> postResponses = postMapperFacade.mapAsList(posts, PostResponse.class)

        List<String> postIds = postResponses.collect { postResponse -> postResponse.postId }
        List<UserPost> userPosts = userPostService.findByUserIdAndPostIds(userOwnerId, postIds)

        postResponses.each { item ->
            item.userOwner = userOwner
            userPosts.each { userPost ->
                if (userPost.postId == item.postId) {
                    item.userStatus = userPost.userStatus
                }
            }
        }

        return postResponses
    }

    Post save(Post post) {
        if (StringUtils.isBlank(post.postId)) {
            post.postId = new ObjectId().toString()
            long time = new Date().getTime()
            post.createdAt = time
            post.updatedAt = time
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
}
