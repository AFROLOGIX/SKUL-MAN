package com.afrologix.skulman.web.rest;

import com.afrologix.skulman.SkulmanApp;
import com.afrologix.skulman.domain.Note;
import com.afrologix.skulman.repository.NoteRepository;
import com.afrologix.skulman.repository.search.NoteSearchRepository;

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
 * Test class for the NoteResource REST controller.
 *
 * @see NoteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SkulmanApp.class)
@WebAppConfiguration
@IntegrationTest
public class NoteResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final Double DEFAULT_NOTE = 1D;
    private static final Double UPDATED_NOTE = 2D;

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;
    private static final String DEFAULT_PERIODICITE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_PERIODICITE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
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
    private NoteRepository noteRepository;

    @Inject
    private NoteSearchRepository noteSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restNoteMockMvc;

    private Note note;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NoteResource noteResource = new NoteResource();
        ReflectionTestUtils.setField(noteResource, "noteSearchRepository", noteSearchRepository);
        ReflectionTestUtils.setField(noteResource, "noteRepository", noteRepository);
        this.restNoteMockMvc = MockMvcBuilders.standaloneSetup(noteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        noteSearchRepository.deleteAll();
        note = new Note();
        note.setNote(DEFAULT_NOTE);
        note.setDeleted(DEFAULT_DELETED);
        note.setPeriodicite(DEFAULT_PERIODICITE);
        note.setCreateBy(DEFAULT_CREATE_BY);
        note.setUpdateBy(DEFAULT_UPDATE_BY);
        note.setCreateAt(DEFAULT_CREATE_AT);
        note.setUpdateAt(DEFAULT_UPDATE_AT);
    }

    @Test
    @Transactional
    public void createNote() throws Exception {
        int databaseSizeBeforeCreate = noteRepository.findAll().size();

        // Create the Note

        restNoteMockMvc.perform(post("/api/notes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(note)))
                .andExpect(status().isCreated());

        // Validate the Note in the database
        List<Note> notes = noteRepository.findAll();
        assertThat(notes).hasSize(databaseSizeBeforeCreate + 1);
        Note testNote = notes.get(notes.size() - 1);
        assertThat(testNote.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testNote.isDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testNote.getPeriodicite()).isEqualTo(DEFAULT_PERIODICITE);
        assertThat(testNote.getCreateBy()).isEqualTo(DEFAULT_CREATE_BY);
        assertThat(testNote.getUpdateBy()).isEqualTo(DEFAULT_UPDATE_BY);
        assertThat(testNote.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testNote.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);

        // Validate the Note in ElasticSearch
        Note noteEs = noteSearchRepository.findOne(testNote.getId());
        assertThat(noteEs).isEqualToComparingFieldByField(testNote);
    }

    @Test
    @Transactional
    public void getAllNotes() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the notes
        restNoteMockMvc.perform(get("/api/notes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(note.getId().intValue())))
                .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.doubleValue())))
                .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
                .andExpect(jsonPath("$.[*].periodicite").value(hasItem(DEFAULT_PERIODICITE.toString())))
                .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY.toString())))
                .andExpect(jsonPath("$.[*].updateBy").value(hasItem(DEFAULT_UPDATE_BY.toString())))
                .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT_STR)))
                .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT_STR)));
    }

    @Test
    @Transactional
    public void getNote() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get the note
        restNoteMockMvc.perform(get("/api/notes/{id}", note.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(note.getId().intValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.doubleValue()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()))
            .andExpect(jsonPath("$.periodicite").value(DEFAULT_PERIODICITE.toString()))
            .andExpect(jsonPath("$.createBy").value(DEFAULT_CREATE_BY.toString()))
            .andExpect(jsonPath("$.updateBy").value(DEFAULT_UPDATE_BY.toString()))
            .andExpect(jsonPath("$.createAt").value(DEFAULT_CREATE_AT_STR))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT_STR));
    }

    @Test
    @Transactional
    public void getNonExistingNote() throws Exception {
        // Get the note
        restNoteMockMvc.perform(get("/api/notes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNote() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);
        noteSearchRepository.save(note);
        int databaseSizeBeforeUpdate = noteRepository.findAll().size();

        // Update the note
        Note updatedNote = new Note();
        updatedNote.setId(note.getId());
        updatedNote.setNote(UPDATED_NOTE);
        updatedNote.setDeleted(UPDATED_DELETED);
        updatedNote.setPeriodicite(UPDATED_PERIODICITE);
        updatedNote.setCreateBy(UPDATED_CREATE_BY);
        updatedNote.setUpdateBy(UPDATED_UPDATE_BY);
        updatedNote.setCreateAt(UPDATED_CREATE_AT);
        updatedNote.setUpdateAt(UPDATED_UPDATE_AT);

        restNoteMockMvc.perform(put("/api/notes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedNote)))
                .andExpect(status().isOk());

        // Validate the Note in the database
        List<Note> notes = noteRepository.findAll();
        assertThat(notes).hasSize(databaseSizeBeforeUpdate);
        Note testNote = notes.get(notes.size() - 1);
        assertThat(testNote.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testNote.isDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testNote.getPeriodicite()).isEqualTo(UPDATED_PERIODICITE);
        assertThat(testNote.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testNote.getUpdateBy()).isEqualTo(UPDATED_UPDATE_BY);
        assertThat(testNote.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testNote.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);

        // Validate the Note in ElasticSearch
        Note noteEs = noteSearchRepository.findOne(testNote.getId());
        assertThat(noteEs).isEqualToComparingFieldByField(testNote);
    }

    @Test
    @Transactional
    public void deleteNote() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);
        noteSearchRepository.save(note);
        int databaseSizeBeforeDelete = noteRepository.findAll().size();

        // Get the note
        restNoteMockMvc.perform(delete("/api/notes/{id}", note.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean noteExistsInEs = noteSearchRepository.exists(note.getId());
        assertThat(noteExistsInEs).isFalse();

        // Validate the database is empty
        List<Note> notes = noteRepository.findAll();
        assertThat(notes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchNote() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);
        noteSearchRepository.save(note);

        // Search the note
        restNoteMockMvc.perform(get("/api/_search/notes?query=id:" + note.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(note.getId().intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.doubleValue())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].periodicite").value(hasItem(DEFAULT_PERIODICITE.toString())))
            .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY.toString())))
            .andExpect(jsonPath("$.[*].updateBy").value(hasItem(DEFAULT_UPDATE_BY.toString())))
            .andExpect(jsonPath("$.[*].createAt").value(hasItem(DEFAULT_CREATE_AT_STR)))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT_STR)));
    }
}
