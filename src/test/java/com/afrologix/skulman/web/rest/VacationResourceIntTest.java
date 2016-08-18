package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Vacation;
import com.afrologix.skulman.repository.VacationRepository;
import com.afrologix.skulman.repository.search.VacationSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the VacationResource REST controller.
 *
 * @see VacationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class VacationResourceIntTest {

    private static final String DEFAULT_TYPE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_PERIODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_PERIODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_CLASSE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CLASSE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_ANNEE_SCOLAIRE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ANNEE_SCOLAIRE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private VacationRepository vacationRepository;

    @Inject
    private VacationSearchRepository vacationSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restVacationMockMvc;

    private Vacation vacation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VacationResource vacationResource = new VacationResource();
        ReflectionTestUtils.setField(vacationResource, "vacationSearchRepository", vacationSearchRepository);
        ReflectionTestUtils.setField(vacationResource, "vacationRepository", vacationRepository);
        this.restVacationMockMvc = MockMvcBuilders.standaloneSetup(vacationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        vacationSearchRepository.deleteAll();
        vacation = new Vacation();
        vacation.setType(DEFAULT_TYPE);
        vacation.setDate(DEFAULT_DATE);
        vacation.setPeriode(DEFAULT_PERIODE);
        vacation.setClasse(DEFAULT_CLASSE);
        vacation.setAnneeScolaire(DEFAULT_ANNEE_SCOLAIRE);
    }

    @Test
    @Transactional
    public void createVacation() throws Exception {
        int databaseSizeBeforeCreate = vacationRepository.findAll().size();

        // Create the Vacation

        restVacationMockMvc.perform(post("/api/vacations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(vacation)))
                .andExpect(status().isCreated());

        // Validate the Vacation in the database
        List<Vacation> vacations = vacationRepository.findAll();
        assertThat(vacations).hasSize(databaseSizeBeforeCreate + 1);
        Vacation testVacation = vacations.get(vacations.size() - 1);
        assertThat(testVacation.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testVacation.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testVacation.getPeriode()).isEqualTo(DEFAULT_PERIODE);
        assertThat(testVacation.getClasse()).isEqualTo(DEFAULT_CLASSE);
        assertThat(testVacation.getAnneeScolaire()).isEqualTo(DEFAULT_ANNEE_SCOLAIRE);

        // Validate the Vacation in ElasticSearch
        Vacation vacationEs = vacationSearchRepository.findOne(testVacation.getId());
        assertThat(vacationEs).isEqualToComparingFieldByField(testVacation);
    }

    @Test
    @Transactional
    public void getAllVacations() throws Exception {
        // Initialize the database
        vacationRepository.saveAndFlush(vacation);

        // Get all the vacations
        restVacationMockMvc.perform(get("/api/vacations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(vacation.getId().intValue())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].periode").value(hasItem(DEFAULT_PERIODE.toString())))
                .andExpect(jsonPath("$.[*].classe").value(hasItem(DEFAULT_CLASSE.toString())))
                .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())));
    }

    @Test
    @Transactional
    public void getVacation() throws Exception {
        // Initialize the database
        vacationRepository.saveAndFlush(vacation);

        // Get the vacation
        restVacationMockMvc.perform(get("/api/vacations/{id}", vacation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(vacation.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.periode").value(DEFAULT_PERIODE.toString()))
            .andExpect(jsonPath("$.classe").value(DEFAULT_CLASSE.toString()))
            .andExpect(jsonPath("$.anneeScolaire").value(DEFAULT_ANNEE_SCOLAIRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVacation() throws Exception {
        // Get the vacation
        restVacationMockMvc.perform(get("/api/vacations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVacation() throws Exception {
        // Initialize the database
        vacationRepository.saveAndFlush(vacation);
        vacationSearchRepository.save(vacation);
        int databaseSizeBeforeUpdate = vacationRepository.findAll().size();

        // Update the vacation
        Vacation updatedVacation = new Vacation();
        updatedVacation.setId(vacation.getId());
        updatedVacation.setType(UPDATED_TYPE);
        updatedVacation.setDate(UPDATED_DATE);
        updatedVacation.setPeriode(UPDATED_PERIODE);
        updatedVacation.setClasse(UPDATED_CLASSE);
        updatedVacation.setAnneeScolaire(UPDATED_ANNEE_SCOLAIRE);

        restVacationMockMvc.perform(put("/api/vacations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedVacation)))
                .andExpect(status().isOk());

        // Validate the Vacation in the database
        List<Vacation> vacations = vacationRepository.findAll();
        assertThat(vacations).hasSize(databaseSizeBeforeUpdate);
        Vacation testVacation = vacations.get(vacations.size() - 1);
        assertThat(testVacation.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testVacation.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testVacation.getPeriode()).isEqualTo(UPDATED_PERIODE);
        assertThat(testVacation.getClasse()).isEqualTo(UPDATED_CLASSE);
        assertThat(testVacation.getAnneeScolaire()).isEqualTo(UPDATED_ANNEE_SCOLAIRE);

        // Validate the Vacation in ElasticSearch
        Vacation vacationEs = vacationSearchRepository.findOne(testVacation.getId());
        assertThat(vacationEs).isEqualToComparingFieldByField(testVacation);
    }

    @Test
    @Transactional
    public void deleteVacation() throws Exception {
        // Initialize the database
        vacationRepository.saveAndFlush(vacation);
        vacationSearchRepository.save(vacation);
        int databaseSizeBeforeDelete = vacationRepository.findAll().size();

        // Get the vacation
        restVacationMockMvc.perform(delete("/api/vacations/{id}", vacation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean vacationExistsInEs = vacationSearchRepository.exists(vacation.getId());
        assertThat(vacationExistsInEs).isFalse();

        // Validate the database is empty
        List<Vacation> vacations = vacationRepository.findAll();
        assertThat(vacations).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchVacation() throws Exception {
        // Initialize the database
        vacationRepository.saveAndFlush(vacation);
        vacationSearchRepository.save(vacation);

        // Search the vacation
        restVacationMockMvc.perform(get("/api/_search/vacations?query=id:" + vacation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vacation.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].periode").value(hasItem(DEFAULT_PERIODE.toString())))
            .andExpect(jsonPath("$.[*].classe").value(hasItem(DEFAULT_CLASSE.toString())))
            .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())));
    }
}
