package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Tranche;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Tranche entity.
 */
public interface TrancheSearchRepository extends ElasticsearchRepository<Tranche, Long> {
}
