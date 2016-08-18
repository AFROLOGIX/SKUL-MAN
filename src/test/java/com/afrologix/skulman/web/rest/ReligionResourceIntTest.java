package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Religion;
import com.afrologix.skulman.repository.ReligionRepository;
import com.afrologix.skulman.repository.search.ReligionSearchRepository;

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
 * Test class for the ReligionResource REST controller.
 *
 * @see ReligionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class ReligionResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_FR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_FR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_EN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_EN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private ReligionRepository religionRepository;

    @Inject
    private ReligionSearchRepository religionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restReligionMockMvc;

    private Religion religion;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReligionResource religionResource = new ReligionResource();
        ReflectionTestUtils.setField(religionResource, "religionSearchRepository", religionSearchRepository);
        ReflectionTestUtils.setField(religionResource, "religionRepository", religionRepository);
        this.restReligionMockMvc = MockMvcBuilders.standaloneSetup(religionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        religionSearchRepository.deleteAll();
        religion = new Religion();
        religion.setCode(DEFAULT_CODE);
        religion.setLibelleFr(DEFAULT_LIBELLE_FR);
        religion.setLibelleEn(DEFAULT_LIBELLE_EN);
    }

    @Test
    @Transactional
    public void createReligion() throws Exception {
        int databaseSizeBeforeCreate = religionRepository.findAll().size();

        // Create the Religion

        restReligionMockMvc.perform(post("/api/religions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(religion)))
                .andExpect(status().isCreated());

        // Validate the Religion in the database
        List<Religion> religions = religionRepository.findAll();
        assertThat(religions).hasSize(databaseSizeBeforeCreate + 1);
        Religion testReligion = religions.get(religions.size() - 1);
        assertThat(testReligion.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testReligion.getLibelleFr()).isEqualTo(DEFAULT_LIBELLE_FR);
        assertThat(testReligion.getLibelleEn()).isEqualTo(DEFAULT_LIBELLE_EN);

        // Validate the Religion in ElasticSearch
        Religion religionEs = religionSearchRepository.findOne(testReligion.getId());
        assertThat(religionEs).isEqualToComparingFieldByField(testReligion);
    }

    @Test
    @Transactional
    public void getAllReligions() throws Exception {
        // Initialize the database
        religionRepository.saveAndFlush(religion);

        // Get all the religions
        restReligionMockMvc.perform(get("/api/religions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(religion.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
                .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())));
    }

    @Test
    @Transactional
    public void getReligion() throws Exception {
        // Initialize the database
        religionRepository.saveAndFlush(religion);

        // Get the religion
        restReligionMockMvc.perform(get("/api/religions/{id}", religion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(religion.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelleFr").value(DEFAULT_LIBELLE_FR.toString()))
            .andExpect(jsonPath("$.libelleEn").value(DEFAULT_LIBELLE_EN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingReligion() throws Exception {
        // Get the religion
        restReligionMockMvc.perform(get("/api/religions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReligion() throws Exception {
        // Initialize the database
        religionRepository.saveAndFlush(religion);
        religionSearchRepository.save(religion);
        int databaseSizeBeforeUpdate = religionRepository.findAll().size();

        // Update the religion
        Religion updatedReligion = new Religion();
        updatedReligion.setId(religion.getId());
        updatedReligion.setCode(UPDATED_CODE);
        updatedReligion.setLibelleFr(UPDATED_LIBELLE_FR);
        updatedReligion.setLibelleEn(UPDATED_LIBELLE_EN);

        restReligionMockMvc.perform(put("/api/religions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedReligion)))
                .andExpect(status().isOk());

        // Validate the Religion in the database
        List<Religion> religions = religionRepository.findAll();
        assertThat(religions).hasSize(databaseSizeBeforeUpdate);
        Religion testReligion = religions.get(religions.size() - 1);
        assertThat(testReligion.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testReligion.getLibelleFr()).isEqualTo(UPDATED_LIBELLE_FR);
        assertThat(testReligion.getLibelleEn()).isEqualTo(UPDATED_LIBELLE_EN);

        // Validate the Religion in ElasticSearch
        Religion religionEs = religionSearchRepository.findOne(testReligion.getId());
        assertThat(religionEs).isEqualToComparingFieldByField(testReligion);
    }

    @Test
    @Transactional
    public void deleteReligion() throws Exception {
        // Initialize the database
        religionRepository.saveAndFlush(religion);
        religionSearchRepository.save(religion);
        int databaseSizeBeforeDelete = religionRepository.findAll().size();

        // Get the religion
        restReligionMockMvc.perform(delete("/api/religions/{id}", religion.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean religionExistsInEs = religionSearchRepository.exists(religion.getId());
        assertThat(religionExistsInEs).isFalse();

        // Validate the database is empty
        List<Religion> religions = religionRepository.findAll();
        assertThat(religions).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchReligion() throws Exception {
        // Initialize the database
        religionRepository.saveAndFlush(religion);
        religionSearchRepository.save(religion);

        // Search the religion
        restReligionMockMvc.perform(get("/api/_search/religions?query=id:" + religion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(religion.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
            .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())));
    }
}
