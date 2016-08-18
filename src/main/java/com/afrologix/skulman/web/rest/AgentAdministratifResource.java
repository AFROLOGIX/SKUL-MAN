package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.AgentAdministratif;
import com.afrologix.skulman.repository.AgentAdministratifRepository;
import com.afrologix.skulman.repository.search.AgentAdministratifSearchRepository;
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
 * REST controller for managing AgentAdministratif.
 */
@RestController
@RequestMapping("/api")
public class AgentAdministratifResource {

    private final Logger log = LoggerFactory.getLogger(AgentAdministratifResource.class);
        
    @Inject
    private AgentAdministratifRepository agentAdministratifRepository;
    
    @Inject
    private AgentAdministratifSearchRepository agentAdministratifSearchRepository;
    
    /**
     * POST  /agent-administratifs : Create a new agentAdministratif.
     *
     * @param agentAdministratif the agentAdministratif to create
     * @return the ResponseEntity with status 201 (Created) and with body the new agentAdministratif, or with status 400 (Bad Request) if the agentAdministratif has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/agent-administratifs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AgentAdministratif> createAgentAdministratif(@Valid @RequestBody AgentAdministratif agentAdministratif) throws URISyntaxException {
        log.debug("REST request to save AgentAdministratif : {}", agentAdministratif);
        if (agentAdministratif.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("agentAdministratif", "idexists", "A new agentAdministratif cannot already have an ID")).body(null);
        }
        AgentAdministratif result = agentAdministratifRepository.save(agentAdministratif);
        agentAdministratifSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/agent-administratifs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("agentAdministratif", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /agent-administratifs : Updates an existing agentAdministratif.
     *
     * @param agentAdministratif the agentAdministratif to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated agentAdministratif,
     * or with status 400 (Bad Request) if the agentAdministratif is not valid,
     * or with status 500 (Internal Server Error) if the agentAdministratif couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/agent-administratifs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AgentAdministratif> updateAgentAdministratif(@Valid @RequestBody AgentAdministratif agentAdministratif) throws URISyntaxException {
        log.debug("REST request to update AgentAdministratif : {}", agentAdministratif);
        if (agentAdministratif.getId() == null) {
            return createAgentAdministratif(agentAdministratif);
        }
        AgentAdministratif result = agentAdministratifRepository.save(agentAdministratif);
        agentAdministratifSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("agentAdministratif", agentAdministratif.getId().toString()))
            .body(result);
    }

    /**
     * GET  /agent-administratifs : get all the agentAdministratifs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of agentAdministratifs in body
     */
    @RequestMapping(value = "/agent-administratifs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AgentAdministratif> getAllAgentAdministratifs() {
        log.debug("REST request to get all AgentAdministratifs");
        List<AgentAdministratif> agentAdministratifs = agentAdministratifRepository.findAll();
        return agentAdministratifs;
    }

    /**
     * GET  /agent-administratifs/:id : get the "id" agentAdministratif.
     *
     * @param id the id of the agentAdministratif to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the agentAdministratif, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/agent-administratifs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AgentAdministratif> getAgentAdministratif(@PathVariable Long id) {
        log.debug("REST request to get AgentAdministratif : {}", id);
        AgentAdministratif agentAdministratif = agentAdministratifRepository.findOne(id);
        return Optional.ofNullable(agentAdministratif)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /agent-administratifs/:id : delete the "id" agentAdministratif.
     *
     * @param id the id of the agentAdministratif to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/agent-administratifs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAgentAdministratif(@PathVariable Long id) {
        log.debug("REST request to delete AgentAdministratif : {}", id);
        agentAdministratifRepository.delete(id);
        agentAdministratifSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("agentAdministratif", id.toString())).build();
    }

    /**
     * SEARCH  /_search/agent-administratifs?query=:query : search for the agentAdministratif corresponding
     * to the query.
     *
     * @param query the query of the agentAdministratif search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/agent-administratifs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AgentAdministratif> searchAgentAdministratifs(@RequestParam String query) {
        log.debug("REST request to search AgentAdministratifs for query {}", query);
        return StreamSupport
            .stream(agentAdministratifSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
