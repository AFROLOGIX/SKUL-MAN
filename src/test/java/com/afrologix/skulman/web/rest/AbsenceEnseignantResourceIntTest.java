package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.AbsenceEnseignant;
import com.afrologix.skulman.repository.AbsenceEnseignantRepository;
import com.afrologix.skulman.repository.search.AbsenceEnseignantSearchRepository;

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
 * Test class for the AbsenceEnseignantResource REST controller.
 *
 * @see AbsenceEnseignantResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class AbsenceEnseignantResourceIntTest {

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
    private AbsenceEnseignantRepository absenceEnseignantRepository;

    @Inject
    private AbsenceEnseignantSearchRepository absenceEnseignantSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAbsenceEnseignantMockMvc;

    private AbsenceEnseignant absenceEnseignant;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AbsenceEnseignantResource absenceEnseignantResource = new AbsenceEnseignantResource();
        ReflectionTestUtils.setField(absenceEnseignantResource, "absenceEnseignantSearchRepository", absenceEnseignantSearchRepository);
        ReflectionTestUtils.setField(absenceEnseignantResource, "absenceEnseignantRepository", absenceEnseignantRepository);
        this.restAbsenceEnseignantMockMvc = MockMvcBuilders.standaloneSetup(absenceEnseignantResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        absenceEnseignantSearchRepository.deleteAll();
        absenceEnseignant = new AbsenceEnseignant();
        absenceEnseignant.setAnneeScolaire(DEFAULT_ANNEE_SCOLAIRE);
        absenceEnseignant.setPlageHoraire(DEFAULT_PLAGE_HORAIRE);
        absenceEnseignant.setJustifiee(DEFAULT_JUSTIFIEE);
        absenceEnseignant.setCreateBy(DEFAULT_CREATE_BY);
        absenceEnseignant.setUpdateBy(DEFAULT_UPDATE_BY);
        absenceEnseignant.setCreateAt(DEFAULT_CREATE_AT);
        absenceEnseignant.setUpdateAt(DEFAULT_UPDATE_AT);
    }

    @Test
    @Transactional
    public void createAbsenceEnseignant() throws Exception {
        int databaseSizeBeforeCreate = absenceEnseignantRepository.findAll().size();

        // Create the AbsenceEnseignant

        restAbsenceEnseignantMockMvc.perform(post("/api/absence-enseignants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(absenceEnseignant)))
                .andExpect(status().isCreated());

        // Validate the AbsenceEnseignant in the database
        List<AbsenceEnseignant> absenceEnseignants = absenceEnseignantRepository.findAll();
        assertThat(absenceEnseignants).hasSize(databaseSizeBeforeCreate + 1);
        AbsenceEnseignant testAbsenceEnseignant = absenceEnseignants.get(absenceEnseignants.size() - 1);
        assertThat(testAbsenceEnseignant.getAnneeScolaire()).isEqualTo(DEFAULT_ANNEE_SCOLAIRE);
        assertThat(testAbsenceEnseignant.getPlageHoraire()).isEqualTo(DEFAULT_PLAGE_HORAIRE);
        assertThat(testAbsenceEnseignant.isJustifiee()).isEqualTo(DEFAULT_JUSTIFIEE);
        assertThat(testAbsenceEnseignant.getCreateBy()).isEqualTo(DEFAULT_CREATE_BY);
        assertThat(testAbsenceEnseignant.getUpdateBy()).isEqualTo(DEFAULT_UPDATE_BY);
        assertThat(testAbsenceEnseignant.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testAbsenceEnseignant.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);

        // Validate the AbsenceEnseignant in ElasticSearch
        AbsenceEnseignant absenceEnseignantEs = absenceEnseignantSearchRepository.findOne(testAbsenceEnseignant.getId());
        assertThat(absenceEnseignantEs).isEqualToComparingFieldByField(testAbsenceEnseignant);
    }

    @Test
    @Transactional
    public void checkAnneeScolaireIsRequired() throws Exception {
        int databaseSizeBeforeTest = absenceEnseignantRepository.findAll().size();
        // set the field null
        absenceEnseignant.setAnneeScolaire(null);

        // Create the AbsenceEnseignant, which fails.

        restAbsenceEnseignantMockMvc.perform(post("/api/absence-enseignants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(absenceEnseignant)))
                .andExpect(status().isBadRequest());

        List<AbsenceEnseignant> absenceEnseignants = absenceEnseignantRepository.findAll();
        assertThat(absenceEnseignants).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAbsenceEnseignants() throws Exception {
        // Initialize the database
        absenceEnseignantRepository.saveAndFlush(absenceEnseignant);

        // Get all the absenceEnseignants
        restAbsenceEnseignantMockMvc.perform(get("/api/absence-enseignants?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(absenceEnseignant.getId().intValue())))
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
    public void getAbsenceEnseignant() throws Exception {
        // Initialize the database
        absenceEnseignantRepository.saveAndFlush(absenceEnseignant);

        // Get the absenceEnseignant
        restAbsenceEnseignantMockMvc.perform(get("/api/absence-enseignants/{id}", absenceEnseignant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(absenceEnseignant.getId().intValue()))
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
    public void getNonExistingAbsenceEnseignant() throws Exception {
        // Get the absenceEnseignant
        restAbsenceEnseignantMockMvc.perform(get("/api/absence-enseignants/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAbsenceEnseignant() throws Exception {
        // Initialize the database
        absenceEnseignantRepository.saveAndFlush(absenceEnseignant);
        absenceEnseignantSearchRepository.save(absenceEnseignant);
        int databaseSizeBeforeUpdate = absenceEnseignantRepository.findAll().size();

        // Update the absenceEnseignant
        AbsenceEnseignant updatedAbsenceEnseignant = new AbsenceEnseignant();
        updatedAbsenceEnseignant.setId(absenceEnseignant.getId());
        updatedAbsenceEnseignant.setAnneeScolaire(UPDATED_ANNEE_SCOLAIRE);
        updatedAbsenceEnseignant.setPlageHoraire(UPDATED_PLAGE_HORAIRE);
        updatedAbsenceEnseignant.setJustifiee(UPDATED_JUSTIFIEE);
        updatedAbsenceEnseignant.setCreateBy(UPDATED_CREATE_BY);
        updatedAbsenceEnseignant.setUpdateBy(UPDATED_UPDATE_BY);
        updatedAbsenceEnseignant.setCreateAt(UPDATED_CREATE_AT);
        updatedAbsenceEnseignant.setUpdateAt(UPDATED_UPDATE_AT);

        restAbsenceEnseignantMockMvc.perform(put("/api/absence-enseignants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAbsenceEnseignant)))
                .andExpect(status().isOk());

        // Validate the AbsenceEnseignant in the database
        List<AbsenceEnseignant> absenceEnseignants = absenceEnseignantRepository.findAll();
        assertThat(absenceEnseignants).hasSize(databaseSizeBeforeUpdate);
        AbsenceEnseignant testAbsenceEnseignant = absenceEnseignants.get(absenceEnseignants.size() - 1);
        assertThat(testAbsenceEnseignant.getAnneeScolaire()).isEqualTo(UPDATED_ANNEE_SCOLAIRE);
        assertThat(testAbsenceEnseignant.getPlageHoraire()).isEqualTo(UPDATED_PLAGE_HORAIRE);
        assertThat(testAbsenceEnseignant.isJustifiee()).isEqualTo(UPDATED_JUSTIFIEE);
        assertThat(testAbsenceEnseignant.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testAbsenceEnseignant.getUpdateBy()).isEqualTo(UPDATED_UPDATE_BY);
        assertThat(testAbsenceEnseignant.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testAbsenceEnseignant.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);

        // Validate the AbsenceEnseignant in ElasticSearch
        AbsenceEnseignant absenceEnseignantEs = absenceEnseignantSearchRepository.findOne(testAbsenceEnseignant.getId());
        assertThat(absenceEnseignantEs).isEqualToComparingFieldByField(testAbsenceEnseignant);
    }

    @Test
    @Transactional
    public void deleteAbsenceEnseignant() throws Exception {
        // Initialize the database
        absenceEnseignantRepository.saveAndFlush(absenceEnseignant);
        absenceEnseignantSearchRepository.save(absenceEnseignant);
        int databaseSizeBeforeDelete = absenceEnseignantRepository.findAll().size();

        // Get the absenceEnseignant
        restAbsenceEnseignantMockMvc.perform(delete("/api/absence-enseignants/{id}", absenceEnseignant.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean absenceEnseignantExistsInEs = absenceEnseignantSearchRepository.exists(absenceEnseignant.getId());
        assertThat(absenceEnseignantExistsInEs).isFalse();

        // Validate the database is empty
        List<AbsenceEnseignant> absenceEnseignants = absenceEnseignantRepository.findAll();
        assertThat(absenceEnseignants).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAbsenceEnseignant() throws Exception {
        // Initialize the database
        absenceEnseignantRepository.saveAndFlush(absenceEnseignant);
        absenceEnseignantSearchRepository.save(absenceEnseignant);

        // Search the absenceEnseignant
        restAbsenceEnseignantMockMvc.perform(get("/api/_search/absence-enseignants?query=id:" + absenceEnseignant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(absenceEnseignant.getId().intValue())))
            .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())))
            .andExpect(jsonPath("$.[*].plageHoraire").value(hasItem(DEFAULT_PLAGE_HORAIRE.toString())))
            .andExpect(jsonPath("$.[*].justifiee").value(hasItem(DEFAULT_JUSTIFIEE.booleanValue())))
            .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY.toString())))
            .andExpect(jsonPath("$.[*].updateBy").value(hasItem(DEFAULT_UPDATE_BY.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT_STR)))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT_STR)));
    }
}
