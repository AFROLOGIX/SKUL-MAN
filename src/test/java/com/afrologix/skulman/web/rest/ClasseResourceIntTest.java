package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Classe;
import com.afrologix.skulman.repository.ClasseRepository;
import com.afrologix.skulman.repository.search.ClasseSearchRepository;

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
 * Test class for the ClasseResource REST controller.
 *
 * @see ClasseResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class ClasseResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_FR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_FR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_EN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_EN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private ClasseRepository classeRepository;

    @Inject
    private ClasseSearchRepository classeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restClasseMockMvc;

    private Classe classe;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClasseResource classeResource = new ClasseResource();
        ReflectionTestUtils.setField(classeResource, "classeSearchRepository", classeSearchRepository);
        ReflectionTestUtils.setField(classeResource, "classeRepository", classeRepository);
        this.restClasseMockMvc = MockMvcBuilders.standaloneSetup(classeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        classeSearchRepository.deleteAll();
        classe = new Classe();
        classe.setCode(DEFAULT_CODE);
        classe.setLibelleFr(DEFAULT_LIBELLE_FR);
        classe.setLibelleEn(DEFAULT_LIBELLE_EN);
    }

    @Test
    @Transactional
    public void createClasse() throws Exception {
        int databaseSizeBeforeCreate = classeRepository.findAll().size();

        // Create the Classe

        restClasseMockMvc.perform(post("/api/classes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(classe)))
                .andExpect(status().isCreated());

        // Validate the Classe in the database
        List<Classe> classes = classeRepository.findAll();
        assertThat(classes).hasSize(databaseSizeBeforeCreate + 1);
        Classe testClasse = classes.get(classes.size() - 1);
        assertThat(testClasse.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testClasse.getLibelleFr()).isEqualTo(DEFAULT_LIBELLE_FR);
        assertThat(testClasse.getLibelleEn()).isEqualTo(DEFAULT_LIBELLE_EN);

        // Validate the Classe in ElasticSearch
        Classe classeEs = classeSearchRepository.findOne(testClasse.getId());
        assertThat(classeEs).isEqualToComparingFieldByField(testClasse);
    }

    @Test
    @Transactional
    public void getAllClasses() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        // Get all the classes
        restClasseMockMvc.perform(get("/api/classes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(classe.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
                .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())));
    }

    @Test
    @Transactional
    public void getClasse() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        // Get the classe
        restClasseMockMvc.perform(get("/api/classes/{id}", classe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(classe.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelleFr").value(DEFAULT_LIBELLE_FR.toString()))
            .andExpect(jsonPath("$.libelleEn").value(DEFAULT_LIBELLE_EN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingClasse() throws Exception {
        // Get the classe
        restClasseMockMvc.perform(get("/api/classes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClasse() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);
        classeSearchRepository.save(classe);
        int databaseSizeBeforeUpdate = classeRepository.findAll().size();

        // Update the classe
        Classe updatedClasse = new Classe();
        updatedClasse.setId(classe.getId());
        updatedClasse.setCode(UPDATED_CODE);
        updatedClasse.setLibelleFr(UPDATED_LIBELLE_FR);
        updatedClasse.setLibelleEn(UPDATED_LIBELLE_EN);

        restClasseMockMvc.perform(put("/api/classes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedClasse)))
                .andExpect(status().isOk());

        // Validate the Classe in the database
        List<Classe> classes = classeRepository.findAll();
        assertThat(classes).hasSize(databaseSizeBeforeUpdate);
        Classe testClasse = classes.get(classes.size() - 1);
        assertThat(testClasse.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testClasse.getLibelleFr()).isEqualTo(UPDATED_LIBELLE_FR);
        assertThat(testClasse.getLibelleEn()).isEqualTo(UPDATED_LIBELLE_EN);

        // Validate the Classe in ElasticSearch
        Classe classeEs = classeSearchRepository.findOne(testClasse.getId());
        assertThat(classeEs).isEqualToComparingFieldByField(testClasse);
    }

    @Test
    @Transactional
    public void deleteClasse() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);
        classeSearchRepository.save(classe);
        int databaseSizeBeforeDelete = classeRepository.findAll().size();

        // Get the classe
        restClasseMockMvc.perform(delete("/api/classes/{id}", classe.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean classeExistsInEs = classeSearchRepository.exists(classe.getId());
        assertThat(classeExistsInEs).isFalse();

        // Validate the database is empty
        List<Classe> classes = classeRepository.findAll();
        assertThat(classes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchClasse() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);
        classeSearchRepository.save(classe);

        // Search the classe
        restClasseMockMvc.perform(get("/api/_search/classes?query=id:" + classe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classe.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
            .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())));
    }
}
