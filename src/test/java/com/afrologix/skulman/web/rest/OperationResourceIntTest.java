package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Operation;
import com.afrologix.skulman.repository.OperationRepository;
import com.afrologix.skulman.repository.search.OperationSearchRepository;

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
 * Test class for the OperationResource REST controller.
 *
 * @see OperationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class OperationResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_FR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_FR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_EN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_EN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_ANNEE_SCOLAIRE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ANNEE_SCOLAIRE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Boolean DEFAULT_IS_CREDIT = false;
    private static final Boolean UPDATED_IS_CREDIT = true;

    @Inject
    private OperationRepository operationRepository;

    @Inject
    private OperationSearchRepository operationSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restOperationMockMvc;

    private Operation operation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OperationResource operationResource = new OperationResource();
        ReflectionTestUtils.setField(operationResource, "operationSearchRepository", operationSearchRepository);
        ReflectionTestUtils.setField(operationResource, "operationRepository", operationRepository);
        this.restOperationMockMvc = MockMvcBuilders.standaloneSetup(operationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        operationSearchRepository.deleteAll();
        operation = new Operation();
        operation.setCode(DEFAULT_CODE);
        operation.setLibelleFr(DEFAULT_LIBELLE_FR);
        operation.setLibelleEn(DEFAULT_LIBELLE_EN);
        operation.setAnneeScolaire(DEFAULT_ANNEE_SCOLAIRE);
        operation.setIsCredit(DEFAULT_IS_CREDIT);
    }

    @Test
    @Transactional
    public void createOperation() throws Exception {
        int databaseSizeBeforeCreate = operationRepository.findAll().size();

        // Create the Operation

        restOperationMockMvc.perform(post("/api/operations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(operation)))
                .andExpect(status().isCreated());

        // Validate the Operation in the database
        List<Operation> operations = operationRepository.findAll();
        assertThat(operations).hasSize(databaseSizeBeforeCreate + 1);
        Operation testOperation = operations.get(operations.size() - 1);
        assertThat(testOperation.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOperation.getLibelleFr()).isEqualTo(DEFAULT_LIBELLE_FR);
        assertThat(testOperation.getLibelleEn()).isEqualTo(DEFAULT_LIBELLE_EN);
        assertThat(testOperation.getAnneeScolaire()).isEqualTo(DEFAULT_ANNEE_SCOLAIRE);
        assertThat(testOperation.isIsCredit()).isEqualTo(DEFAULT_IS_CREDIT);

        // Validate the Operation in ElasticSearch
        Operation operationEs = operationSearchRepository.findOne(testOperation.getId());
        assertThat(operationEs).isEqualToComparingFieldByField(testOperation);
    }

    @Test
    @Transactional
    public void getAllOperations() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        // Get all the operations
        restOperationMockMvc.perform(get("/api/operations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(operation.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
                .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())))
                .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())))
                .andExpect(jsonPath("$.[*].isCredit").value(hasItem(DEFAULT_IS_CREDIT.booleanValue())));
    }

    @Test
    @Transactional
    public void getOperation() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);

        // Get the operation
        restOperationMockMvc.perform(get("/api/operations/{id}", operation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(operation.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelleFr").value(DEFAULT_LIBELLE_FR.toString()))
            .andExpect(jsonPath("$.libelleEn").value(DEFAULT_LIBELLE_EN.toString()))
            .andExpect(jsonPath("$.anneeScolaire").value(DEFAULT_ANNEE_SCOLAIRE.toString()))
            .andExpect(jsonPath("$.isCredit").value(DEFAULT_IS_CREDIT.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingOperation() throws Exception {
        // Get the operation
        restOperationMockMvc.perform(get("/api/operations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOperation() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);
        operationSearchRepository.save(operation);
        int databaseSizeBeforeUpdate = operationRepository.findAll().size();

        // Update the operation
        Operation updatedOperation = new Operation();
        updatedOperation.setId(operation.getId());
        updatedOperation.setCode(UPDATED_CODE);
        updatedOperation.setLibelleFr(UPDATED_LIBELLE_FR);
        updatedOperation.setLibelleEn(UPDATED_LIBELLE_EN);
        updatedOperation.setAnneeScolaire(UPDATED_ANNEE_SCOLAIRE);
        updatedOperation.setIsCredit(UPDATED_IS_CREDIT);

        restOperationMockMvc.perform(put("/api/operations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedOperation)))
                .andExpect(status().isOk());

        // Validate the Operation in the database
        List<Operation> operations = operationRepository.findAll();
        assertThat(operations).hasSize(databaseSizeBeforeUpdate);
        Operation testOperation = operations.get(operations.size() - 1);
        assertThat(testOperation.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOperation.getLibelleFr()).isEqualTo(UPDATED_LIBELLE_FR);
        assertThat(testOperation.getLibelleEn()).isEqualTo(UPDATED_LIBELLE_EN);
        assertThat(testOperation.getAnneeScolaire()).isEqualTo(UPDATED_ANNEE_SCOLAIRE);
        assertThat(testOperation.isIsCredit()).isEqualTo(UPDATED_IS_CREDIT);

        // Validate the Operation in ElasticSearch
        Operation operationEs = operationSearchRepository.findOne(testOperation.getId());
        assertThat(operationEs).isEqualToComparingFieldByField(testOperation);
    }

    @Test
    @Transactional
    public void deleteOperation() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);
        operationSearchRepository.save(operation);
        int databaseSizeBeforeDelete = operationRepository.findAll().size();

        // Get the operation
        restOperationMockMvc.perform(delete("/api/operations/{id}", operation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean operationExistsInEs = operationSearchRepository.exists(operation.getId());
        assertThat(operationExistsInEs).isFalse();

        // Validate the database is empty
        List<Operation> operations = operationRepository.findAll();
        assertThat(operations).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchOperation() throws Exception {
        // Initialize the database
        operationRepository.saveAndFlush(operation);
        operationSearchRepository.save(operation);

        // Search the operation
        restOperationMockMvc.perform(get("/api/_search/operations?query=id:" + operation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operation.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
            .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())))
            .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())))
            .andExpect(jsonPath("$.[*].isCredit").value(hasItem(DEFAULT_IS_CREDIT.booleanValue())));
    }
}
