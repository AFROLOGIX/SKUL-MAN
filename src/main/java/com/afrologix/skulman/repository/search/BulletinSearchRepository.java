package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Bulletin;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Bulletin entity.
 */
public interface BulletinSearchRepository extends ElasticsearchRepository<Bulletin, Long> {
}
