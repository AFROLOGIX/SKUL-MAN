package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.TrancheHoraire;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TrancheHoraire entity.
 */
public interface TrancheHoraireSearchRepository extends ElasticsearchRepository<TrancheHoraire, Long> {
}
