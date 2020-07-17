package com.dangkhoa.socialnetwork.entities.mongo.post;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("posts")
@Getter
@Setter
public class Post {
    private String id;
    private String postId;
    private String userOwnerId;
    private String topicId;
    private String title;
    private String content;
    private List<String> images;
    private Integer numberLike;
    private Integer numberDislike;
    private Integer numberComment;
    private Boolean isNew;
    private Boolean lock;
    private Long createdAt;
    private Long updatedAt;
}
