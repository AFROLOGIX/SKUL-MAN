package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Salaire;
import com.afrologix.skulman.repository.SalaireRepository;
import com.afrologix.skulman.repository.search.SalaireSearchRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the SalaireResource REST controller.
 *
 * @see SalaireResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class SalaireResourceIntTest {

    private static final String DEFAULT_TYPE_SALAIRE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_TYPE_SALAIRE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Double DEFAULT_MONTANT = 1D;
    private static final Double UPDATED_MONTANT = 2D;
    private static final String DEFAULT_ANNEE_SCOLAIRE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ANNEE_SCOLAIRE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private SalaireRepository salaireRepository;

    @Inject
    private SalaireSearchRepository salaireSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSalaireMockMvc;

    private Salaire salaire;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SalaireResource salaireResource = new SalaireResource();
        ReflectionTestUtils.setField(salaireResource, "salaireSearchRepository", salaireSearchRepository);
        ReflectionTestUtils.setField(salaireResource, "salaireRepository", salaireRepository);
        this.restSalaireMockMvc = MockMvcBuilders.standaloneSetup(salaireResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        salaireSearchRepository.deleteAll();
        salaire = new Salaire();
        salaire.setTypeSalaire(DEFAULT_TYPE_SALAIRE);
        salaire.setMontant(DEFAULT_MONTANT);
        salaire.setAnneeScolaire(DEFAULT_ANNEE_SCOLAIRE);
    }

    @Test
    @Transactional
    public void createSalaire() throws Exception {
        int databaseSizeBeforeCreate = salaireRepository.findAll().size();

        // Create the Salaire

        restSalaireMockMvc.perform(post("/api/salaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(salaire)))
                .andExpect(status().isCreated());

        // Validate the Salaire in the database
        List<Salaire> salaires = salaireRepository.findAll();
        assertThat(salaires).hasSize(databaseSizeBeforeCreate + 1);
        Salaire testSalaire = salaires.get(salaires.size() - 1);
        assertThat(testSalaire.getTypeSalaire()).isEqualTo(DEFAULT_TYPE_SALAIRE);
        assertThat(testSalaire.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testSalaire.getAnneeScolaire()).isEqualTo(DEFAULT_ANNEE_SCOLAIRE);

        // Validate the Salaire in ElasticSearch
        Salaire salaireEs = salaireSearchRepository.findOne(testSalaire.getId());
        assertThat(salaireEs).isEqualToComparingFieldByField(testSalaire);
    }

    @Test
    @Transactional
    public void getAllSalaires() throws Exception {
        // Initialize the database
        salaireRepository.saveAndFlush(salaire);

        // Get all the salaires
        restSalaireMockMvc.perform(get("/api/salaires?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(salaire.getId().intValue())))
                .andExpect(jsonPath("$.[*].typeSalaire").value(hasItem(DEFAULT_TYPE_SALAIRE.toString())))
                .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())))
                .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())));
    }

    @Test
    @Transactional
    public void getSalaire() throws Exception {
        // Initialize the database
        salaireRepository.saveAndFlush(salaire);

        // Get the salaire
        restSalaireMockMvc.perform(get("/api/salaires/{id}", salaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(salaire.getId().intValue()))
            .andExpect(jsonPath("$.typeSalaire").value(DEFAULT_TYPE_SALAIRE.toString()))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.doubleValue()))
            .andExpect(jsonPath("$.anneeScolaire").value(DEFAULT_ANNEE_SCOLAIRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSalaire() throws Exception {
        // Get the salaire
        restSalaireMockMvc.perform(get("/api/salaires/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSalaire() throws Exception {
        // Initialize the database
        salaireRepository.saveAndFlush(salaire);
        salaireSearchRepository.save(salaire);
        int databaseSizeBeforeUpdate = salaireRepository.findAll().size();

        // Update the salaire
        Salaire updatedSalaire = new Salaire();
        updatedSalaire.setId(salaire.getId());
        updatedSalaire.setTypeSalaire(UPDATED_TYPE_SALAIRE);
        updatedSalaire.setMontant(UPDATED_MONTANT);
        updatedSalaire.setAnneeScolaire(UPDATED_ANNEE_SCOLAIRE);

        restSalaireMockMvc.perform(put("/api/salaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSalaire)))
                .andExpect(status().isOk());

        // Validate the Salaire in the database
        List<Salaire> salaires = salaireRepository.findAll();
        assertThat(salaires).hasSize(databaseSizeBeforeUpdate);
        Salaire testSalaire = salaires.get(salaires.size() - 1);
        assertThat(testSalaire.getTypeSalaire()).isEqualTo(UPDATED_TYPE_SALAIRE);
        assertThat(testSalaire.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testSalaire.getAnneeScolaire()).isEqualTo(UPDATED_ANNEE_SCOLAIRE);

        // Validate the Salaire in ElasticSearch
        Salaire salaireEs = salaireSearchRepository.findOne(testSalaire.getId());
        assertThat(salaireEs).isEqualToComparingFieldByField(testSalaire);
    }

    @Test
    @Transactional
    public void deleteSalaire() throws Exception {
        // Initialize the database
        salaireRepository.saveAndFlush(salaire);
        salaireSearchRepository.save(salaire);
        int databaseSizeBeforeDelete = salaireRepository.findAll().size();

        // Get the salaire
        restSalaireMockMvc.perform(delete("/api/salaires/{id}", salaire.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean salaireExistsInEs = salaireSearchRepository.exists(salaire.getId());
        assertThat(salaireExistsInEs).isFalse();

        // Validate the database is empty
        List<Salaire> salaires = salaireRepository.findAll();
        assertThat(salaires).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSalaire() throws Exception {
        // Initialize the database
        salaireRepository.saveAndFlush(salaire);
        salaireSearchRepository.save(salaire);

        // Search the salaire
        restSalaireMockMvc.perform(get("/api/_search/salaires?query=id:" + salaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeSalaire").value(hasItem(DEFAULT_TYPE_SALAIRE.toString())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())))
            .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())));
    }
}
