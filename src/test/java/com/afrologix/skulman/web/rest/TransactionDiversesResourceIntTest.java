package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.TransactionDiverses;
import com.afrologix.skulman.repository.TransactionDiversesRepository;
import com.afrologix.skulman.repository.search.TransactionDiversesSearchRepository;

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
 * Test class for the TransactionDiversesResource REST controller.
 *
 * @see TransactionDiversesResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class TransactionDiversesResourceIntTest {

    private static final String DEFAULT_TYPE_USAGER = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_TYPE_USAGER = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Boolean DEFAULT_IS_CREDIT = false;
    private static final Boolean UPDATED_IS_CREDIT = true;

    private static final Integer DEFAULT_USAGER_ID = 1;
    private static final Integer UPDATED_USAGER_ID = 2;

    @Inject
    private TransactionDiversesRepository transactionDiversesRepository;

    @Inject
    private TransactionDiversesSearchRepository transactionDiversesSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTransactionDiversesMockMvc;

    private TransactionDiverses transactionDiverses;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TransactionDiversesResource transactionDiversesResource = new TransactionDiversesResource();
        ReflectionTestUtils.setField(transactionDiversesResource, "transactionDiversesSearchRepository", transactionDiversesSearchRepository);
        ReflectionTestUtils.setField(transactionDiversesResource, "transactionDiversesRepository", transactionDiversesRepository);
        this.restTransactionDiversesMockMvc = MockMvcBuilders.standaloneSetup(transactionDiversesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        transactionDiversesSearchRepository.deleteAll();
        transactionDiverses = new TransactionDiverses();
        transactionDiverses.setTypeUsager(DEFAULT_TYPE_USAGER);
        transactionDiverses.setIsCredit(DEFAULT_IS_CREDIT);
        transactionDiverses.setUsagerId(DEFAULT_USAGER_ID);
    }

    @Test
    @Transactional
    public void createTransactionDiverses() throws Exception {
        int databaseSizeBeforeCreate = transactionDiversesRepository.findAll().size();

        // Create the TransactionDiverses

        restTransactionDiversesMockMvc.perform(post("/api/transaction-diverses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(transactionDiverses)))
                .andExpect(status().isCreated());

        // Validate the TransactionDiverses in the database
        List<TransactionDiverses> transactionDiverses = transactionDiversesRepository.findAll();
        assertThat(transactionDiverses).hasSize(databaseSizeBeforeCreate + 1);
        TransactionDiverses testTransactionDiverses = transactionDiverses.get(transactionDiverses.size() - 1);
        assertThat(testTransactionDiverses.getTypeUsager()).isEqualTo(DEFAULT_TYPE_USAGER);
        assertThat(testTransactionDiverses.isIsCredit()).isEqualTo(DEFAULT_IS_CREDIT);
        assertThat(testTransactionDiverses.getUsagerId()).isEqualTo(DEFAULT_USAGER_ID);

        // Validate the TransactionDiverses in ElasticSearch
        TransactionDiverses transactionDiversesEs = transactionDiversesSearchRepository.findOne(testTransactionDiverses.getId());
        assertThat(transactionDiversesEs).isEqualToComparingFieldByField(testTransactionDiverses);
    }

    @Test
    @Transactional
    public void getAllTransactionDiverses() throws Exception {
        // Initialize the database
        transactionDiversesRepository.saveAndFlush(transactionDiverses);

        // Get all the transactionDiverses
        restTransactionDiversesMockMvc.perform(get("/api/transaction-diverses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(transactionDiverses.getId().intValue())))
                .andExpect(jsonPath("$.[*].typeUsager").value(hasItem(DEFAULT_TYPE_USAGER.toString())))
                .andExpect(jsonPath("$.[*].isCredit").value(hasItem(DEFAULT_IS_CREDIT.booleanValue())))
                .andExpect(jsonPath("$.[*].usagerId").value(hasItem(DEFAULT_USAGER_ID)));
    }

    @Test
    @Transactional
    public void getTransactionDiverses() throws Exception {
        // Initialize the database
        transactionDiversesRepository.saveAndFlush(transactionDiverses);

        // Get the transactionDiverses
        restTransactionDiversesMockMvc.perform(get("/api/transaction-diverses/{id}", transactionDiverses.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(transactionDiverses.getId().intValue()))
            .andExpect(jsonPath("$.typeUsager").value(DEFAULT_TYPE_USAGER.toString()))
            .andExpect(jsonPath("$.isCredit").value(DEFAULT_IS_CREDIT.booleanValue()))
            .andExpect(jsonPath("$.usagerId").value(DEFAULT_USAGER_ID));
    }

    @Test
    @Transactional
    public void getNonExistingTransactionDiverses() throws Exception {
        // Get the transactionDiverses
        restTransactionDiversesMockMvc.perform(get("/api/transaction-diverses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransactionDiverses() throws Exception {
        // Initialize the database
        transactionDiversesRepository.saveAndFlush(transactionDiverses);
        transactionDiversesSearchRepository.save(transactionDiverses);
        int databaseSizeBeforeUpdate = transactionDiversesRepository.findAll().size();

        // Update the transactionDiverses
        TransactionDiverses updatedTransactionDiverses = new TransactionDiverses();
        updatedTransactionDiverses.setId(transactionDiverses.getId());
        updatedTransactionDiverses.setTypeUsager(UPDATED_TYPE_USAGER);
        updatedTransactionDiverses.setIsCredit(UPDATED_IS_CREDIT);
        updatedTransactionDiverses.setUsagerId(UPDATED_USAGER_ID);

        restTransactionDiversesMockMvc.perform(put("/api/transaction-diverses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTransactionDiverses)))
                .andExpect(status().isOk());

        // Validate the TransactionDiverses in the database
        List<TransactionDiverses> transactionDiverses = transactionDiversesRepository.findAll();
        assertThat(transactionDiverses).hasSize(databaseSizeBeforeUpdate);
        TransactionDiverses testTransactionDiverses = transactionDiverses.get(transactionDiverses.size() - 1);
        assertThat(testTransactionDiverses.getTypeUsager()).isEqualTo(UPDATED_TYPE_USAGER);
        assertThat(testTransactionDiverses.isIsCredit()).isEqualTo(UPDATED_IS_CREDIT);
        assertThat(testTransactionDiverses.getUsagerId()).isEqualTo(UPDATED_USAGER_ID);

        // Validate the TransactionDiverses in ElasticSearch
        TransactionDiverses transactionDiversesEs = transactionDiversesSearchRepository.findOne(testTransactionDiverses.getId());
        assertThat(transactionDiversesEs).isEqualToComparingFieldByField(testTransactionDiverses);
    }

    @Test
    @Transactional
    public void deleteTransactionDiverses() throws Exception {
        // Initialize the database
        transactionDiversesRepository.saveAndFlush(transactionDiverses);
        transactionDiversesSearchRepository.save(transactionDiverses);
        int databaseSizeBeforeDelete = transactionDiversesRepository.findAll().size();

        // Get the transactionDiverses
        restTransactionDiversesMockMvc.perform(delete("/api/transaction-diverses/{id}", transactionDiverses.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean transactionDiversesExistsInEs = transactionDiversesSearchRepository.exists(transactionDiverses.getId());
        assertThat(transactionDiversesExistsInEs).isFalse();

        // Validate the database is empty
        List<TransactionDiverses> transactionDiverses = transactionDiversesRepository.findAll();
        assertThat(transactionDiverses).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTransactionDiverses() throws Exception {
        // Initialize the database
        transactionDiversesRepository.saveAndFlush(transactionDiverses);
        transactionDiversesSearchRepository.save(transactionDiverses);

        // Search the transactionDiverses
        restTransactionDiversesMockMvc.perform(get("/api/_search/transaction-diverses?query=id:" + transactionDiverses.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionDiverses.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeUsager").value(hasItem(DEFAULT_TYPE_USAGER.toString())))
            .andExpect(jsonPath("$.[*].isCredit").value(hasItem(DEFAULT_IS_CREDIT.booleanValue())))
            .andExpect(jsonPath("$.[*].usagerId").value(hasItem(DEFAULT_USAGER_ID)));
    }
}
