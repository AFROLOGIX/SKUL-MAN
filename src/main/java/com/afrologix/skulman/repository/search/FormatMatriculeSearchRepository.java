package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.FormatMatricule;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the FormatMatricule entity.
 */
public interface FormatMatriculeSearchRepository extends ElasticsearchRepository<FormatMatricule, Long> {
}
