package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Batiment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Batiment entity.
 */
public interface BatimentSearchRepository extends ElasticsearchRepository<Batiment, Long> {
}
