package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.StatutEleve;
import com.afrologix.skulman.repository.StatutEleveRepository;
import com.afrologix.skulman.repository.search.StatutEleveSearchRepository;

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
 * Test class for the StatutEleveResource REST controller.
 *
 * @see StatutEleveResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class StatutEleveResourceIntTest {


    private static final Boolean DEFAULT_REDOUBLE = false;
    private static final Boolean UPDATED_REDOUBLE = true;
    private static final String DEFAULT_ANNEE_SCOLAIRE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ANNEE_SCOLAIRE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private StatutEleveRepository statutEleveRepository;

    @Inject
    private StatutEleveSearchRepository statutEleveSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStatutEleveMockMvc;

    private StatutEleve statutEleve;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StatutEleveResource statutEleveResource = new StatutEleveResource();
        ReflectionTestUtils.setField(statutEleveResource, "statutEleveSearchRepository", statutEleveSearchRepository);
        ReflectionTestUtils.setField(statutEleveResource, "statutEleveRepository", statutEleveRepository);
        this.restStatutEleveMockMvc = MockMvcBuilders.standaloneSetup(statutEleveResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        statutEleveSearchRepository.deleteAll();
        statutEleve = new StatutEleve();
        statutEleve.setRedouble(DEFAULT_REDOUBLE);
        statutEleve.setAnneeScolaire(DEFAULT_ANNEE_SCOLAIRE);
    }

    @Test
    @Transactional
    public void createStatutEleve() throws Exception {
        int databaseSizeBeforeCreate = statutEleveRepository.findAll().size();

        // Create the StatutEleve

        restStatutEleveMockMvc.perform(post("/api/statut-eleves")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(statutEleve)))
                .andExpect(status().isCreated());

        // Validate the StatutEleve in the database
        List<StatutEleve> statutEleves = statutEleveRepository.findAll();
        assertThat(statutEleves).hasSize(databaseSizeBeforeCreate + 1);
        StatutEleve testStatutEleve = statutEleves.get(statutEleves.size() - 1);
        assertThat(testStatutEleve.isRedouble()).isEqualTo(DEFAULT_REDOUBLE);
        assertThat(testStatutEleve.getAnneeScolaire()).isEqualTo(DEFAULT_ANNEE_SCOLAIRE);

        // Validate the StatutEleve in ElasticSearch
        StatutEleve statutEleveEs = statutEleveSearchRepository.findOne(testStatutEleve.getId());
        assertThat(statutEleveEs).isEqualToComparingFieldByField(testStatutEleve);
    }

    @Test
    @Transactional
    public void getAllStatutEleves() throws Exception {
        // Initialize the database
        statutEleveRepository.saveAndFlush(statutEleve);

        // Get all the statutEleves
        restStatutEleveMockMvc.perform(get("/api/statut-eleves?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(statutEleve.getId().intValue())))
                .andExpect(jsonPath("$.[*].redouble").value(hasItem(DEFAULT_REDOUBLE.booleanValue())))
                .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())));
    }

    @Test
    @Transactional
    public void getStatutEleve() throws Exception {
        // Initialize the database
        statutEleveRepository.saveAndFlush(statutEleve);

        // Get the statutEleve
        restStatutEleveMockMvc.perform(get("/api/statut-eleves/{id}", statutEleve.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(statutEleve.getId().intValue()))
            .andExpect(jsonPath("$.redouble").value(DEFAULT_REDOUBLE.booleanValue()))
            .andExpect(jsonPath("$.anneeScolaire").value(DEFAULT_ANNEE_SCOLAIRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStatutEleve() throws Exception {
        // Get the statutEleve
        restStatutEleveMockMvc.perform(get("/api/statut-eleves/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStatutEleve() throws Exception {
        // Initialize the database
        statutEleveRepository.saveAndFlush(statutEleve);
        statutEleveSearchRepository.save(statutEleve);
        int databaseSizeBeforeUpdate = statutEleveRepository.findAll().size();

        // Update the statutEleve
        StatutEleve updatedStatutEleve = new StatutEleve();
        updatedStatutEleve.setId(statutEleve.getId());
        updatedStatutEleve.setRedouble(UPDATED_REDOUBLE);
        updatedStatutEleve.setAnneeScolaire(UPDATED_ANNEE_SCOLAIRE);

        restStatutEleveMockMvc.perform(put("/api/statut-eleves")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedStatutEleve)))
                .andExpect(status().isOk());

        // Validate the StatutEleve in the database
        List<StatutEleve> statutEleves = statutEleveRepository.findAll();
        assertThat(statutEleves).hasSize(databaseSizeBeforeUpdate);
        StatutEleve testStatutEleve = statutEleves.get(statutEleves.size() - 1);
        assertThat(testStatutEleve.isRedouble()).isEqualTo(UPDATED_REDOUBLE);
        assertThat(testStatutEleve.getAnneeScolaire()).isEqualTo(UPDATED_ANNEE_SCOLAIRE);

        // Validate the StatutEleve in ElasticSearch
        StatutEleve statutEleveEs = statutEleveSearchRepository.findOne(testStatutEleve.getId());
        assertThat(statutEleveEs).isEqualToComparingFieldByField(testStatutEleve);
    }

    @Test
    @Transactional
    public void deleteStatutEleve() throws Exception {
        // Initialize the database
        statutEleveRepository.saveAndFlush(statutEleve);
        statutEleveSearchRepository.save(statutEleve);
        int databaseSizeBeforeDelete = statutEleveRepository.findAll().size();

        // Get the statutEleve
        restStatutEleveMockMvc.perform(delete("/api/statut-eleves/{id}", statutEleve.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean statutEleveExistsInEs = statutEleveSearchRepository.exists(statutEleve.getId());
        assertThat(statutEleveExistsInEs).isFalse();

        // Validate the database is empty
        List<StatutEleve> statutEleves = statutEleveRepository.findAll();
        assertThat(statutEleves).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchStatutEleve() throws Exception {
        // Initialize the database
        statutEleveRepository.saveAndFlush(statutEleve);
        statutEleveSearchRepository.save(statutEleve);

        // Search the statutEleve
        restStatutEleveMockMvc.perform(get("/api/_search/statut-eleves?query=id:" + statutEleve.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(statutEleve.getId().intValue())))
            .andExpect(jsonPath("$.[*].redouble").value(hasItem(DEFAULT_REDOUBLE.booleanValue())))
            .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())));
    }
}
