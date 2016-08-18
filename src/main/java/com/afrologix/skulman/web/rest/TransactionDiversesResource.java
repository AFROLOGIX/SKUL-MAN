package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.TransactionDiverses;
import com.afrologix.skulman.repository.TransactionDiversesRepository;
import com.afrologix.skulman.repository.search.TransactionDiversesSearchRepository;
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
 * REST controller for managing TransactionDiverses.
 */
@RestController
@RequestMapping("/api")
public class TransactionDiversesResource {

    private final Logger log = LoggerFactory.getLogger(TransactionDiversesResource.class);
        
    @Inject
    private TransactionDiversesRepository transactionDiversesRepository;
    
    @Inject
    private TransactionDiversesSearchRepository transactionDiversesSearchRepository;
    
    /**
     * POST  /transaction-diverses : Create a new transactionDiverses.
     *
     * @param transactionDiverses the transactionDiverses to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transactionDiverses, or with status 400 (Bad Request) if the transactionDiverses has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/transaction-diverses",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TransactionDiverses> createTransactionDiverses(@Valid @RequestBody TransactionDiverses transactionDiverses) throws URISyntaxException {
        log.debug("REST request to save TransactionDiverses : {}", transactionDiverses);
        if (transactionDiverses.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("transactionDiverses", "idexists", "A new transactionDiverses cannot already have an ID")).body(null);
        }
        TransactionDiverses result = transactionDiversesRepository.save(transactionDiverses);
        transactionDiversesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/transaction-diverses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("transactionDiverses", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /transaction-diverses : Updates an existing transactionDiverses.
     *
     * @param transactionDiverses the transactionDiverses to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transactionDiverses,
     * or with status 400 (Bad Request) if the transactionDiverses is not valid,
     * or with status 500 (Internal Server Error) if the transactionDiverses couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/transaction-diverses",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TransactionDiverses> updateTransactionDiverses(@Valid @RequestBody TransactionDiverses transactionDiverses) throws URISyntaxException {
        log.debug("REST request to update TransactionDiverses : {}", transactionDiverses);
        if (transactionDiverses.getId() == null) {
            return createTransactionDiverses(transactionDiverses);
        }
        TransactionDiverses result = transactionDiversesRepository.save(transactionDiverses);
        transactionDiversesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("transactionDiverses", transactionDiverses.getId().toString()))
            .body(result);
    }

    /**
     * GET  /transaction-diverses : get all the transactionDiverses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of transactionDiverses in body
     */
    @RequestMapping(value = "/transaction-diverses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TransactionDiverses> getAllTransactionDiverses() {
        log.debug("REST request to get all TransactionDiverses");
        List<TransactionDiverses> transactionDiverses = transactionDiversesRepository.findAll();
        return transactionDiverses;
    }

    /**
     * GET  /transaction-diverses/:id : get the "id" transactionDiverses.
     *
     * @param id the id of the transactionDiverses to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transactionDiverses, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/transaction-diverses/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TransactionDiverses> getTransactionDiverses(@PathVariable Long id) {
        log.debug("REST request to get TransactionDiverses : {}", id);
        TransactionDiverses transactionDiverses = transactionDiversesRepository.findOne(id);
        return Optional.ofNullable(transactionDiverses)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /transaction-diverses/:id : delete the "id" transactionDiverses.
     *
     * @param id the id of the transactionDiverses to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/transaction-diverses/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTransactionDiverses(@PathVariable Long id) {
        log.debug("REST request to delete TransactionDiverses : {}", id);
        transactionDiversesRepository.delete(id);
        transactionDiversesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("transactionDiverses", id.toString())).build();
    }

    /**
     * SEARCH  /_search/transaction-diverses?query=:query : search for the transactionDiverses corresponding
     * to the query.
     *
     * @param query the query of the transactionDiverses search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/transaction-diverses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TransactionDiverses> searchTransactionDiverses(@RequestParam String query) {
        log.debug("REST request to search TransactionDiverses for query {}", query);
        return StreamSupport
            .stream(transactionDiversesSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
