package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.TypeOperation;
import com.afrologix.skulman.repository.TypeOperationRepository;
import com.afrologix.skulman.repository.search.TypeOperationSearchRepository;

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
 * Test class for the TypeOperationResource REST controller.
 *
 * @see TypeOperationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class TypeOperationResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_FR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_FR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_EN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_EN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private TypeOperationRepository typeOperationRepository;

    @Inject
    private TypeOperationSearchRepository typeOperationSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTypeOperationMockMvc;

    private TypeOperation typeOperation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TypeOperationResource typeOperationResource = new TypeOperationResource();
        ReflectionTestUtils.setField(typeOperationResource, "typeOperationSearchRepository", typeOperationSearchRepository);
        ReflectionTestUtils.setField(typeOperationResource, "typeOperationRepository", typeOperationRepository);
        this.restTypeOperationMockMvc = MockMvcBuilders.standaloneSetup(typeOperationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        typeOperationSearchRepository.deleteAll();
        typeOperation = new TypeOperation();
        typeOperation.setCode(DEFAULT_CODE);
        typeOperation.setLibelleFr(DEFAULT_LIBELLE_FR);
        typeOperation.setLibelleEn(DEFAULT_LIBELLE_EN);
    }

    @Test
    @Transactional
    public void createTypeOperation() throws Exception {
        int databaseSizeBeforeCreate = typeOperationRepository.findAll().size();

        // Create the TypeOperation

        restTypeOperationMockMvc.perform(post("/api/type-operations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(typeOperation)))
                .andExpect(status().isCreated());

        // Validate the TypeOperation in the database
        List<TypeOperation> typeOperations = typeOperationRepository.findAll();
        assertThat(typeOperations).hasSize(databaseSizeBeforeCreate + 1);
        TypeOperation testTypeOperation = typeOperations.get(typeOperations.size() - 1);
        assertThat(testTypeOperation.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTypeOperation.getLibelleFr()).isEqualTo(DEFAULT_LIBELLE_FR);
        assertThat(testTypeOperation.getLibelleEn()).isEqualTo(DEFAULT_LIBELLE_EN);

        // Validate the TypeOperation in ElasticSearch
        TypeOperation typeOperationEs = typeOperationSearchRepository.findOne(testTypeOperation.getId());
        assertThat(typeOperationEs).isEqualToComparingFieldByField(testTypeOperation);
    }

    @Test
    @Transactional
    public void getAllTypeOperations() throws Exception {
        // Initialize the database
        typeOperationRepository.saveAndFlush(typeOperation);

        // Get all the typeOperations
        restTypeOperationMockMvc.perform(get("/api/type-operations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(typeOperation.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
                .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())));
    }

    @Test
    @Transactional
    public void getTypeOperation() throws Exception {
        // Initialize the database
        typeOperationRepository.saveAndFlush(typeOperation);

        // Get the typeOperation
        restTypeOperationMockMvc.perform(get("/api/type-operations/{id}", typeOperation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(typeOperation.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelleFr").value(DEFAULT_LIBELLE_FR.toString()))
            .andExpect(jsonPath("$.libelleEn").value(DEFAULT_LIBELLE_EN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTypeOperation() throws Exception {
        // Get the typeOperation
        restTypeOperationMockMvc.perform(get("/api/type-operations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTypeOperation() throws Exception {
        // Initialize the database
        typeOperationRepository.saveAndFlush(typeOperation);
        typeOperationSearchRepository.save(typeOperation);
        int databaseSizeBeforeUpdate = typeOperationRepository.findAll().size();

        // Update the typeOperation
        TypeOperation updatedTypeOperation = new TypeOperation();
        updatedTypeOperation.setId(typeOperation.getId());
        updatedTypeOperation.setCode(UPDATED_CODE);
        updatedTypeOperation.setLibelleFr(UPDATED_LIBELLE_FR);
        updatedTypeOperation.setLibelleEn(UPDATED_LIBELLE_EN);

        restTypeOperationMockMvc.perform(put("/api/type-operations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTypeOperation)))
                .andExpect(status().isOk());

        // Validate the TypeOperation in the database
        List<TypeOperation> typeOperations = typeOperationRepository.findAll();
        assertThat(typeOperations).hasSize(databaseSizeBeforeUpdate);
        TypeOperation testTypeOperation = typeOperations.get(typeOperations.size() - 1);
        assertThat(testTypeOperation.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTypeOperation.getLibelleFr()).isEqualTo(UPDATED_LIBELLE_FR);
        assertThat(testTypeOperation.getLibelleEn()).isEqualTo(UPDATED_LIBELLE_EN);

        // Validate the TypeOperation in ElasticSearch
        TypeOperation typeOperationEs = typeOperationSearchRepository.findOne(testTypeOperation.getId());
        assertThat(typeOperationEs).isEqualToComparingFieldByField(testTypeOperation);
    }

    @Test
    @Transactional
    public void deleteTypeOperation() throws Exception {
        // Initialize the database
        typeOperationRepository.saveAndFlush(typeOperation);
        typeOperationSearchRepository.save(typeOperation);
        int databaseSizeBeforeDelete = typeOperationRepository.findAll().size();

        // Get the typeOperation
        restTypeOperationMockMvc.perform(delete("/api/type-operations/{id}", typeOperation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean typeOperationExistsInEs = typeOperationSearchRepository.exists(typeOperation.getId());
        assertThat(typeOperationExistsInEs).isFalse();

        // Validate the database is empty
        List<TypeOperation> typeOperations = typeOperationRepository.findAll();
        assertThat(typeOperations).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTypeOperation() throws Exception {
        // Initialize the database
        typeOperationRepository.saveAndFlush(typeOperation);
        typeOperationSearchRepository.save(typeOperation);

        // Search the typeOperation
        restTypeOperationMockMvc.perform(get("/api/_search/type-operations?query=id:" + typeOperation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeOperation.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
            .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())));
    }
}
