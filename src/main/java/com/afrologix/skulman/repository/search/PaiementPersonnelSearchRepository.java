package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.PaiementPersonnel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the PaiementPersonnel entity.
 */
public interface PaiementPersonnelSearchRepository extends ElasticsearchRepository<PaiementPersonnel, Long> {
}
