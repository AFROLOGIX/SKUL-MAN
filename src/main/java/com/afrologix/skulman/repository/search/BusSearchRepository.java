package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Bus;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Bus entity.
 */
public interface BusSearchRepository extends ElasticsearchRepository<Bus, Long> {
}
