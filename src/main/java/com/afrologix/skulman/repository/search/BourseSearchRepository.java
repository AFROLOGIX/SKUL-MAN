package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Bourse;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Bourse entity.
 */
public interface BourseSearchRepository extends ElasticsearchRepository<Bourse, Long> {
}
