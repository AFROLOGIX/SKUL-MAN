package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.MoyenneTableauHonneur;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the MoyenneTableauHonneur entity.
 */
public interface MoyenneTableauHonneurSearchRepository extends ElasticsearchRepository<MoyenneTableauHonneur, Long> {
}
