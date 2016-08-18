package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.MoyenneTableauHonneur;
import com.afrologix.skulman.repository.MoyenneTableauHonneurRepository;
import com.afrologix.skulman.repository.search.MoyenneTableauHonneurSearchRepository;
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
 * REST controller for managing MoyenneTableauHonneur.
 */
@RestController
@RequestMapping("/api")
public class MoyenneTableauHonneurResource {

    private final Logger log = LoggerFactory.getLogger(MoyenneTableauHonneurResource.class);
        
    @Inject
    private MoyenneTableauHonneurRepository moyenneTableauHonneurRepository;
    
    @Inject
    private MoyenneTableauHonneurSearchRepository moyenneTableauHonneurSearchRepository;
    
    /**
     * POST  /moyenne-tableau-honneurs : Create a new moyenneTableauHonneur.
     *
     * @param moyenneTableauHonneur the moyenneTableauHonneur to create
     * @return the ResponseEntity with status 201 (Created) and with body the new moyenneTableauHonneur, or with status 400 (Bad Request) if the moyenneTableauHonneur has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/moyenne-tableau-honneurs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MoyenneTableauHonneur> createMoyenneTableauHonneur(@RequestBody MoyenneTableauHonneur moyenneTableauHonneur) throws URISyntaxException {
        log.debug("REST request to save MoyenneTableauHonneur : {}", moyenneTableauHonneur);
        if (moyenneTableauHonneur.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("moyenneTableauHonneur", "idexists", "A new moyenneTableauHonneur cannot already have an ID")).body(null);
        }
        MoyenneTableauHonneur result = moyenneTableauHonneurRepository.save(moyenneTableauHonneur);
        moyenneTableauHonneurSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/moyenne-tableau-honneurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("moyenneTableauHonneur", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /moyenne-tableau-honneurs : Updates an existing moyenneTableauHonneur.
     *
     * @param moyenneTableauHonneur the moyenneTableauHonneur to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated moyenneTableauHonneur,
     * or with status 400 (Bad Request) if the moyenneTableauHonneur is not valid,
     * or with status 500 (Internal Server Error) if the moyenneTableauHonneur couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/moyenne-tableau-honneurs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MoyenneTableauHonneur> updateMoyenneTableauHonneur(@RequestBody MoyenneTableauHonneur moyenneTableauHonneur) throws URISyntaxException {
        log.debug("REST request to update MoyenneTableauHonneur : {}", moyenneTableauHonneur);
        if (moyenneTableauHonneur.getId() == null) {
            return createMoyenneTableauHonneur(moyenneTableauHonneur);
        }
        MoyenneTableauHonneur result = moyenneTableauHonneurRepository.save(moyenneTableauHonneur);
        moyenneTableauHonneurSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("moyenneTableauHonneur", moyenneTableauHonneur.getId().toString()))
            .body(result);
    }

    /**
     * GET  /moyenne-tableau-honneurs : get all the moyenneTableauHonneurs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of moyenneTableauHonneurs in body
     */
    @RequestMapping(value = "/moyenne-tableau-honneurs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<MoyenneTableauHonneur> getAllMoyenneTableauHonneurs() {
        log.debug("REST request to get all MoyenneTableauHonneurs");
        List<MoyenneTableauHonneur> moyenneTableauHonneurs = moyenneTableauHonneurRepository.findAll();
        return moyenneTableauHonneurs;
    }

    /**
     * GET  /moyenne-tableau-honneurs/:id : get the "id" moyenneTableauHonneur.
     *
     * @param id the id of the moyenneTableauHonneur to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the moyenneTableauHonneur, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/moyenne-tableau-honneurs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MoyenneTableauHonneur> getMoyenneTableauHonneur(@PathVariable Long id) {
        log.debug("REST request to get MoyenneTableauHonneur : {}", id);
        MoyenneTableauHonneur moyenneTableauHonneur = moyenneTableauHonneurRepository.findOne(id);
        return Optional.ofNullable(moyenneTableauHonneur)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /moyenne-tableau-honneurs/:id : delete the "id" moyenneTableauHonneur.
     *
     * @param id the id of the moyenneTableauHonneur to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/moyenne-tableau-honneurs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMoyenneTableauHonneur(@PathVariable Long id) {
        log.debug("REST request to delete MoyenneTableauHonneur : {}", id);
        moyenneTableauHonneurRepository.delete(id);
        moyenneTableauHonneurSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("moyenneTableauHonneur", id.toString())).build();
    }

    /**
     * SEARCH  /_search/moyenne-tableau-honneurs?query=:query : search for the moyenneTableauHonneur corresponding
     * to the query.
     *
     * @param query the query of the moyenneTableauHonneur search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/moyenne-tableau-honneurs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<MoyenneTableauHonneur> searchMoyenneTableauHonneurs(@RequestParam String query) {
        log.debug("REST request to search MoyenneTableauHonneurs for query {}", query);
        return StreamSupport
            .stream(moyenneTableauHonneurSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
