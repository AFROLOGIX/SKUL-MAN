package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Appreciation;
import com.afrologix.skulman.repository.AppreciationRepository;
import com.afrologix.skulman.repository.search.AppreciationSearchRepository;

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
 * Test class for the AppreciationResource REST controller.
 *
 * @see AppreciationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class AppreciationResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_FR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_FR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_EN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_EN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Double DEFAULT_MIN_NOTE = 1D;
    private static final Double UPDATED_MIN_NOTE = 2D;

    private static final Double DEFAULT_MAX_NOTE = 1D;
    private static final Double UPDATED_MAX_NOTE = 2D;

    @Inject
    private AppreciationRepository appreciationRepository;

    @Inject
    private AppreciationSearchRepository appreciationSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAppreciationMockMvc;

    private Appreciation appreciation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AppreciationResource appreciationResource = new AppreciationResource();
        ReflectionTestUtils.setField(appreciationResource, "appreciationSearchRepository", appreciationSearchRepository);
        ReflectionTestUtils.setField(appreciationResource, "appreciationRepository", appreciationRepository);
        this.restAppreciationMockMvc = MockMvcBuilders.standaloneSetup(appreciationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        appreciationSearchRepository.deleteAll();
        appreciation = new Appreciation();
        appreciation.setCode(DEFAULT_CODE);
        appreciation.setLibelleFr(DEFAULT_LIBELLE_FR);
        appreciation.setLibelleEn(DEFAULT_LIBELLE_EN);
        appreciation.setMinNote(DEFAULT_MIN_NOTE);
        appreciation.setMaxNote(DEFAULT_MAX_NOTE);
    }

    @Test
    @Transactional
    public void createAppreciation() throws Exception {
        int databaseSizeBeforeCreate = appreciationRepository.findAll().size();

        // Create the Appreciation

        restAppreciationMockMvc.perform(post("/api/appreciations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(appreciation)))
                .andExpect(status().isCreated());

        // Validate the Appreciation in the database
        List<Appreciation> appreciations = appreciationRepository.findAll();
        assertThat(appreciations).hasSize(databaseSizeBeforeCreate + 1);
        Appreciation testAppreciation = appreciations.get(appreciations.size() - 1);
        assertThat(testAppreciation.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testAppreciation.getLibelleFr()).isEqualTo(DEFAULT_LIBELLE_FR);
        assertThat(testAppreciation.getLibelleEn()).isEqualTo(DEFAULT_LIBELLE_EN);
        assertThat(testAppreciation.getMinNote()).isEqualTo(DEFAULT_MIN_NOTE);
        assertThat(testAppreciation.getMaxNote()).isEqualTo(DEFAULT_MAX_NOTE);

        // Validate the Appreciation in ElasticSearch
        Appreciation appreciationEs = appreciationSearchRepository.findOne(testAppreciation.getId());
        assertThat(appreciationEs).isEqualToComparingFieldByField(testAppreciation);
    }

    @Test
    @Transactional
    public void getAllAppreciations() throws Exception {
        // Initialize the database
        appreciationRepository.saveAndFlush(appreciation);

        // Get all the appreciations
        restAppreciationMockMvc.perform(get("/api/appreciations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(appreciation.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
                .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())))
                .andExpect(jsonPath("$.[*].minNote").value(hasItem(DEFAULT_MIN_NOTE.doubleValue())))
                .andExpect(jsonPath("$.[*].maxNote").value(hasItem(DEFAULT_MAX_NOTE.doubleValue())));
    }

    @Test
    @Transactional
    public void getAppreciation() throws Exception {
        // Initialize the database
        appreciationRepository.saveAndFlush(appreciation);

        // Get the appreciation
        restAppreciationMockMvc.perform(get("/api/appreciations/{id}", appreciation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(appreciation.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelleFr").value(DEFAULT_LIBELLE_FR.toString()))
            .andExpect(jsonPath("$.libelleEn").value(DEFAULT_LIBELLE_EN.toString()))
            .andExpect(jsonPath("$.minNote").value(DEFAULT_MIN_NOTE.doubleValue()))
            .andExpect(jsonPath("$.maxNote").value(DEFAULT_MAX_NOTE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAppreciation() throws Exception {
        // Get the appreciation
        restAppreciationMockMvc.perform(get("/api/appreciations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAppreciation() throws Exception {
        // Initialize the database
        appreciationRepository.saveAndFlush(appreciation);
        appreciationSearchRepository.save(appreciation);
        int databaseSizeBeforeUpdate = appreciationRepository.findAll().size();

        // Update the appreciation
        Appreciation updatedAppreciation = new Appreciation();
        updatedAppreciation.setId(appreciation.getId());
        updatedAppreciation.setCode(UPDATED_CODE);
        updatedAppreciation.setLibelleFr(UPDATED_LIBELLE_FR);
        updatedAppreciation.setLibelleEn(UPDATED_LIBELLE_EN);
        updatedAppreciation.setMinNote(UPDATED_MIN_NOTE);
        updatedAppreciation.setMaxNote(UPDATED_MAX_NOTE);

        restAppreciationMockMvc.perform(put("/api/appreciations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAppreciation)))
                .andExpect(status().isOk());

        // Validate the Appreciation in the database
        List<Appreciation> appreciations = appreciationRepository.findAll();
        assertThat(appreciations).hasSize(databaseSizeBeforeUpdate);
        Appreciation testAppreciation = appreciations.get(appreciations.size() - 1);
        assertThat(testAppreciation.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testAppreciation.getLibelleFr()).isEqualTo(UPDATED_LIBELLE_FR);
        assertThat(testAppreciation.getLibelleEn()).isEqualTo(UPDATED_LIBELLE_EN);
        assertThat(testAppreciation.getMinNote()).isEqualTo(UPDATED_MIN_NOTE);
        assertThat(testAppreciation.getMaxNote()).isEqualTo(UPDATED_MAX_NOTE);

        // Validate the Appreciation in ElasticSearch
        Appreciation appreciationEs = appreciationSearchRepository.findOne(testAppreciation.getId());
        assertThat(appreciationEs).isEqualToComparingFieldByField(testAppreciation);
    }

    @Test
    @Transactional
    public void deleteAppreciation() throws Exception {
        // Initialize the database
        appreciationRepository.saveAndFlush(appreciation);
        appreciationSearchRepository.save(appreciation);
        int databaseSizeBeforeDelete = appreciationRepository.findAll().size();

        // Get the appreciation
        restAppreciationMockMvc.perform(delete("/api/appreciations/{id}", appreciation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean appreciationExistsInEs = appreciationSearchRepository.exists(appreciation.getId());
        assertThat(appreciationExistsInEs).isFalse();

        // Validate the database is empty
        List<Appreciation> appreciations = appreciationRepository.findAll();
        assertThat(appreciations).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAppreciation() throws Exception {
        // Initialize the database
        appreciationRepository.saveAndFlush(appreciation);
        appreciationSearchRepository.save(appreciation);

        // Search the appreciation
        restAppreciationMockMvc.perform(get("/api/_search/appreciations?query=id:" + appreciation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appreciation.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
            .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())))
            .andExpect(jsonPath("$.[*].minNote").value(hasItem(DEFAULT_MIN_NOTE.doubleValue())))
            .andExpect(jsonPath("$.[*].maxNote").value(hasItem(DEFAULT_MAX_NOTE.doubleValue())));
    }
}
