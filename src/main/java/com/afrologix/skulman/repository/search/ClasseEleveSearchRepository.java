package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.ClasseEleve;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ClasseEleve entity.
 */
public interface ClasseEleveSearchRepository extends ElasticsearchRepository<ClasseEleve, Long> {
}
