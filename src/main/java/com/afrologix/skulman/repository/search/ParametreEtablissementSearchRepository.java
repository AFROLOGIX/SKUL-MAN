package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.ParametreEtablissement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ParametreEtablissement entity.
 */
public interface ParametreEtablissementSearchRepository extends ElasticsearchRepository<ParametreEtablissement, Long> {
}
