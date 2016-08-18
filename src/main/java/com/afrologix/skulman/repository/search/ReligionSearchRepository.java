package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Religion;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Religion entity.
 */
public interface ReligionSearchRepository extends ElasticsearchRepository<Religion, Long> {
}
