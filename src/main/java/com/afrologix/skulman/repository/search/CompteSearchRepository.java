package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Compte;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Compte entity.
 */
public interface CompteSearchRepository extends ElasticsearchRepository<Compte, Long> {
}
