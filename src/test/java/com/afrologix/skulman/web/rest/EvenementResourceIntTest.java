package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Evenement;
import com.afrologix.skulman.repository.EvenementRepository;
import com.afrologix.skulman.repository.search.EvenementSearchRepository;

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
 * Test class for the EvenementResource REST controller.
 *
 * @see EvenementResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class EvenementResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_FR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_FR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_EN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_EN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DEB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEB = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_ANNEE_SCOLAIRE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ANNEE_SCOLAIRE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private EvenementRepository evenementRepository;

    @Inject
    private EvenementSearchRepository evenementSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEvenementMockMvc;

    private Evenement evenement;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EvenementResource evenementResource = new EvenementResource();
        ReflectionTestUtils.setField(evenementResource, "evenementSearchRepository", evenementSearchRepository);
        ReflectionTestUtils.setField(evenementResource, "evenementRepository", evenementRepository);
        this.restEvenementMockMvc = MockMvcBuilders.standaloneSetup(evenementResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        evenementSearchRepository.deleteAll();
        evenement = new Evenement();
        evenement.setCode(DEFAULT_CODE);
        evenement.setLibelleFr(DEFAULT_LIBELLE_FR);
        evenement.setLibelleEn(DEFAULT_LIBELLE_EN);
        evenement.setDateDeb(DEFAULT_DATE_DEB);
        evenement.setDateFin(DEFAULT_DATE_FIN);
        evenement.setAnneeScolaire(DEFAULT_ANNEE_SCOLAIRE);
    }

    @Test
    @Transactional
    public void createEvenement() throws Exception {
        int databaseSizeBeforeCreate = evenementRepository.findAll().size();

        // Create the Evenement

        restEvenementMockMvc.perform(post("/api/evenements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(evenement)))
                .andExpect(status().isCreated());

        // Validate the Evenement in the database
        List<Evenement> evenements = evenementRepository.findAll();
        assertThat(evenements).hasSize(databaseSizeBeforeCreate + 1);
        Evenement testEvenement = evenements.get(evenements.size() - 1);
        assertThat(testEvenement.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testEvenement.getLibelleFr()).isEqualTo(DEFAULT_LIBELLE_FR);
        assertThat(testEvenement.getLibelleEn()).isEqualTo(DEFAULT_LIBELLE_EN);
        assertThat(testEvenement.getDateDeb()).isEqualTo(DEFAULT_DATE_DEB);
        assertThat(testEvenement.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testEvenement.getAnneeScolaire()).isEqualTo(DEFAULT_ANNEE_SCOLAIRE);

        // Validate the Evenement in ElasticSearch
        Evenement evenementEs = evenementSearchRepository.findOne(testEvenement.getId());
        assertThat(evenementEs).isEqualToComparingFieldByField(testEvenement);
    }

    @Test
    @Transactional
    public void getAllEvenements() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenements
        restEvenementMockMvc.perform(get("/api/evenements?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(evenement.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
                .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())))
                .andExpect(jsonPath("$.[*].dateDeb").value(hasItem(DEFAULT_DATE_DEB.toString())))
                .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
                .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())));
    }

    @Test
    @Transactional
    public void getEvenement() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get the evenement
        restEvenementMockMvc.perform(get("/api/evenements/{id}", evenement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(evenement.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelleFr").value(DEFAULT_LIBELLE_FR.toString()))
            .andExpect(jsonPath("$.libelleEn").value(DEFAULT_LIBELLE_EN.toString()))
            .andExpect(jsonPath("$.dateDeb").value(DEFAULT_DATE_DEB.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.anneeScolaire").value(DEFAULT_ANNEE_SCOLAIRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEvenement() throws Exception {
        // Get the evenement
        restEvenementMockMvc.perform(get("/api/evenements/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEvenement() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);
        evenementSearchRepository.save(evenement);
        int databaseSizeBeforeUpdate = evenementRepository.findAll().size();

        // Update the evenement
        Evenement updatedEvenement = new Evenement();
        updatedEvenement.setId(evenement.getId());
        updatedEvenement.setCode(UPDATED_CODE);
        updatedEvenement.setLibelleFr(UPDATED_LIBELLE_FR);
        updatedEvenement.setLibelleEn(UPDATED_LIBELLE_EN);
        updatedEvenement.setDateDeb(UPDATED_DATE_DEB);
        updatedEvenement.setDateFin(UPDATED_DATE_FIN);
        updatedEvenement.setAnneeScolaire(UPDATED_ANNEE_SCOLAIRE);

        restEvenementMockMvc.perform(put("/api/evenements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEvenement)))
                .andExpect(status().isOk());

        // Validate the Evenement in the database
        List<Evenement> evenements = evenementRepository.findAll();
        assertThat(evenements).hasSize(databaseSizeBeforeUpdate);
        Evenement testEvenement = evenements.get(evenements.size() - 1);
        assertThat(testEvenement.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testEvenement.getLibelleFr()).isEqualTo(UPDATED_LIBELLE_FR);
        assertThat(testEvenement.getLibelleEn()).isEqualTo(UPDATED_LIBELLE_EN);
        assertThat(testEvenement.getDateDeb()).isEqualTo(UPDATED_DATE_DEB);
        assertThat(testEvenement.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testEvenement.getAnneeScolaire()).isEqualTo(UPDATED_ANNEE_SCOLAIRE);

        // Validate the Evenement in ElasticSearch
        Evenement evenementEs = evenementSearchRepository.findOne(testEvenement.getId());
        assertThat(evenementEs).isEqualToComparingFieldByField(testEvenement);
    }

    @Test
    @Transactional
    public void deleteEvenement() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);
        evenementSearchRepository.save(evenement);
        int databaseSizeBeforeDelete = evenementRepository.findAll().size();

        // Get the evenement
        restEvenementMockMvc.perform(delete("/api/evenements/{id}", evenement.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean evenementExistsInEs = evenementSearchRepository.exists(evenement.getId());
        assertThat(evenementExistsInEs).isFalse();

        // Validate the database is empty
        List<Evenement> evenements = evenementRepository.findAll();
        assertThat(evenements).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEvenement() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);
        evenementSearchRepository.save(evenement);

        // Search the evenement
        restEvenementMockMvc.perform(get("/api/_search/evenements?query=id:" + evenement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evenement.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
            .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())))
            .andExpect(jsonPath("$.[*].dateDeb").value(hasItem(DEFAULT_DATE_DEB.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())));
    }
}
