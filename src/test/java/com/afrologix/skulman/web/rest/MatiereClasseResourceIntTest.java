package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.MatiereClasse;
import com.afrologix.skulman.repository.MatiereClasseRepository;
import com.afrologix.skulman.repository.search.MatiereClasseSearchRepository;

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
 * Test class for the MatiereClasseResource REST controller.
 *
 * @see MatiereClasseResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class MatiereClasseResourceIntTest {

    private static final String DEFAULT_ANNEE_SCOLAIRE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ANNEE_SCOLAIRE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Integer DEFAULT_COEF = 50;
    private static final Integer UPDATED_COEF = 49;

    @Inject
    private MatiereClasseRepository matiereClasseRepository;

    @Inject
    private MatiereClasseSearchRepository matiereClasseSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMatiereClasseMockMvc;

    private MatiereClasse matiereClasse;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MatiereClasseResource matiereClasseResource = new MatiereClasseResource();
        ReflectionTestUtils.setField(matiereClasseResource, "matiereClasseSearchRepository", matiereClasseSearchRepository);
        ReflectionTestUtils.setField(matiereClasseResource, "matiereClasseRepository", matiereClasseRepository);
        this.restMatiereClasseMockMvc = MockMvcBuilders.standaloneSetup(matiereClasseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        matiereClasseSearchRepository.deleteAll();
        matiereClasse = new MatiereClasse();
        matiereClasse.setAnneeScolaire(DEFAULT_ANNEE_SCOLAIRE);
        matiereClasse.setCoef(DEFAULT_COEF);
    }

    @Test
    @Transactional
    public void createMatiereClasse() throws Exception {
        int databaseSizeBeforeCreate = matiereClasseRepository.findAll().size();

        // Create the MatiereClasse

        restMatiereClasseMockMvc.perform(post("/api/matiere-classes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(matiereClasse)))
                .andExpect(status().isCreated());

        // Validate the MatiereClasse in the database
        List<MatiereClasse> matiereClasses = matiereClasseRepository.findAll();
        assertThat(matiereClasses).hasSize(databaseSizeBeforeCreate + 1);
        MatiereClasse testMatiereClasse = matiereClasses.get(matiereClasses.size() - 1);
        assertThat(testMatiereClasse.getAnneeScolaire()).isEqualTo(DEFAULT_ANNEE_SCOLAIRE);
        assertThat(testMatiereClasse.getCoef()).isEqualTo(DEFAULT_COEF);

        // Validate the MatiereClasse in ElasticSearch
        MatiereClasse matiereClasseEs = matiereClasseSearchRepository.findOne(testMatiereClasse.getId());
        assertThat(matiereClasseEs).isEqualToComparingFieldByField(testMatiereClasse);
    }

    @Test
    @Transactional
    public void getAllMatiereClasses() throws Exception {
        // Initialize the database
        matiereClasseRepository.saveAndFlush(matiereClasse);

        // Get all the matiereClasses
        restMatiereClasseMockMvc.perform(get("/api/matiere-classes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(matiereClasse.getId().intValue())))
                .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())))
                .andExpect(jsonPath("$.[*].coef").value(hasItem(DEFAULT_COEF)));
    }

    @Test
    @Transactional
    public void getMatiereClasse() throws Exception {
        // Initialize the database
        matiereClasseRepository.saveAndFlush(matiereClasse);

        // Get the matiereClasse
        restMatiereClasseMockMvc.perform(get("/api/matiere-classes/{id}", matiereClasse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(matiereClasse.getId().intValue()))
            .andExpect(jsonPath("$.anneeScolaire").value(DEFAULT_ANNEE_SCOLAIRE.toString()))
            .andExpect(jsonPath("$.coef").value(DEFAULT_COEF));
    }

    @Test
    @Transactional
    public void getNonExistingMatiereClasse() throws Exception {
        // Get the matiereClasse
        restMatiereClasseMockMvc.perform(get("/api/matiere-classes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMatiereClasse() throws Exception {
        // Initialize the database
        matiereClasseRepository.saveAndFlush(matiereClasse);
        matiereClasseSearchRepository.save(matiereClasse);
        int databaseSizeBeforeUpdate = matiereClasseRepository.findAll().size();

        // Update the matiereClasse
        MatiereClasse updatedMatiereClasse = new MatiereClasse();
        updatedMatiereClasse.setId(matiereClasse.getId());
        updatedMatiereClasse.setAnneeScolaire(UPDATED_ANNEE_SCOLAIRE);
        updatedMatiereClasse.setCoef(UPDATED_COEF);

        restMatiereClasseMockMvc.perform(put("/api/matiere-classes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMatiereClasse)))
                .andExpect(status().isOk());

        // Validate the MatiereClasse in the database
        List<MatiereClasse> matiereClasses = matiereClasseRepository.findAll();
        assertThat(matiereClasses).hasSize(databaseSizeBeforeUpdate);
        MatiereClasse testMatiereClasse = matiereClasses.get(matiereClasses.size() - 1);
        assertThat(testMatiereClasse.getAnneeScolaire()).isEqualTo(UPDATED_ANNEE_SCOLAIRE);
        assertThat(testMatiereClasse.getCoef()).isEqualTo(UPDATED_COEF);

        // Validate the MatiereClasse in ElasticSearch
        MatiereClasse matiereClasseEs = matiereClasseSearchRepository.findOne(testMatiereClasse.getId());
        assertThat(matiereClasseEs).isEqualToComparingFieldByField(testMatiereClasse);
    }

    @Test
    @Transactional
    public void deleteMatiereClasse() throws Exception {
        // Initialize the database
        matiereClasseRepository.saveAndFlush(matiereClasse);
        matiereClasseSearchRepository.save(matiereClasse);
        int databaseSizeBeforeDelete = matiereClasseRepository.findAll().size();

        // Get the matiereClasse
        restMatiereClasseMockMvc.perform(delete("/api/matiere-classes/{id}", matiereClasse.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean matiereClasseExistsInEs = matiereClasseSearchRepository.exists(matiereClasse.getId());
        assertThat(matiereClasseExistsInEs).isFalse();

        // Validate the database is empty
        List<MatiereClasse> matiereClasses = matiereClasseRepository.findAll();
        assertThat(matiereClasses).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMatiereClasse() throws Exception {
        // Initialize the database
        matiereClasseRepository.saveAndFlush(matiereClasse);
        matiereClasseSearchRepository.save(matiereClasse);

        // Search the matiereClasse
        restMatiereClasseMockMvc.perform(get("/api/_search/matiere-classes?query=id:" + matiereClasse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(matiereClasse.getId().intValue())))
            .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())))
            .andExpect(jsonPath("$.[*].coef").value(hasItem(DEFAULT_COEF)));
    }
}
