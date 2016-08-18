package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.TypeChambre;
import com.afrologix.skulman.repository.TypeChambreRepository;
import com.afrologix.skulman.repository.search.TypeChambreSearchRepository;

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
 * Test class for the TypeChambreResource REST controller.
 *
 * @see TypeChambreResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class TypeChambreResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_FR = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_FR = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIBELLE_EN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE_EN = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Double DEFAULT_MONTANT = 1D;
    private static final Double UPDATED_MONTANT = 2D;

    @Inject
    private TypeChambreRepository typeChambreRepository;

    @Inject
    private TypeChambreSearchRepository typeChambreSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTypeChambreMockMvc;

    private TypeChambre typeChambre;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TypeChambreResource typeChambreResource = new TypeChambreResource();
        ReflectionTestUtils.setField(typeChambreResource, "typeChambreSearchRepository", typeChambreSearchRepository);
        ReflectionTestUtils.setField(typeChambreResource, "typeChambreRepository", typeChambreRepository);
        this.restTypeChambreMockMvc = MockMvcBuilders.standaloneSetup(typeChambreResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        typeChambreSearchRepository.deleteAll();
        typeChambre = new TypeChambre();
        typeChambre.setCode(DEFAULT_CODE);
        typeChambre.setLibelleFr(DEFAULT_LIBELLE_FR);
        typeChambre.setLibelleEn(DEFAULT_LIBELLE_EN);
        typeChambre.setMontant(DEFAULT_MONTANT);
    }

    @Test
    @Transactional
    public void createTypeChambre() throws Exception {
        int databaseSizeBeforeCreate = typeChambreRepository.findAll().size();

        // Create the TypeChambre

        restTypeChambreMockMvc.perform(post("/api/type-chambres")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(typeChambre)))
                .andExpect(status().isCreated());

        // Validate the TypeChambre in the database
        List<TypeChambre> typeChambres = typeChambreRepository.findAll();
        assertThat(typeChambres).hasSize(databaseSizeBeforeCreate + 1);
        TypeChambre testTypeChambre = typeChambres.get(typeChambres.size() - 1);
        assertThat(testTypeChambre.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTypeChambre.getLibelleFr()).isEqualTo(DEFAULT_LIBELLE_FR);
        assertThat(testTypeChambre.getLibelleEn()).isEqualTo(DEFAULT_LIBELLE_EN);
        assertThat(testTypeChambre.getMontant()).isEqualTo(DEFAULT_MONTANT);

        // Validate the TypeChambre in ElasticSearch
        TypeChambre typeChambreEs = typeChambreSearchRepository.findOne(testTypeChambre.getId());
        assertThat(typeChambreEs).isEqualToComparingFieldByField(testTypeChambre);
    }

    @Test
    @Transactional
    public void getAllTypeChambres() throws Exception {
        // Initialize the database
        typeChambreRepository.saveAndFlush(typeChambre);

        // Get all the typeChambres
        restTypeChambreMockMvc.perform(get("/api/type-chambres?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(typeChambre.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
                .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())))
                .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())));
    }

    @Test
    @Transactional
    public void getTypeChambre() throws Exception {
        // Initialize the database
        typeChambreRepository.saveAndFlush(typeChambre);

        // Get the typeChambre
        restTypeChambreMockMvc.perform(get("/api/type-chambres/{id}", typeChambre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(typeChambre.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.libelleFr").value(DEFAULT_LIBELLE_FR.toString()))
            .andExpect(jsonPath("$.libelleEn").value(DEFAULT_LIBELLE_EN.toString()))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTypeChambre() throws Exception {
        // Get the typeChambre
        restTypeChambreMockMvc.perform(get("/api/type-chambres/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTypeChambre() throws Exception {
        // Initialize the database
        typeChambreRepository.saveAndFlush(typeChambre);
        typeChambreSearchRepository.save(typeChambre);
        int databaseSizeBeforeUpdate = typeChambreRepository.findAll().size();

        // Update the typeChambre
        TypeChambre updatedTypeChambre = new TypeChambre();
        updatedTypeChambre.setId(typeChambre.getId());
        updatedTypeChambre.setCode(UPDATED_CODE);
        updatedTypeChambre.setLibelleFr(UPDATED_LIBELLE_FR);
        updatedTypeChambre.setLibelleEn(UPDATED_LIBELLE_EN);
        updatedTypeChambre.setMontant(UPDATED_MONTANT);

        restTypeChambreMockMvc.perform(put("/api/type-chambres")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTypeChambre)))
                .andExpect(status().isOk());

        // Validate the TypeChambre in the database
        List<TypeChambre> typeChambres = typeChambreRepository.findAll();
        assertThat(typeChambres).hasSize(databaseSizeBeforeUpdate);
        TypeChambre testTypeChambre = typeChambres.get(typeChambres.size() - 1);
        assertThat(testTypeChambre.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTypeChambre.getLibelleFr()).isEqualTo(UPDATED_LIBELLE_FR);
        assertThat(testTypeChambre.getLibelleEn()).isEqualTo(UPDATED_LIBELLE_EN);
        assertThat(testTypeChambre.getMontant()).isEqualTo(UPDATED_MONTANT);

        // Validate the TypeChambre in ElasticSearch
        TypeChambre typeChambreEs = typeChambreSearchRepository.findOne(testTypeChambre.getId());
        assertThat(typeChambreEs).isEqualToComparingFieldByField(testTypeChambre);
    }

    @Test
    @Transactional
    public void deleteTypeChambre() throws Exception {
        // Initialize the database
        typeChambreRepository.saveAndFlush(typeChambre);
        typeChambreSearchRepository.save(typeChambre);
        int databaseSizeBeforeDelete = typeChambreRepository.findAll().size();

        // Get the typeChambre
        restTypeChambreMockMvc.perform(delete("/api/type-chambres/{id}", typeChambre.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean typeChambreExistsInEs = typeChambreSearchRepository.exists(typeChambre.getId());
        assertThat(typeChambreExistsInEs).isFalse();

        // Validate the database is empty
        List<TypeChambre> typeChambres = typeChambreRepository.findAll();
        assertThat(typeChambres).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTypeChambre() throws Exception {
        // Initialize the database
        typeChambreRepository.saveAndFlush(typeChambre);
        typeChambreSearchRepository.save(typeChambre);

        // Search the typeChambre
        restTypeChambreMockMvc.perform(get("/api/_search/type-chambres?query=id:" + typeChambre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeChambre.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].libelleFr").value(hasItem(DEFAULT_LIBELLE_FR.toString())))
            .andExpect(jsonPath("$.[*].libelleEn").value(hasItem(DEFAULT_LIBELLE_EN.toString())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())));
    }
}
