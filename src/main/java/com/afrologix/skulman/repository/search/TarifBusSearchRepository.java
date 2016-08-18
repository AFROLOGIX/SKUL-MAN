package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.TarifBus;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TarifBus entity.
 */
public interface TarifBusSearchRepository extends ElasticsearchRepository<TarifBus, Long> {
}
