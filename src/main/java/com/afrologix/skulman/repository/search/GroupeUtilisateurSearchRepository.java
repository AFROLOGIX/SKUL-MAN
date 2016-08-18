package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.GroupeUtilisateur;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the GroupeUtilisateur entity.
 */
public interface GroupeUtilisateurSearchRepository extends ElasticsearchRepository<GroupeUtilisateur, Long> {
}
