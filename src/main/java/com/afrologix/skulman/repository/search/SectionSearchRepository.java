package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Section;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Section entity.
 */
public interface SectionSearchRepository extends ElasticsearchRepository<Section, Long> {
}
