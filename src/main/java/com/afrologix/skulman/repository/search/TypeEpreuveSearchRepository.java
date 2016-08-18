package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.TypeEpreuve;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TypeEpreuve entity.
 */
public interface TypeEpreuveSearchRepository extends ElasticsearchRepository<TypeEpreuve, Long> {
}
