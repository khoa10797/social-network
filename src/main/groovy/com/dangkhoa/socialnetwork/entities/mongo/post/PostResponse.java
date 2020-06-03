package com.dangkhoa.socialnetwork.entities.mongo.post;

import com.dangkhoa.socialnetwork.entities.mongo.user.UserResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostResponse {
    private String postId;
    private String userOwnerId;
    private String topicId;
    private String postType;
    private String title;
    private String content;
    private List<String> images;
    private Integer numberLike;
    private Integer numberDislike;
    private UserResponse userOwner;
    private Long numberComment;
    private String userStatus;
    private Long createdAt;
    private Long updatedAt;
}
