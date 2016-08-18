package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.FormatMatricule;
import com.afrologix.skulman.repository.FormatMatriculeRepository;
import com.afrologix.skulman.repository.search.FormatMatriculeSearchRepository;

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
 * Test class for the FormatMatriculeResource REST controller.
 *
 * @see FormatMatriculeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class FormatMatriculeResourceIntTest {

    private static final String DEFAULT_FORMAT = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_FORMAT = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private FormatMatriculeRepository formatMatriculeRepository;

    @Inject
    private FormatMatriculeSearchRepository formatMatriculeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFormatMatriculeMockMvc;

    private FormatMatricule formatMatricule;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FormatMatriculeResource formatMatriculeResource = new FormatMatriculeResource();
        ReflectionTestUtils.setField(formatMatriculeResource, "formatMatriculeSearchRepository", formatMatriculeSearchRepository);
        ReflectionTestUtils.setField(formatMatriculeResource, "formatMatriculeRepository", formatMatriculeRepository);
        this.restFormatMatriculeMockMvc = MockMvcBuilders.standaloneSetup(formatMatriculeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        formatMatriculeSearchRepository.deleteAll();
        formatMatricule = new FormatMatricule();
        formatMatricule.setFormat(DEFAULT_FORMAT);
    }

    @Test
    @Transactional
    public void createFormatMatricule() throws Exception {
        int databaseSizeBeforeCreate = formatMatriculeRepository.findAll().size();

        // Create the FormatMatricule

        restFormatMatriculeMockMvc.perform(post("/api/format-matricules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(formatMatricule)))
                .andExpect(status().isCreated());

        // Validate the FormatMatricule in the database
        List<FormatMatricule> formatMatricules = formatMatriculeRepository.findAll();
        assertThat(formatMatricules).hasSize(databaseSizeBeforeCreate + 1);
        FormatMatricule testFormatMatricule = formatMatricules.get(formatMatricules.size() - 1);
        assertThat(testFormatMatricule.getFormat()).isEqualTo(DEFAULT_FORMAT);

        // Validate the FormatMatricule in ElasticSearch
        FormatMatricule formatMatriculeEs = formatMatriculeSearchRepository.findOne(testFormatMatricule.getId());
        assertThat(formatMatriculeEs).isEqualToComparingFieldByField(testFormatMatricule);
    }

    @Test
    @Transactional
    public void getAllFormatMatricules() throws Exception {
        // Initialize the database
        formatMatriculeRepository.saveAndFlush(formatMatricule);

        // Get all the formatMatricules
        restFormatMatriculeMockMvc.perform(get("/api/format-matricules?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(formatMatricule.getId().intValue())))
                .andExpect(jsonPath("$.[*].format").value(hasItem(DEFAULT_FORMAT.toString())));
    }

    @Test
    @Transactional
    public void getFormatMatricule() throws Exception {
        // Initialize the database
        formatMatriculeRepository.saveAndFlush(formatMatricule);

        // Get the formatMatricule
        restFormatMatriculeMockMvc.perform(get("/api/format-matricules/{id}", formatMatricule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(formatMatricule.getId().intValue()))
            .andExpect(jsonPath("$.format").value(DEFAULT_FORMAT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFormatMatricule() throws Exception {
        // Get the formatMatricule
        restFormatMatriculeMockMvc.perform(get("/api/format-matricules/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFormatMatricule() throws Exception {
        // Initialize the database
        formatMatriculeRepository.saveAndFlush(formatMatricule);
        formatMatriculeSearchRepository.save(formatMatricule);
        int databaseSizeBeforeUpdate = formatMatriculeRepository.findAll().size();

        // Update the formatMatricule
        FormatMatricule updatedFormatMatricule = new FormatMatricule();
        updatedFormatMatricule.setId(formatMatricule.getId());
        updatedFormatMatricule.setFormat(UPDATED_FORMAT);

        restFormatMatriculeMockMvc.perform(put("/api/format-matricules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFormatMatricule)))
                .andExpect(status().isOk());

        // Validate the FormatMatricule in the database
        List<FormatMatricule> formatMatricules = formatMatriculeRepository.findAll();
        assertThat(formatMatricules).hasSize(databaseSizeBeforeUpdate);
        FormatMatricule testFormatMatricule = formatMatricules.get(formatMatricules.size() - 1);
        assertThat(testFormatMatricule.getFormat()).isEqualTo(UPDATED_FORMAT);

        // Validate the FormatMatricule in ElasticSearch
        FormatMatricule formatMatriculeEs = formatMatriculeSearchRepository.findOne(testFormatMatricule.getId());
        assertThat(formatMatriculeEs).isEqualToComparingFieldByField(testFormatMatricule);
    }

    @Test
    @Transactional
    public void deleteFormatMatricule() throws Exception {
        // Initialize the database
        formatMatriculeRepository.saveAndFlush(formatMatricule);
        formatMatriculeSearchRepository.save(formatMatricule);
        int databaseSizeBeforeDelete = formatMatriculeRepository.findAll().size();

        // Get the formatMatricule
        restFormatMatriculeMockMvc.perform(delete("/api/format-matricules/{id}", formatMatricule.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean formatMatriculeExistsInEs = formatMatriculeSearchRepository.exists(formatMatricule.getId());
        assertThat(formatMatriculeExistsInEs).isFalse();

        // Validate the database is empty
        List<FormatMatricule> formatMatricules = formatMatriculeRepository.findAll();
        assertThat(formatMatricules).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFormatMatricule() throws Exception {
        // Initialize the database
        formatMatriculeRepository.saveAndFlush(formatMatricule);
        formatMatriculeSearchRepository.save(formatMatricule);

        // Search the formatMatricule
        restFormatMatriculeMockMvc.perform(get("/api/_search/format-matricules?query=id:" + formatMatricule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formatMatricule.getId().intValue())))
            .andExpect(jsonPath("$.[*].format").value(hasItem(DEFAULT_FORMAT.toString())));
    }
}
