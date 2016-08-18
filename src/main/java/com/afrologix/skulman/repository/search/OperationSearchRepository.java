package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Operation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Operation entity.
 */
public interface OperationSearchRepository extends ElasticsearchRepository<Operation, Long> {
}
