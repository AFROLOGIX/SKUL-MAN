package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Fichier;
import com.afrologix.skulman.repository.FichierRepository;
import com.afrologix.skulman.repository.search.FichierSearchRepository;

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
 * Test class for the FichierResource REST controller.
 *
 * @see FichierResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class FichierResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_TYPE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_FR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_FR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_EN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_EN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_EMPLACEMENT = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_EMPLACEMENT = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
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

    @Inject
    private FichierRepository fichierRepository;

    @Inject
    private FichierSearchRepository fichierSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFichierMockMvc;

    private Fichier fichier;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FichierResource fichierResource = new FichierResource();
        ReflectionTestUtils.setField(fichierResource, "fichierSearchRepository", fichierSearchRepository);
        ReflectionTestUtils.setField(fichierResource, "fichierRepository", fichierRepository);
        this.restFichierMockMvc = MockMvcBuilders.standaloneSetup(fichierResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        fichierSearchRepository.deleteAll();
        fichier = new Fichier();
        fichier.setType(DEFAULT_TYPE);
        fichier.setLibelleFr(DEFAULT_LIBELLE_FR);
        fichier.setLibelleEn(DEFAULT_LIBELLE_EN);
        fichier.setEmplacement(DEFAULT_EMPLACEMENT);
        fichier.setCreateBy(DEFAULT_CREATE_BY);
        fichier.setUpdateBy(DEFAULT_UPDATE_BY);
        fichier.setCreateAt(DEFAULT_CREATE_AT);
        fichier.setUpdateAt(DEFAULT_UPDATE_AT);
    }

    @Test
    @Transactional
    public void createFichier() throws Exception {
        int databaseSizeBeforeCreate = fichierRepository.findAll().size();

        // Create the Fichier

        restFichierMockMvc.perform(post("/api/fichiers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fichier)))
                .andExpect(status().isCreated());

        // Validate the Fichier in the database
        List<Fichier> fichiers = fichierRepository.findAll();
        assertThat(fichiers).hasSize(databaseSizeBeforeCreate + 1);
        Fichier testFichier = fichiers.get(fichiers.size() - 1);
        assertThat(testFichier.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testFichier.getLibelleFr()).isEqualTo(DEFAULT_LIBELLE_FR);
        assertThat(testFichier.getLibelleEn()).isEqualTo(DEFAULT_LIBELLE_EN);
        assertThat(testFichier.getEmplacement()).isEqualTo(DEFAULT_EMPLACEMENT);
        assertThat(testFichier.getCreateBy()).isEqualTo(DEFAULT_CREATE_BY);
        assertThat(testFichier.getUpdateBy()).isEqualTo(DEFAULT_UPDATE_BY);
        assertThat(testFichier.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testFichier.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);

        // Validate the Fichier in ElasticSearch
        Fichier fichierEs = fichierSearchRepository.findOne(testFichier.getId());
        assertThat(fichierEs).isEqualToComparingFieldByField(testFichier);
    }

    @Test
    @Transactional
    public void getAllFichiers() throws Exception {
        // Initialize the database
        fichierRepository.saveAndFlush(fichier);

        // Get all the fichiers
        restFichierMockMvc.perform(get("/api/fichiers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(fichier.getId().intValue())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
                .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())))
                .andExpect(jsonPath("$.[*].emplacement").value(hasItem(DEFAULT_EMPLACEMENT.toString())))
                .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY.toString())))
                .andExpect(jsonPath("$.[*].updateBy").value(hasItem(DEFAULT_UPDATE_BY.toString())))
                .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT_STR)))
                .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT_STR)));
    }

    @Test
    @Transactional
    public void getFichier() throws Exception {
        // Initialize the database
        fichierRepository.saveAndFlush(fichier);

        // Get the fichier
        restFichierMockMvc.perform(get("/api/fichiers/{id}", fichier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(fichier.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.libelleFr").value(DEFAULT_LIBELLE_FR.toString()))
            .andExpect(jsonPath("$.libelleEn").value(DEFAULT_LIBELLE_EN.toString()))
            .andExpect(jsonPath("$.emplacement").value(DEFAULT_EMPLACEMENT.toString()))
            .andExpect(jsonPath("$.createBy").value(DEFAULT_CREATE_BY.toString()))
            .andExpect(jsonPath("$.updateBy").value(DEFAULT_UPDATE_BY.toString()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT_STR))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT_STR));
    }

    @Test
    @Transactional
    public void getNonExistingFichier() throws Exception {
        // Get the fichier
        restFichierMockMvc.perform(get("/api/fichiers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFichier() throws Exception {
        // Initialize the database
        fichierRepository.saveAndFlush(fichier);
        fichierSearchRepository.save(fichier);
        int databaseSizeBeforeUpdate = fichierRepository.findAll().size();

        // Update the fichier
        Fichier updatedFichier = new Fichier();
        updatedFichier.setId(fichier.getId());
        updatedFichier.setType(UPDATED_TYPE);
        updatedFichier.setLibelleFr(UPDATED_LIBELLE_FR);
        updatedFichier.setLibelleEn(UPDATED_LIBELLE_EN);
        updatedFichier.setEmplacement(UPDATED_EMPLACEMENT);
        updatedFichier.setCreateBy(UPDATED_CREATE_BY);
        updatedFichier.setUpdateBy(UPDATED_UPDATE_BY);
        updatedFichier.setCreateAt(UPDATED_CREATE_AT);
        updatedFichier.setUpdateAt(UPDATED_UPDATE_AT);

        restFichierMockMvc.perform(put("/api/fichiers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFichier)))
                .andExpect(status().isOk());

        // Validate the Fichier in the database
        List<Fichier> fichiers = fichierRepository.findAll();
        assertThat(fichiers).hasSize(databaseSizeBeforeUpdate);
        Fichier testFichier = fichiers.get(fichiers.size() - 1);
        assertThat(testFichier.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testFichier.getLibelleFr()).isEqualTo(UPDATED_LIBELLE_FR);
        assertThat(testFichier.getLibelleEn()).isEqualTo(UPDATED_LIBELLE_EN);
        assertThat(testFichier.getEmplacement()).isEqualTo(UPDATED_EMPLACEMENT);
        assertThat(testFichier.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testFichier.getUpdateBy()).isEqualTo(UPDATED_UPDATE_BY);
        assertThat(testFichier.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testFichier.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);

        // Validate the Fichier in ElasticSearch
        Fichier fichierEs = fichierSearchRepository.findOne(testFichier.getId());
        assertThat(fichierEs).isEqualToComparingFieldByField(testFichier);
    }

    @Test
    @Transactional
    public void deleteFichier() throws Exception {
        // Initialize the database
        fichierRepository.saveAndFlush(fichier);
        fichierSearchRepository.save(fichier);
        int databaseSizeBeforeDelete = fichierRepository.findAll().size();

        // Get the fichier
        restFichierMockMvc.perform(delete("/api/fichiers/{id}", fichier.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean fichierExistsInEs = fichierSearchRepository.exists(fichier.getId());
        assertThat(fichierExistsInEs).isFalse();

        // Validate the database is empty
        List<Fichier> fichiers = fichierRepository.findAll();
        assertThat(fichiers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFichier() throws Exception {
        // Initialize the database
        fichierRepository.saveAndFlush(fichier);
        fichierSearchRepository.save(fichier);

        // Search the fichier
        restFichierMockMvc.perform(get("/api/_search/fichiers?query=id:" + fichier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fichier.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
            .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())))
            .andExpect(jsonPath("$.[*].emplacement").value(hasItem(DEFAULT_EMPLACEMENT.toString())))
            .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY.toString())))
            .andExpect(jsonPath("$.[*].updateBy").value(hasItem(DEFAULT_UPDATE_BY.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT_STR)))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT_STR)));
    }
}
