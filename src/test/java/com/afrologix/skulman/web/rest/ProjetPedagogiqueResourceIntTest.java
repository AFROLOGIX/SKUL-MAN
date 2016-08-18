package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.ProjetPedagogique;
import com.afrologix.skulman.repository.ProjetPedagogiqueRepository;
import com.afrologix.skulman.repository.search.ProjetPedagogiqueSearchRepository;

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
 * Test class for the ProjetPedagogiqueResource REST controller.
 *
 * @see ProjetPedagogiqueResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class ProjetPedagogiqueResourceIntTest {

    private static final String DEFAULT_ELEMENT_PROG = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ELEMENT_PROG = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Double DEFAULT_VOLUME_HORAIRE = 1D;
    private static final Double UPDATED_VOLUME_HORAIRE = 2D;

    private static final LocalDate DEFAULT_DATE_DEB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEB = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    @Inject
    private ProjetPedagogiqueRepository projetPedagogiqueRepository;

    @Inject
    private ProjetPedagogiqueSearchRepository projetPedagogiqueSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restProjetPedagogiqueMockMvc;

    private ProjetPedagogique projetPedagogique;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProjetPedagogiqueResource projetPedagogiqueResource = new ProjetPedagogiqueResource();
        ReflectionTestUtils.setField(projetPedagogiqueResource, "projetPedagogiqueSearchRepository", projetPedagogiqueSearchRepository);
        ReflectionTestUtils.setField(projetPedagogiqueResource, "projetPedagogiqueRepository", projetPedagogiqueRepository);
        this.restProjetPedagogiqueMockMvc = MockMvcBuilders.standaloneSetup(projetPedagogiqueResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        projetPedagogiqueSearchRepository.deleteAll();
        projetPedagogique = new ProjetPedagogique();
        projetPedagogique.setElementProg(DEFAULT_ELEMENT_PROG);
        projetPedagogique.setVolumeHoraire(DEFAULT_VOLUME_HORAIRE);
        projetPedagogique.setDateDeb(DEFAULT_DATE_DEB);
        projetPedagogique.setDateFin(DEFAULT_DATE_FIN);
        projetPedagogique.setStatus(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createProjetPedagogique() throws Exception {
        int databaseSizeBeforeCreate = projetPedagogiqueRepository.findAll().size();

        // Create the ProjetPedagogique

        restProjetPedagogiqueMockMvc.perform(post("/api/projet-pedagogiques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projetPedagogique)))
                .andExpect(status().isCreated());

        // Validate the ProjetPedagogique in the database
        List<ProjetPedagogique> projetPedagogiques = projetPedagogiqueRepository.findAll();
        assertThat(projetPedagogiques).hasSize(databaseSizeBeforeCreate + 1);
        ProjetPedagogique testProjetPedagogique = projetPedagogiques.get(projetPedagogiques.size() - 1);
        assertThat(testProjetPedagogique.getElementProg()).isEqualTo(DEFAULT_ELEMENT_PROG);
        assertThat(testProjetPedagogique.getVolumeHoraire()).isEqualTo(DEFAULT_VOLUME_HORAIRE);
        assertThat(testProjetPedagogique.getDateDeb()).isEqualTo(DEFAULT_DATE_DEB);
        assertThat(testProjetPedagogique.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testProjetPedagogique.isStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the ProjetPedagogique in ElasticSearch
        ProjetPedagogique projetPedagogiqueEs = projetPedagogiqueSearchRepository.findOne(testProjetPedagogique.getId());
        assertThat(projetPedagogiqueEs).isEqualToComparingFieldByField(testProjetPedagogique);
    }

    @Test
    @Transactional
    public void getAllProjetPedagogiques() throws Exception {
        // Initialize the database
        projetPedagogiqueRepository.saveAndFlush(projetPedagogique);

        // Get all the projetPedagogiques
        restProjetPedagogiqueMockMvc.perform(get("/api/projet-pedagogiques?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(projetPedagogique.getId().intValue())))
                .andExpect(jsonPath("$.[*].elementProg").value(hasItem(DEFAULT_ELEMENT_PROG.toString())))
                .andExpect(jsonPath("$.[*].volumeHoraire").value(hasItem(DEFAULT_VOLUME_HORAIRE.doubleValue())))
                .andExpect(jsonPath("$.[*].dateDeb").value(hasItem(DEFAULT_DATE_DEB.toString())))
                .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    public void getProjetPedagogique() throws Exception {
        // Initialize the database
        projetPedagogiqueRepository.saveAndFlush(projetPedagogique);

        // Get the projetPedagogique
        restProjetPedagogiqueMockMvc.perform(get("/api/projet-pedagogiques/{id}", projetPedagogique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(projetPedagogique.getId().intValue()))
            .andExpect(jsonPath("$.elementProg").value(DEFAULT_ELEMENT_PROG.toString()))
            .andExpect(jsonPath("$.volumeHoraire").value(DEFAULT_VOLUME_HORAIRE.doubleValue()))
            .andExpect(jsonPath("$.dateDeb").value(DEFAULT_DATE_DEB.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProjetPedagogique() throws Exception {
        // Get the projetPedagogique
        restProjetPedagogiqueMockMvc.perform(get("/api/projet-pedagogiques/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjetPedagogique() throws Exception {
        // Initialize the database
        projetPedagogiqueRepository.saveAndFlush(projetPedagogique);
        projetPedagogiqueSearchRepository.save(projetPedagogique);
        int databaseSizeBeforeUpdate = projetPedagogiqueRepository.findAll().size();

        // Update the projetPedagogique
        ProjetPedagogique updatedProjetPedagogique = new ProjetPedagogique();
        updatedProjetPedagogique.setId(projetPedagogique.getId());
        updatedProjetPedagogique.setElementProg(UPDATED_ELEMENT_PROG);
        updatedProjetPedagogique.setVolumeHoraire(UPDATED_VOLUME_HORAIRE);
        updatedProjetPedagogique.setDateDeb(UPDATED_DATE_DEB);
        updatedProjetPedagogique.setDateFin(UPDATED_DATE_FIN);
        updatedProjetPedagogique.setStatus(UPDATED_STATUS);

        restProjetPedagogiqueMockMvc.perform(put("/api/projet-pedagogiques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProjetPedagogique)))
                .andExpect(status().isOk());

        // Validate the ProjetPedagogique in the database
        List<ProjetPedagogique> projetPedagogiques = projetPedagogiqueRepository.findAll();
        assertThat(projetPedagogiques).hasSize(databaseSizeBeforeUpdate);
        ProjetPedagogique testProjetPedagogique = projetPedagogiques.get(projetPedagogiques.size() - 1);
        assertThat(testProjetPedagogique.getElementProg()).isEqualTo(UPDATED_ELEMENT_PROG);
        assertThat(testProjetPedagogique.getVolumeHoraire()).isEqualTo(UPDATED_VOLUME_HORAIRE);
        assertThat(testProjetPedagogique.getDateDeb()).isEqualTo(UPDATED_DATE_DEB);
        assertThat(testProjetPedagogique.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testProjetPedagogique.isStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the ProjetPedagogique in ElasticSearch
        ProjetPedagogique projetPedagogiqueEs = projetPedagogiqueSearchRepository.findOne(testProjetPedagogique.getId());
        assertThat(projetPedagogiqueEs).isEqualToComparingFieldByField(testProjetPedagogique);
    }

    @Test
    @Transactional
    public void deleteProjetPedagogique() throws Exception {
        // Initialize the database
        projetPedagogiqueRepository.saveAndFlush(projetPedagogique);
        projetPedagogiqueSearchRepository.save(projetPedagogique);
        int databaseSizeBeforeDelete = projetPedagogiqueRepository.findAll().size();

        // Get the projetPedagogique
        restProjetPedagogiqueMockMvc.perform(delete("/api/projet-pedagogiques/{id}", projetPedagogique.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean projetPedagogiqueExistsInEs = projetPedagogiqueSearchRepository.exists(projetPedagogique.getId());
        assertThat(projetPedagogiqueExistsInEs).isFalse();

        // Validate the database is empty
        List<ProjetPedagogique> projetPedagogiques = projetPedagogiqueRepository.findAll();
        assertThat(projetPedagogiques).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProjetPedagogique() throws Exception {
        // Initialize the database
        projetPedagogiqueRepository.saveAndFlush(projetPedagogique);
        projetPedagogiqueSearchRepository.save(projetPedagogique);

        // Search the projetPedagogique
        restProjetPedagogiqueMockMvc.perform(get("/api/_search/projet-pedagogiques?query=id:" + projetPedagogique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projetPedagogique.getId().intValue())))
            .andExpect(jsonPath("$.[*].elementProg").value(hasItem(DEFAULT_ELEMENT_PROG.toString())))
            .andExpect(jsonPath("$.[*].volumeHoraire").value(hasItem(DEFAULT_VOLUME_HORAIRE.doubleValue())))
            .andExpect(jsonPath("$.[*].dateDeb").value(hasItem(DEFAULT_DATE_DEB.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }
}
