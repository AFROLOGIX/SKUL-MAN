package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.RegimePension;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the RegimePension entity.
 */
public interface RegimePensionSearchRepository extends ElasticsearchRepository<RegimePension, Long> {
}
