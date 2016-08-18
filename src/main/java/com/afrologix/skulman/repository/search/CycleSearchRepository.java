package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Cycle;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Cycle entity.
 */
public interface CycleSearchRepository extends ElasticsearchRepository<Cycle, Long> {
}
