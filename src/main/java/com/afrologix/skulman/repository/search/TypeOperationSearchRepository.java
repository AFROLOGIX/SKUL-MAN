package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.TypeOperation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TypeOperation entity.
 */
public interface TypeOperationSearchRepository extends ElasticsearchRepository<TypeOperation, Long> {
}
