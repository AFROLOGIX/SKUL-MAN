package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.AnneeScolaire;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AnneeScolaire entity.
 */
public interface AnneeScolaireSearchRepository extends ElasticsearchRepository<AnneeScolaire, Long> {
}
