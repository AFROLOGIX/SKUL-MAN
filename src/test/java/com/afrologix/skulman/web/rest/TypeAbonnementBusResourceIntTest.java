package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.TypeAbonnementBus;
import com.afrologix.skulman.repository.TypeAbonnementBusRepository;
import com.afrologix.skulman.repository.search.TypeAbonnementBusSearchRepository;

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
 * Test class for the TypeAbonnementBusResource REST controller.
 *
 * @see TypeAbonnementBusResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class TypeAbonnementBusResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_FR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_FR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_EN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_EN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_ANNEE_SCOLAIRE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ANNEE_SCOLAIRE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Double DEFAULT_MONTANT_ABONNEMENT = 1D;
    private static final Double UPDATED_MONTANT_ABONNEMENT = 2D;

    private static final Integer DEFAULT_DUREE_ABONNEMENT = 7;
    private static final Integer UPDATED_DUREE_ABONNEMENT = 6;

    @Inject
    private TypeAbonnementBusRepository typeAbonnementBusRepository;

    @Inject
    private TypeAbonnementBusSearchRepository typeAbonnementBusSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTypeAbonnementBusMockMvc;

    private TypeAbonnementBus typeAbonnementBus;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TypeAbonnementBusResource typeAbonnementBusResource = new TypeAbonnementBusResource();
        ReflectionTestUtils.setField(typeAbonnementBusResource, "typeAbonnementBusSearchRepository", typeAbonnementBusSearchRepository);
        ReflectionTestUtils.setField(typeAbonnementBusResource, "typeAbonnementBusRepository", typeAbonnementBusRepository);
        this.restTypeAbonnementBusMockMvc = MockMvcBuilders.standaloneSetup(typeAbonnementBusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        typeAbonnementBusSearchRepository.deleteAll();
        typeAbonnementBus = new TypeAbonnementBus();
        typeAbonnementBus.setCode(DEFAULT_CODE);
        typeAbonnementBus.setLibelleFr(DEFAULT_LIBELLE_FR);
        typeAbonnementBus.setLibelleEn(DEFAULT_LIBELLE_EN);
        typeAbonnementBus.setAnneeScolaire(DEFAULT_ANNEE_SCOLAIRE);
        typeAbonnementBus.setMontantAbonnement(DEFAULT_MONTANT_ABONNEMENT);
        typeAbonnementBus.setDureeAbonnement(DEFAULT_DUREE_ABONNEMENT);
    }

    @Test
    @Transactional
    public void createTypeAbonnementBus() throws Exception {
        int databaseSizeBeforeCreate = typeAbonnementBusRepository.findAll().size();

        // Create the TypeAbonnementBus

        restTypeAbonnementBusMockMvc.perform(post("/api/type-abonnement-buses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(typeAbonnementBus)))
                .andExpect(status().isCreated());

        // Validate the TypeAbonnementBus in the database
        List<TypeAbonnementBus> typeAbonnementBuses = typeAbonnementBusRepository.findAll();
        assertThat(typeAbonnementBuses).hasSize(databaseSizeBeforeCreate + 1);
        TypeAbonnementBus testTypeAbonnementBus = typeAbonnementBuses.get(typeAbonnementBuses.size() - 1);
        assertThat(testTypeAbonnementBus.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTypeAbonnementBus.getLibelleFr()).isEqualTo(DEFAULT_LIBELLE_FR);
        assertThat(testTypeAbonnementBus.getLibelleEn()).isEqualTo(DEFAULT_LIBELLE_EN);
        assertThat(testTypeAbonnementBus.getAnneeScolaire()).isEqualTo(DEFAULT_ANNEE_SCOLAIRE);
        assertThat(testTypeAbonnementBus.getMontantAbonnement()).isEqualTo(DEFAULT_MONTANT_ABONNEMENT);
        assertThat(testTypeAbonnementBus.getDureeAbonnement()).isEqualTo(DEFAULT_DUREE_ABONNEMENT);

        // Validate the TypeAbonnementBus in ElasticSearch
        TypeAbonnementBus typeAbonnementBusEs = typeAbonnementBusSearchRepository.findOne(testTypeAbonnementBus.getId());
        assertThat(typeAbonnementBusEs).isEqualToComparingFieldByField(testTypeAbonnementBus);
    }

    @Test
    @Transactional
    public void getAllTypeAbonnementBuses() throws Exception {
        // Initialize the database
        typeAbonnementBusRepository.saveAndFlush(typeAbonnementBus);

        // Get all the typeAbonnementBuses
        restTypeAbonnementBusMockMvc.perform(get("/api/type-abonnement-buses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(typeAbonnementBus.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
                .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())))
                .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())))
                .andExpect(jsonPath("$.[*].montantAbonnement").value(hasItem(DEFAULT_MONTANT_ABONNEMENT.doubleValue())))
                .andExpect(jsonPath("$.[*].dureeAbonnement").value(hasItem(DEFAULT_DUREE_ABONNEMENT)));
    }

    @Test
    @Transactional
    public void getTypeAbonnementBus() throws Exception {
        // Initialize the database
        typeAbonnementBusRepository.saveAndFlush(typeAbonnementBus);

        // Get the typeAbonnementBus
        restTypeAbonnementBusMockMvc.perform(get("/api/type-abonnement-buses/{id}", typeAbonnementBus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(typeAbonnementBus.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelleFr").value(DEFAULT_LIBELLE_FR.toString()))
            .andExpect(jsonPath("$.libelleEn").value(DEFAULT_LIBELLE_EN.toString()))
            .andExpect(jsonPath("$.anneeScolaire").value(DEFAULT_ANNEE_SCOLAIRE.toString()))
            .andExpect(jsonPath("$.montantAbonnement").value(DEFAULT_MONTANT_ABONNEMENT.doubleValue()))
            .andExpect(jsonPath("$.dureeAbonnement").value(DEFAULT_DUREE_ABONNEMENT));
    }

    @Test
    @Transactional
    public void getNonExistingTypeAbonnementBus() throws Exception {
        // Get the typeAbonnementBus
        restTypeAbonnementBusMockMvc.perform(get("/api/type-abonnement-buses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTypeAbonnementBus() throws Exception {
        // Initialize the database
        typeAbonnementBusRepository.saveAndFlush(typeAbonnementBus);
        typeAbonnementBusSearchRepository.save(typeAbonnementBus);
        int databaseSizeBeforeUpdate = typeAbonnementBusRepository.findAll().size();

        // Update the typeAbonnementBus
        TypeAbonnementBus updatedTypeAbonnementBus = new TypeAbonnementBus();
        updatedTypeAbonnementBus.setId(typeAbonnementBus.getId());
        updatedTypeAbonnementBus.setCode(UPDATED_CODE);
        updatedTypeAbonnementBus.setLibelleFr(UPDATED_LIBELLE_FR);
        updatedTypeAbonnementBus.setLibelleEn(UPDATED_LIBELLE_EN);
        updatedTypeAbonnementBus.setAnneeScolaire(UPDATED_ANNEE_SCOLAIRE);
        updatedTypeAbonnementBus.setMontantAbonnement(UPDATED_MONTANT_ABONNEMENT);
        updatedTypeAbonnementBus.setDureeAbonnement(UPDATED_DUREE_ABONNEMENT);

        restTypeAbonnementBusMockMvc.perform(put("/api/type-abonnement-buses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTypeAbonnementBus)))
                .andExpect(status().isOk());

        // Validate the TypeAbonnementBus in the database
        List<TypeAbonnementBus> typeAbonnementBuses = typeAbonnementBusRepository.findAll();
        assertThat(typeAbonnementBuses).hasSize(databaseSizeBeforeUpdate);
        TypeAbonnementBus testTypeAbonnementBus = typeAbonnementBuses.get(typeAbonnementBuses.size() - 1);
        assertThat(testTypeAbonnementBus.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTypeAbonnementBus.getLibelleFr()).isEqualTo(UPDATED_LIBELLE_FR);
        assertThat(testTypeAbonnementBus.getLibelleEn()).isEqualTo(UPDATED_LIBELLE_EN);
        assertThat(testTypeAbonnementBus.getAnneeScolaire()).isEqualTo(UPDATED_ANNEE_SCOLAIRE);
        assertThat(testTypeAbonnementBus.getMontantAbonnement()).isEqualTo(UPDATED_MONTANT_ABONNEMENT);
        assertThat(testTypeAbonnementBus.getDureeAbonnement()).isEqualTo(UPDATED_DUREE_ABONNEMENT);

        // Validate the TypeAbonnementBus in ElasticSearch
        TypeAbonnementBus typeAbonnementBusEs = typeAbonnementBusSearchRepository.findOne(testTypeAbonnementBus.getId());
        assertThat(typeAbonnementBusEs).isEqualToComparingFieldByField(testTypeAbonnementBus);
    }

    @Test
    @Transactional
    public void deleteTypeAbonnementBus() throws Exception {
        // Initialize the database
        typeAbonnementBusRepository.saveAndFlush(typeAbonnementBus);
        typeAbonnementBusSearchRepository.save(typeAbonnementBus);
        int databaseSizeBeforeDelete = typeAbonnementBusRepository.findAll().size();

        // Get the typeAbonnementBus
        restTypeAbonnementBusMockMvc.perform(delete("/api/type-abonnement-buses/{id}", typeAbonnementBus.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean typeAbonnementBusExistsInEs = typeAbonnementBusSearchRepository.exists(typeAbonnementBus.getId());
        assertThat(typeAbonnementBusExistsInEs).isFalse();

        // Validate the database is empty
        List<TypeAbonnementBus> typeAbonnementBuses = typeAbonnementBusRepository.findAll();
        assertThat(typeAbonnementBuses).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTypeAbonnementBus() throws Exception {
        // Initialize the database
        typeAbonnementBusRepository.saveAndFlush(typeAbonnementBus);
        typeAbonnementBusSearchRepository.save(typeAbonnementBus);

        // Search the typeAbonnementBus
        restTypeAbonnementBusMockMvc.perform(get("/api/_search/type-abonnement-buses?query=id:" + typeAbonnementBus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeAbonnementBus.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
            .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())))
            .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE.toString())))
            .andExpect(jsonPath("$.[*].montantAbonnement").value(hasItem(DEFAULT_MONTANT_ABONNEMENT.doubleValue())))
            .andExpect(jsonPath("$.[*].dureeAbonnement").value(hasItem(DEFAULT_DUREE_ABONNEMENT)));
    }
}
