package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Evenement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Evenement entity.
 */
public interface EvenementSearchRepository extends ElasticsearchRepository<Evenement, Long> {
}
