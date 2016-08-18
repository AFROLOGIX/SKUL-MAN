package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Deliberation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Deliberation entity.
 */
public interface DeliberationSearchRepository extends ElasticsearchRepository<Deliberation, Long> {
}
