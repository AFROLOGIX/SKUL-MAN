package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.TypeAbonnementBus;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TypeAbonnementBus entity.
 */
public interface TypeAbonnementBusSearchRepository extends ElasticsearchRepository<TypeAbonnementBus, Long> {
}
