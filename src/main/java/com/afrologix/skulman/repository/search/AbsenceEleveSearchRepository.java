package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.AbsenceEleve;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AbsenceEleve entity.
 */
public interface AbsenceEleveSearchRepository extends ElasticsearchRepository<AbsenceEleve, Long> {
}
