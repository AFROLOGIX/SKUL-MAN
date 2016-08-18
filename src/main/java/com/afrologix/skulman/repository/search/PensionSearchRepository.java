package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Pension;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Pension entity.
 */
public interface PensionSearchRepository extends ElasticsearchRepository<Pension, Long> {
}
