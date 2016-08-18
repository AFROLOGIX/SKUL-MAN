package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.Personnel;
import com.afrologix.skulman.repository.PersonnelRepository;
import com.afrologix.skulman.repository.search.PersonnelSearchRepository;
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
 * REST controller for managing Personnel.
 */
@RestController
@RequestMapping("/api")
public class PersonnelResource {

    private final Logger log = LoggerFactory.getLogger(PersonnelResource.class);
        
    @Inject
    private PersonnelRepository personnelRepository;
    
    @Inject
    private PersonnelSearchRepository personnelSearchRepository;
    
    /**
     * POST  /personnels : Create a new personnel.
     *
     * @param personnel the personnel to create
     * @return the ResponseEntity with status 201 (Created) and with body the new personnel, or with status 400 (Bad Request) if the personnel has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/personnels",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Personnel> createPersonnel(@Valid @RequestBody Personnel personnel) throws URISyntaxException {
        log.debug("REST request to save Personnel : {}", personnel);
        if (personnel.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("personnel", "idexists", "A new personnel cannot already have an ID")).body(null);
        }
        Personnel result = personnelRepository.save(personnel);
        personnelSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/personnels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("personnel", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /personnels : Updates an existing personnel.
     *
     * @param personnel the personnel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated personnel,
     * or with status 400 (Bad Request) if the personnel is not valid,
     * or with status 500 (Internal Server Error) if the personnel couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/personnels",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Personnel> updatePersonnel(@Valid @RequestBody Personnel personnel) throws URISyntaxException {
        log.debug("REST request to update Personnel : {}", personnel);
        if (personnel.getId() == null) {
            return createPersonnel(personnel);
        }
        Personnel result = personnelRepository.save(personnel);
        personnelSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("personnel", personnel.getId().toString()))
            .body(result);
    }

    /**
     * GET  /personnels : get all the personnels.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of personnels in body
     */
    @RequestMapping(value = "/personnels",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Personnel> getAllPersonnels() {
        log.debug("REST request to get all Personnels");
        List<Personnel> personnels = personnelRepository.findAll();
        return personnels;
    }

    /**
     * GET  /personnels/:id : get the "id" personnel.
     *
     * @param id the id of the personnel to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the personnel, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/personnels/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Personnel> getPersonnel(@PathVariable Long id) {
        log.debug("REST request to get Personnel : {}", id);
        Personnel personnel = personnelRepository.findOne(id);
        return Optional.ofNullable(personnel)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /personnels/:id : delete the "id" personnel.
     *
     * @param id the id of the personnel to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/personnels/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePersonnel(@PathVariable Long id) {
        log.debug("REST request to delete Personnel : {}", id);
        personnelRepository.delete(id);
        personnelSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("personnel", id.toString())).build();
    }

    /**
     * SEARCH  /_search/personnels?query=:query : search for the personnel corresponding
     * to the query.
     *
     * @param query the query of the personnel search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/personnels",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Personnel> searchPersonnels(@RequestParam String query) {
        log.debug("REST request to search Personnels for query {}", query);
        return StreamSupport
            .stream(personnelSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
