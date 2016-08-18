package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.MotifExclusion;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the MotifExclusion entity.
 */
public interface MotifExclusionSearchRepository extends ElasticsearchRepository<MotifExclusion, Long> {
}
