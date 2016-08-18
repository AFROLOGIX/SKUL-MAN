package com.afrologix.skulman.repository.search;

import com.afrologix.skulman.domain.Note;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Note entity.
 */
public interface NoteSearchRepository extends ElasticsearchRepository<Note, Long> {
}
