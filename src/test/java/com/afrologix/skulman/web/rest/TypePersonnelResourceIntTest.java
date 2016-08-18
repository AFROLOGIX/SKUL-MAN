package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.TypePersonnel;
import com.afrologix.skulman.repository.TypePersonnelRepository;
import com.afrologix.skulman.repository.search.TypePersonnelSearchRepository;

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
 * Test class for the TypePersonnelResource REST controller.
 *
 * @see TypePersonnelResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class TypePersonnelResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_FR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_FR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_EN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_EN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private TypePersonnelRepository typePersonnelRepository;

    @Inject
    private TypePersonnelSearchRepository typePersonnelSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTypePersonnelMockMvc;

    private TypePersonnel typePersonnel;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TypePersonnelResource typePersonnelResource = new TypePersonnelResource();
        ReflectionTestUtils.setField(typePersonnelResource, "typePersonnelSearchRepository", typePersonnelSearchRepository);
        ReflectionTestUtils.setField(typePersonnelResource, "typePersonnelRepository", typePersonnelRepository);
        this.restTypePersonnelMockMvc = MockMvcBuilders.standaloneSetup(typePersonnelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        typePersonnelSearchRepository.deleteAll();
        typePersonnel = new TypePersonnel();
        typePersonnel.setCode(DEFAULT_CODE);
        typePersonnel.setLibelleFr(DEFAULT_LIBELLE_FR);
        typePersonnel.setLibelleEn(DEFAULT_LIBELLE_EN);
    }

    @Test
    @Transactional
    public void createTypePersonnel() throws Exception {
        int databaseSizeBeforeCreate = typePersonnelRepository.findAll().size();

        // Create the TypePersonnel

        restTypePersonnelMockMvc.perform(post("/api/type-personnels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(typePersonnel)))
                .andExpect(status().isCreated());

        // Validate the TypePersonnel in the database
        List<TypePersonnel> typePersonnels = typePersonnelRepository.findAll();
        assertThat(typePersonnels).hasSize(databaseSizeBeforeCreate + 1);
        TypePersonnel testTypePersonnel = typePersonnels.get(typePersonnels.size() - 1);
        assertThat(testTypePersonnel.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTypePersonnel.getLibelleFr()).isEqualTo(DEFAULT_LIBELLE_FR);
        assertThat(testTypePersonnel.getLibelleEn()).isEqualTo(DEFAULT_LIBELLE_EN);

        // Validate the TypePersonnel in ElasticSearch
        TypePersonnel typePersonnelEs = typePersonnelSearchRepository.findOne(testTypePersonnel.getId());
        assertThat(typePersonnelEs).isEqualToComparingFieldByField(testTypePersonnel);
    }

    @Test
    @Transactional
    public void getAllTypePersonnels() throws Exception {
        // Initialize the database
        typePersonnelRepository.saveAndFlush(typePersonnel);

        // Get all the typePersonnels
        restTypePersonnelMockMvc.perform(get("/api/type-personnels?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(typePersonnel.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
                .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())));
    }

    @Test
    @Transactional
    public void getTypePersonnel() throws Exception {
        // Initialize the database
        typePersonnelRepository.saveAndFlush(typePersonnel);

        // Get the typePersonnel
        restTypePersonnelMockMvc.perform(get("/api/type-personnels/{id}", typePersonnel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(typePersonnel.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelleFr").value(DEFAULT_LIBELLE_FR.toString()))
            .andExpect(jsonPath("$.libelleEn").value(DEFAULT_LIBELLE_EN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTypePersonnel() throws Exception {
        // Get the typePersonnel
        restTypePersonnelMockMvc.perform(get("/api/type-personnels/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTypePersonnel() throws Exception {
        // Initialize the database
        typePersonnelRepository.saveAndFlush(typePersonnel);
        typePersonnelSearchRepository.save(typePersonnel);
        int databaseSizeBeforeUpdate = typePersonnelRepository.findAll().size();

        // Update the typePersonnel
        TypePersonnel updatedTypePersonnel = new TypePersonnel();
        updatedTypePersonnel.setId(typePersonnel.getId());
        updatedTypePersonnel.setCode(UPDATED_CODE);
        updatedTypePersonnel.setLibelleFr(UPDATED_LIBELLE_FR);
        updatedTypePersonnel.setLibelleEn(UPDATED_LIBELLE_EN);

        restTypePersonnelMockMvc.perform(put("/api/type-personnels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTypePersonnel)))
                .andExpect(status().isOk());

        // Validate the TypePersonnel in the database
        List<TypePersonnel> typePersonnels = typePersonnelRepository.findAll();
        assertThat(typePersonnels).hasSize(databaseSizeBeforeUpdate);
        TypePersonnel testTypePersonnel = typePersonnels.get(typePersonnels.size() - 1);
        assertThat(testTypePersonnel.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTypePersonnel.getLibelleFr()).isEqualTo(UPDATED_LIBELLE_FR);
        assertThat(testTypePersonnel.getLibelleEn()).isEqualTo(UPDATED_LIBELLE_EN);

        // Validate the TypePersonnel in ElasticSearch
        TypePersonnel typePersonnelEs = typePersonnelSearchRepository.findOne(testTypePersonnel.getId());
        assertThat(typePersonnelEs).isEqualToComparingFieldByField(testTypePersonnel);
    }

    @Test
    @Transactional
    public void deleteTypePersonnel() throws Exception {
        // Initialize the database
        typePersonnelRepository.saveAndFlush(typePersonnel);
        typePersonnelSearchRepository.save(typePersonnel);
        int databaseSizeBeforeDelete = typePersonnelRepository.findAll().size();

        // Get the typePersonnel
        restTypePersonnelMockMvc.perform(delete("/api/type-personnels/{id}", typePersonnel.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean typePersonnelExistsInEs = typePersonnelSearchRepository.exists(typePersonnel.getId());
        assertThat(typePersonnelExistsInEs).isFalse();

        // Validate the database is empty
        List<TypePersonnel> typePersonnels = typePersonnelRepository.findAll();
        assertThat(typePersonnels).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTypePersonnel() throws Exception {
        // Initialize the database
        typePersonnelRepository.saveAndFlush(typePersonnel);
        typePersonnelSearchRepository.save(typePersonnel);

        // Search the typePersonnel
        restTypePersonnelMockMvc.perform(get("/api/_search/type-personnels?query=id:" + typePersonnel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typePersonnel.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
            .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())));
    }
}
