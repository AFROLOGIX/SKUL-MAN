package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.PeriodeSaisieNote;
import com.afrologix.skulman.repository.PeriodeSaisieNoteRepository;
import com.afrologix.skulman.repository.search.PeriodeSaisieNoteSearchRepository;
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
 * REST controller for managing PeriodeSaisieNote.
 */
@RestController
@RequestMapping("/api")
public class PeriodeSaisieNoteResource {

    private final Logger log = LoggerFactory.getLogger(PeriodeSaisieNoteResource.class);
        
    @Inject
    private PeriodeSaisieNoteRepository periodeSaisieNoteRepository;
    
    @Inject
    private PeriodeSaisieNoteSearchRepository periodeSaisieNoteSearchRepository;
    
    /**
     * POST  /periode-saisie-notes : Create a new periodeSaisieNote.
     *
     * @param periodeSaisieNote the periodeSaisieNote to create
     * @return the ResponseEntity with status 201 (Created) and with body the new periodeSaisieNote, or with status 400 (Bad Request) if the periodeSaisieNote has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/periode-saisie-notes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PeriodeSaisieNote> createPeriodeSaisieNote(@Valid @RequestBody PeriodeSaisieNote periodeSaisieNote) throws URISyntaxException {
        log.debug("REST request to save PeriodeSaisieNote : {}", periodeSaisieNote);
        if (periodeSaisieNote.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("periodeSaisieNote", "idexists", "A new periodeSaisieNote cannot already have an ID")).body(null);
        }
        PeriodeSaisieNote result = periodeSaisieNoteRepository.save(periodeSaisieNote);
        periodeSaisieNoteSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/periode-saisie-notes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("periodeSaisieNote", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /periode-saisie-notes : Updates an existing periodeSaisieNote.
     *
     * @param periodeSaisieNote the periodeSaisieNote to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated periodeSaisieNote,
     * or with status 400 (Bad Request) if the periodeSaisieNote is not valid,
     * or with status 500 (Internal Server Error) if the periodeSaisieNote couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/periode-saisie-notes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PeriodeSaisieNote> updatePeriodeSaisieNote(@Valid @RequestBody PeriodeSaisieNote periodeSaisieNote) throws URISyntaxException {
        log.debug("REST request to update PeriodeSaisieNote : {}", periodeSaisieNote);
        if (periodeSaisieNote.getId() == null) {
            return createPeriodeSaisieNote(periodeSaisieNote);
        }
        PeriodeSaisieNote result = periodeSaisieNoteRepository.save(periodeSaisieNote);
        periodeSaisieNoteSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("periodeSaisieNote", periodeSaisieNote.getId().toString()))
            .body(result);
    }

    /**
     * GET  /periode-saisie-notes : get all the periodeSaisieNotes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of periodeSaisieNotes in body
     */
    @RequestMapping(value = "/periode-saisie-notes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PeriodeSaisieNote> getAllPeriodeSaisieNotes() {
        log.debug("REST request to get all PeriodeSaisieNotes");
        List<PeriodeSaisieNote> periodeSaisieNotes = periodeSaisieNoteRepository.findAll();
        return periodeSaisieNotes;
    }

    /**
     * GET  /periode-saisie-notes/:id : get the "id" periodeSaisieNote.
     *
     * @param id the id of the periodeSaisieNote to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the periodeSaisieNote, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/periode-saisie-notes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PeriodeSaisieNote> getPeriodeSaisieNote(@PathVariable Long id) {
        log.debug("REST request to get PeriodeSaisieNote : {}", id);
        PeriodeSaisieNote periodeSaisieNote = periodeSaisieNoteRepository.findOne(id);
        return Optional.ofNullable(periodeSaisieNote)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /periode-saisie-notes/:id : delete the "id" periodeSaisieNote.
     *
     * @param id the id of the periodeSaisieNote to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/periode-saisie-notes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePeriodeSaisieNote(@PathVariable Long id) {
        log.debug("REST request to delete PeriodeSaisieNote : {}", id);
        periodeSaisieNoteRepository.delete(id);
        periodeSaisieNoteSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("periodeSaisieNote", id.toString())).build();
    }

    /**
     * SEARCH  /_search/periode-saisie-notes?query=:query : search for the periodeSaisieNote corresponding
     * to the query.
     *
     * @param query the query of the periodeSaisieNote search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/periode-saisie-notes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PeriodeSaisieNote> searchPeriodeSaisieNotes(@RequestParam String query) {
        log.debug("REST request to search PeriodeSaisieNotes for query {}", query);
        return StreamSupport
            .stream(periodeSaisieNoteSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
