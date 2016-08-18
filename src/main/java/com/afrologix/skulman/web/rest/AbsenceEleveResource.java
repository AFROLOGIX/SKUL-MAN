package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.AbsenceEleve;
import com.afrologix.skulman.repository.AbsenceEleveRepository;
import com.afrologix.skulman.repository.search.AbsenceEleveSearchRepository;
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
 * REST controller for managing AbsenceEleve.
 */
@RestController
@RequestMapping("/api")
public class AbsenceEleveResource {

    private final Logger log = LoggerFactory.getLogger(AbsenceEleveResource.class);
        
    @Inject
    private AbsenceEleveRepository absenceEleveRepository;
    
    @Inject
    private AbsenceEleveSearchRepository absenceEleveSearchRepository;
    
    /**
     * POST  /absence-eleves : Create a new absenceEleve.
     *
     * @param absenceEleve the absenceEleve to create
     * @return the ResponseEntity with status 201 (Created) and with body the new absenceEleve, or with status 400 (Bad Request) if the absenceEleve has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/absence-eleves",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AbsenceEleve> createAbsenceEleve(@Valid @RequestBody AbsenceEleve absenceEleve) throws URISyntaxException {
        log.debug("REST request to save AbsenceEleve : {}", absenceEleve);
        if (absenceEleve.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("absenceEleve", "idexists", "A new absenceEleve cannot already have an ID")).body(null);
        }
        AbsenceEleve result = absenceEleveRepository.save(absenceEleve);
        absenceEleveSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/absence-eleves/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("absenceEleve", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /absence-eleves : Updates an existing absenceEleve.
     *
     * @param absenceEleve the absenceEleve to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated absenceEleve,
     * or with status 400 (Bad Request) if the absenceEleve is not valid,
     * or with status 500 (Internal Server Error) if the absenceEleve couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/absence-eleves",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AbsenceEleve> updateAbsenceEleve(@Valid @RequestBody AbsenceEleve absenceEleve) throws URISyntaxException {
        log.debug("REST request to update AbsenceEleve : {}", absenceEleve);
        if (absenceEleve.getId() == null) {
            return createAbsenceEleve(absenceEleve);
        }
        AbsenceEleve result = absenceEleveRepository.save(absenceEleve);
        absenceEleveSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("absenceEleve", absenceEleve.getId().toString()))
            .body(result);
    }

    /**
     * GET  /absence-eleves : get all the absenceEleves.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of absenceEleves in body
     */
    @RequestMapping(value = "/absence-eleves",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AbsenceEleve> getAllAbsenceEleves() {
        log.debug("REST request to get all AbsenceEleves");
        List<AbsenceEleve> absenceEleves = absenceEleveRepository.findAll();
        return absenceEleves;
    }

    /**
     * GET  /absence-eleves/:id : get the "id" absenceEleve.
     *
     * @param id the id of the absenceEleve to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the absenceEleve, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/absence-eleves/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AbsenceEleve> getAbsenceEleve(@PathVariable Long id) {
        log.debug("REST request to get AbsenceEleve : {}", id);
        AbsenceEleve absenceEleve = absenceEleveRepository.findOne(id);
        return Optional.ofNullable(absenceEleve)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /absence-eleves/:id : delete the "id" absenceEleve.
     *
     * @param id the id of the absenceEleve to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/absence-eleves/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAbsenceEleve(@PathVariable Long id) {
        log.debug("REST request to delete AbsenceEleve : {}", id);
        absenceEleveRepository.delete(id);
        absenceEleveSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("absenceEleve", id.toString())).build();
    }

    /**
     * SEARCH  /_search/absence-eleves?query=:query : search for the absenceEleve corresponding
     * to the query.
     *
     * @param query the query of the absenceEleve search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/absence-eleves",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AbsenceEleve> searchAbsenceEleves(@RequestParam String query) {
        log.debug("REST request to search AbsenceEleves for query {}", query);
        return StreamSupport
            .stream(absenceEleveSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
