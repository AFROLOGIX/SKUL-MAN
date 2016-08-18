package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.LoginAction;
import com.afrologix.skulman.repository.LoginActionRepository;
import com.afrologix.skulman.repository.search.LoginActionSearchRepository;
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
 * REST controller for managing LoginAction.
 */
@RestController
@RequestMapping("/api")
public class LoginActionResource {

    private final Logger log = LoggerFactory.getLogger(LoginActionResource.class);
        
    @Inject
    private LoginActionRepository loginActionRepository;
    
    @Inject
    private LoginActionSearchRepository loginActionSearchRepository;
    
    /**
     * POST  /login-actions : Create a new loginAction.
     *
     * @param loginAction the loginAction to create
     * @return the ResponseEntity with status 201 (Created) and with body the new loginAction, or with status 400 (Bad Request) if the loginAction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/login-actions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LoginAction> createLoginAction(@Valid @RequestBody LoginAction loginAction) throws URISyntaxException {
        log.debug("REST request to save LoginAction : {}", loginAction);
        if (loginAction.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("loginAction", "idexists", "A new loginAction cannot already have an ID")).body(null);
        }
        LoginAction result = loginActionRepository.save(loginAction);
        loginActionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/login-actions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("loginAction", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /login-actions : Updates an existing loginAction.
     *
     * @param loginAction the loginAction to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated loginAction,
     * or with status 400 (Bad Request) if the loginAction is not valid,
     * or with status 500 (Internal Server Error) if the loginAction couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/login-actions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LoginAction> updateLoginAction(@Valid @RequestBody LoginAction loginAction) throws URISyntaxException {
        log.debug("REST request to update LoginAction : {}", loginAction);
        if (loginAction.getId() == null) {
            return createLoginAction(loginAction);
        }
        LoginAction result = loginActionRepository.save(loginAction);
        loginActionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("loginAction", loginAction.getId().toString()))
            .body(result);
    }

    /**
     * GET  /login-actions : get all the loginActions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of loginActions in body
     */
    @RequestMapping(value = "/login-actions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<LoginAction> getAllLoginActions() {
        log.debug("REST request to get all LoginActions");
        List<LoginAction> loginActions = loginActionRepository.findAll();
        return loginActions;
    }

    /**
     * GET  /login-actions/:id : get the "id" loginAction.
     *
     * @param id the id of the loginAction to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the loginAction, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/login-actions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LoginAction> getLoginAction(@PathVariable Long id) {
        log.debug("REST request to get LoginAction : {}", id);
        LoginAction loginAction = loginActionRepository.findOne(id);
        return Optional.ofNullable(loginAction)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /login-actions/:id : delete the "id" loginAction.
     *
     * @param id the id of the loginAction to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/login-actions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLoginAction(@PathVariable Long id) {
        log.debug("REST request to delete LoginAction : {}", id);
        loginActionRepository.delete(id);
        loginActionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("loginAction", id.toString())).build();
    }

    /**
     * SEARCH  /_search/login-actions?query=:query : search for the loginAction corresponding
     * to the query.
     *
     * @param query the query of the loginAction search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/login-actions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<LoginAction> searchLoginActions(@RequestParam String query) {
        log.debug("REST request to search LoginActions for query {}", query);
        return StreamSupport
            .stream(loginActionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
