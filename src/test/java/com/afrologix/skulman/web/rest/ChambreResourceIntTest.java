package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Chambre;
import com.afrologix.skulman.repository.ChambreRepository;
import com.afrologix.skulman.repository.search.ChambreSearchRepository;

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
 * Test class for the ChambreResource REST controller.
 *
 * @see ChambreResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class ChambreResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Integer DEFAULT_NOMBRE_MAX_PERSONNE = 10;
    private static final Integer UPDATED_NOMBRE_MAX_PERSONNE = 9;

    @Inject
    private ChambreRepository chambreRepository;

    @Inject
    private ChambreSearchRepository chambreSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restChambreMockMvc;

    private Chambre chambre;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ChambreResource chambreResource = new ChambreResource();
        ReflectionTestUtils.setField(chambreResource, "chambreSearchRepository", chambreSearchRepository);
        ReflectionTestUtils.setField(chambreResource, "chambreRepository", chambreRepository);
        this.restChambreMockMvc = MockMvcBuilders.standaloneSetup(chambreResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        chambreSearchRepository.deleteAll();
        chambre = new Chambre();
        chambre.setCode(DEFAULT_CODE);
        chambre.setLibelle(DEFAULT_LIBELLE);
        chambre.setNombreMaxPersonne(DEFAULT_NOMBRE_MAX_PERSONNE);
    }

    @Test
    @Transactional
    public void createChambre() throws Exception {
        int databaseSizeBeforeCreate = chambreRepository.findAll().size();

        // Create the Chambre

        restChambreMockMvc.perform(post("/api/chambres")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(chambre)))
                .andExpect(status().isCreated());

        // Validate the Chambre in the database
        List<Chambre> chambres = chambreRepository.findAll();
        assertThat(chambres).hasSize(databaseSizeBeforeCreate + 1);
        Chambre testChambre = chambres.get(chambres.size() - 1);
        assertThat(testChambre.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testChambre.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testChambre.getNombreMaxPersonne()).isEqualTo(DEFAULT_NOMBRE_MAX_PERSONNE);

        // Validate the Chambre in ElasticSearch
        Chambre chambreEs = chambreSearchRepository.findOne(testChambre.getId());
        assertThat(chambreEs).isEqualToComparingFieldByField(testChambre);
    }

    @Test
    @Transactional
    public void getAllChambres() throws Exception {
        // Initialize the database
        chambreRepository.saveAndFlush(chambre);

        // Get all the chambres
        restChambreMockMvc.perform(get("/api/chambres?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(chambre.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
                .andExpect(jsonPath("$.[*].nombreMaxPersonne").value(hasItem(DEFAULT_NOMBRE_MAX_PERSONNE)));
    }

    @Test
    @Transactional
    public void getChambre() throws Exception {
        // Initialize the database
        chambreRepository.saveAndFlush(chambre);

        // Get the chambre
        restChambreMockMvc.perform(get("/api/chambres/{id}", chambre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(chambre.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()))
            .andExpect(jsonPath("$.nombreMaxPersonne").value(DEFAULT_NOMBRE_MAX_PERSONNE));
    }

    @Test
    @Transactional
    public void getNonExistingChambre() throws Exception {
        // Get the chambre
        restChambreMockMvc.perform(get("/api/chambres/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChambre() throws Exception {
        // Initialize the database
        chambreRepository.saveAndFlush(chambre);
        chambreSearchRepository.save(chambre);
        int databaseSizeBeforeUpdate = chambreRepository.findAll().size();

        // Update the chambre
        Chambre updatedChambre = new Chambre();
        updatedChambre.setId(chambre.getId());
        updatedChambre.setCode(UPDATED_CODE);
        updatedChambre.setLibelle(UPDATED_LIBELLE);
        updatedChambre.setNombreMaxPersonne(UPDATED_NOMBRE_MAX_PERSONNE);

        restChambreMockMvc.perform(put("/api/chambres")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedChambre)))
                .andExpect(status().isOk());

        // Validate the Chambre in the database
        List<Chambre> chambres = chambreRepository.findAll();
        assertThat(chambres).hasSize(databaseSizeBeforeUpdate);
        Chambre testChambre = chambres.get(chambres.size() - 1);
        assertThat(testChambre.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testChambre.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testChambre.getNombreMaxPersonne()).isEqualTo(UPDATED_NOMBRE_MAX_PERSONNE);

        // Validate the Chambre in ElasticSearch
        Chambre chambreEs = chambreSearchRepository.findOne(testChambre.getId());
        assertThat(chambreEs).isEqualToComparingFieldByField(testChambre);
    }

    @Test
    @Transactional
    public void deleteChambre() throws Exception {
        // Initialize the database
        chambreRepository.saveAndFlush(chambre);
        chambreSearchRepository.save(chambre);
        int databaseSizeBeforeDelete = chambreRepository.findAll().size();

        // Get the chambre
        restChambreMockMvc.perform(delete("/api/chambres/{id}", chambre.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean chambreExistsInEs = chambreSearchRepository.exists(chambre.getId());
        assertThat(chambreExistsInEs).isFalse();

        // Validate the database is empty
        List<Chambre> chambres = chambreRepository.findAll();
        assertThat(chambres).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchChambre() throws Exception {
        // Initialize the database
        chambreRepository.saveAndFlush(chambre);
        chambreSearchRepository.save(chambre);

        // Search the chambre
        restChambreMockMvc.perform(get("/api/_search/chambres?query=id:" + chambre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chambre.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].nombreMaxPersonne").value(hasItem(DEFAULT_NOMBRE_MAX_PERSONNE)));
    }
}
