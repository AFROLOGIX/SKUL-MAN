package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.EnseignantTitulaire;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the EnseignantTitulaire entity.
 */
public interface EnseignantTitulaireSearchRepository extends ElasticsearchRepository<EnseignantTitulaire, Long> {
}
