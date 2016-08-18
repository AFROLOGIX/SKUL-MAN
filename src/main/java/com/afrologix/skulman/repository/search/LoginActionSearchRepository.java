package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.LoginAction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the LoginAction entity.
 */
public interface LoginActionSearchRepository extends ElasticsearchRepository<LoginAction, Long> {
}
