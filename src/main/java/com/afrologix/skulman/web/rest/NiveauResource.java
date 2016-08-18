package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.Niveau;
import com.afrologix.skulman.repository.NiveauRepository;
import com.afrologix.skulman.repository.search.NiveauSearchRepository;
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
 * REST controller for managing Niveau.
 */
@RestController
@RequestMapping("/api")
public class NiveauResource {

    private final Logger log = LoggerFactory.getLogger(NiveauResource.class);
        
    @Inject
    private NiveauRepository niveauRepository;
    
    @Inject
    private NiveauSearchRepository niveauSearchRepository;
    
    /**
     * POST  /niveaus : Create a new niveau.
     *
     * @param niveau the niveau to create
     * @return the ResponseEntity with status 201 (Created) and with body the new niveau, or with status 400 (Bad Request) if the niveau has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/niveaus",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Niveau> createNiveau(@Valid @RequestBody Niveau niveau) throws URISyntaxException {
        log.debug("REST request to save Niveau : {}", niveau);
        if (niveau.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("niveau", "idexists", "A new niveau cannot already have an ID")).body(null);
        }
        Niveau result = niveauRepository.save(niveau);
        niveauSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/niveaus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("niveau", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /niveaus : Updates an existing niveau.
     *
     * @param niveau the niveau to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated niveau,
     * or with status 400 (Bad Request) if the niveau is not valid,
     * or with status 500 (Internal Server Error) if the niveau couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/niveaus",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Niveau> updateNiveau(@Valid @RequestBody Niveau niveau) throws URISyntaxException {
        log.debug("REST request to update Niveau : {}", niveau);
        if (niveau.getId() == null) {
            return createNiveau(niveau);
        }
        Niveau result = niveauRepository.save(niveau);
        niveauSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("niveau", niveau.getId().toString()))
            .body(result);
    }

    /**
     * GET  /niveaus : get all the niveaus.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of niveaus in body
     */
    @RequestMapping(value = "/niveaus",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Niveau> getAllNiveaus() {
        log.debug("REST request to get all Niveaus");
        List<Niveau> niveaus = niveauRepository.findAll();
        return niveaus;
    }

    /**
     * GET  /niveaus/:id : get the "id" niveau.
     *
     * @param id the id of the niveau to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the niveau, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/niveaus/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Niveau> getNiveau(@PathVariable Long id) {
        log.debug("REST request to get Niveau : {}", id);
        Niveau niveau = niveauRepository.findOne(id);
        return Optional.ofNullable(niveau)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /niveaus/:id : delete the "id" niveau.
     *
     * @param id the id of the niveau to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/niveaus/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteNiveau(@PathVariable Long id) {
        log.debug("REST request to delete Niveau : {}", id);
        niveauRepository.delete(id);
        niveauSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("niveau", id.toString())).build();
    }

    /**
     * SEARCH  /_search/niveaus?query=:query : search for the niveau corresponding
     * to the query.
     *
     * @param query the query of the niveau search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/niveaus",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Niveau> searchNiveaus(@RequestParam String query) {
        log.debug("REST request to search Niveaus for query {}", query);
        return StreamSupport
            .stream(niveauSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
