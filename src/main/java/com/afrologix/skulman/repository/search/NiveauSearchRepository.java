package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Niveau;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Niveau entity.
 */
public interface NiveauSearchRepository extends ElasticsearchRepository<Niveau, Long> {
}
