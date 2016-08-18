package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.MatiereClasse;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the MatiereClasse entity.
 */
public interface MatiereClasseSearchRepository extends ElasticsearchRepository<MatiereClasse, Long> {
}
