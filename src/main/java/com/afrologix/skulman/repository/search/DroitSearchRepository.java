package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Droit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Droit entity.
 */
public interface DroitSearchRepository extends ElasticsearchRepository<Droit, Long> {
}
