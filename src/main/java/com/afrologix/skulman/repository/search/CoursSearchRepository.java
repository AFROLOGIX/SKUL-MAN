package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Cours;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Cours entity.
 */
public interface CoursSearchRepository extends ElasticsearchRepository<Cours, Long> {
}
