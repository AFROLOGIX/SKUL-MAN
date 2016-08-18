package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Utilisateur;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Utilisateur entity.
 */
public interface UtilisateurSearchRepository extends ElasticsearchRepository<Utilisateur, Long> {
}
