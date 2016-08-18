package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Etablissement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Etablissement entity.
 */
public interface EtablissementSearchRepository extends ElasticsearchRepository<Etablissement, Long> {
}
