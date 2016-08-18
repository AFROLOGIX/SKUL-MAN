package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Appreciation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Appreciation entity.
 */
public interface AppreciationSearchRepository extends ElasticsearchRepository<Appreciation, Long> {
}
