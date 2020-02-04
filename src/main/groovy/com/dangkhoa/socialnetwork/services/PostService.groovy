package com.dangkhoa.socialnetwork.services

import com.dangkhoa.socialnetwork.common.Constant
import com.dangkhoa.socialnetwork.entities.post.Post
import com.dangkhoa.socialnetwork.entities.post.PostResponse
import com.dangkhoa.socialnetwork.entities.user.UserResponse
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

    Post findByPostId(String id) {
        return postRepository.findByPostId(id)
    }

    PostResponse getByPostId(String id) {
        Post post = findByPostId(id)
        UserResponse userResponse = userService.getByUserId(post.userId)
        PostResponse postResponse = postMapperFacade.map(post, PostResponse.class)
        postResponse.owner = userResponse
        return postResponse
    }

    List<Post> findByUserId(String userId, Integer page, Integer limit) {
        return postRepository.findByUserId(userId, page ?: 1, limit ?: Constant.DEFAULT_PAGE_SIZE)
    }

    List<Post> findByUserId(String userId, Integer page) {
        return findByUserId(userId, page, Constant.DEFAULT_PAGE_SIZE)
    }

    List<PostResponse> getByUserId(String userId, Integer page) {
        List<Post> posts = findByUserId(userId, page)
        UserResponse userResponse = userService.getByUserId(userId)
        List<PostResponse> postResponses = postMapperFacade.mapAsList(posts, PostResponse.class)
        postResponses.each { it.owner = userResponse }
        return postResponses
    }

    Post save(Post post) {
        if (StringUtils.isBlank(post.postId)) {
            post.postId = new ObjectId().toString()
            post.createdAt = new Date().getTime()
            post.updatedAt = new Date().getTime()
            return postRepository.save(post)
        }
        Post existPost = findByPostId(post.postId)
        if (existPost == null)
            throw new InValidObjectException("post not found")

        setUpdateValue(existPost, post)
        return postRepository.save(existPost)
    }

    Long remove(String id) {
        return postRepository.remove(id)
    }

    @SuppressWarnings("Duplicates")
    static void setUpdateValue(Post oldPost, Post newPost) {
        oldPost.content = StringUtils.isBlank(newPost.content) ? oldPost.content : newPost.content
        oldPost.postType = StringUtils.isBlank(newPost.postType) ? oldPost.postType : newPost.postType
        oldPost.numberLike = newPost.numberLike == null ? oldPost.numberLike : newPost.numberLike
        oldPost.numberDislike = newPost.numberDislike == null ? oldPost.numberDislike : newPost.numberDislike
        oldPost.updatedAt = new Date().getTime()
    }
}
