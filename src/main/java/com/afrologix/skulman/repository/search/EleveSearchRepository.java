package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Eleve;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Eleve entity.
 */
public interface EleveSearchRepository extends ElasticsearchRepository<Eleve, Long> {
}
