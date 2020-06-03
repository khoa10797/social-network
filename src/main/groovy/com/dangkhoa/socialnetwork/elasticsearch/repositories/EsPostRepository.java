package com.dangkhoa.socialnetwork.elasticsearch.repositories;

import com.dangkhoa.socialnetwork.entities.elasticsearch.EsPost;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class EsPostRepository {

    private final ObjectMapper objectMapper;
    private final ElasticsearchOperations elasticsearchOperations;
    private final RestHighLevelClient elasticsearchClient;

    public EsPostRepository(ObjectMapper objectMapper, ElasticsearchOperations elasticsearchOperations, RestHighLevelClient elasticsearchClient) {
        this.objectMapper = objectMapper;
        this.elasticsearchOperations = elasticsearchOperations;
        this.elasticsearchClient = elasticsearchClient;
    }

    public String indexDocument(EsPost esPost) {
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(esPost.getId())
                .withIndexName("social-network")
                .withType("posts")
                .withObject(esPost)
                .build();
        return elasticsearchOperations.index(indexQuery);
    }

    public void bulkIndexDocument(List<EsPost> esPosts) {
        List<IndexQuery> indexQueries = esPosts.stream()
                .map(item -> new IndexQueryBuilder()
                        .withId(item.getId())
                        .withIndexName("social-network")
                        .withType("posts")
                        .withObject(item)
                        .build()
                ).collect(Collectors.toList());

        elasticsearchOperations.bulkIndex(indexQueries);
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
}
