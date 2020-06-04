package com.dangkhoa.socialnetwork.entities.elasticsearch;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "social-network", type = "posts")
@Getter
@Setter
public class EsPost {
    @Id
    private String id;
    private String postId;
    private String title;
    private String content;
}
