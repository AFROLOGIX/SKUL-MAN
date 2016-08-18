package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.StatutEleve;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the StatutEleve entity.
 */
public interface StatutEleveSearchRepository extends ElasticsearchRepository<StatutEleve, Long> {
}
