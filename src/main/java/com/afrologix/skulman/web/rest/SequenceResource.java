package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.Sequence;
import com.afrologix.skulman.repository.SequenceRepository;
import com.afrologix.skulman.repository.search.SequenceSearchRepository;
import com.afrologix.skulman.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Sequence.
 */
@RestController
@RequestMapping("/api")
public class SequenceResource {

    private final Logger log = LoggerFactory.getLogger(SequenceResource.class);
        
    @Inject
    private SequenceRepository sequenceRepository;
    
    @Inject
    private SequenceSearchRepository sequenceSearchRepository;
    
    /**
     * POST  /sequences : Create a new sequence.
     *
     * @param sequence the sequence to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sequence, or with status 400 (Bad Request) if the sequence has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/sequences",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sequence> createSequence(@Valid @RequestBody Sequence sequence) throws URISyntaxException {
        log.debug("REST request to save Sequence : {}", sequence);
        if (sequence.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("sequence", "idexists", "A new sequence cannot already have an ID")).body(null);
        }
        Sequence result = sequenceRepository.save(sequence);
        sequenceSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/sequences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("sequence", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sequences : Updates an existing sequence.
     *
     * @param sequence the sequence to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sequence,
     * or with status 400 (Bad Request) if the sequence is not valid,
     * or with status 500 (Internal Server Error) if the sequence couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/sequences",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sequence> updateSequence(@Valid @RequestBody Sequence sequence) throws URISyntaxException {
        log.debug("REST request to update Sequence : {}", sequence);
        if (sequence.getId() == null) {
            return createSequence(sequence);
        }
        Sequence result = sequenceRepository.save(sequence);
        sequenceSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("sequence", sequence.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sequences : get all the sequences.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sequences in body
     */
    @RequestMapping(value = "/sequences",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Sequence> getAllSequences() {
        log.debug("REST request to get all Sequences");
        List<Sequence> sequences = sequenceRepository.findAll();
        return sequences;
    }

    /**
     * GET  /sequences/:id : get the "id" sequence.
     *
     * @param id the id of the sequence to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sequence, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/sequences/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sequence> getSequence(@PathVariable Long id) {
        log.debug("REST request to get Sequence : {}", id);
        Sequence sequence = sequenceRepository.findOne(id);
        return Optional.ofNullable(sequence)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /sequences/:id : delete the "id" sequence.
     *
     * @param id the id of the sequence to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/sequences/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSequence(@PathVariable Long id) {
        log.debug("REST request to delete Sequence : {}", id);
        sequenceRepository.delete(id);
        sequenceSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("sequence", id.toString())).build();
    }

    /**
     * SEARCH  /_search/sequences?query=:query : search for the sequence corresponding
     * to the query.
     *
     * @param query the query of the sequence search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/sequences",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Sequence> searchSequences(@RequestParam String query) {
        log.debug("REST request to search Sequences for query {}", query);
        return StreamSupport
            .stream(sequenceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
