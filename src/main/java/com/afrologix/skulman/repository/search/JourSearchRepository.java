package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Jour;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Jour entity.
 */
public interface JourSearchRepository extends ElasticsearchRepository<Jour, Long> {
}
