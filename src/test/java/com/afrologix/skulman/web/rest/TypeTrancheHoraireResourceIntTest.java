package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.TypeTrancheHoraire;
import com.afrologix.skulman.repository.TypeTrancheHoraireRepository;
import com.afrologix.skulman.repository.search.TypeTrancheHoraireSearchRepository;

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
 * Test class for the TypeTrancheHoraireResource REST controller.
 *
 * @see TypeTrancheHoraireResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class TypeTrancheHoraireResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_CODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_FR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_FR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_EN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_EN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_HEURE_DEB = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_HEURE_DEB = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_HEURE_DEB_STR = dateTimeFormatter.format(DEFAULT_HEURE_DEB);

    private static final ZonedDateTime DEFAULT_HEURE_FIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_HEURE_FIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_HEURE_FIN_STR = dateTimeFormatter.format(DEFAULT_HEURE_FIN);

    @Inject
    private TypeTrancheHoraireRepository typeTrancheHoraireRepository;

    @Inject
    private TypeTrancheHoraireSearchRepository typeTrancheHoraireSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTypeTrancheHoraireMockMvc;

    private TypeTrancheHoraire typeTrancheHoraire;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TypeTrancheHoraireResource typeTrancheHoraireResource = new TypeTrancheHoraireResource();
        ReflectionTestUtils.setField(typeTrancheHoraireResource, "typeTrancheHoraireSearchRepository", typeTrancheHoraireSearchRepository);
        ReflectionTestUtils.setField(typeTrancheHoraireResource, "typeTrancheHoraireRepository", typeTrancheHoraireRepository);
        this.restTypeTrancheHoraireMockMvc = MockMvcBuilders.standaloneSetup(typeTrancheHoraireResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        typeTrancheHoraireSearchRepository.deleteAll();
        typeTrancheHoraire = new TypeTrancheHoraire();
        typeTrancheHoraire.setCode(DEFAULT_CODE);
        typeTrancheHoraire.setLibelleFr(DEFAULT_LIBELLE_FR);
        typeTrancheHoraire.setLibelleEn(DEFAULT_LIBELLE_EN);
        typeTrancheHoraire.setHeureDeb(DEFAULT_HEURE_DEB);
        typeTrancheHoraire.setHeureFin(DEFAULT_HEURE_FIN);
    }

    @Test
    @Transactional
    public void createTypeTrancheHoraire() throws Exception {
        int databaseSizeBeforeCreate = typeTrancheHoraireRepository.findAll().size();

        // Create the TypeTrancheHoraire

        restTypeTrancheHoraireMockMvc.perform(post("/api/type-tranche-horaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(typeTrancheHoraire)))
                .andExpect(status().isCreated());

        // Validate the TypeTrancheHoraire in the database
        List<TypeTrancheHoraire> typeTrancheHoraires = typeTrancheHoraireRepository.findAll();
        assertThat(typeTrancheHoraires).hasSize(databaseSizeBeforeCreate + 1);
        TypeTrancheHoraire testTypeTrancheHoraire = typeTrancheHoraires.get(typeTrancheHoraires.size() - 1);
        assertThat(testTypeTrancheHoraire.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTypeTrancheHoraire.getLibelleFr()).isEqualTo(DEFAULT_LIBELLE_FR);
        assertThat(testTypeTrancheHoraire.getLibelleEn()).isEqualTo(DEFAULT_LIBELLE_EN);
        assertThat(testTypeTrancheHoraire.getHeureDeb()).isEqualTo(DEFAULT_HEURE_DEB);
        assertThat(testTypeTrancheHoraire.getHeureFin()).isEqualTo(DEFAULT_HEURE_FIN);

        // Validate the TypeTrancheHoraire in ElasticSearch
        TypeTrancheHoraire typeTrancheHoraireEs = typeTrancheHoraireSearchRepository.findOne(testTypeTrancheHoraire.getId());
        assertThat(typeTrancheHoraireEs).isEqualToComparingFieldByField(testTypeTrancheHoraire);
    }

    @Test
    @Transactional
    public void getAllTypeTrancheHoraires() throws Exception {
        // Initialize the database
        typeTrancheHoraireRepository.saveAndFlush(typeTrancheHoraire);

        // Get all the typeTrancheHoraires
        restTypeTrancheHoraireMockMvc.perform(get("/api/type-tranche-horaires?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(typeTrancheHoraire.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
                .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())))
                .andExpect(jsonPath("$.[*].heureDeb").value(hasItem(DEFAULT_HEURE_DEB_STR)))
                .andExpect(jsonPath("$.[*].heureFin").value(hasItem(DEFAULT_HEURE_FIN_STR)));
    }

    @Test
    @Transactional
    public void getTypeTrancheHoraire() throws Exception {
        // Initialize the database
        typeTrancheHoraireRepository.saveAndFlush(typeTrancheHoraire);

        // Get the typeTrancheHoraire
        restTypeTrancheHoraireMockMvc.perform(get("/api/type-tranche-horaires/{id}", typeTrancheHoraire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(typeTrancheHoraire.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelleFr").value(DEFAULT_LIBELLE_FR.toString()))
            .andExpect(jsonPath("$.libelleEn").value(DEFAULT_LIBELLE_EN.toString()))
            .andExpect(jsonPath("$.heureDeb").value(DEFAULT_HEURE_DEB_STR))
            .andExpect(jsonPath("$.heureFin").value(DEFAULT_HEURE_FIN_STR));
    }

    @Test
    @Transactional
    public void getNonExistingTypeTrancheHoraire() throws Exception {
        // Get the typeTrancheHoraire
        restTypeTrancheHoraireMockMvc.perform(get("/api/type-tranche-horaires/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTypeTrancheHoraire() throws Exception {
        // Initialize the database
        typeTrancheHoraireRepository.saveAndFlush(typeTrancheHoraire);
        typeTrancheHoraireSearchRepository.save(typeTrancheHoraire);
        int databaseSizeBeforeUpdate = typeTrancheHoraireRepository.findAll().size();

        // Update the typeTrancheHoraire
        TypeTrancheHoraire updatedTypeTrancheHoraire = new TypeTrancheHoraire();
        updatedTypeTrancheHoraire.setId(typeTrancheHoraire.getId());
        updatedTypeTrancheHoraire.setCode(UPDATED_CODE);
        updatedTypeTrancheHoraire.setLibelleFr(UPDATED_LIBELLE_FR);
        updatedTypeTrancheHoraire.setLibelleEn(UPDATED_LIBELLE_EN);
        updatedTypeTrancheHoraire.setHeureDeb(UPDATED_HEURE_DEB);
        updatedTypeTrancheHoraire.setHeureFin(UPDATED_HEURE_FIN);

        restTypeTrancheHoraireMockMvc.perform(put("/api/type-tranche-horaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTypeTrancheHoraire)))
                .andExpect(status().isOk());

        // Validate the TypeTrancheHoraire in the database
        List<TypeTrancheHoraire> typeTrancheHoraires = typeTrancheHoraireRepository.findAll();
        assertThat(typeTrancheHoraires).hasSize(databaseSizeBeforeUpdate);
        TypeTrancheHoraire testTypeTrancheHoraire = typeTrancheHoraires.get(typeTrancheHoraires.size() - 1);
        assertThat(testTypeTrancheHoraire.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTypeTrancheHoraire.getLibelleFr()).isEqualTo(UPDATED_LIBELLE_FR);
        assertThat(testTypeTrancheHoraire.getLibelleEn()).isEqualTo(UPDATED_LIBELLE_EN);
        assertThat(testTypeTrancheHoraire.getHeureDeb()).isEqualTo(UPDATED_HEURE_DEB);
        assertThat(testTypeTrancheHoraire.getHeureFin()).isEqualTo(UPDATED_HEURE_FIN);

        // Validate the TypeTrancheHoraire in ElasticSearch
        TypeTrancheHoraire typeTrancheHoraireEs = typeTrancheHoraireSearchRepository.findOne(testTypeTrancheHoraire.getId());
        assertThat(typeTrancheHoraireEs).isEqualToComparingFieldByField(testTypeTrancheHoraire);
    }

    @Test
    @Transactional
    public void deleteTypeTrancheHoraire() throws Exception {
        // Initialize the database
        typeTrancheHoraireRepository.saveAndFlush(typeTrancheHoraire);
        typeTrancheHoraireSearchRepository.save(typeTrancheHoraire);
        int databaseSizeBeforeDelete = typeTrancheHoraireRepository.findAll().size();

        // Get the typeTrancheHoraire
        restTypeTrancheHoraireMockMvc.perform(delete("/api/type-tranche-horaires/{id}", typeTrancheHoraire.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean typeTrancheHoraireExistsInEs = typeTrancheHoraireSearchRepository.exists(typeTrancheHoraire.getId());
        assertThat(typeTrancheHoraireExistsInEs).isFalse();

        // Validate the database is empty
        List<TypeTrancheHoraire> typeTrancheHoraires = typeTrancheHoraireRepository.findAll();
        assertThat(typeTrancheHoraires).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTypeTrancheHoraire() throws Exception {
        // Initialize the database
        typeTrancheHoraireRepository.saveAndFlush(typeTrancheHoraire);
        typeTrancheHoraireSearchRepository.save(typeTrancheHoraire);

        // Search the typeTrancheHoraire
        restTypeTrancheHoraireMockMvc.perform(get("/api/_search/type-tranche-horaires?query=id:" + typeTrancheHoraire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeTrancheHoraire.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
            .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())))
            .andExpect(jsonPath("$.[*].heureDeb").value(hasItem(DEFAULT_HEURE_DEB_STR)))
            .andExpect(jsonPath("$.[*].heureFin").value(hasItem(DEFAULT_HEURE_FIN_STR)));
    }
}
