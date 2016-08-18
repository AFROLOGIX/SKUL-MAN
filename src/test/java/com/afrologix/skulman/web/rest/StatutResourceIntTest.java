package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Statut;
import com.afrologix.skulman.repository.StatutRepository;
import com.afrologix.skulman.repository.search.StatutSearchRepository;

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
 * Test class for the StatutResource REST controller.
 *
 * @see StatutResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class StatutResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_FR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_FR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_EN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_EN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private StatutRepository statutRepository;

    @Inject
    private StatutSearchRepository statutSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStatutMockMvc;

    private Statut statut;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StatutResource statutResource = new StatutResource();
        ReflectionTestUtils.setField(statutResource, "statutSearchRepository", statutSearchRepository);
        ReflectionTestUtils.setField(statutResource, "statutRepository", statutRepository);
        this.restStatutMockMvc = MockMvcBuilders.standaloneSetup(statutResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        statutSearchRepository.deleteAll();
        statut = new Statut();
        statut.setCode(DEFAULT_CODE);
        statut.setLibelleFr(DEFAULT_LIBELLE_FR);
        statut.setLibelleEn(DEFAULT_LIBELLE_EN);
    }

    @Test
    @Transactional
    public void createStatut() throws Exception {
        int databaseSizeBeforeCreate = statutRepository.findAll().size();

        // Create the Statut

        restStatutMockMvc.perform(post("/api/statuts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(statut)))
                .andExpect(status().isCreated());

        // Validate the Statut in the database
        List<Statut> statuts = statutRepository.findAll();
        assertThat(statuts).hasSize(databaseSizeBeforeCreate + 1);
        Statut testStatut = statuts.get(statuts.size() - 1);
        assertThat(testStatut.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testStatut.getLibelleFr()).isEqualTo(DEFAULT_LIBELLE_FR);
        assertThat(testStatut.getLibelleEn()).isEqualTo(DEFAULT_LIBELLE_EN);

        // Validate the Statut in ElasticSearch
        Statut statutEs = statutSearchRepository.findOne(testStatut.getId());
        assertThat(statutEs).isEqualToComparingFieldByField(testStatut);
    }

    @Test
    @Transactional
    public void getAllStatuts() throws Exception {
        // Initialize the database
        statutRepository.saveAndFlush(statut);

        // Get all the statuts
        restStatutMockMvc.perform(get("/api/statuts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(statut.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
                .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())));
    }

    @Test
    @Transactional
    public void getStatut() throws Exception {
        // Initialize the database
        statutRepository.saveAndFlush(statut);

        // Get the statut
        restStatutMockMvc.perform(get("/api/statuts/{id}", statut.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(statut.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelleFr").value(DEFAULT_LIBELLE_FR.toString()))
            .andExpect(jsonPath("$.libelleEn").value(DEFAULT_LIBELLE_EN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStatut() throws Exception {
        // Get the statut
        restStatutMockMvc.perform(get("/api/statuts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStatut() throws Exception {
        // Initialize the database
        statutRepository.saveAndFlush(statut);
        statutSearchRepository.save(statut);
        int databaseSizeBeforeUpdate = statutRepository.findAll().size();

        // Update the statut
        Statut updatedStatut = new Statut();
        updatedStatut.setId(statut.getId());
        updatedStatut.setCode(UPDATED_CODE);
        updatedStatut.setLibelleFr(UPDATED_LIBELLE_FR);
        updatedStatut.setLibelleEn(UPDATED_LIBELLE_EN);

        restStatutMockMvc.perform(put("/api/statuts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedStatut)))
                .andExpect(status().isOk());

        // Validate the Statut in the database
        List<Statut> statuts = statutRepository.findAll();
        assertThat(statuts).hasSize(databaseSizeBeforeUpdate);
        Statut testStatut = statuts.get(statuts.size() - 1);
        assertThat(testStatut.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testStatut.getLibelleFr()).isEqualTo(UPDATED_LIBELLE_FR);
        assertThat(testStatut.getLibelleEn()).isEqualTo(UPDATED_LIBELLE_EN);

        // Validate the Statut in ElasticSearch
        Statut statutEs = statutSearchRepository.findOne(testStatut.getId());
        assertThat(statutEs).isEqualToComparingFieldByField(testStatut);
    }

    @Test
    @Transactional
    public void deleteStatut() throws Exception {
        // Initialize the database
        statutRepository.saveAndFlush(statut);
        statutSearchRepository.save(statut);
        int databaseSizeBeforeDelete = statutRepository.findAll().size();

        // Get the statut
        restStatutMockMvc.perform(delete("/api/statuts/{id}", statut.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean statutExistsInEs = statutSearchRepository.exists(statut.getId());
        assertThat(statutExistsInEs).isFalse();

        // Validate the database is empty
        List<Statut> statuts = statutRepository.findAll();
        assertThat(statuts).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchStatut() throws Exception {
        // Initialize the database
        statutRepository.saveAndFlush(statut);
        statutSearchRepository.save(statut);

        // Search the statut
        restStatutMockMvc.perform(get("/api/_search/statuts?query=id:" + statut.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(statut.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
            .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())));
    }
}
