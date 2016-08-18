package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Cycle;
import com.afrologix.skulman.repository.CycleRepository;
import com.afrologix.skulman.repository.search.CycleSearchRepository;

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
 * Test class for the CycleResource REST controller.
 *
 * @see CycleResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class CycleResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_FR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_FR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_EN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_EN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private CycleRepository cycleRepository;

    @Inject
    private CycleSearchRepository cycleSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCycleMockMvc;

    private Cycle cycle;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CycleResource cycleResource = new CycleResource();
        ReflectionTestUtils.setField(cycleResource, "cycleSearchRepository", cycleSearchRepository);
        ReflectionTestUtils.setField(cycleResource, "cycleRepository", cycleRepository);
        this.restCycleMockMvc = MockMvcBuilders.standaloneSetup(cycleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        cycleSearchRepository.deleteAll();
        cycle = new Cycle();
        cycle.setCode(DEFAULT_CODE);
        cycle.setLibelleFr(DEFAULT_LIBELLE_FR);
        cycle.setLibelleEn(DEFAULT_LIBELLE_EN);
    }

    @Test
    @Transactional
    public void createCycle() throws Exception {
        int databaseSizeBeforeCreate = cycleRepository.findAll().size();

        // Create the Cycle

        restCycleMockMvc.perform(post("/api/cycles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cycle)))
                .andExpect(status().isCreated());

        // Validate the Cycle in the database
        List<Cycle> cycles = cycleRepository.findAll();
        assertThat(cycles).hasSize(databaseSizeBeforeCreate + 1);
        Cycle testCycle = cycles.get(cycles.size() - 1);
        assertThat(testCycle.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCycle.getLibelleFr()).isEqualTo(DEFAULT_LIBELLE_FR);
        assertThat(testCycle.getLibelleEn()).isEqualTo(DEFAULT_LIBELLE_EN);

        // Validate the Cycle in ElasticSearch
        Cycle cycleEs = cycleSearchRepository.findOne(testCycle.getId());
        assertThat(cycleEs).isEqualToComparingFieldByField(testCycle);
    }

    @Test
    @Transactional
    public void getAllCycles() throws Exception {
        // Initialize the database
        cycleRepository.saveAndFlush(cycle);

        // Get all the cycles
        restCycleMockMvc.perform(get("/api/cycles?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(cycle.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
                .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())));
    }

    @Test
    @Transactional
    public void getCycle() throws Exception {
        // Initialize the database
        cycleRepository.saveAndFlush(cycle);

        // Get the cycle
        restCycleMockMvc.perform(get("/api/cycles/{id}", cycle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(cycle.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelleFr").value(DEFAULT_LIBELLE_FR.toString()))
            .andExpect(jsonPath("$.libelleEn").value(DEFAULT_LIBELLE_EN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCycle() throws Exception {
        // Get the cycle
        restCycleMockMvc.perform(get("/api/cycles/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCycle() throws Exception {
        // Initialize the database
        cycleRepository.saveAndFlush(cycle);
        cycleSearchRepository.save(cycle);
        int databaseSizeBeforeUpdate = cycleRepository.findAll().size();

        // Update the cycle
        Cycle updatedCycle = new Cycle();
        updatedCycle.setId(cycle.getId());
        updatedCycle.setCode(UPDATED_CODE);
        updatedCycle.setLibelleFr(UPDATED_LIBELLE_FR);
        updatedCycle.setLibelleEn(UPDATED_LIBELLE_EN);

        restCycleMockMvc.perform(put("/api/cycles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCycle)))
                .andExpect(status().isOk());

        // Validate the Cycle in the database
        List<Cycle> cycles = cycleRepository.findAll();
        assertThat(cycles).hasSize(databaseSizeBeforeUpdate);
        Cycle testCycle = cycles.get(cycles.size() - 1);
        assertThat(testCycle.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCycle.getLibelleFr()).isEqualTo(UPDATED_LIBELLE_FR);
        assertThat(testCycle.getLibelleEn()).isEqualTo(UPDATED_LIBELLE_EN);

        // Validate the Cycle in ElasticSearch
        Cycle cycleEs = cycleSearchRepository.findOne(testCycle.getId());
        assertThat(cycleEs).isEqualToComparingFieldByField(testCycle);
    }

    @Test
    @Transactional
    public void deleteCycle() throws Exception {
        // Initialize the database
        cycleRepository.saveAndFlush(cycle);
        cycleSearchRepository.save(cycle);
        int databaseSizeBeforeDelete = cycleRepository.findAll().size();

        // Get the cycle
        restCycleMockMvc.perform(delete("/api/cycles/{id}", cycle.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean cycleExistsInEs = cycleSearchRepository.exists(cycle.getId());
        assertThat(cycleExistsInEs).isFalse();

        // Validate the database is empty
        List<Cycle> cycles = cycleRepository.findAll();
        assertThat(cycles).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCycle() throws Exception {
        // Initialize the database
        cycleRepository.saveAndFlush(cycle);
        cycleSearchRepository.save(cycle);

        // Search the cycle
        restCycleMockMvc.perform(get("/api/_search/cycles?query=id:" + cycle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cycle.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
            .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())));
    }
}
