package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.TypeTrancheHoraire;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TypeTrancheHoraire entity.
 */
public interface TypeTrancheHoraireSearchRepository extends ElasticsearchRepository<TypeTrancheHoraire, Long> {
}
