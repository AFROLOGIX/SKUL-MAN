package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Bourse;
import com.afrologix.skulman.repository.BourseRepository;
import com.afrologix.skulman.repository.search.BourseSearchRepository;

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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the BourseResource REST controller.
 *
 * @see BourseResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class BourseResourceIntTest {

    private static final String DEFAULT_MOTIF = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_MOTIF = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final BigDecimal DEFAULT_MONTANT = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTANT = new BigDecimal(2);
    private static final String DEFAULT_ANNEE_SCOLAIRE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ANNEE_SCOLAIRE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private BourseRepository bourseRepository;

    @Inject
    private BourseSearchRepository bourseSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBourseMockMvc;

    private Bourse bourse;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BourseResource bourseResource = new BourseResource();
        ReflectionTestUtils.setField(bourseResource, "bourseSearchRepository", bourseSearchRepository);
        ReflectionTestUtils.setField(bourseResource, "bourseRepository", bourseRepository);
        this.restBourseMockMvc = MockMvcBuilders.standaloneSetup(bourseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        bourseSearchRepository.deleteAll();
        bourse = new Bourse();
        bourse.setMotif(DEFAULT_MOTIF);
        bourse.setMontant(DEFAULT_MONTANT);
        bourse.setAnneeScolaire(DEFAULT_ANNEE_SCOLAIRE);
    }

    @Test
    @Transactional
    public void createBourse() throws Exception {
        int databaseSizeBeforeCreate = bourseRepository.findAll().size();

        // Create the Bourse

        restBourseMockMvc.perform(post("/api/bourses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bourse)))
                .andExpect(status().isCreated());

        // Validate the Bourse in the database
        List<Bourse> bourses = bourseRepository.findAll();
        assertThat(bourses).hasSize(databaseSizeBeforeCreate + 1);
        Bourse testBourse = bourses.get(bourses.size() - 1);
        assertThat(testBourse.getMotif()).isEqualTo(DEFAULT_MOTIF);
        assertThat(testBourse.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testBourse.getAnneeScolaire()).isEqualTo(DEFAULT_ANNEE_SCOLAIRE);

        // Validate the Bourse in ElasticSearch
        Bourse bourseEs = bourseSearchRepository.findOne(testBourse.getId());
        assertThat(bourseEs).isEqualToComparingFieldByField(testBourse);
    }

    @Test
    @Transactional
    public void checkMontantIsRequired() throws Exception {
        int databaseSizeBeforeTest = bourseRepository.findAll().size();
        // set the field null
        bourse.setMontant(null);

        // Create the Bourse, which fails.

        restBourseMockMvc.perform(post("/api/bourses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bourse)))
                .andExpect(status().isBadRequest());

        List<Bourse> bourses = bourseRepository.findAll();
        assertThat(bourses).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBourses() throws Exception {
        // Initialize the database
        bourseRepository.saveAndFlush(bourse);

        // Get all the bourses
        restBourseMockMvc.perform(get("/api/bourses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(bourse.getId().intValue())))
                .andExpect(jsonPath("$.[*].motif").value(hasItem(DEFAULT_MOTIF.toString())))
                .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.intValue())))
                .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())));
    }

    @Test
    @Transactional
    public void getBourse() throws Exception {
        // Initialize the database
        bourseRepository.saveAndFlush(bourse);

        // Get the bourse
        restBourseMockMvc.perform(get("/api/bourses/{id}", bourse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(bourse.getId().intValue()))
            .andExpect(jsonPath("$.motif").value(DEFAULT_MOTIF.toString()))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.intValue()))
            .andExpect(jsonPath("$.anneeScolaire").value(DEFAULT_ANNEE_SCOLAIRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBourse() throws Exception {
        // Get the bourse
        restBourseMockMvc.perform(get("/api/bourses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBourse() throws Exception {
        // Initialize the database
        bourseRepository.saveAndFlush(bourse);
        bourseSearchRepository.save(bourse);
        int databaseSizeBeforeUpdate = bourseRepository.findAll().size();

        // Update the bourse
        Bourse updatedBourse = new Bourse();
        updatedBourse.setId(bourse.getId());
        updatedBourse.setMotif(UPDATED_MOTIF);
        updatedBourse.setMontant(UPDATED_MONTANT);
        updatedBourse.setAnneeScolaire(UPDATED_ANNEE_SCOLAIRE);

        restBourseMockMvc.perform(put("/api/bourses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBourse)))
                .andExpect(status().isOk());

        // Validate the Bourse in the database
        List<Bourse> bourses = bourseRepository.findAll();
        assertThat(bourses).hasSize(databaseSizeBeforeUpdate);
        Bourse testBourse = bourses.get(bourses.size() - 1);
        assertThat(testBourse.getMotif()).isEqualTo(UPDATED_MOTIF);
        assertThat(testBourse.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testBourse.getAnneeScolaire()).isEqualTo(UPDATED_ANNEE_SCOLAIRE);

        // Validate the Bourse in ElasticSearch
        Bourse bourseEs = bourseSearchRepository.findOne(testBourse.getId());
        assertThat(bourseEs).isEqualToComparingFieldByField(testBourse);
    }

    @Test
    @Transactional
    public void deleteBourse() throws Exception {
        // Initialize the database
        bourseRepository.saveAndFlush(bourse);
        bourseSearchRepository.save(bourse);
        int databaseSizeBeforeDelete = bourseRepository.findAll().size();

        // Get the bourse
        restBourseMockMvc.perform(delete("/api/bourses/{id}", bourse.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean bourseExistsInEs = bourseSearchRepository.exists(bourse.getId());
        assertThat(bourseExistsInEs).isFalse();

        // Validate the database is empty
        List<Bourse> bourses = bourseRepository.findAll();
        assertThat(bourses).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBourse() throws Exception {
        // Initialize the database
        bourseRepository.saveAndFlush(bourse);
        bourseSearchRepository.save(bourse);

        // Search the bourse
        restBourseMockMvc.perform(get("/api/_search/bourses?query=id:" + bourse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bourse.getId().intValue())))
            .andExpect(jsonPath("$.[*].motif").value(hasItem(DEFAULT_MOTIF.toString())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.intValue())))
            .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())));
    }
}
