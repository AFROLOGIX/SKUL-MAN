package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Epreuve;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Epreuve entity.
 */
public interface EpreuveSearchRepository extends ElasticsearchRepository<Epreuve, Long> {
}
