package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.LoginConnexion;
import com.afrologix.skulman.repository.LoginConnexionRepository;
import com.afrologix.skulman.repository.search.LoginConnexionSearchRepository;
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
 * REST controller for managing LoginConnexion.
 */
@RestController
@RequestMapping("/api")
public class LoginConnexionResource {

    private final Logger log = LoggerFactory.getLogger(LoginConnexionResource.class);
        
    @Inject
    private LoginConnexionRepository loginConnexionRepository;
    
    @Inject
    private LoginConnexionSearchRepository loginConnexionSearchRepository;
    
    /**
     * POST  /login-connexions : Create a new loginConnexion.
     *
     * @param loginConnexion the loginConnexion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new loginConnexion, or with status 400 (Bad Request) if the loginConnexion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/login-connexions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LoginConnexion> createLoginConnexion(@Valid @RequestBody LoginConnexion loginConnexion) throws URISyntaxException {
        log.debug("REST request to save LoginConnexion : {}", loginConnexion);
        if (loginConnexion.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("loginConnexion", "idexists", "A new loginConnexion cannot already have an ID")).body(null);
        }
        LoginConnexion result = loginConnexionRepository.save(loginConnexion);
        loginConnexionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/login-connexions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("loginConnexion", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /login-connexions : Updates an existing loginConnexion.
     *
     * @param loginConnexion the loginConnexion to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated loginConnexion,
     * or with status 400 (Bad Request) if the loginConnexion is not valid,
     * or with status 500 (Internal Server Error) if the loginConnexion couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/login-connexions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LoginConnexion> updateLoginConnexion(@Valid @RequestBody LoginConnexion loginConnexion) throws URISyntaxException {
        log.debug("REST request to update LoginConnexion : {}", loginConnexion);
        if (loginConnexion.getId() == null) {
            return createLoginConnexion(loginConnexion);
        }
        LoginConnexion result = loginConnexionRepository.save(loginConnexion);
        loginConnexionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("loginConnexion", loginConnexion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /login-connexions : get all the loginConnexions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of loginConnexions in body
     */
    @RequestMapping(value = "/login-connexions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<LoginConnexion> getAllLoginConnexions() {
        log.debug("REST request to get all LoginConnexions");
        List<LoginConnexion> loginConnexions = loginConnexionRepository.findAll();
        return loginConnexions;
    }

    /**
     * GET  /login-connexions/:id : get the "id" loginConnexion.
     *
     * @param id the id of the loginConnexion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the loginConnexion, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/login-connexions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LoginConnexion> getLoginConnexion(@PathVariable Long id) {
        log.debug("REST request to get LoginConnexion : {}", id);
        LoginConnexion loginConnexion = loginConnexionRepository.findOne(id);
        return Optional.ofNullable(loginConnexion)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /login-connexions/:id : delete the "id" loginConnexion.
     *
     * @param id the id of the loginConnexion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/login-connexions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLoginConnexion(@PathVariable Long id) {
        log.debug("REST request to delete LoginConnexion : {}", id);
        loginConnexionRepository.delete(id);
        loginConnexionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("loginConnexion", id.toString())).build();
    }

    /**
     * SEARCH  /_search/login-connexions?query=:query : search for the loginConnexion corresponding
     * to the query.
     *
     * @param query the query of the loginConnexion search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/login-connexions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<LoginConnexion> searchLoginConnexions(@RequestParam String query) {
        log.debug("REST request to search LoginConnexions for query {}", query);
        return StreamSupport
            .stream(loginConnexionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
