package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.AbsenceEleve;
import com.afrologix.skulman.repository.AbsenceEleveRepository;
import com.afrologix.skulman.repository.search.AbsenceEleveSearchRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the AbsenceEleveResource REST controller.
 *
 * @see AbsenceEleveResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class AbsenceEleveResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_ANNEE_SCOLAIRE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ANNEE_SCOLAIRE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_PLAGE_HORAIRE = "AAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_PLAGE_HORAIRE = "BBBBBBBBBBBBBBBBBBBB";

    private static final Boolean DEFAULT_JUSTIFIEE = false;
    private static final Boolean UPDATED_JUSTIFIEE = true;
    private static final String DEFAULT_CREATE_BY = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CREATE_BY = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_UPDATE_BY = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_UPDATE_BY = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATE_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATE_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATE_AT_STR = dateTimeFormatter.format(DEFAULT_CREATE_AT);

    private static final ZonedDateTime DEFAULT_UPDATE_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_UPDATE_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_UPDATE_AT_STR = dateTimeFormatter.format(DEFAULT_UPDATE_AT);

    @Inject
    private AbsenceEleveRepository absenceEleveRepository;

    @Inject
    private AbsenceEleveSearchRepository absenceEleveSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAbsenceEleveMockMvc;

    private AbsenceEleve absenceEleve;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AbsenceEleveResource absenceEleveResource = new AbsenceEleveResource();
        ReflectionTestUtils.setField(absenceEleveResource, "absenceEleveSearchRepository", absenceEleveSearchRepository);
        ReflectionTestUtils.setField(absenceEleveResource, "absenceEleveRepository", absenceEleveRepository);
        this.restAbsenceEleveMockMvc = MockMvcBuilders.standaloneSetup(absenceEleveResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        absenceEleveSearchRepository.deleteAll();
        absenceEleve = new AbsenceEleve();
        absenceEleve.setAnneeScolaire(DEFAULT_ANNEE_SCOLAIRE);
        absenceEleve.setPlageHoraire(DEFAULT_PLAGE_HORAIRE);
        absenceEleve.setJustifiee(DEFAULT_JUSTIFIEE);
        absenceEleve.setCreateBy(DEFAULT_CREATE_BY);
        absenceEleve.setUpdateBy(DEFAULT_UPDATE_BY);
        absenceEleve.setCreateAt(DEFAULT_CREATE_AT);
        absenceEleve.setUpdateAt(DEFAULT_UPDATE_AT);
    }

    @Test
    @Transactional
    public void createAbsenceEleve() throws Exception {
        int databaseSizeBeforeCreate = absenceEleveRepository.findAll().size();

        // Create the AbsenceEleve

        restAbsenceEleveMockMvc.perform(post("/api/absence-eleves")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(absenceEleve)))
                .andExpect(status().isCreated());

        // Validate the AbsenceEleve in the database
        List<AbsenceEleve> absenceEleves = absenceEleveRepository.findAll();
        assertThat(absenceEleves).hasSize(databaseSizeBeforeCreate + 1);
        AbsenceEleve testAbsenceEleve = absenceEleves.get(absenceEleves.size() - 1);
        assertThat(testAbsenceEleve.getAnneeScolaire()).isEqualTo(DEFAULT_ANNEE_SCOLAIRE);
        assertThat(testAbsenceEleve.getPlageHoraire()).isEqualTo(DEFAULT_PLAGE_HORAIRE);
        assertThat(testAbsenceEleve.isJustifiee()).isEqualTo(DEFAULT_JUSTIFIEE);
        assertThat(testAbsenceEleve.getCreateBy()).isEqualTo(DEFAULT_CREATE_BY);
        assertThat(testAbsenceEleve.getUpdateBy()).isEqualTo(DEFAULT_UPDATE_BY);
        assertThat(testAbsenceEleve.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testAbsenceEleve.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);

        // Validate the AbsenceEleve in ElasticSearch
        AbsenceEleve absenceEleveEs = absenceEleveSearchRepository.findOne(testAbsenceEleve.getId());
        assertThat(absenceEleveEs).isEqualToComparingFieldByField(testAbsenceEleve);
    }

    @Test
    @Transactional
    public void checkAnneeScolaireIsRequired() throws Exception {
        int databaseSizeBeforeTest = absenceEleveRepository.findAll().size();
        // set the field null
        absenceEleve.setAnneeScolaire(null);

        // Create the AbsenceEleve, which fails.

        restAbsenceEleveMockMvc.perform(post("/api/absence-eleves")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(absenceEleve)))
                .andExpect(status().isBadRequest());

        List<AbsenceEleve> absenceEleves = absenceEleveRepository.findAll();
        assertThat(absenceEleves).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAbsenceEleves() throws Exception {
        // Initialize the database
        absenceEleveRepository.saveAndFlush(absenceEleve);

        // Get all the absenceEleves
        restAbsenceEleveMockMvc.perform(get("/api/absence-eleves?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(absenceEleve.getId().intValue())))
                .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())))
                .andExpect(jsonPath("$.[*].plageHoraire").value(hasItem(DEFAULT_PLAGE_HORAIRE.toString())))
                .andExpect(jsonPath("$.[*].justifiee").value(hasItem(DEFAULT_JUSTIFIEE.booleanValue())))
                .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY.toString())))
                .andExpect(jsonPath("$.[*].updateBy").value(hasItem(DEFAULT_UPDATE_BY.toString())))
                .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT_STR)))
                .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT_STR)));
    }

    @Test
    @Transactional
    public void getAbsenceEleve() throws Exception {
        // Initialize the database
        absenceEleveRepository.saveAndFlush(absenceEleve);

        // Get the absenceEleve
        restAbsenceEleveMockMvc.perform(get("/api/absence-eleves/{id}", absenceEleve.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(absenceEleve.getId().intValue()))
            .andExpect(jsonPath("$.anneeScolaire").value(DEFAULT_ANNEE_SCOLAIRE.toString()))
            .andExpect(jsonPath("$.plageHoraire").value(DEFAULT_PLAGE_HORAIRE.toString()))
            .andExpect(jsonPath("$.justifiee").value(DEFAULT_JUSTIFIEE.booleanValue()))
            .andExpect(jsonPath("$.createBy").value(DEFAULT_CREATE_BY.toString()))
            .andExpect(jsonPath("$.updateBy").value(DEFAULT_UPDATE_BY.toString()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT_STR))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT_STR));
    }

    @Test
    @Transactional
    public void getNonExistingAbsenceEleve() throws Exception {
        // Get the absenceEleve
        restAbsenceEleveMockMvc.perform(get("/api/absence-eleves/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAbsenceEleve() throws Exception {
        // Initialize the database
        absenceEleveRepository.saveAndFlush(absenceEleve);
        absenceEleveSearchRepository.save(absenceEleve);
        int databaseSizeBeforeUpdate = absenceEleveRepository.findAll().size();

        // Update the absenceEleve
        AbsenceEleve updatedAbsenceEleve = new AbsenceEleve();
        updatedAbsenceEleve.setId(absenceEleve.getId());
        updatedAbsenceEleve.setAnneeScolaire(UPDATED_ANNEE_SCOLAIRE);
        updatedAbsenceEleve.setPlageHoraire(UPDATED_PLAGE_HORAIRE);
        updatedAbsenceEleve.setJustifiee(UPDATED_JUSTIFIEE);
        updatedAbsenceEleve.setCreateBy(UPDATED_CREATE_BY);
        updatedAbsenceEleve.setUpdateBy(UPDATED_UPDATE_BY);
        updatedAbsenceEleve.setCreateAt(UPDATED_CREATE_AT);
        updatedAbsenceEleve.setUpdateAt(UPDATED_UPDATE_AT);

        restAbsenceEleveMockMvc.perform(put("/api/absence-eleves")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAbsenceEleve)))
                .andExpect(status().isOk());

        // Validate the AbsenceEleve in the database
        List<AbsenceEleve> absenceEleves = absenceEleveRepository.findAll();
        assertThat(absenceEleves).hasSize(databaseSizeBeforeUpdate);
        AbsenceEleve testAbsenceEleve = absenceEleves.get(absenceEleves.size() - 1);
        assertThat(testAbsenceEleve.getAnneeScolaire()).isEqualTo(UPDATED_ANNEE_SCOLAIRE);
        assertThat(testAbsenceEleve.getPlageHoraire()).isEqualTo(UPDATED_PLAGE_HORAIRE);
        assertThat(testAbsenceEleve.isJustifiee()).isEqualTo(UPDATED_JUSTIFIEE);
        assertThat(testAbsenceEleve.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testAbsenceEleve.getUpdateBy()).isEqualTo(UPDATED_UPDATE_BY);
        assertThat(testAbsenceEleve.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testAbsenceEleve.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);

        // Validate the AbsenceEleve in ElasticSearch
        AbsenceEleve absenceEleveEs = absenceEleveSearchRepository.findOne(testAbsenceEleve.getId());
        assertThat(absenceEleveEs).isEqualToComparingFieldByField(testAbsenceEleve);
    }

    @Test
    @Transactional
    public void deleteAbsenceEleve() throws Exception {
        // Initialize the database
        absenceEleveRepository.saveAndFlush(absenceEleve);
        absenceEleveSearchRepository.save(absenceEleve);
        int databaseSizeBeforeDelete = absenceEleveRepository.findAll().size();

        // Get the absenceEleve
        restAbsenceEleveMockMvc.perform(delete("/api/absence-eleves/{id}", absenceEleve.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean absenceEleveExistsInEs = absenceEleveSearchRepository.exists(absenceEleve.getId());
        assertThat(absenceEleveExistsInEs).isFalse();

        // Validate the database is empty
        List<AbsenceEleve> absenceEleves = absenceEleveRepository.findAll();
        assertThat(absenceEleves).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAbsenceEleve() throws Exception {
        // Initialize the database
        absenceEleveRepository.saveAndFlush(absenceEleve);
        absenceEleveSearchRepository.save(absenceEleve);

        // Search the absenceEleve
        restAbsenceEleveMockMvc.perform(get("/api/_search/absence-eleves?query=id:" + absenceEleve.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(absenceEleve.getId().intValue())))
            .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())))
            .andExpect(jsonPath("$.[*].plageHoraire").value(hasItem(DEFAULT_PLAGE_HORAIRE.toString())))
            .andExpect(jsonPath("$.[*].justifiee").value(hasItem(DEFAULT_JUSTIFIEE.booleanValue())))
            .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY.toString())))
            .andExpect(jsonPath("$.[*].updateBy").value(hasItem(DEFAULT_UPDATE_BY.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT_STR)))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT_STR)));
    }
}
