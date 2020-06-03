package com.dangkhoa.socialnetwork.entities.elasticsearch;

import com.dangkhoa.socialnetwork.entities.mongo.user.UserResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Document(indexName = "social-network", type = "posts")
@Getter
@Setter
public class EsPost {
    @Id
    @JsonProperty("id")
    private String id;

    @JsonProperty("postId")
    private String postId;

    @JsonProperty("userOwnerId")
    private String userOwnerId;

    @JsonProperty("topicId")
    private String topicId;

    @JsonProperty("userOwner")
    private UserResponse userOwner;

    @JsonProperty("title")
    private String title;

    @JsonProperty("content")
    private String content;

    @JsonProperty("images")
    private List<String> images;

    @JsonProperty("numberLike")
    private Integer numberLike;

    @JsonProperty("numberDislike")
    private Integer numberDislike;

    @JsonProperty("numberComment")
    private Integer numberComment;

    @JsonProperty("createdAt")
    private Long createdAt;

    @JsonProperty("updatedAt")
    private Long updatedAt;
}
