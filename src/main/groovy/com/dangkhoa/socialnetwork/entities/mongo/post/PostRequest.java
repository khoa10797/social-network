package com.dangkhoa.socialnetwork.entities.mongo.post;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostRequest {
    private String userOwnerId;
    private String topicId;
    private String postType;
    private String title;
    private String content;
    private List<String> images;
    private Integer numberLike;
    private Integer numberDislike;
    private Integer numberComment;
    private Boolean lock;
}
