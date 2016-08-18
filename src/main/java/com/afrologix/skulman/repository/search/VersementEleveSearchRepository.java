package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.VersementEleve;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the VersementEleve entity.
 */
public interface VersementEleveSearchRepository extends ElasticsearchRepository<VersementEleve, Long> {
}
