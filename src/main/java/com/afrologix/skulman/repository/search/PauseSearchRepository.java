package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Pause;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Pause entity.
 */
public interface PauseSearchRepository extends ElasticsearchRepository<Pause, Long> {
}
