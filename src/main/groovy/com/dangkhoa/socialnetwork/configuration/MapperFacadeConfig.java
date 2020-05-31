package com.dangkhoa.socialnetwork.configuration;

import com.dangkhoa.socialnetwork.entities.comment.Comment;
import com.dangkhoa.socialnetwork.entities.comment.CommentRequest;
import com.dangkhoa.socialnetwork.entities.comment.CommentResponse;
import com.dangkhoa.socialnetwork.entities.post.Post;
import com.dangkhoa.socialnetwork.entities.post.PostRequest;
import com.dangkhoa.socialnetwork.entities.post.PostResponse;
import com.dangkhoa.socialnetwork.entities.topic.Topic;
import com.dangkhoa.socialnetwork.entities.topic.TopicRequest;
import com.dangkhoa.socialnetwork.entities.topic.TopicResponse;
import com.dangkhoa.socialnetwork.entities.user.User;
import com.dangkhoa.socialnetwork.entities.user.UserRequest;
import com.dangkhoa.socialnetwork.entities.user.UserResponse;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperFacadeConfig {

    @Bean
    public MapperFacade userMapperFacade() {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        factory.classMap(User.class, UserResponse.class)
                .field("userId", "userId")
                .field("name", "name")
                .field("age", "age")
                .field("sex", "sex")
                .field("userName", "userName")
                .field("email", "email")
                .field("avatar", "avatar")
                .field("roleGroupIds", "roleGroupIds")
                .field("intro", "intro")
                .field("description", "description")
                .field("createdAt", "createdAt")
                .field("updatedAt", "updatedAt")
                .register();

        factory.classMap(UserRequest.class, User.class)
                .field("name", "name")
                .field("age", "age")
                .field("sex", "sex")
                .field("userName", "userName")
                .field("email", "email")
                .field("password", "password")
                .field("roleGroupIds", "roleGroupIds")
                .field("avatar", "avatar")
                .field("intro", "intro")
                .field("description", "description")
                .register();

        return factory.getMapperFacade();
    }

    @Bean
    public MapperFacade postMapperFacade() {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        factory.classMap(Post.class, PostResponse.class)
                .field("postId", "postId")
                .field("userOwnerId", "userOwnerId")
                .field("topicId", "topicId")
                .field("title", "title")
                .field("content", "content")
                .field("images", "images")
                .field("numberLike", "numberLike")
                .field("numberDislike", "numberDislike")
                .field("numberComment", "numberComment")
                .field("createdAt", "createdAt")
                .field("updatedAt", "updatedAt")
                .register();

        factory.classMap(PostRequest.class, Post.class)
                .field("userOwnerId", "userOwnerId")
                .field("topicId", "topicId")
                .field("title", "title")
                .field("content", "content")
                .field("images", "images")
                .field("numberLike", "numberLike")
                .field("numberDislike", "numberDislike")
                .field("numberComment", "numberComment")
                .register();

        return factory.getMapperFacade();
    }

    @Bean
    public MapperFacade commentMapperFacade() {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        factory.classMap(Comment.class, CommentResponse.class)
                .field("commentId", "commentId")
                .field("postId", "postId")
                .field("userOwnerId", "userOwnerId")
                .field("parentId", "parentId")
                .field("content", "content")
                .field("numberLike", "numberLike")
                .field("numberDislike", "numberDislike")
                .field("createdAt", "createdAt")
                .field("updatedAt", "updatedAt")
                .register();

        factory.classMap(CommentRequest.class, Comment.class)
                .field("postId", "postId")
                .field("userOwnerId", "userOwnerId")
                .field("parentId", "parentId")
                .field("content", "content")
                .field("numberLike", "numberLike")
                .field("numberDislike", "numberDislike")
                .register();

        return factory.getMapperFacade();
    }

    @Bean
    public MapperFacade topicMapperFacade() {
        MapperFactory factory = new DefaultMapperFactory.Builder().build();

        factory.classMap(Topic.class, TopicResponse.class)
                .field("topicId", "topicId")
                .field("name", "name")
                .field("numberPost", "numberPost")
                .field("numberFollow", "numberFollow")
                .field("intro", "intro")
                .field("description", "description")
                .field("backgroundImage", "backgroundImage")
                .field("avatar", "avatar")
                .field("createdAt", "createdAt")
                .field("updatedAt", "updatedAt")
                .register();

        factory.classMap(TopicRequest.class, Topic.class)
                .field("name", "name")
                .field("intro", "intro")
                .field("description", "description")
                .field("backgroundImage", "backgroundImage")
                .field("avatar", "avatar")
                .register();

        return factory.getMapperFacade();
    }
}
