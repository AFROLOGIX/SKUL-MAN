package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.AgentAdministratif;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AgentAdministratif entity.
 */
public interface AgentAdministratifSearchRepository extends ElasticsearchRepository<AgentAdministratif, Long> {
}
