package com.dangkhoa.socialnetwork.elasticsearch.services

import com.dangkhoa.socialnetwork.elasticsearch.repositories.EsPostRepository
import com.dangkhoa.socialnetwork.entities.elasticsearch.post.EsPost
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EsPostService {

    @Autowired
    EsPostRepository esPostRepository

    String indexDocument(EsPost esPost){
        return esPostRepository.indexDocument(esPost)
    }

    void bulkIndexDocument(List<EsPost> esPosts) {
        esPostRepository.bulkIndexDocument(esPosts)
    }
}
