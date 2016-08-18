package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Parent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Parent entity.
 */
public interface ParentSearchRepository extends ElasticsearchRepository<Parent, Long> {
}
