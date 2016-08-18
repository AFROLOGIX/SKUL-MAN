package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.TypePersonnel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TypePersonnel entity.
 */
public interface TypePersonnelSearchRepository extends ElasticsearchRepository<TypePersonnel, Long> {
}
