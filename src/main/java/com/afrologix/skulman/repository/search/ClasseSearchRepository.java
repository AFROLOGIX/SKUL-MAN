package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Classe;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Classe entity.
 */
public interface ClasseSearchRepository extends ElasticsearchRepository<Classe, Long> {
}
