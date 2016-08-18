package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Bulletin;
import com.afrologix.skulman.repository.BulletinRepository;
import com.afrologix.skulman.repository.search.BulletinSearchRepository;

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
 * Test class for the BulletinResource REST controller.
 *
 * @see BulletinResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class BulletinResourceIntTest {


    private static final Double DEFAULT_MOYENNE = 1D;
    private static final Double UPDATED_MOYENNE = 2D;

    private static final Integer DEFAULT_RANG = 1000;
    private static final Integer UPDATED_RANG = 999;
    private static final String DEFAULT_ANNEE_SCOLAIRE = "AAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ANNEE_SCOLAIRE = "BBBBBBBBBBBBBBBBBBBB";

    @Inject
    private BulletinRepository bulletinRepository;

    @Inject
    private BulletinSearchRepository bulletinSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBulletinMockMvc;

    private Bulletin bulletin;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BulletinResource bulletinResource = new BulletinResource();
        ReflectionTestUtils.setField(bulletinResource, "bulletinSearchRepository", bulletinSearchRepository);
        ReflectionTestUtils.setField(bulletinResource, "bulletinRepository", bulletinRepository);
        this.restBulletinMockMvc = MockMvcBuilders.standaloneSetup(bulletinResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        bulletinSearchRepository.deleteAll();
        bulletin = new Bulletin();
        bulletin.setMoyenne(DEFAULT_MOYENNE);
        bulletin.setRang(DEFAULT_RANG);
        bulletin.setAnneeScolaire(DEFAULT_ANNEE_SCOLAIRE);
    }

    @Test
    @Transactional
    public void createBulletin() throws Exception {
        int databaseSizeBeforeCreate = bulletinRepository.findAll().size();

        // Create the Bulletin

        restBulletinMockMvc.perform(post("/api/bulletins")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bulletin)))
                .andExpect(status().isCreated());

        // Validate the Bulletin in the database
        List<Bulletin> bulletins = bulletinRepository.findAll();
        assertThat(bulletins).hasSize(databaseSizeBeforeCreate + 1);
        Bulletin testBulletin = bulletins.get(bulletins.size() - 1);
        assertThat(testBulletin.getMoyenne()).isEqualTo(DEFAULT_MOYENNE);
        assertThat(testBulletin.getRang()).isEqualTo(DEFAULT_RANG);
        assertThat(testBulletin.getAnneeScolaire()).isEqualTo(DEFAULT_ANNEE_SCOLAIRE);

        // Validate the Bulletin in ElasticSearch
        Bulletin bulletinEs = bulletinSearchRepository.findOne(testBulletin.getId());
        assertThat(bulletinEs).isEqualToComparingFieldByField(testBulletin);
    }

    @Test
    @Transactional
    public void getAllBulletins() throws Exception {
        // Initialize the database
        bulletinRepository.saveAndFlush(bulletin);

        // Get all the bulletins
        restBulletinMockMvc.perform(get("/api/bulletins?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(bulletin.getId().intValue())))
                .andExpect(jsonPath("$.[*].moyenne").value(hasItem(DEFAULT_MOYENNE.doubleValue())))
                .andExpect(jsonPath("$.[*].rang").value(hasItem(DEFAULT_RANG)))
                .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())));
    }

    @Test
    @Transactional
    public void getBulletin() throws Exception {
        // Initialize the database
        bulletinRepository.saveAndFlush(bulletin);

        // Get the bulletin
        restBulletinMockMvc.perform(get("/api/bulletins/{id}", bulletin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(bulletin.getId().intValue()))
            .andExpect(jsonPath("$.moyenne").value(DEFAULT_MOYENNE.doubleValue()))
            .andExpect(jsonPath("$.rang").value(DEFAULT_RANG))
            .andExpect(jsonPath("$.anneeScolaire").value(DEFAULT_ANNEE_SCOLAIRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBulletin() throws Exception {
        // Get the bulletin
        restBulletinMockMvc.perform(get("/api/bulletins/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBulletin() throws Exception {
        // Initialize the database
        bulletinRepository.saveAndFlush(bulletin);
        bulletinSearchRepository.save(bulletin);
        int databaseSizeBeforeUpdate = bulletinRepository.findAll().size();

        // Update the bulletin
        Bulletin updatedBulletin = new Bulletin();
        updatedBulletin.setId(bulletin.getId());
        updatedBulletin.setMoyenne(UPDATED_MOYENNE);
        updatedBulletin.setRang(UPDATED_RANG);
        updatedBulletin.setAnneeScolaire(UPDATED_ANNEE_SCOLAIRE);

        restBulletinMockMvc.perform(put("/api/bulletins")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBulletin)))
                .andExpect(status().isOk());

        // Validate the Bulletin in the database
        List<Bulletin> bulletins = bulletinRepository.findAll();
        assertThat(bulletins).hasSize(databaseSizeBeforeUpdate);
        Bulletin testBulletin = bulletins.get(bulletins.size() - 1);
        assertThat(testBulletin.getMoyenne()).isEqualTo(UPDATED_MOYENNE);
        assertThat(testBulletin.getRang()).isEqualTo(UPDATED_RANG);
        assertThat(testBulletin.getAnneeScolaire()).isEqualTo(UPDATED_ANNEE_SCOLAIRE);

        // Validate the Bulletin in ElasticSearch
        Bulletin bulletinEs = bulletinSearchRepository.findOne(testBulletin.getId());
        assertThat(bulletinEs).isEqualToComparingFieldByField(testBulletin);
    }

    @Test
    @Transactional
    public void deleteBulletin() throws Exception {
        // Initialize the database
        bulletinRepository.saveAndFlush(bulletin);
        bulletinSearchRepository.save(bulletin);
        int databaseSizeBeforeDelete = bulletinRepository.findAll().size();

        // Get the bulletin
        restBulletinMockMvc.perform(delete("/api/bulletins/{id}", bulletin.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean bulletinExistsInEs = bulletinSearchRepository.exists(bulletin.getId());
        assertThat(bulletinExistsInEs).isFalse();

        // Validate the database is empty
        List<Bulletin> bulletins = bulletinRepository.findAll();
        assertThat(bulletins).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBulletin() throws Exception {
        // Initialize the database
        bulletinRepository.saveAndFlush(bulletin);
        bulletinSearchRepository.save(bulletin);

        // Search the bulletin
        restBulletinMockMvc.perform(get("/api/_search/bulletins?query=id:" + bulletin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bulletin.getId().intValue())))
            .andExpect(jsonPath("$.[*].moyenne").value(hasItem(DEFAULT_MOYENNE.doubleValue())))
            .andExpect(jsonPath("$.[*].rang").value(hasItem(DEFAULT_RANG)))
            .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())));
    }
}
