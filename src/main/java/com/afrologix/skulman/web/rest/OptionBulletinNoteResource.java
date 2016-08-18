package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.OptionBulletinNote;
import com.afrologix.skulman.repository.OptionBulletinNoteRepository;
import com.afrologix.skulman.repository.search.OptionBulletinNoteSearchRepository;
import com.afrologix.skulman.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing OptionBulletinNote.
 */
@RestController
@RequestMapping("/api")
public class OptionBulletinNoteResource {

    private final Logger log = LoggerFactory.getLogger(OptionBulletinNoteResource.class);
        
    @Inject
    private OptionBulletinNoteRepository optionBulletinNoteRepository;
    
    @Inject
    private OptionBulletinNoteSearchRepository optionBulletinNoteSearchRepository;
    
    /**
     * POST  /option-bulletin-notes : Create a new optionBulletinNote.
     *
     * @param optionBulletinNote the optionBulletinNote to create
     * @return the ResponseEntity with status 201 (Created) and with body the new optionBulletinNote, or with status 400 (Bad Request) if the optionBulletinNote has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/option-bulletin-notes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OptionBulletinNote> createOptionBulletinNote(@RequestBody OptionBulletinNote optionBulletinNote) throws URISyntaxException {
        log.debug("REST request to save OptionBulletinNote : {}", optionBulletinNote);
        if (optionBulletinNote.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("optionBulletinNote", "idexists", "A new optionBulletinNote cannot already have an ID")).body(null);
        }
        OptionBulletinNote result = optionBulletinNoteRepository.save(optionBulletinNote);
        optionBulletinNoteSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/option-bulletin-notes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("optionBulletinNote", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /option-bulletin-notes : Updates an existing optionBulletinNote.
     *
     * @param optionBulletinNote the optionBulletinNote to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated optionBulletinNote,
     * or with status 400 (Bad Request) if the optionBulletinNote is not valid,
     * or with status 500 (Internal Server Error) if the optionBulletinNote couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/option-bulletin-notes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OptionBulletinNote> updateOptionBulletinNote(@RequestBody OptionBulletinNote optionBulletinNote) throws URISyntaxException {
        log.debug("REST request to update OptionBulletinNote : {}", optionBulletinNote);
        if (optionBulletinNote.getId() == null) {
            return createOptionBulletinNote(optionBulletinNote);
        }
        OptionBulletinNote result = optionBulletinNoteRepository.save(optionBulletinNote);
        optionBulletinNoteSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("optionBulletinNote", optionBulletinNote.getId().toString()))
            .body(result);
    }

    /**
     * GET  /option-bulletin-notes : get all the optionBulletinNotes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of optionBulletinNotes in body
     */
    @RequestMapping(value = "/option-bulletin-notes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<OptionBulletinNote> getAllOptionBulletinNotes() {
        log.debug("REST request to get all OptionBulletinNotes");
        List<OptionBulletinNote> optionBulletinNotes = optionBulletinNoteRepository.findAll();
        return optionBulletinNotes;
    }

    /**
     * GET  /option-bulletin-notes/:id : get the "id" optionBulletinNote.
     *
     * @param id the id of the optionBulletinNote to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the optionBulletinNote, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/option-bulletin-notes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OptionBulletinNote> getOptionBulletinNote(@PathVariable Long id) {
        log.debug("REST request to get OptionBulletinNote : {}", id);
        OptionBulletinNote optionBulletinNote = optionBulletinNoteRepository.findOne(id);
        return Optional.ofNullable(optionBulletinNote)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /option-bulletin-notes/:id : delete the "id" optionBulletinNote.
     *
     * @param id the id of the optionBulletinNote to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/option-bulletin-notes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOptionBulletinNote(@PathVariable Long id) {
        log.debug("REST request to delete OptionBulletinNote : {}", id);
        optionBulletinNoteRepository.delete(id);
        optionBulletinNoteSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("optionBulletinNote", id.toString())).build();
    }

    /**
     * SEARCH  /_search/option-bulletin-notes?query=:query : search for the optionBulletinNote corresponding
     * to the query.
     *
     * @param query the query of the optionBulletinNote search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/option-bulletin-notes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<OptionBulletinNote> searchOptionBulletinNotes(@RequestParam String query) {
        log.debug("REST request to search OptionBulletinNotes for query {}", query);
        return StreamSupport
            .stream(optionBulletinNoteSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
