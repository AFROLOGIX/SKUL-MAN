package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Cours;
import com.afrologix.skulman.repository.CoursRepository;
import com.afrologix.skulman.repository.search.CoursSearchRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the CoursResource REST controller.
 *
 * @see CoursResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class CoursResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_CODE = "AAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_CREATE_BY = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CREATE_BY = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_UPDATE_BY = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_UPDATE_BY = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATE_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATE_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATE_AT_STR = dateTimeFormatter.format(DEFAULT_CREATE_AT);

    private static final ZonedDateTime DEFAULT_UPDATE_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_UPDATE_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_UPDATE_AT_STR = dateTimeFormatter.format(DEFAULT_UPDATE_AT);
    private static final String DEFAULT_ANNEE_SCOLAIRE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ANNEE_SCOLAIRE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private CoursRepository coursRepository;

    @Inject
    private CoursSearchRepository coursSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCoursMockMvc;

    private Cours cours;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CoursResource coursResource = new CoursResource();
        ReflectionTestUtils.setField(coursResource, "coursSearchRepository", coursSearchRepository);
        ReflectionTestUtils.setField(coursResource, "coursRepository", coursRepository);
        this.restCoursMockMvc = MockMvcBuilders.standaloneSetup(coursResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        coursSearchRepository.deleteAll();
        cours = new Cours();
        cours.setCode(DEFAULT_CODE);
        cours.setCreateBy(DEFAULT_CREATE_BY);
        cours.setUpdateBy(DEFAULT_UPDATE_BY);
        cours.setCreateAt(DEFAULT_CREATE_AT);
        cours.setUpdateAt(DEFAULT_UPDATE_AT);
        cours.setAnneeScolaire(DEFAULT_ANNEE_SCOLAIRE);
    }

    @Test
    @Transactional
    public void createCours() throws Exception {
        int databaseSizeBeforeCreate = coursRepository.findAll().size();

        // Create the Cours

        restCoursMockMvc.perform(post("/api/cours")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cours)))
                .andExpect(status().isCreated());

        // Validate the Cours in the database
        List<Cours> cours = coursRepository.findAll();
        assertThat(cours).hasSize(databaseSizeBeforeCreate + 1);
        Cours testCours = cours.get(cours.size() - 1);
        assertThat(testCours.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCours.getCreateBy()).isEqualTo(DEFAULT_CREATE_BY);
        assertThat(testCours.getUpdateBy()).isEqualTo(DEFAULT_UPDATE_BY);
        assertThat(testCours.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testCours.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
        assertThat(testCours.getAnneeScolaire()).isEqualTo(DEFAULT_ANNEE_SCOLAIRE);

        // Validate the Cours in ElasticSearch
        Cours coursEs = coursSearchRepository.findOne(testCours.getId());
        assertThat(coursEs).isEqualToComparingFieldByField(testCours);
    }

    @Test
    @Transactional
    public void getAllCours() throws Exception {
        // Initialize the database
        coursRepository.saveAndFlush(cours);

        // Get all the cours
        restCoursMockMvc.perform(get("/api/cours?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(cours.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY.toString())))
                .andExpect(jsonPath("$.[*].updateBy").value(hasItem(DEFAULT_UPDATE_BY.toString())))
                .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT_STR)))
                .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT_STR)))
                .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())));
    }

    @Test
    @Transactional
    public void getCours() throws Exception {
        // Initialize the database
        coursRepository.saveAndFlush(cours);

        // Get the cours
        restCoursMockMvc.perform(get("/api/cours/{id}", cours.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(cours.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.createBy").value(DEFAULT_CREATE_BY.toString()))
            .andExpect(jsonPath("$.updateBy").value(DEFAULT_UPDATE_BY.toString()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT_STR))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT_STR))
            .andExpect(jsonPath("$.anneeScolaire").value(DEFAULT_ANNEE_SCOLAIRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCours() throws Exception {
        // Get the cours
        restCoursMockMvc.perform(get("/api/cours/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCours() throws Exception {
        // Initialize the database
        coursRepository.saveAndFlush(cours);
        coursSearchRepository.save(cours);
        int databaseSizeBeforeUpdate = coursRepository.findAll().size();

        // Update the cours
        Cours updatedCours = new Cours();
        updatedCours.setId(cours.getId());
        updatedCours.setCode(UPDATED_CODE);
        updatedCours.setCreateBy(UPDATED_CREATE_BY);
        updatedCours.setUpdateBy(UPDATED_UPDATE_BY);
        updatedCours.setCreateAt(UPDATED_CREATE_AT);
        updatedCours.setUpdateAt(UPDATED_UPDATE_AT);
        updatedCours.setAnneeScolaire(UPDATED_ANNEE_SCOLAIRE);

        restCoursMockMvc.perform(put("/api/cours")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCours)))
                .andExpect(status().isOk());

        // Validate the Cours in the database
        List<Cours> cours = coursRepository.findAll();
        assertThat(cours).hasSize(databaseSizeBeforeUpdate);
        Cours testCours = cours.get(cours.size() - 1);
        assertThat(testCours.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCours.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testCours.getUpdateBy()).isEqualTo(UPDATED_UPDATE_BY);
        assertThat(testCours.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testCours.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
        assertThat(testCours.getAnneeScolaire()).isEqualTo(UPDATED_ANNEE_SCOLAIRE);

        // Validate the Cours in ElasticSearch
        Cours coursEs = coursSearchRepository.findOne(testCours.getId());
        assertThat(coursEs).isEqualToComparingFieldByField(testCours);
    }

    @Test
    @Transactional
    public void deleteCours() throws Exception {
        // Initialize the database
        coursRepository.saveAndFlush(cours);
        coursSearchRepository.save(cours);
        int databaseSizeBeforeDelete = coursRepository.findAll().size();

        // Get the cours
        restCoursMockMvc.perform(delete("/api/cours/{id}", cours.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean coursExistsInEs = coursSearchRepository.exists(cours.getId());
        assertThat(coursExistsInEs).isFalse();

        // Validate the database is empty
        List<Cours> cours = coursRepository.findAll();
        assertThat(cours).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCours() throws Exception {
        // Initialize the database
        coursRepository.saveAndFlush(cours);
        coursSearchRepository.save(cours);

        // Search the cours
        restCoursMockMvc.perform(get("/api/_search/cours?query=id:" + cours.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cours.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY.toString())))
            .andExpect(jsonPath("$.[*].updateBy").value(hasItem(DEFAULT_UPDATE_BY.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT_STR)))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT_STR)))
            .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())));
    }
}
