package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.AbsenceEnseignant;
import com.afrologix.skulman.repository.AbsenceEnseignantRepository;
import com.afrologix.skulman.repository.search.AbsenceEnseignantSearchRepository;
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
 * REST controller for managing AbsenceEnseignant.
 */
@RestController
@RequestMapping("/api")
public class AbsenceEnseignantResource {

    private final Logger log = LoggerFactory.getLogger(AbsenceEnseignantResource.class);
        
    @Inject
    private AbsenceEnseignantRepository absenceEnseignantRepository;
    
    @Inject
    private AbsenceEnseignantSearchRepository absenceEnseignantSearchRepository;
    
    /**
     * POST  /absence-enseignants : Create a new absenceEnseignant.
     *
     * @param absenceEnseignant the absenceEnseignant to create
     * @return the ResponseEntity with status 201 (Created) and with body the new absenceEnseignant, or with status 400 (Bad Request) if the absenceEnseignant has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/absence-enseignants",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AbsenceEnseignant> createAbsenceEnseignant(@Valid @RequestBody AbsenceEnseignant absenceEnseignant) throws URISyntaxException {
        log.debug("REST request to save AbsenceEnseignant : {}", absenceEnseignant);
        if (absenceEnseignant.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("absenceEnseignant", "idexists", "A new absenceEnseignant cannot already have an ID")).body(null);
        }
        AbsenceEnseignant result = absenceEnseignantRepository.save(absenceEnseignant);
        absenceEnseignantSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/absence-enseignants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("absenceEnseignant", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /absence-enseignants : Updates an existing absenceEnseignant.
     *
     * @param absenceEnseignant the absenceEnseignant to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated absenceEnseignant,
     * or with status 400 (Bad Request) if the absenceEnseignant is not valid,
     * or with status 500 (Internal Server Error) if the absenceEnseignant couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/absence-enseignants",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AbsenceEnseignant> updateAbsenceEnseignant(@Valid @RequestBody AbsenceEnseignant absenceEnseignant) throws URISyntaxException {
        log.debug("REST request to update AbsenceEnseignant : {}", absenceEnseignant);
        if (absenceEnseignant.getId() == null) {
            return createAbsenceEnseignant(absenceEnseignant);
        }
        AbsenceEnseignant result = absenceEnseignantRepository.save(absenceEnseignant);
        absenceEnseignantSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("absenceEnseignant", absenceEnseignant.getId().toString()))
            .body(result);
    }

    /**
     * GET  /absence-enseignants : get all the absenceEnseignants.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of absenceEnseignants in body
     */
    @RequestMapping(value = "/absence-enseignants",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AbsenceEnseignant> getAllAbsenceEnseignants() {
        log.debug("REST request to get all AbsenceEnseignants");
        List<AbsenceEnseignant> absenceEnseignants = absenceEnseignantRepository.findAll();
        return absenceEnseignants;
    }

    /**
     * GET  /absence-enseignants/:id : get the "id" absenceEnseignant.
     *
     * @param id the id of the absenceEnseignant to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the absenceEnseignant, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/absence-enseignants/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AbsenceEnseignant> getAbsenceEnseignant(@PathVariable Long id) {
        log.debug("REST request to get AbsenceEnseignant : {}", id);
        AbsenceEnseignant absenceEnseignant = absenceEnseignantRepository.findOne(id);
        return Optional.ofNullable(absenceEnseignant)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /absence-enseignants/:id : delete the "id" absenceEnseignant.
     *
     * @param id the id of the absenceEnseignant to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/absence-enseignants/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAbsenceEnseignant(@PathVariable Long id) {
        log.debug("REST request to delete AbsenceEnseignant : {}", id);
        absenceEnseignantRepository.delete(id);
        absenceEnseignantSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("absenceEnseignant", id.toString())).build();
    }

    /**
     * SEARCH  /_search/absence-enseignants?query=:query : search for the absenceEnseignant corresponding
     * to the query.
     *
     * @param query the query of the absenceEnseignant search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/absence-enseignants",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AbsenceEnseignant> searchAbsenceEnseignants(@RequestParam String query) {
        log.debug("REST request to search AbsenceEnseignants for query {}", query);
        return StreamSupport
            .stream(absenceEnseignantSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
