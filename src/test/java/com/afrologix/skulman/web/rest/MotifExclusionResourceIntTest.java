package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.MotifExclusion;
import com.afrologix.skulman.repository.MotifExclusionRepository;
import com.afrologix.skulman.repository.search.MotifExclusionSearchRepository;

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
 * Test class for the MotifExclusionResource REST controller.
 *
 * @see MotifExclusionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class MotifExclusionResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_FR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_FR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_EN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_EN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private MotifExclusionRepository motifExclusionRepository;

    @Inject
    private MotifExclusionSearchRepository motifExclusionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMotifExclusionMockMvc;

    private MotifExclusion motifExclusion;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MotifExclusionResource motifExclusionResource = new MotifExclusionResource();
        ReflectionTestUtils.setField(motifExclusionResource, "motifExclusionSearchRepository", motifExclusionSearchRepository);
        ReflectionTestUtils.setField(motifExclusionResource, "motifExclusionRepository", motifExclusionRepository);
        this.restMotifExclusionMockMvc = MockMvcBuilders.standaloneSetup(motifExclusionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        motifExclusionSearchRepository.deleteAll();
        motifExclusion = new MotifExclusion();
        motifExclusion.setCode(DEFAULT_CODE);
        motifExclusion.setLibelleFr(DEFAULT_LIBELLE_FR);
        motifExclusion.setLibelleEn(DEFAULT_LIBELLE_EN);
    }

    @Test
    @Transactional
    public void createMotifExclusion() throws Exception {
        int databaseSizeBeforeCreate = motifExclusionRepository.findAll().size();

        // Create the MotifExclusion

        restMotifExclusionMockMvc.perform(post("/api/motif-exclusions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(motifExclusion)))
                .andExpect(status().isCreated());

        // Validate the MotifExclusion in the database
        List<MotifExclusion> motifExclusions = motifExclusionRepository.findAll();
        assertThat(motifExclusions).hasSize(databaseSizeBeforeCreate + 1);
        MotifExclusion testMotifExclusion = motifExclusions.get(motifExclusions.size() - 1);
        assertThat(testMotifExclusion.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testMotifExclusion.getLibelleFr()).isEqualTo(DEFAULT_LIBELLE_FR);
        assertThat(testMotifExclusion.getLibelleEn()).isEqualTo(DEFAULT_LIBELLE_EN);

        // Validate the MotifExclusion in ElasticSearch
        MotifExclusion motifExclusionEs = motifExclusionSearchRepository.findOne(testMotifExclusion.getId());
        assertThat(motifExclusionEs).isEqualToComparingFieldByField(testMotifExclusion);
    }

    @Test
    @Transactional
    public void getAllMotifExclusions() throws Exception {
        // Initialize the database
        motifExclusionRepository.saveAndFlush(motifExclusion);

        // Get all the motifExclusions
        restMotifExclusionMockMvc.perform(get("/api/motif-exclusions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(motifExclusion.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
                .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())));
    }

    @Test
    @Transactional
    public void getMotifExclusion() throws Exception {
        // Initialize the database
        motifExclusionRepository.saveAndFlush(motifExclusion);

        // Get the motifExclusion
        restMotifExclusionMockMvc.perform(get("/api/motif-exclusions/{id}", motifExclusion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(motifExclusion.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelleFr").value(DEFAULT_LIBELLE_FR.toString()))
            .andExpect(jsonPath("$.libelleEn").value(DEFAULT_LIBELLE_EN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMotifExclusion() throws Exception {
        // Get the motifExclusion
        restMotifExclusionMockMvc.perform(get("/api/motif-exclusions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMotifExclusion() throws Exception {
        // Initialize the database
        motifExclusionRepository.saveAndFlush(motifExclusion);
        motifExclusionSearchRepository.save(motifExclusion);
        int databaseSizeBeforeUpdate = motifExclusionRepository.findAll().size();

        // Update the motifExclusion
        MotifExclusion updatedMotifExclusion = new MotifExclusion();
        updatedMotifExclusion.setId(motifExclusion.getId());
        updatedMotifExclusion.setCode(UPDATED_CODE);
        updatedMotifExclusion.setLibelleFr(UPDATED_LIBELLE_FR);
        updatedMotifExclusion.setLibelleEn(UPDATED_LIBELLE_EN);

        restMotifExclusionMockMvc.perform(put("/api/motif-exclusions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMotifExclusion)))
                .andExpect(status().isOk());

        // Validate the MotifExclusion in the database
        List<MotifExclusion> motifExclusions = motifExclusionRepository.findAll();
        assertThat(motifExclusions).hasSize(databaseSizeBeforeUpdate);
        MotifExclusion testMotifExclusion = motifExclusions.get(motifExclusions.size() - 1);
        assertThat(testMotifExclusion.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testMotifExclusion.getLibelleFr()).isEqualTo(UPDATED_LIBELLE_FR);
        assertThat(testMotifExclusion.getLibelleEn()).isEqualTo(UPDATED_LIBELLE_EN);

        // Validate the MotifExclusion in ElasticSearch
        MotifExclusion motifExclusionEs = motifExclusionSearchRepository.findOne(testMotifExclusion.getId());
        assertThat(motifExclusionEs).isEqualToComparingFieldByField(testMotifExclusion);
    }

    @Test
    @Transactional
    public void deleteMotifExclusion() throws Exception {
        // Initialize the database
        motifExclusionRepository.saveAndFlush(motifExclusion);
        motifExclusionSearchRepository.save(motifExclusion);
        int databaseSizeBeforeDelete = motifExclusionRepository.findAll().size();

        // Get the motifExclusion
        restMotifExclusionMockMvc.perform(delete("/api/motif-exclusions/{id}", motifExclusion.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean motifExclusionExistsInEs = motifExclusionSearchRepository.exists(motifExclusion.getId());
        assertThat(motifExclusionExistsInEs).isFalse();

        // Validate the database is empty
        List<MotifExclusion> motifExclusions = motifExclusionRepository.findAll();
        assertThat(motifExclusions).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMotifExclusion() throws Exception {
        // Initialize the database
        motifExclusionRepository.saveAndFlush(motifExclusion);
        motifExclusionSearchRepository.save(motifExclusion);

        // Search the motifExclusion
        restMotifExclusionMockMvc.perform(get("/api/_search/motif-exclusions?query=id:" + motifExclusion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(motifExclusion.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
            .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())));
    }
}
