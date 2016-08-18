package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.ProjetPedagogique;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ProjetPedagogique entity.
 */
public interface ProjetPedagogiqueSearchRepository extends ElasticsearchRepository<ProjetPedagogique, Long> {
}
