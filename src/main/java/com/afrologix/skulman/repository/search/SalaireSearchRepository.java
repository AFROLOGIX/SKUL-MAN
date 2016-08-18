package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Salaire;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Salaire entity.
 */
public interface SalaireSearchRepository extends ElasticsearchRepository<Salaire, Long> {
}
