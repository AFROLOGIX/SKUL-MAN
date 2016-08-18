package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.MoyenneTableauHonneur;
import com.afrologix.skulman.repository.MoyenneTableauHonneurRepository;
import com.afrologix.skulman.repository.search.MoyenneTableauHonneurSearchRepository;

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
 * Test class for the MoyenneTableauHonneurResource REST controller.
 *
 * @see MoyenneTableauHonneurResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class MoyenneTableauHonneurResourceIntTest {


    private static final Double DEFAULT_CONDITION_TABLEAU_HONNEUR = 1D;
    private static final Double UPDATED_CONDITION_TABLEAU_HONNEUR = 2D;

    private static final Double DEFAULT_CONDITION_ENCOURAGEMENT = 1D;
    private static final Double UPDATED_CONDITION_ENCOURAGEMENT = 2D;

    private static final Double DEFAULT_CONDITION_FELICITATION = 1D;
    private static final Double UPDATED_CONDITION_FELICITATION = 2D;

    @Inject
    private MoyenneTableauHonneurRepository moyenneTableauHonneurRepository;

    @Inject
    private MoyenneTableauHonneurSearchRepository moyenneTableauHonneurSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMoyenneTableauHonneurMockMvc;

    private MoyenneTableauHonneur moyenneTableauHonneur;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MoyenneTableauHonneurResource moyenneTableauHonneurResource = new MoyenneTableauHonneurResource();
        ReflectionTestUtils.setField(moyenneTableauHonneurResource, "moyenneTableauHonneurSearchRepository", moyenneTableauHonneurSearchRepository);
        ReflectionTestUtils.setField(moyenneTableauHonneurResource, "moyenneTableauHonneurRepository", moyenneTableauHonneurRepository);
        this.restMoyenneTableauHonneurMockMvc = MockMvcBuilders.standaloneSetup(moyenneTableauHonneurResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        moyenneTableauHonneurSearchRepository.deleteAll();
        moyenneTableauHonneur = new MoyenneTableauHonneur();
        moyenneTableauHonneur.setConditionTableauHonneur(DEFAULT_CONDITION_TABLEAU_HONNEUR);
        moyenneTableauHonneur.setConditionEncouragement(DEFAULT_CONDITION_ENCOURAGEMENT);
        moyenneTableauHonneur.setConditionFelicitation(DEFAULT_CONDITION_FELICITATION);
    }

    @Test
    @Transactional
    public void createMoyenneTableauHonneur() throws Exception {
        int databaseSizeBeforeCreate = moyenneTableauHonneurRepository.findAll().size();

        // Create the MoyenneTableauHonneur

        restMoyenneTableauHonneurMockMvc.perform(post("/api/moyenne-tableau-honneurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(moyenneTableauHonneur)))
                .andExpect(status().isCreated());

        // Validate the MoyenneTableauHonneur in the database
        List<MoyenneTableauHonneur> moyenneTableauHonneurs = moyenneTableauHonneurRepository.findAll();
        assertThat(moyenneTableauHonneurs).hasSize(databaseSizeBeforeCreate + 1);
        MoyenneTableauHonneur testMoyenneTableauHonneur = moyenneTableauHonneurs.get(moyenneTableauHonneurs.size() - 1);
        assertThat(testMoyenneTableauHonneur.getConditionTableauHonneur()).isEqualTo(DEFAULT_CONDITION_TABLEAU_HONNEUR);
        assertThat(testMoyenneTableauHonneur.getConditionEncouragement()).isEqualTo(DEFAULT_CONDITION_ENCOURAGEMENT);
        assertThat(testMoyenneTableauHonneur.getConditionFelicitation()).isEqualTo(DEFAULT_CONDITION_FELICITATION);

        // Validate the MoyenneTableauHonneur in ElasticSearch
        MoyenneTableauHonneur moyenneTableauHonneurEs = moyenneTableauHonneurSearchRepository.findOne(testMoyenneTableauHonneur.getId());
        assertThat(moyenneTableauHonneurEs).isEqualToComparingFieldByField(testMoyenneTableauHonneur);
    }

    @Test
    @Transactional
    public void getAllMoyenneTableauHonneurs() throws Exception {
        // Initialize the database
        moyenneTableauHonneurRepository.saveAndFlush(moyenneTableauHonneur);

        // Get all the moyenneTableauHonneurs
        restMoyenneTableauHonneurMockMvc.perform(get("/api/moyenne-tableau-honneurs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(moyenneTableauHonneur.getId().intValue())))
                .andExpect(jsonPath("$.[*].conditionTableauHonneur").value(hasItem(DEFAULT_CONDITION_TABLEAU_HONNEUR.doubleValue())))
                .andExpect(jsonPath("$.[*].conditionEncouragement").value(hasItem(DEFAULT_CONDITION_ENCOURAGEMENT.doubleValue())))
                .andExpect(jsonPath("$.[*].conditionFelicitation").value(hasItem(DEFAULT_CONDITION_FELICITATION.doubleValue())));
    }

    @Test
    @Transactional
    public void getMoyenneTableauHonneur() throws Exception {
        // Initialize the database
        moyenneTableauHonneurRepository.saveAndFlush(moyenneTableauHonneur);

        // Get the moyenneTableauHonneur
        restMoyenneTableauHonneurMockMvc.perform(get("/api/moyenne-tableau-honneurs/{id}", moyenneTableauHonneur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(moyenneTableauHonneur.getId().intValue()))
            .andExpect(jsonPath("$.conditionTableauHonneur").value(DEFAULT_CONDITION_TABLEAU_HONNEUR.doubleValue()))
            .andExpect(jsonPath("$.conditionEncouragement").value(DEFAULT_CONDITION_ENCOURAGEMENT.doubleValue()))
            .andExpect(jsonPath("$.conditionFelicitation").value(DEFAULT_CONDITION_FELICITATION.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMoyenneTableauHonneur() throws Exception {
        // Get the moyenneTableauHonneur
        restMoyenneTableauHonneurMockMvc.perform(get("/api/moyenne-tableau-honneurs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMoyenneTableauHonneur() throws Exception {
        // Initialize the database
        moyenneTableauHonneurRepository.saveAndFlush(moyenneTableauHonneur);
        moyenneTableauHonneurSearchRepository.save(moyenneTableauHonneur);
        int databaseSizeBeforeUpdate = moyenneTableauHonneurRepository.findAll().size();

        // Update the moyenneTableauHonneur
        MoyenneTableauHonneur updatedMoyenneTableauHonneur = new MoyenneTableauHonneur();
        updatedMoyenneTableauHonneur.setId(moyenneTableauHonneur.getId());
        updatedMoyenneTableauHonneur.setConditionTableauHonneur(UPDATED_CONDITION_TABLEAU_HONNEUR);
        updatedMoyenneTableauHonneur.setConditionEncouragement(UPDATED_CONDITION_ENCOURAGEMENT);
        updatedMoyenneTableauHonneur.setConditionFelicitation(UPDATED_CONDITION_FELICITATION);

        restMoyenneTableauHonneurMockMvc.perform(put("/api/moyenne-tableau-honneurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMoyenneTableauHonneur)))
                .andExpect(status().isOk());

        // Validate the MoyenneTableauHonneur in the database
        List<MoyenneTableauHonneur> moyenneTableauHonneurs = moyenneTableauHonneurRepository.findAll();
        assertThat(moyenneTableauHonneurs).hasSize(databaseSizeBeforeUpdate);
        MoyenneTableauHonneur testMoyenneTableauHonneur = moyenneTableauHonneurs.get(moyenneTableauHonneurs.size() - 1);
        assertThat(testMoyenneTableauHonneur.getConditionTableauHonneur()).isEqualTo(UPDATED_CONDITION_TABLEAU_HONNEUR);
        assertThat(testMoyenneTableauHonneur.getConditionEncouragement()).isEqualTo(UPDATED_CONDITION_ENCOURAGEMENT);
        assertThat(testMoyenneTableauHonneur.getConditionFelicitation()).isEqualTo(UPDATED_CONDITION_FELICITATION);

        // Validate the MoyenneTableauHonneur in ElasticSearch
        MoyenneTableauHonneur moyenneTableauHonneurEs = moyenneTableauHonneurSearchRepository.findOne(testMoyenneTableauHonneur.getId());
        assertThat(moyenneTableauHonneurEs).isEqualToComparingFieldByField(testMoyenneTableauHonneur);
    }

    @Test
    @Transactional
    public void deleteMoyenneTableauHonneur() throws Exception {
        // Initialize the database
        moyenneTableauHonneurRepository.saveAndFlush(moyenneTableauHonneur);
        moyenneTableauHonneurSearchRepository.save(moyenneTableauHonneur);
        int databaseSizeBeforeDelete = moyenneTableauHonneurRepository.findAll().size();

        // Get the moyenneTableauHonneur
        restMoyenneTableauHonneurMockMvc.perform(delete("/api/moyenne-tableau-honneurs/{id}", moyenneTableauHonneur.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean moyenneTableauHonneurExistsInEs = moyenneTableauHonneurSearchRepository.exists(moyenneTableauHonneur.getId());
        assertThat(moyenneTableauHonneurExistsInEs).isFalse();

        // Validate the database is empty
        List<MoyenneTableauHonneur> moyenneTableauHonneurs = moyenneTableauHonneurRepository.findAll();
        assertThat(moyenneTableauHonneurs).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMoyenneTableauHonneur() throws Exception {
        // Initialize the database
        moyenneTableauHonneurRepository.saveAndFlush(moyenneTableauHonneur);
        moyenneTableauHonneurSearchRepository.save(moyenneTableauHonneur);

        // Search the moyenneTableauHonneur
        restMoyenneTableauHonneurMockMvc.perform(get("/api/_search/moyenne-tableau-honneurs?query=id:" + moyenneTableauHonneur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moyenneTableauHonneur.getId().intValue())))
            .andExpect(jsonPath("$.[*].conditionTableauHonneur").value(hasItem(DEFAULT_CONDITION_TABLEAU_HONNEUR.doubleValue())))
            .andExpect(jsonPath("$.[*].conditionEncouragement").value(hasItem(DEFAULT_CONDITION_ENCOURAGEMENT.doubleValue())))
            .andExpect(jsonPath("$.[*].conditionFelicitation").value(hasItem(DEFAULT_CONDITION_FELICITATION.doubleValue())));
    }
}
