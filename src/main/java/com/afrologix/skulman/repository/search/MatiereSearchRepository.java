package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Matiere;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Matiere entity.
 */
public interface MatiereSearchRepository extends ElasticsearchRepository<Matiere, Long> {
}
