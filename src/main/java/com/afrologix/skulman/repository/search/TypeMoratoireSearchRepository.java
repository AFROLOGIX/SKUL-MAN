package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.TypeMoratoire;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TypeMoratoire entity.
 */
public interface TypeMoratoireSearchRepository extends ElasticsearchRepository<TypeMoratoire, Long> {
}
