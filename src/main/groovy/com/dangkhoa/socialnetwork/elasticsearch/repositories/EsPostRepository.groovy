package com.dangkhoa.socialnetwork.elasticsearch.repositories

import com.dangkhoa.socialnetwork.entities.elasticsearch.post.EsPost
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.query.IndexQuery
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder
import org.springframework.stereotype.Repository

@Repository
class EsPostRepository {

    @Autowired
    ElasticsearchOperations elasticsearchOperations;

    String indexDocument(EsPost esPost) {
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(esPost.id)
                .withIndexName("social-network")
                .withType("posts")
                .withObject(esPost)
                .build()
        return elasticsearchOperations.index(indexQuery)
    }

    void bulkIndexDocument(List<EsPost> esPosts) {
        List<IndexQuery> indexQueries = esPosts.collect { item ->
            IndexQuery indexQuery = new IndexQueryBuilder()
                    .withId(item.id)
                    .withIndexName("social-network")
                    .withType("posts")
                    .withObject(item)
                    .build()
            return indexQuery
        }.toList()

        elasticsearchOperations.bulkIndex(indexQueries)
    }
}
