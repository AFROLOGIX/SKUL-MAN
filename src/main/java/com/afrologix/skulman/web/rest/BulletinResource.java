package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.Bulletin;
import com.afrologix.skulman.repository.BulletinRepository;
import com.afrologix.skulman.repository.search.BulletinSearchRepository;
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
 * REST controller for managing Bulletin.
 */
@RestController
@RequestMapping("/api")
public class BulletinResource {

    private final Logger log = LoggerFactory.getLogger(BulletinResource.class);
        
    @Inject
    private BulletinRepository bulletinRepository;
    
    @Inject
    private BulletinSearchRepository bulletinSearchRepository;
    
    /**
     * POST  /bulletins : Create a new bulletin.
     *
     * @param bulletin the bulletin to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bulletin, or with status 400 (Bad Request) if the bulletin has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/bulletins",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Bulletin> createBulletin(@Valid @RequestBody Bulletin bulletin) throws URISyntaxException {
        log.debug("REST request to save Bulletin : {}", bulletin);
        if (bulletin.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bulletin", "idexists", "A new bulletin cannot already have an ID")).body(null);
        }
        Bulletin result = bulletinRepository.save(bulletin);
        bulletinSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/bulletins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("bulletin", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bulletins : Updates an existing bulletin.
     *
     * @param bulletin the bulletin to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bulletin,
     * or with status 400 (Bad Request) if the bulletin is not valid,
     * or with status 500 (Internal Server Error) if the bulletin couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/bulletins",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Bulletin> updateBulletin(@Valid @RequestBody Bulletin bulletin) throws URISyntaxException {
        log.debug("REST request to update Bulletin : {}", bulletin);
        if (bulletin.getId() == null) {
            return createBulletin(bulletin);
        }
        Bulletin result = bulletinRepository.save(bulletin);
        bulletinSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("bulletin", bulletin.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bulletins : get all the bulletins.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of bulletins in body
     */
    @RequestMapping(value = "/bulletins",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Bulletin> getAllBulletins() {
        log.debug("REST request to get all Bulletins");
        List<Bulletin> bulletins = bulletinRepository.findAll();
        return bulletins;
    }

    /**
     * GET  /bulletins/:id : get the "id" bulletin.
     *
     * @param id the id of the bulletin to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bulletin, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/bulletins/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Bulletin> getBulletin(@PathVariable Long id) {
        log.debug("REST request to get Bulletin : {}", id);
        Bulletin bulletin = bulletinRepository.findOne(id);
        return Optional.ofNullable(bulletin)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bulletins/:id : delete the "id" bulletin.
     *
     * @param id the id of the bulletin to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/bulletins/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBulletin(@PathVariable Long id) {
        log.debug("REST request to delete Bulletin : {}", id);
        bulletinRepository.delete(id);
        bulletinSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bulletin", id.toString())).build();
    }

    /**
     * SEARCH  /_search/bulletins?query=:query : search for the bulletin corresponding
     * to the query.
     *
     * @param query the query of the bulletin search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/bulletins",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Bulletin> searchBulletins(@RequestParam String query) {
        log.debug("REST request to search Bulletins for query {}", query);
        return StreamSupport
            .stream(bulletinSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
