package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.AbsencePersonnel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AbsencePersonnel entity.
 */
public interface AbsencePersonnelSearchRepository extends ElasticsearchRepository<AbsencePersonnel, Long> {
}
