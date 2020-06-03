package com.dangkhoa.socialnetwork.elasticsearch.services;

import com.dangkhoa.socialnetwork.elasticsearch.repositories.EsPostRepository;
import com.dangkhoa.socialnetwork.entities.elasticsearch.EsPost;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class EsPostService {

    private final EsPostRepository esPostRepository;

    public EsPostService(EsPostRepository esPostRepository) {
        this.esPostRepository = esPostRepository;
    }

    public String indexDocument(EsPost esPost) throws JsonProcessingException {
        return esPostRepository.indexDocument(esPost);
    }

    public void bulkIndexDocument(List<EsPost> esPosts) {
        esPostRepository.bulkIndexDocument(esPosts);
    }

    public List<EsPost> filter(EsPost esPost) throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        SearchRequest searchRequest = new SearchRequest();


        if (StringUtils.isNotBlank(esPost.getTitle())) {
            queryBuilder.filter(QueryBuilders.matchQuery("title", esPost.getTitle()));
        }

        if (StringUtils.isNotBlank(esPost.getContent())) {
            queryBuilder.filter(QueryBuilders.matchQuery("content", esPost.getContent()));
        }

        searchSourceBuilder.query(queryBuilder);
        searchRequest.source(searchSourceBuilder);
        return esPostRepository.filter(searchRequest);
    }
}
