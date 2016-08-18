package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.LoginConnexion;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the LoginConnexion entity.
 */
public interface LoginConnexionSearchRepository extends ElasticsearchRepository<LoginConnexion, Long> {
}
