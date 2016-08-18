package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Matiere;
import com.afrologix.skulman.repository.MatiereRepository;
import com.afrologix.skulman.repository.search.MatiereSearchRepository;

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
 * Test class for the MatiereResource REST controller.
 *
 * @see MatiereResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class MatiereResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_FR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_FR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_EN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_EN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private MatiereRepository matiereRepository;

    @Inject
    private MatiereSearchRepository matiereSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMatiereMockMvc;

    private Matiere matiere;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MatiereResource matiereResource = new MatiereResource();
        ReflectionTestUtils.setField(matiereResource, "matiereSearchRepository", matiereSearchRepository);
        ReflectionTestUtils.setField(matiereResource, "matiereRepository", matiereRepository);
        this.restMatiereMockMvc = MockMvcBuilders.standaloneSetup(matiereResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        matiereSearchRepository.deleteAll();
        matiere = new Matiere();
        matiere.setCode(DEFAULT_CODE);
        matiere.setLibelleFr(DEFAULT_LIBELLE_FR);
        matiere.setLibelleEn(DEFAULT_LIBELLE_EN);
    }

    @Test
    @Transactional
    public void createMatiere() throws Exception {
        int databaseSizeBeforeCreate = matiereRepository.findAll().size();

        // Create the Matiere

        restMatiereMockMvc.perform(post("/api/matieres")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(matiere)))
                .andExpect(status().isCreated());

        // Validate the Matiere in the database
        List<Matiere> matieres = matiereRepository.findAll();
        assertThat(matieres).hasSize(databaseSizeBeforeCreate + 1);
        Matiere testMatiere = matieres.get(matieres.size() - 1);
        assertThat(testMatiere.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testMatiere.getLibelleFr()).isEqualTo(DEFAULT_LIBELLE_FR);
        assertThat(testMatiere.getLibelleEn()).isEqualTo(DEFAULT_LIBELLE_EN);

        // Validate the Matiere in ElasticSearch
        Matiere matiereEs = matiereSearchRepository.findOne(testMatiere.getId());
        assertThat(matiereEs).isEqualToComparingFieldByField(testMatiere);
    }

    @Test
    @Transactional
    public void getAllMatieres() throws Exception {
        // Initialize the database
        matiereRepository.saveAndFlush(matiere);

        // Get all the matieres
        restMatiereMockMvc.perform(get("/api/matieres?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(matiere.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
                .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())));
    }

    @Test
    @Transactional
    public void getMatiere() throws Exception {
        // Initialize the database
        matiereRepository.saveAndFlush(matiere);

        // Get the matiere
        restMatiereMockMvc.perform(get("/api/matieres/{id}", matiere.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(matiere.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelleFr").value(DEFAULT_LIBELLE_FR.toString()))
            .andExpect(jsonPath("$.libelleEn").value(DEFAULT_LIBELLE_EN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMatiere() throws Exception {
        // Get the matiere
        restMatiereMockMvc.perform(get("/api/matieres/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMatiere() throws Exception {
        // Initialize the database
        matiereRepository.saveAndFlush(matiere);
        matiereSearchRepository.save(matiere);
        int databaseSizeBeforeUpdate = matiereRepository.findAll().size();

        // Update the matiere
        Matiere updatedMatiere = new Matiere();
        updatedMatiere.setId(matiere.getId());
        updatedMatiere.setCode(UPDATED_CODE);
        updatedMatiere.setLibelleFr(UPDATED_LIBELLE_FR);
        updatedMatiere.setLibelleEn(UPDATED_LIBELLE_EN);

        restMatiereMockMvc.perform(put("/api/matieres")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMatiere)))
                .andExpect(status().isOk());

        // Validate the Matiere in the database
        List<Matiere> matieres = matiereRepository.findAll();
        assertThat(matieres).hasSize(databaseSizeBeforeUpdate);
        Matiere testMatiere = matieres.get(matieres.size() - 1);
        assertThat(testMatiere.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testMatiere.getLibelleFr()).isEqualTo(UPDATED_LIBELLE_FR);
        assertThat(testMatiere.getLibelleEn()).isEqualTo(UPDATED_LIBELLE_EN);

        // Validate the Matiere in ElasticSearch
        Matiere matiereEs = matiereSearchRepository.findOne(testMatiere.getId());
        assertThat(matiereEs).isEqualToComparingFieldByField(testMatiere);
    }

    @Test
    @Transactional
    public void deleteMatiere() throws Exception {
        // Initialize the database
        matiereRepository.saveAndFlush(matiere);
        matiereSearchRepository.save(matiere);
        int databaseSizeBeforeDelete = matiereRepository.findAll().size();

        // Get the matiere
        restMatiereMockMvc.perform(delete("/api/matieres/{id}", matiere.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean matiereExistsInEs = matiereSearchRepository.exists(matiere.getId());
        assertThat(matiereExistsInEs).isFalse();

        // Validate the database is empty
        List<Matiere> matieres = matiereRepository.findAll();
        assertThat(matieres).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMatiere() throws Exception {
        // Initialize the database
        matiereRepository.saveAndFlush(matiere);
        matiereSearchRepository.save(matiere);

        // Search the matiere
        restMatiereMockMvc.perform(get("/api/_search/matieres?query=id:" + matiere.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(matiere.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
            .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())));
    }
}
