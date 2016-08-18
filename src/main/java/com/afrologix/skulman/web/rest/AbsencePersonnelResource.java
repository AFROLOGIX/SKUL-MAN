package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.AbsencePersonnel;
import com.afrologix.skulman.repository.AbsencePersonnelRepository;
import com.afrologix.skulman.repository.search.AbsencePersonnelSearchRepository;
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
 * REST controller for managing AbsencePersonnel.
 */
@RestController
@RequestMapping("/api")
public class AbsencePersonnelResource {

    private final Logger log = LoggerFactory.getLogger(AbsencePersonnelResource.class);
        
    @Inject
    private AbsencePersonnelRepository absencePersonnelRepository;
    
    @Inject
    private AbsencePersonnelSearchRepository absencePersonnelSearchRepository;
    
    /**
     * POST  /absence-personnels : Create a new absencePersonnel.
     *
     * @param absencePersonnel the absencePersonnel to create
     * @return the ResponseEntity with status 201 (Created) and with body the new absencePersonnel, or with status 400 (Bad Request) if the absencePersonnel has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/absence-personnels",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AbsencePersonnel> createAbsencePersonnel(@Valid @RequestBody AbsencePersonnel absencePersonnel) throws URISyntaxException {
        log.debug("REST request to save AbsencePersonnel : {}", absencePersonnel);
        if (absencePersonnel.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("absencePersonnel", "idexists", "A new absencePersonnel cannot already have an ID")).body(null);
        }
        AbsencePersonnel result = absencePersonnelRepository.save(absencePersonnel);
        absencePersonnelSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/absence-personnels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("absencePersonnel", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /absence-personnels : Updates an existing absencePersonnel.
     *
     * @param absencePersonnel the absencePersonnel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated absencePersonnel,
     * or with status 400 (Bad Request) if the absencePersonnel is not valid,
     * or with status 500 (Internal Server Error) if the absencePersonnel couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/absence-personnels",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AbsencePersonnel> updateAbsencePersonnel(@Valid @RequestBody AbsencePersonnel absencePersonnel) throws URISyntaxException {
        log.debug("REST request to update AbsencePersonnel : {}", absencePersonnel);
        if (absencePersonnel.getId() == null) {
            return createAbsencePersonnel(absencePersonnel);
        }
        AbsencePersonnel result = absencePersonnelRepository.save(absencePersonnel);
        absencePersonnelSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("absencePersonnel", absencePersonnel.getId().toString()))
            .body(result);
    }

    /**
     * GET  /absence-personnels : get all the absencePersonnels.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of absencePersonnels in body
     */
    @RequestMapping(value = "/absence-personnels",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AbsencePersonnel> getAllAbsencePersonnels() {
        log.debug("REST request to get all AbsencePersonnels");
        List<AbsencePersonnel> absencePersonnels = absencePersonnelRepository.findAll();
        return absencePersonnels;
    }

    /**
     * GET  /absence-personnels/:id : get the "id" absencePersonnel.
     *
     * @param id the id of the absencePersonnel to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the absencePersonnel, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/absence-personnels/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AbsencePersonnel> getAbsencePersonnel(@PathVariable Long id) {
        log.debug("REST request to get AbsencePersonnel : {}", id);
        AbsencePersonnel absencePersonnel = absencePersonnelRepository.findOne(id);
        return Optional.ofNullable(absencePersonnel)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /absence-personnels/:id : delete the "id" absencePersonnel.
     *
     * @param id the id of the absencePersonnel to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/absence-personnels/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAbsencePersonnel(@PathVariable Long id) {
        log.debug("REST request to delete AbsencePersonnel : {}", id);
        absencePersonnelRepository.delete(id);
        absencePersonnelSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("absencePersonnel", id.toString())).build();
    }

    /**
     * SEARCH  /_search/absence-personnels?query=:query : search for the absencePersonnel corresponding
     * to the query.
     *
     * @param query the query of the absencePersonnel search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/absence-personnels",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AbsencePersonnel> searchAbsencePersonnels(@RequestParam String query) {
        log.debug("REST request to search AbsencePersonnels for query {}", query);
        return StreamSupport
            .stream(absencePersonnelSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
