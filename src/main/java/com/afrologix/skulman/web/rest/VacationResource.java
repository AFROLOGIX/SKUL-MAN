package com.afrologix.skulman.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.afrologix.skulman.domain.Vacation;
import com.afrologix.skulman.repository.VacationRepository;
import com.afrologix.skulman.repository.search.VacationSearchRepository;
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
 * REST controller for managing Vacation.
 */
@RestController
@RequestMapping("/api")
public class VacationResource {

    private final Logger log = LoggerFactory.getLogger(VacationResource.class);
        
    @Inject
    private VacationRepository vacationRepository;
    
    @Inject
    private VacationSearchRepository vacationSearchRepository;
    
    /**
     * POST  /vacations : Create a new vacation.
     *
     * @param vacation the vacation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new vacation, or with status 400 (Bad Request) if the vacation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/vacations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Vacation> createVacation(@Valid @RequestBody Vacation vacation) throws URISyntaxException {
        log.debug("REST request to save Vacation : {}", vacation);
        if (vacation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("vacation", "idexists", "A new vacation cannot already have an ID")).body(null);
        }
        Vacation result = vacationRepository.save(vacation);
        vacationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/vacations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("vacation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /vacations : Updates an existing vacation.
     *
     * @param vacation the vacation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated vacation,
     * or with status 400 (Bad Request) if the vacation is not valid,
     * or with status 500 (Internal Server Error) if the vacation couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/vacations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Vacation> updateVacation(@Valid @RequestBody Vacation vacation) throws URISyntaxException {
        log.debug("REST request to update Vacation : {}", vacation);
        if (vacation.getId() == null) {
            return createVacation(vacation);
        }
        Vacation result = vacationRepository.save(vacation);
        vacationSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("vacation", vacation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /vacations : get all the vacations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of vacations in body
     */
    @RequestMapping(value = "/vacations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Vacation> getAllVacations() {
        log.debug("REST request to get all Vacations");
        List<Vacation> vacations = vacationRepository.findAll();
        return vacations;
    }

    /**
     * GET  /vacations/:id : get the "id" vacation.
     *
     * @param id the id of the vacation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the vacation, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/vacations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Vacation> getVacation(@PathVariable Long id) {
        log.debug("REST request to get Vacation : {}", id);
        Vacation vacation = vacationRepository.findOne(id);
        return Optional.ofNullable(vacation)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /vacations/:id : delete the "id" vacation.
     *
     * @param id the id of the vacation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/vacations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteVacation(@PathVariable Long id) {
        log.debug("REST request to delete Vacation : {}", id);
        vacationRepository.delete(id);
        vacationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("vacation", id.toString())).build();
    }

    /**
     * SEARCH  /_search/vacations?query=:query : search for the vacation corresponding
     * to the query.
     *
     * @param query the query of the vacation search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/vacations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Vacation> searchVacations(@RequestParam String query) {
        log.debug("REST request to search Vacations for query {}", query);
        return StreamSupport
            .stream(vacationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
