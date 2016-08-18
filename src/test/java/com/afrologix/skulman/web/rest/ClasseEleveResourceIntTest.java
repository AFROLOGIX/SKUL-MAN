package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.ClasseEleve;
import com.afrologix.skulman.repository.ClasseEleveRepository;
import com.afrologix.skulman.repository.search.ClasseEleveSearchRepository;

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
 * Test class for the ClasseEleveResource REST controller.
 *
 * @see ClasseEleveResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class ClasseEleveResourceIntTest {

    private static final String DEFAULT_ANNEE_SCOLAIRE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ANNEE_SCOLAIRE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;
    private static final String DEFAULT_OBSERVATION = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_OBSERVATION = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private ClasseEleveRepository classeEleveRepository;

    @Inject
    private ClasseEleveSearchRepository classeEleveSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restClasseEleveMockMvc;

    private ClasseEleve classeEleve;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClasseEleveResource classeEleveResource = new ClasseEleveResource();
        ReflectionTestUtils.setField(classeEleveResource, "classeEleveSearchRepository", classeEleveSearchRepository);
        ReflectionTestUtils.setField(classeEleveResource, "classeEleveRepository", classeEleveRepository);
        this.restClasseEleveMockMvc = MockMvcBuilders.standaloneSetup(classeEleveResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        classeEleveSearchRepository.deleteAll();
        classeEleve = new ClasseEleve();
        classeEleve.setAnneeScolaire(DEFAULT_ANNEE_SCOLAIRE);
        classeEleve.setIsActive(DEFAULT_IS_ACTIVE);
        classeEleve.setObservation(DEFAULT_OBSERVATION);
    }

    @Test
    @Transactional
    public void createClasseEleve() throws Exception {
        int databaseSizeBeforeCreate = classeEleveRepository.findAll().size();

        // Create the ClasseEleve

        restClasseEleveMockMvc.perform(post("/api/classe-eleves")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(classeEleve)))
                .andExpect(status().isCreated());

        // Validate the ClasseEleve in the database
        List<ClasseEleve> classeEleves = classeEleveRepository.findAll();
        assertThat(classeEleves).hasSize(databaseSizeBeforeCreate + 1);
        ClasseEleve testClasseEleve = classeEleves.get(classeEleves.size() - 1);
        assertThat(testClasseEleve.getAnneeScolaire()).isEqualTo(DEFAULT_ANNEE_SCOLAIRE);
        assertThat(testClasseEleve.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testClasseEleve.getObservation()).isEqualTo(DEFAULT_OBSERVATION);

        // Validate the ClasseEleve in ElasticSearch
        ClasseEleve classeEleveEs = classeEleveSearchRepository.findOne(testClasseEleve.getId());
        assertThat(classeEleveEs).isEqualToComparingFieldByField(testClasseEleve);
    }

    @Test
    @Transactional
    public void getAllClasseEleves() throws Exception {
        // Initialize the database
        classeEleveRepository.saveAndFlush(classeEleve);

        // Get all the classeEleves
        restClasseEleveMockMvc.perform(get("/api/classe-eleves?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(classeEleve.getId().intValue())))
                .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())))
                .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
                .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION.toString())));
    }

    @Test
    @Transactional
    public void getClasseEleve() throws Exception {
        // Initialize the database
        classeEleveRepository.saveAndFlush(classeEleve);

        // Get the classeEleve
        restClasseEleveMockMvc.perform(get("/api/classe-eleves/{id}", classeEleve.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(classeEleve.getId().intValue()))
            .andExpect(jsonPath("$.anneeScolaire").value(DEFAULT_ANNEE_SCOLAIRE.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.observation").value(DEFAULT_OBSERVATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingClasseEleve() throws Exception {
        // Get the classeEleve
        restClasseEleveMockMvc.perform(get("/api/classe-eleves/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClasseEleve() throws Exception {
        // Initialize the database
        classeEleveRepository.saveAndFlush(classeEleve);
        classeEleveSearchRepository.save(classeEleve);
        int databaseSizeBeforeUpdate = classeEleveRepository.findAll().size();

        // Update the classeEleve
        ClasseEleve updatedClasseEleve = new ClasseEleve();
        updatedClasseEleve.setId(classeEleve.getId());
        updatedClasseEleve.setAnneeScolaire(UPDATED_ANNEE_SCOLAIRE);
        updatedClasseEleve.setIsActive(UPDATED_IS_ACTIVE);
        updatedClasseEleve.setObservation(UPDATED_OBSERVATION);

        restClasseEleveMockMvc.perform(put("/api/classe-eleves")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedClasseEleve)))
                .andExpect(status().isOk());

        // Validate the ClasseEleve in the database
        List<ClasseEleve> classeEleves = classeEleveRepository.findAll();
        assertThat(classeEleves).hasSize(databaseSizeBeforeUpdate);
        ClasseEleve testClasseEleve = classeEleves.get(classeEleves.size() - 1);
        assertThat(testClasseEleve.getAnneeScolaire()).isEqualTo(UPDATED_ANNEE_SCOLAIRE);
        assertThat(testClasseEleve.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testClasseEleve.getObservation()).isEqualTo(UPDATED_OBSERVATION);

        // Validate the ClasseEleve in ElasticSearch
        ClasseEleve classeEleveEs = classeEleveSearchRepository.findOne(testClasseEleve.getId());
        assertThat(classeEleveEs).isEqualToComparingFieldByField(testClasseEleve);
    }

    @Test
    @Transactional
    public void deleteClasseEleve() throws Exception {
        // Initialize the database
        classeEleveRepository.saveAndFlush(classeEleve);
        classeEleveSearchRepository.save(classeEleve);
        int databaseSizeBeforeDelete = classeEleveRepository.findAll().size();

        // Get the classeEleve
        restClasseEleveMockMvc.perform(delete("/api/classe-eleves/{id}", classeEleve.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean classeEleveExistsInEs = classeEleveSearchRepository.exists(classeEleve.getId());
        assertThat(classeEleveExistsInEs).isFalse();

        // Validate the database is empty
        List<ClasseEleve> classeEleves = classeEleveRepository.findAll();
        assertThat(classeEleves).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchClasseEleve() throws Exception {
        // Initialize the database
        classeEleveRepository.saveAndFlush(classeEleve);
        classeEleveSearchRepository.save(classeEleve);

        // Search the classeEleve
        restClasseEleveMockMvc.perform(get("/api/_search/classe-eleves?query=id:" + classeEleve.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classeEleve.getId().intValue())))
            .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION.toString())));
    }
}
