package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.ChambreEleve;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ChambreEleve entity.
 */
public interface ChambreEleveSearchRepository extends ElasticsearchRepository<ChambreEleve, Long> {
}
