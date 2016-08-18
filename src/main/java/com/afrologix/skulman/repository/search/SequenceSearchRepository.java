package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Sequence;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Sequence entity.
 */
public interface SequenceSearchRepository extends ElasticsearchRepository<Sequence, Long> {
}
