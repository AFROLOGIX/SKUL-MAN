package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.TarifBus;
import com.afrologix.skulman.repository.TarifBusRepository;
import com.afrologix.skulman.repository.search.TarifBusSearchRepository;

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
 * Test class for the TarifBusResource REST controller.
 *
 * @see TarifBusResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class TarifBusResourceIntTest {

    private static final String DEFAULT_PERIODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_PERIODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Double DEFAULT_TARIF = 1D;
    private static final Double UPDATED_TARIF = 2D;

    @Inject
    private TarifBusRepository tarifBusRepository;

    @Inject
    private TarifBusSearchRepository tarifBusSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTarifBusMockMvc;

    private TarifBus tarifBus;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TarifBusResource tarifBusResource = new TarifBusResource();
        ReflectionTestUtils.setField(tarifBusResource, "tarifBusSearchRepository", tarifBusSearchRepository);
        ReflectionTestUtils.setField(tarifBusResource, "tarifBusRepository", tarifBusRepository);
        this.restTarifBusMockMvc = MockMvcBuilders.standaloneSetup(tarifBusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        tarifBusSearchRepository.deleteAll();
        tarifBus = new TarifBus();
        tarifBus.setPeriode(DEFAULT_PERIODE);
        tarifBus.setTarif(DEFAULT_TARIF);
    }

    @Test
    @Transactional
    public void createTarifBus() throws Exception {
        int databaseSizeBeforeCreate = tarifBusRepository.findAll().size();

        // Create the TarifBus

        restTarifBusMockMvc.perform(post("/api/tarif-buses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tarifBus)))
                .andExpect(status().isCreated());

        // Validate the TarifBus in the database
        List<TarifBus> tarifBuses = tarifBusRepository.findAll();
        assertThat(tarifBuses).hasSize(databaseSizeBeforeCreate + 1);
        TarifBus testTarifBus = tarifBuses.get(tarifBuses.size() - 1);
        assertThat(testTarifBus.getPeriode()).isEqualTo(DEFAULT_PERIODE);
        assertThat(testTarifBus.getTarif()).isEqualTo(DEFAULT_TARIF);

        // Validate the TarifBus in ElasticSearch
        TarifBus tarifBusEs = tarifBusSearchRepository.findOne(testTarifBus.getId());
        assertThat(tarifBusEs).isEqualToComparingFieldByField(testTarifBus);
    }

    @Test
    @Transactional
    public void getAllTarifBuses() throws Exception {
        // Initialize the database
        tarifBusRepository.saveAndFlush(tarifBus);

        // Get all the tarifBuses
        restTarifBusMockMvc.perform(get("/api/tarif-buses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tarifBus.getId().intValue())))
                .andExpect(jsonPath("$.[*].periode").value(hasItem(DEFAULT_PERIODE.toString())))
                .andExpect(jsonPath("$.[*].tarif").value(hasItem(DEFAULT_TARIF.doubleValue())));
    }

    @Test
    @Transactional
    public void getTarifBus() throws Exception {
        // Initialize the database
        tarifBusRepository.saveAndFlush(tarifBus);

        // Get the tarifBus
        restTarifBusMockMvc.perform(get("/api/tarif-buses/{id}", tarifBus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(tarifBus.getId().intValue()))
            .andExpect(jsonPath("$.periode").value(DEFAULT_PERIODE.toString()))
            .andExpect(jsonPath("$.tarif").value(DEFAULT_TARIF.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTarifBus() throws Exception {
        // Get the tarifBus
        restTarifBusMockMvc.perform(get("/api/tarif-buses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTarifBus() throws Exception {
        // Initialize the database
        tarifBusRepository.saveAndFlush(tarifBus);
        tarifBusSearchRepository.save(tarifBus);
        int databaseSizeBeforeUpdate = tarifBusRepository.findAll().size();

        // Update the tarifBus
        TarifBus updatedTarifBus = new TarifBus();
        updatedTarifBus.setId(tarifBus.getId());
        updatedTarifBus.setPeriode(UPDATED_PERIODE);
        updatedTarifBus.setTarif(UPDATED_TARIF);

        restTarifBusMockMvc.perform(put("/api/tarif-buses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTarifBus)))
                .andExpect(status().isOk());

        // Validate the TarifBus in the database
        List<TarifBus> tarifBuses = tarifBusRepository.findAll();
        assertThat(tarifBuses).hasSize(databaseSizeBeforeUpdate);
        TarifBus testTarifBus = tarifBuses.get(tarifBuses.size() - 1);
        assertThat(testTarifBus.getPeriode()).isEqualTo(UPDATED_PERIODE);
        assertThat(testTarifBus.getTarif()).isEqualTo(UPDATED_TARIF);

        // Validate the TarifBus in ElasticSearch
        TarifBus tarifBusEs = tarifBusSearchRepository.findOne(testTarifBus.getId());
        assertThat(tarifBusEs).isEqualToComparingFieldByField(testTarifBus);
    }

    @Test
    @Transactional
    public void deleteTarifBus() throws Exception {
        // Initialize the database
        tarifBusRepository.saveAndFlush(tarifBus);
        tarifBusSearchRepository.save(tarifBus);
        int databaseSizeBeforeDelete = tarifBusRepository.findAll().size();

        // Get the tarifBus
        restTarifBusMockMvc.perform(delete("/api/tarif-buses/{id}", tarifBus.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean tarifBusExistsInEs = tarifBusSearchRepository.exists(tarifBus.getId());
        assertThat(tarifBusExistsInEs).isFalse();

        // Validate the database is empty
        List<TarifBus> tarifBuses = tarifBusRepository.findAll();
        assertThat(tarifBuses).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTarifBus() throws Exception {
        // Initialize the database
        tarifBusRepository.saveAndFlush(tarifBus);
        tarifBusSearchRepository.save(tarifBus);

        // Search the tarifBus
        restTarifBusMockMvc.perform(get("/api/_search/tarif-buses?query=id:" + tarifBus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tarifBus.getId().intValue())))
            .andExpect(jsonPath("$.[*].periode").value(hasItem(DEFAULT_PERIODE.toString())))
            .andExpect(jsonPath("$.[*].tarif").value(hasItem(DEFAULT_TARIF.doubleValue())));
    }
}
