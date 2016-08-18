package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Fonctionnalite;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Fonctionnalite entity.
 */
public interface FonctionnaliteSearchRepository extends ElasticsearchRepository<Fonctionnalite, Long> {
}
