package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Serie;
import com.afrologix.skulman.repository.SerieRepository;
import com.afrologix.skulman.repository.search.SerieSearchRepository;

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
 * Test class for the SerieResource REST controller.
 *
 * @see SerieResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class SerieResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_FR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_FR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_EN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_EN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private SerieRepository serieRepository;

    @Inject
    private SerieSearchRepository serieSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSerieMockMvc;

    private Serie serie;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SerieResource serieResource = new SerieResource();
        ReflectionTestUtils.setField(serieResource, "serieSearchRepository", serieSearchRepository);
        ReflectionTestUtils.setField(serieResource, "serieRepository", serieRepository);
        this.restSerieMockMvc = MockMvcBuilders.standaloneSetup(serieResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        serieSearchRepository.deleteAll();
        serie = new Serie();
        serie.setCode(DEFAULT_CODE);
        serie.setLibelleFr(DEFAULT_LIBELLE_FR);
        serie.setLibelleEn(DEFAULT_LIBELLE_EN);
    }

    @Test
    @Transactional
    public void createSerie() throws Exception {
        int databaseSizeBeforeCreate = serieRepository.findAll().size();

        // Create the Serie

        restSerieMockMvc.perform(post("/api/series")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(serie)))
                .andExpect(status().isCreated());

        // Validate the Serie in the database
        List<Serie> series = serieRepository.findAll();
        assertThat(series).hasSize(databaseSizeBeforeCreate + 1);
        Serie testSerie = series.get(series.size() - 1);
        assertThat(testSerie.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSerie.getLibelleFr()).isEqualTo(DEFAULT_LIBELLE_FR);
        assertThat(testSerie.getLibelleEn()).isEqualTo(DEFAULT_LIBELLE_EN);

        // Validate the Serie in ElasticSearch
        Serie serieEs = serieSearchRepository.findOne(testSerie.getId());
        assertThat(serieEs).isEqualToComparingFieldByField(testSerie);
    }

    @Test
    @Transactional
    public void getAllSeries() throws Exception {
        // Initialize the database
        serieRepository.saveAndFlush(serie);

        // Get all the series
        restSerieMockMvc.perform(get("/api/series?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(serie.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
                .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())));
    }

    @Test
    @Transactional
    public void getSerie() throws Exception {
        // Initialize the database
        serieRepository.saveAndFlush(serie);

        // Get the serie
        restSerieMockMvc.perform(get("/api/series/{id}", serie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(serie.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelleFr").value(DEFAULT_LIBELLE_FR.toString()))
            .andExpect(jsonPath("$.libelleEn").value(DEFAULT_LIBELLE_EN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSerie() throws Exception {
        // Get the serie
        restSerieMockMvc.perform(get("/api/series/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSerie() throws Exception {
        // Initialize the database
        serieRepository.saveAndFlush(serie);
        serieSearchRepository.save(serie);
        int databaseSizeBeforeUpdate = serieRepository.findAll().size();

        // Update the serie
        Serie updatedSerie = new Serie();
        updatedSerie.setId(serie.getId());
        updatedSerie.setCode(UPDATED_CODE);
        updatedSerie.setLibelleFr(UPDATED_LIBELLE_FR);
        updatedSerie.setLibelleEn(UPDATED_LIBELLE_EN);

        restSerieMockMvc.perform(put("/api/series")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSerie)))
                .andExpect(status().isOk());

        // Validate the Serie in the database
        List<Serie> series = serieRepository.findAll();
        assertThat(series).hasSize(databaseSizeBeforeUpdate);
        Serie testSerie = series.get(series.size() - 1);
        assertThat(testSerie.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSerie.getLibelleFr()).isEqualTo(UPDATED_LIBELLE_FR);
        assertThat(testSerie.getLibelleEn()).isEqualTo(UPDATED_LIBELLE_EN);

        // Validate the Serie in ElasticSearch
        Serie serieEs = serieSearchRepository.findOne(testSerie.getId());
        assertThat(serieEs).isEqualToComparingFieldByField(testSerie);
    }

    @Test
    @Transactional
    public void deleteSerie() throws Exception {
        // Initialize the database
        serieRepository.saveAndFlush(serie);
        serieSearchRepository.save(serie);
        int databaseSizeBeforeDelete = serieRepository.findAll().size();

        // Get the serie
        restSerieMockMvc.perform(delete("/api/series/{id}", serie.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean serieExistsInEs = serieSearchRepository.exists(serie.getId());
        assertThat(serieExistsInEs).isFalse();

        // Validate the database is empty
        List<Serie> series = serieRepository.findAll();
        assertThat(series).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSerie() throws Exception {
        // Initialize the database
        serieRepository.saveAndFlush(serie);
        serieSearchRepository.save(serie);

        // Search the serie
        restSerieMockMvc.perform(get("/api/_search/series?query=id:" + serie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serie.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
            .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())));
    }
}
