package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Chambre;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Chambre entity.
 */
public interface ChambreSearchRepository extends ElasticsearchRepository<Chambre, Long> {
}
