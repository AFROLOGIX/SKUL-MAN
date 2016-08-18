package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.OptionBulletinNote;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the OptionBulletinNote entity.
 */
public interface OptionBulletinNoteSearchRepository extends ElasticsearchRepository<OptionBulletinNote, Long> {
}
