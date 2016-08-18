package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.TypeMoratoire;
import com.afrologix.skulman.repository.TypeMoratoireRepository;
import com.afrologix.skulman.repository.search.TypeMoratoireSearchRepository;

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
 * Test class for the TypeMoratoireResource REST controller.
 *
 * @see TypeMoratoireResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class TypeMoratoireResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_FR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_FR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_EN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_EN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private TypeMoratoireRepository typeMoratoireRepository;

    @Inject
    private TypeMoratoireSearchRepository typeMoratoireSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTypeMoratoireMockMvc;

    private TypeMoratoire typeMoratoire;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TypeMoratoireResource typeMoratoireResource = new TypeMoratoireResource();
        ReflectionTestUtils.setField(typeMoratoireResource, "typeMoratoireSearchRepository", typeMoratoireSearchRepository);
        ReflectionTestUtils.setField(typeMoratoireResource, "typeMoratoireRepository", typeMoratoireRepository);
        this.restTypeMoratoireMockMvc = MockMvcBuilders.standaloneSetup(typeMoratoireResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        typeMoratoireSearchRepository.deleteAll();
        typeMoratoire = new TypeMoratoire();
        typeMoratoire.setCode(DEFAULT_CODE);
        typeMoratoire.setLibelleFr(DEFAULT_LIBELLE_FR);
        typeMoratoire.setLibelleEn(DEFAULT_LIBELLE_EN);
    }

    @Test
    @Transactional
    public void createTypeMoratoire() throws Exception {
        int databaseSizeBeforeCreate = typeMoratoireRepository.findAll().size();

        // Create the TypeMoratoire

        restTypeMoratoireMockMvc.perform(post("/api/type-moratoires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(typeMoratoire)))
                .andExpect(status().isCreated());

        // Validate the TypeMoratoire in the database
        List<TypeMoratoire> typeMoratoires = typeMoratoireRepository.findAll();
        assertThat(typeMoratoires).hasSize(databaseSizeBeforeCreate + 1);
        TypeMoratoire testTypeMoratoire = typeMoratoires.get(typeMoratoires.size() - 1);
        assertThat(testTypeMoratoire.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTypeMoratoire.getLibelleFr()).isEqualTo(DEFAULT_LIBELLE_FR);
        assertThat(testTypeMoratoire.getLibelleEn()).isEqualTo(DEFAULT_LIBELLE_EN);

        // Validate the TypeMoratoire in ElasticSearch
        TypeMoratoire typeMoratoireEs = typeMoratoireSearchRepository.findOne(testTypeMoratoire.getId());
        assertThat(typeMoratoireEs).isEqualToComparingFieldByField(testTypeMoratoire);
    }

    @Test
    @Transactional
    public void getAllTypeMoratoires() throws Exception {
        // Initialize the database
        typeMoratoireRepository.saveAndFlush(typeMoratoire);

        // Get all the typeMoratoires
        restTypeMoratoireMockMvc.perform(get("/api/type-moratoires?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(typeMoratoire.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
                .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())));
    }

    @Test
    @Transactional
    public void getTypeMoratoire() throws Exception {
        // Initialize the database
        typeMoratoireRepository.saveAndFlush(typeMoratoire);

        // Get the typeMoratoire
        restTypeMoratoireMockMvc.perform(get("/api/type-moratoires/{id}", typeMoratoire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(typeMoratoire.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelleFr").value(DEFAULT_LIBELLE_FR.toString()))
            .andExpect(jsonPath("$.libelleEn").value(DEFAULT_LIBELLE_EN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTypeMoratoire() throws Exception {
        // Get the typeMoratoire
        restTypeMoratoireMockMvc.perform(get("/api/type-moratoires/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTypeMoratoire() throws Exception {
        // Initialize the database
        typeMoratoireRepository.saveAndFlush(typeMoratoire);
        typeMoratoireSearchRepository.save(typeMoratoire);
        int databaseSizeBeforeUpdate = typeMoratoireRepository.findAll().size();

        // Update the typeMoratoire
        TypeMoratoire updatedTypeMoratoire = new TypeMoratoire();
        updatedTypeMoratoire.setId(typeMoratoire.getId());
        updatedTypeMoratoire.setCode(UPDATED_CODE);
        updatedTypeMoratoire.setLibelleFr(UPDATED_LIBELLE_FR);
        updatedTypeMoratoire.setLibelleEn(UPDATED_LIBELLE_EN);

        restTypeMoratoireMockMvc.perform(put("/api/type-moratoires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTypeMoratoire)))
                .andExpect(status().isOk());

        // Validate the TypeMoratoire in the database
        List<TypeMoratoire> typeMoratoires = typeMoratoireRepository.findAll();
        assertThat(typeMoratoires).hasSize(databaseSizeBeforeUpdate);
        TypeMoratoire testTypeMoratoire = typeMoratoires.get(typeMoratoires.size() - 1);
        assertThat(testTypeMoratoire.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTypeMoratoire.getLibelleFr()).isEqualTo(UPDATED_LIBELLE_FR);
        assertThat(testTypeMoratoire.getLibelleEn()).isEqualTo(UPDATED_LIBELLE_EN);

        // Validate the TypeMoratoire in ElasticSearch
        TypeMoratoire typeMoratoireEs = typeMoratoireSearchRepository.findOne(testTypeMoratoire.getId());
        assertThat(typeMoratoireEs).isEqualToComparingFieldByField(testTypeMoratoire);
    }

    @Test
    @Transactional
    public void deleteTypeMoratoire() throws Exception {
        // Initialize the database
        typeMoratoireRepository.saveAndFlush(typeMoratoire);
        typeMoratoireSearchRepository.save(typeMoratoire);
        int databaseSizeBeforeDelete = typeMoratoireRepository.findAll().size();

        // Get the typeMoratoire
        restTypeMoratoireMockMvc.perform(delete("/api/type-moratoires/{id}", typeMoratoire.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean typeMoratoireExistsInEs = typeMoratoireSearchRepository.exists(typeMoratoire.getId());
        assertThat(typeMoratoireExistsInEs).isFalse();

        // Validate the database is empty
        List<TypeMoratoire> typeMoratoires = typeMoratoireRepository.findAll();
        assertThat(typeMoratoires).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTypeMoratoire() throws Exception {
        // Initialize the database
        typeMoratoireRepository.saveAndFlush(typeMoratoire);
        typeMoratoireSearchRepository.save(typeMoratoire);

        // Search the typeMoratoire
        restTypeMoratoireMockMvc.perform(get("/api/_search/type-moratoires?query=id:" + typeMoratoire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeMoratoire.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
            .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())));
    }
}
