package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Droit;
import com.afrologix.skulman.repository.DroitRepository;
import com.afrologix.skulman.repository.search.DroitSearchRepository;

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
 * Test class for the DroitResource REST controller.
 *
 * @see DroitResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class DroitResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_FR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_FR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_EN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_EN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private DroitRepository droitRepository;

    @Inject
    private DroitSearchRepository droitSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDroitMockMvc;

    private Droit droit;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DroitResource droitResource = new DroitResource();
        ReflectionTestUtils.setField(droitResource, "droitSearchRepository", droitSearchRepository);
        ReflectionTestUtils.setField(droitResource, "droitRepository", droitRepository);
        this.restDroitMockMvc = MockMvcBuilders.standaloneSetup(droitResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        droitSearchRepository.deleteAll();
        droit = new Droit();
        droit.setCode(DEFAULT_CODE);
        droit.setLibelleFr(DEFAULT_LIBELLE_FR);
        droit.setLibelleEn(DEFAULT_LIBELLE_EN);
    }

    @Test
    @Transactional
    public void createDroit() throws Exception {
        int databaseSizeBeforeCreate = droitRepository.findAll().size();

        // Create the Droit

        restDroitMockMvc.perform(post("/api/droits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(droit)))
                .andExpect(status().isCreated());

        // Validate the Droit in the database
        List<Droit> droits = droitRepository.findAll();
        assertThat(droits).hasSize(databaseSizeBeforeCreate + 1);
        Droit testDroit = droits.get(droits.size() - 1);
        assertThat(testDroit.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDroit.getLibelleFr()).isEqualTo(DEFAULT_LIBELLE_FR);
        assertThat(testDroit.getLibelleEn()).isEqualTo(DEFAULT_LIBELLE_EN);

        // Validate the Droit in ElasticSearch
        Droit droitEs = droitSearchRepository.findOne(testDroit.getId());
        assertThat(droitEs).isEqualToComparingFieldByField(testDroit);
    }

    @Test
    @Transactional
    public void getAllDroits() throws Exception {
        // Initialize the database
        droitRepository.saveAndFlush(droit);

        // Get all the droits
        restDroitMockMvc.perform(get("/api/droits?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(droit.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
                .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())));
    }

    @Test
    @Transactional
    public void getDroit() throws Exception {
        // Initialize the database
        droitRepository.saveAndFlush(droit);

        // Get the droit
        restDroitMockMvc.perform(get("/api/droits/{id}", droit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(droit.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelleFr").value(DEFAULT_LIBELLE_FR.toString()))
            .andExpect(jsonPath("$.libelleEn").value(DEFAULT_LIBELLE_EN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDroit() throws Exception {
        // Get the droit
        restDroitMockMvc.perform(get("/api/droits/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDroit() throws Exception {
        // Initialize the database
        droitRepository.saveAndFlush(droit);
        droitSearchRepository.save(droit);
        int databaseSizeBeforeUpdate = droitRepository.findAll().size();

        // Update the droit
        Droit updatedDroit = new Droit();
        updatedDroit.setId(droit.getId());
        updatedDroit.setCode(UPDATED_CODE);
        updatedDroit.setLibelleFr(UPDATED_LIBELLE_FR);
        updatedDroit.setLibelleEn(UPDATED_LIBELLE_EN);

        restDroitMockMvc.perform(put("/api/droits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDroit)))
                .andExpect(status().isOk());

        // Validate the Droit in the database
        List<Droit> droits = droitRepository.findAll();
        assertThat(droits).hasSize(databaseSizeBeforeUpdate);
        Droit testDroit = droits.get(droits.size() - 1);
        assertThat(testDroit.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDroit.getLibelleFr()).isEqualTo(UPDATED_LIBELLE_FR);
        assertThat(testDroit.getLibelleEn()).isEqualTo(UPDATED_LIBELLE_EN);

        // Validate the Droit in ElasticSearch
        Droit droitEs = droitSearchRepository.findOne(testDroit.getId());
        assertThat(droitEs).isEqualToComparingFieldByField(testDroit);
    }

    @Test
    @Transactional
    public void deleteDroit() throws Exception {
        // Initialize the database
        droitRepository.saveAndFlush(droit);
        droitSearchRepository.save(droit);
        int databaseSizeBeforeDelete = droitRepository.findAll().size();

        // Get the droit
        restDroitMockMvc.perform(delete("/api/droits/{id}", droit.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean droitExistsInEs = droitSearchRepository.exists(droit.getId());
        assertThat(droitExistsInEs).isFalse();

        // Validate the database is empty
        List<Droit> droits = droitRepository.findAll();
        assertThat(droits).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDroit() throws Exception {
        // Initialize the database
        droitRepository.saveAndFlush(droit);
        droitSearchRepository.save(droit);

        // Search the droit
        restDroitMockMvc.perform(get("/api/_search/droits?query=id:" + droit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(droit.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
            .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())));
    }
}
