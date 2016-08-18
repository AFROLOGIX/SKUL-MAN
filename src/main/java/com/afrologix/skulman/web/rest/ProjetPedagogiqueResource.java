package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.ProjetPedagogique;
import com.afrologix.skulman.repository.ProjetPedagogiqueRepository;
import com.afrologix.skulman.repository.search.ProjetPedagogiqueSearchRepository;
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
 * REST controller for managing ProjetPedagogique.
 */
@RestController
@RequestMapping("/api")
public class ProjetPedagogiqueResource {

    private final Logger log = LoggerFactory.getLogger(ProjetPedagogiqueResource.class);
        
    @Inject
    private ProjetPedagogiqueRepository projetPedagogiqueRepository;
    
    @Inject
    private ProjetPedagogiqueSearchRepository projetPedagogiqueSearchRepository;
    
    /**
     * POST  /projet-pedagogiques : Create a new projetPedagogique.
     *
     * @param projetPedagogique the projetPedagogique to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projetPedagogique, or with status 400 (Bad Request) if the projetPedagogique has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/projet-pedagogiques",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjetPedagogique> createProjetPedagogique(@Valid @RequestBody ProjetPedagogique projetPedagogique) throws URISyntaxException {
        log.debug("REST request to save ProjetPedagogique : {}", projetPedagogique);
        if (projetPedagogique.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("projetPedagogique", "idexists", "A new projetPedagogique cannot already have an ID")).body(null);
        }
        ProjetPedagogique result = projetPedagogiqueRepository.save(projetPedagogique);
        projetPedagogiqueSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/projet-pedagogiques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("projetPedagogique", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /projet-pedagogiques : Updates an existing projetPedagogique.
     *
     * @param projetPedagogique the projetPedagogique to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projetPedagogique,
     * or with status 400 (Bad Request) if the projetPedagogique is not valid,
     * or with status 500 (Internal Server Error) if the projetPedagogique couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/projet-pedagogiques",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjetPedagogique> updateProjetPedagogique(@Valid @RequestBody ProjetPedagogique projetPedagogique) throws URISyntaxException {
        log.debug("REST request to update ProjetPedagogique : {}", projetPedagogique);
        if (projetPedagogique.getId() == null) {
            return createProjetPedagogique(projetPedagogique);
        }
        ProjetPedagogique result = projetPedagogiqueRepository.save(projetPedagogique);
        projetPedagogiqueSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("projetPedagogique", projetPedagogique.getId().toString()))
            .body(result);
    }

    /**
     * GET  /projet-pedagogiques : get all the projetPedagogiques.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of projetPedagogiques in body
     */
    @RequestMapping(value = "/projet-pedagogiques",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ProjetPedagogique> getAllProjetPedagogiques() {
        log.debug("REST request to get all ProjetPedagogiques");
        List<ProjetPedagogique> projetPedagogiques = projetPedagogiqueRepository.findAll();
        return projetPedagogiques;
    }

    /**
     * GET  /projet-pedagogiques/:id : get the "id" projetPedagogique.
     *
     * @param id the id of the projetPedagogique to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projetPedagogique, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/projet-pedagogiques/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjetPedagogique> getProjetPedagogique(@PathVariable Long id) {
        log.debug("REST request to get ProjetPedagogique : {}", id);
        ProjetPedagogique projetPedagogique = projetPedagogiqueRepository.findOne(id);
        return Optional.ofNullable(projetPedagogique)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /projet-pedagogiques/:id : delete the "id" projetPedagogique.
     *
     * @param id the id of the projetPedagogique to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/projet-pedagogiques/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProjetPedagogique(@PathVariable Long id) {
        log.debug("REST request to delete ProjetPedagogique : {}", id);
        projetPedagogiqueRepository.delete(id);
        projetPedagogiqueSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("projetPedagogique", id.toString())).build();
    }

    /**
     * SEARCH  /_search/projet-pedagogiques?query=:query : search for the projetPedagogique corresponding
     * to the query.
     *
     * @param query the query of the projetPedagogique search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/projet-pedagogiques",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ProjetPedagogique> searchProjetPedagogiques(@RequestParam String query) {
        log.debug("REST request to search ProjetPedagogiques for query {}", query);
        return StreamSupport
            .stream(projetPedagogiqueSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
