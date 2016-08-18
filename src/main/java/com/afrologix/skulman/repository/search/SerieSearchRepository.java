package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Serie;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Serie entity.
 */
public interface SerieSearchRepository extends ElasticsearchRepository<Serie, Long> {
}
