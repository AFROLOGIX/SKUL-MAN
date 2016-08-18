package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Fichier;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Fichier entity.
 */
public interface FichierSearchRepository extends ElasticsearchRepository<Fichier, Long> {
}
