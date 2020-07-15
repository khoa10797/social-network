package com.dangkhoa.socialnetwork.job;

import com.dangkhoa.socialnetwork.entities.mongo.post.Post;
import com.dangkhoa.socialnetwork.mongo.services.PostService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostTask {

    private final PostService postService;

    public PostTask(PostService postService) {
        this.postService = postService;
    }

    @Scheduled(fixedRate = 3600000L)
    public void setOldPost() {
        List<Post> newPosts = postService.findNewPost();
        long now = new Date().getTime();

        List<String> oldPostIds = newPosts.stream()
                .filter(item -> now - item.getCreatedAt() > 86400000)
                .map(Post::getPostId)
                .collect(Collectors.toList());

        postService.convertNewPostToOld(oldPostIds);
    }
}
