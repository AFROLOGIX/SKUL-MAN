package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.AbsenceEnseignant;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AbsenceEnseignant entity.
 */
public interface AbsenceEnseignantSearchRepository extends ElasticsearchRepository<AbsenceEnseignant, Long> {
}
