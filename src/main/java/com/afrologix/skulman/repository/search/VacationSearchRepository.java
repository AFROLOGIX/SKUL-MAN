package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Vacation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Vacation entity.
 */
public interface VacationSearchRepository extends ElasticsearchRepository<Vacation, Long> {
}
