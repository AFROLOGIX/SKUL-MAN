package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Trimestre;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Trimestre entity.
 */
public interface TrimestreSearchRepository extends ElasticsearchRepository<Trimestre, Long> {
}
