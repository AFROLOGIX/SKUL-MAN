package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.TypeEpreuve;
import com.afrologix.skulman.repository.TypeEpreuveRepository;
import com.afrologix.skulman.repository.search.TypeEpreuveSearchRepository;

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
 * Test class for the TypeEpreuveResource REST controller.
 *
 * @see TypeEpreuveResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class TypeEpreuveResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_FR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_FR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_EN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_EN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private TypeEpreuveRepository typeEpreuveRepository;

    @Inject
    private TypeEpreuveSearchRepository typeEpreuveSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTypeEpreuveMockMvc;

    private TypeEpreuve typeEpreuve;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TypeEpreuveResource typeEpreuveResource = new TypeEpreuveResource();
        ReflectionTestUtils.setField(typeEpreuveResource, "typeEpreuveSearchRepository", typeEpreuveSearchRepository);
        ReflectionTestUtils.setField(typeEpreuveResource, "typeEpreuveRepository", typeEpreuveRepository);
        this.restTypeEpreuveMockMvc = MockMvcBuilders.standaloneSetup(typeEpreuveResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        typeEpreuveSearchRepository.deleteAll();
        typeEpreuve = new TypeEpreuve();
        typeEpreuve.setCode(DEFAULT_CODE);
        typeEpreuve.setLibelleFr(DEFAULT_LIBELLE_FR);
        typeEpreuve.setLibelleEn(DEFAULT_LIBELLE_EN);
    }

    @Test
    @Transactional
    public void createTypeEpreuve() throws Exception {
        int databaseSizeBeforeCreate = typeEpreuveRepository.findAll().size();

        // Create the TypeEpreuve

        restTypeEpreuveMockMvc.perform(post("/api/type-epreuves")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(typeEpreuve)))
                .andExpect(status().isCreated());

        // Validate the TypeEpreuve in the database
        List<TypeEpreuve> typeEpreuves = typeEpreuveRepository.findAll();
        assertThat(typeEpreuves).hasSize(databaseSizeBeforeCreate + 1);
        TypeEpreuve testTypeEpreuve = typeEpreuves.get(typeEpreuves.size() - 1);
        assertThat(testTypeEpreuve.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTypeEpreuve.getLibelleFr()).isEqualTo(DEFAULT_LIBELLE_FR);
        assertThat(testTypeEpreuve.getLibelleEn()).isEqualTo(DEFAULT_LIBELLE_EN);

        // Validate the TypeEpreuve in ElasticSearch
        TypeEpreuve typeEpreuveEs = typeEpreuveSearchRepository.findOne(testTypeEpreuve.getId());
        assertThat(typeEpreuveEs).isEqualToComparingFieldByField(testTypeEpreuve);
    }

    @Test
    @Transactional
    public void getAllTypeEpreuves() throws Exception {
        // Initialize the database
        typeEpreuveRepository.saveAndFlush(typeEpreuve);

        // Get all the typeEpreuves
        restTypeEpreuveMockMvc.perform(get("/api/type-epreuves?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(typeEpreuve.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
                .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())));
    }

    @Test
    @Transactional
    public void getTypeEpreuve() throws Exception {
        // Initialize the database
        typeEpreuveRepository.saveAndFlush(typeEpreuve);

        // Get the typeEpreuve
        restTypeEpreuveMockMvc.perform(get("/api/type-epreuves/{id}", typeEpreuve.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(typeEpreuve.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelleFr").value(DEFAULT_LIBELLE_FR.toString()))
            .andExpect(jsonPath("$.libelleEn").value(DEFAULT_LIBELLE_EN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTypeEpreuve() throws Exception {
        // Get the typeEpreuve
        restTypeEpreuveMockMvc.perform(get("/api/type-epreuves/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTypeEpreuve() throws Exception {
        // Initialize the database
        typeEpreuveRepository.saveAndFlush(typeEpreuve);
        typeEpreuveSearchRepository.save(typeEpreuve);
        int databaseSizeBeforeUpdate = typeEpreuveRepository.findAll().size();

        // Update the typeEpreuve
        TypeEpreuve updatedTypeEpreuve = new TypeEpreuve();
        updatedTypeEpreuve.setId(typeEpreuve.getId());
        updatedTypeEpreuve.setCode(UPDATED_CODE);
        updatedTypeEpreuve.setLibelleFr(UPDATED_LIBELLE_FR);
        updatedTypeEpreuve.setLibelleEn(UPDATED_LIBELLE_EN);

        restTypeEpreuveMockMvc.perform(put("/api/type-epreuves")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTypeEpreuve)))
                .andExpect(status().isOk());

        // Validate the TypeEpreuve in the database
        List<TypeEpreuve> typeEpreuves = typeEpreuveRepository.findAll();
        assertThat(typeEpreuves).hasSize(databaseSizeBeforeUpdate);
        TypeEpreuve testTypeEpreuve = typeEpreuves.get(typeEpreuves.size() - 1);
        assertThat(testTypeEpreuve.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTypeEpreuve.getLibelleFr()).isEqualTo(UPDATED_LIBELLE_FR);
        assertThat(testTypeEpreuve.getLibelleEn()).isEqualTo(UPDATED_LIBELLE_EN);

        // Validate the TypeEpreuve in ElasticSearch
        TypeEpreuve typeEpreuveEs = typeEpreuveSearchRepository.findOne(testTypeEpreuve.getId());
        assertThat(typeEpreuveEs).isEqualToComparingFieldByField(testTypeEpreuve);
    }

    @Test
    @Transactional
    public void deleteTypeEpreuve() throws Exception {
        // Initialize the database
        typeEpreuveRepository.saveAndFlush(typeEpreuve);
        typeEpreuveSearchRepository.save(typeEpreuve);
        int databaseSizeBeforeDelete = typeEpreuveRepository.findAll().size();

        // Get the typeEpreuve
        restTypeEpreuveMockMvc.perform(delete("/api/type-epreuves/{id}", typeEpreuve.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean typeEpreuveExistsInEs = typeEpreuveSearchRepository.exists(typeEpreuve.getId());
        assertThat(typeEpreuveExistsInEs).isFalse();

        // Validate the database is empty
        List<TypeEpreuve> typeEpreuves = typeEpreuveRepository.findAll();
        assertThat(typeEpreuves).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTypeEpreuve() throws Exception {
        // Initialize the database
        typeEpreuveRepository.saveAndFlush(typeEpreuve);
        typeEpreuveSearchRepository.save(typeEpreuve);

        // Search the typeEpreuve
        restTypeEpreuveMockMvc.perform(get("/api/_search/type-epreuves?query=id:" + typeEpreuve.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeEpreuve.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
            .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())));
    }
}
