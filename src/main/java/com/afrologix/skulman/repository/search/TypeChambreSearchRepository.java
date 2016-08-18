package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.TypeChambre;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TypeChambre entity.
 */
public interface TypeChambreSearchRepository extends ElasticsearchRepository<TypeChambre, Long> {
}
