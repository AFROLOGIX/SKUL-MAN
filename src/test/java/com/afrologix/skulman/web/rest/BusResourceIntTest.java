package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Bus;
import com.afrologix.skulman.repository.BusRepository;
import com.afrologix.skulman.repository.search.BusSearchRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the BusResource REST controller.
 *
 * @see BusResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class BusResourceIntTest {

    private static final String DEFAULT_IMMATRICULATION = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_IMMATRICULATION = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_FR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_FR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_EN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_EN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Integer DEFAULT_CAPACITE = 120;
    private static final Integer UPDATED_CAPACITE = 119;
    private static final String DEFAULT_MARQUE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_MARQUE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_ACQUISITION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ACQUISITION = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Inject
    private BusRepository busRepository;

    @Inject
    private BusSearchRepository busSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBusMockMvc;

    private Bus bus;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BusResource busResource = new BusResource();
        ReflectionTestUtils.setField(busResource, "busSearchRepository", busSearchRepository);
        ReflectionTestUtils.setField(busResource, "busRepository", busRepository);
        this.restBusMockMvc = MockMvcBuilders.standaloneSetup(busResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        busSearchRepository.deleteAll();
        bus = new Bus();
        bus.setImmatriculation(DEFAULT_IMMATRICULATION);
        bus.setLibelleFr(DEFAULT_LIBELLE_FR);
        bus.setLibelleEn(DEFAULT_LIBELLE_EN);
        bus.setCapacite(DEFAULT_CAPACITE);
        bus.setMarque(DEFAULT_MARQUE);
        bus.setDateAcquisition(DEFAULT_DATE_ACQUISITION);
        bus.setIsActive(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void createBus() throws Exception {
        int databaseSizeBeforeCreate = busRepository.findAll().size();

        // Create the Bus

        restBusMockMvc.perform(post("/api/buses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bus)))
                .andExpect(status().isCreated());

        // Validate the Bus in the database
        List<Bus> buses = busRepository.findAll();
        assertThat(buses).hasSize(databaseSizeBeforeCreate + 1);
        Bus testBus = buses.get(buses.size() - 1);
        assertThat(testBus.getImmatriculation()).isEqualTo(DEFAULT_IMMATRICULATION);
        assertThat(testBus.getLibelleFr()).isEqualTo(DEFAULT_LIBELLE_FR);
        assertThat(testBus.getLibelleEn()).isEqualTo(DEFAULT_LIBELLE_EN);
        assertThat(testBus.getCapacite()).isEqualTo(DEFAULT_CAPACITE);
        assertThat(testBus.getMarque()).isEqualTo(DEFAULT_MARQUE);
        assertThat(testBus.getDateAcquisition()).isEqualTo(DEFAULT_DATE_ACQUISITION);
        assertThat(testBus.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);

        // Validate the Bus in ElasticSearch
        Bus busEs = busSearchRepository.findOne(testBus.getId());
        assertThat(busEs).isEqualToComparingFieldByField(testBus);
    }

    @Test
    @Transactional
    public void getAllBuses() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get all the buses
        restBusMockMvc.perform(get("/api/buses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(bus.getId().intValue())))
                .andExpect(jsonPath("$.[*].immatriculation").value(hasItem(DEFAULT_IMMATRICULATION.toString())))
                .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
                .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())))
                .andExpect(jsonPath("$.[*].capacite").value(hasItem(DEFAULT_CAPACITE)))
                .andExpect(jsonPath("$.[*].marque").value(hasItem(DEFAULT_MARQUE.toString())))
                .andExpect(jsonPath("$.[*].dateAcquisition").value(hasItem(DEFAULT_DATE_ACQUISITION.toString())))
                .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getBus() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);

        // Get the bus
        restBusMockMvc.perform(get("/api/buses/{id}", bus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(bus.getId().intValue()))
            .andExpect(jsonPath("$.immatriculation").value(DEFAULT_IMMATRICULATION.toString()))
            .andExpect(jsonPath("$.libelleFr").value(DEFAULT_LIBELLE_FR.toString()))
            .andExpect(jsonPath("$.libelleEn").value(DEFAULT_LIBELLE_EN.toString()))
            .andExpect(jsonPath("$.capacite").value(DEFAULT_CAPACITE))
            .andExpect(jsonPath("$.marque").value(DEFAULT_MARQUE.toString()))
            .andExpect(jsonPath("$.dateAcquisition").value(DEFAULT_DATE_ACQUISITION.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBus() throws Exception {
        // Get the bus
        restBusMockMvc.perform(get("/api/buses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBus() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);
        busSearchRepository.save(bus);
        int databaseSizeBeforeUpdate = busRepository.findAll().size();

        // Update the bus
        Bus updatedBus = new Bus();
        updatedBus.setId(bus.getId());
        updatedBus.setImmatriculation(UPDATED_IMMATRICULATION);
        updatedBus.setLibelleFr(UPDATED_LIBELLE_FR);
        updatedBus.setLibelleEn(UPDATED_LIBELLE_EN);
        updatedBus.setCapacite(UPDATED_CAPACITE);
        updatedBus.setMarque(UPDATED_MARQUE);
        updatedBus.setDateAcquisition(UPDATED_DATE_ACQUISITION);
        updatedBus.setIsActive(UPDATED_IS_ACTIVE);

        restBusMockMvc.perform(put("/api/buses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBus)))
                .andExpect(status().isOk());

        // Validate the Bus in the database
        List<Bus> buses = busRepository.findAll();
        assertThat(buses).hasSize(databaseSizeBeforeUpdate);
        Bus testBus = buses.get(buses.size() - 1);
        assertThat(testBus.getImmatriculation()).isEqualTo(UPDATED_IMMATRICULATION);
        assertThat(testBus.getLibelleFr()).isEqualTo(UPDATED_LIBELLE_FR);
        assertThat(testBus.getLibelleEn()).isEqualTo(UPDATED_LIBELLE_EN);
        assertThat(testBus.getCapacite()).isEqualTo(UPDATED_CAPACITE);
        assertThat(testBus.getMarque()).isEqualTo(UPDATED_MARQUE);
        assertThat(testBus.getDateAcquisition()).isEqualTo(UPDATED_DATE_ACQUISITION);
        assertThat(testBus.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);

        // Validate the Bus in ElasticSearch
        Bus busEs = busSearchRepository.findOne(testBus.getId());
        assertThat(busEs).isEqualToComparingFieldByField(testBus);
    }

    @Test
    @Transactional
    public void deleteBus() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);
        busSearchRepository.save(bus);
        int databaseSizeBeforeDelete = busRepository.findAll().size();

        // Get the bus
        restBusMockMvc.perform(delete("/api/buses/{id}", bus.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean busExistsInEs = busSearchRepository.exists(bus.getId());
        assertThat(busExistsInEs).isFalse();

        // Validate the database is empty
        List<Bus> buses = busRepository.findAll();
        assertThat(buses).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBus() throws Exception {
        // Initialize the database
        busRepository.saveAndFlush(bus);
        busSearchRepository.save(bus);

        // Search the bus
        restBusMockMvc.perform(get("/api/_search/buses?query=id:" + bus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bus.getId().intValue())))
            .andExpect(jsonPath("$.[*].immatriculation").value(hasItem(DEFAULT_IMMATRICULATION.toString())))
            .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
            .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())))
            .andExpect(jsonPath("$.[*].capacite").value(hasItem(DEFAULT_CAPACITE)))
            .andExpect(jsonPath("$.[*].marque").value(hasItem(DEFAULT_MARQUE.toString())))
            .andExpect(jsonPath("$.[*].dateAcquisition").value(hasItem(DEFAULT_DATE_ACQUISITION.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
}
