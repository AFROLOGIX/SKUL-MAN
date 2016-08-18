package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Moratoire;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Moratoire entity.
 */
public interface MoratoireSearchRepository extends ElasticsearchRepository<Moratoire, Long> {
}
