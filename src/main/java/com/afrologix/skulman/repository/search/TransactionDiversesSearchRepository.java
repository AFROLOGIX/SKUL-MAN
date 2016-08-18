package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.TransactionDiverses;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TransactionDiverses entity.
 */
public interface TransactionDiversesSearchRepository extends ElasticsearchRepository<TransactionDiverses, Long> {
}
