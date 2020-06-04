package com.dangkhoa.socialnetwork.elasticsearch.repositories;

import com.dangkhoa.socialnetwork.entities.elasticsearch.EsPost;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchHit;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EsPostRepository {

    private static final String TYPE_NAME = "posts";
    private static final String INDEX_NAME = "social-network";

    private final ObjectMapper objectMapper;
    private final ElasticsearchOperations elasticsearchOperations;
    private final RestHighLevelClient elasticsearchClient;

    public EsPostRepository(ObjectMapper objectMapper, ElasticsearchOperations elasticsearchOperations, RestHighLevelClient elasticsearchClient) {
        this.objectMapper = objectMapper;
        this.elasticsearchOperations = elasticsearchOperations;
        this.elasticsearchClient = elasticsearchClient;
    }

    public String indexDocument(EsPost esPost) throws IOException {
        IndexRequest indexRequest = new IndexRequest(INDEX_NAME, TYPE_NAME, esPost.getPostId());
        String source = objectMapper.writeValueAsString(esPost);
        indexRequest.source(source, XContentType.JSON);

        return elasticsearchClient.index(indexRequest, RequestOptions.DEFAULT).getId();
    }

    public List<EsPost> filter(SearchRequest searchRequest) throws IOException {
        SearchResponse searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);

        SearchHit[] searchHits = searchResponse.getHits().getHits();
        List<EsPost> result = new ArrayList<>();

        for (SearchHit searchHit : searchHits) {
            String source = searchHit.getSourceAsString();
            if (StringUtils.isNotBlank(source)) {
                result.add(objectMapper.readValue(source, EsPost.class));
            }
        }

        return result;
    }

    public String deleteById(String id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(INDEX_NAME, TYPE_NAME, id);
        return elasticsearchClient.delete(deleteRequest, RequestOptions.DEFAULT).getId();
    }

    public UpdateResponse update(String id, EsPost esPost) throws IOException {
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.timeField("title", esPost.getTitle());
            builder.field("content", esPost.getContent());
        }
        builder.endObject();
        UpdateRequest updateRequest = new UpdateRequest(INDEX_NAME, TYPE_NAME, id).doc(builder);

        return elasticsearchClient.update(updateRequest, RequestOptions.DEFAULT);
    }
}
