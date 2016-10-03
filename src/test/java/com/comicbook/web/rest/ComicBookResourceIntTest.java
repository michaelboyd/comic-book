package com.comicbook.web.rest;

import com.comicbook.ComicBookApp;

import com.comicbook.domain.ComicBook;
import com.comicbook.repository.ComicBookRepository;
import com.comicbook.service.ComicBookService;
import com.comicbook.service.dto.ComicBookDTO;
import com.comicbook.service.mapper.ComicBookMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ComicBookResource REST controller.
 *
 * @see ComicBookResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ComicBookApp.class)
public class ComicBookResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private ComicBookRepository comicBookRepository;

    @Inject
    private ComicBookMapper comicBookMapper;

    @Inject
    private ComicBookService comicBookService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restComicBookMockMvc;

    private ComicBook comicBook;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ComicBookResource comicBookResource = new ComicBookResource();
        ReflectionTestUtils.setField(comicBookResource, "comicBookService", comicBookService);
        this.restComicBookMockMvc = MockMvcBuilders.standaloneSetup(comicBookResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ComicBook createEntity(EntityManager em) {
        ComicBook comicBook = new ComicBook()
                .title(DEFAULT_TITLE)
                .description(DEFAULT_DESCRIPTION)
                .createDate(DEFAULT_CREATE_DATE);
        return comicBook;
    }

    @Before
    public void initTest() {
        comicBook = createEntity(em);
    }

    @Test
    @Transactional
    public void createComicBook() throws Exception {
        int databaseSizeBeforeCreate = comicBookRepository.findAll().size();

        // Create the ComicBook
        ComicBookDTO comicBookDTO = comicBookMapper.comicBookToComicBookDTO(comicBook);

        restComicBookMockMvc.perform(post("/api/comic-books")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(comicBookDTO)))
                .andExpect(status().isCreated());

        // Validate the ComicBook in the database
        List<ComicBook> comicBooks = comicBookRepository.findAll();
        assertThat(comicBooks).hasSize(databaseSizeBeforeCreate + 1);
        ComicBook testComicBook = comicBooks.get(comicBooks.size() - 1);
        assertThat(testComicBook.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testComicBook.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testComicBook.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = comicBookRepository.findAll().size();
        // set the field null
        comicBook.setTitle(null);

        // Create the ComicBook, which fails.
        ComicBookDTO comicBookDTO = comicBookMapper.comicBookToComicBookDTO(comicBook);

        restComicBookMockMvc.perform(post("/api/comic-books")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(comicBookDTO)))
                .andExpect(status().isBadRequest());

        List<ComicBook> comicBooks = comicBookRepository.findAll();
        assertThat(comicBooks).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreateDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = comicBookRepository.findAll().size();
        // set the field null
        comicBook.setCreateDate(null);

        // Create the ComicBook, which fails.
        ComicBookDTO comicBookDTO = comicBookMapper.comicBookToComicBookDTO(comicBook);

        restComicBookMockMvc.perform(post("/api/comic-books")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(comicBookDTO)))
                .andExpect(status().isBadRequest());

        List<ComicBook> comicBooks = comicBookRepository.findAll();
        assertThat(comicBooks).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllComicBooks() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get all the comicBooks
        restComicBookMockMvc.perform(get("/api/comic-books?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(comicBook.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())));
    }

    @Test
    @Transactional
    public void getComicBook() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);

        // Get the comicBook
        restComicBookMockMvc.perform(get("/api/comic-books/{id}", comicBook.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(comicBook.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingComicBook() throws Exception {
        // Get the comicBook
        restComicBookMockMvc.perform(get("/api/comic-books/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateComicBook() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);
        int databaseSizeBeforeUpdate = comicBookRepository.findAll().size();

        // Update the comicBook
        ComicBook updatedComicBook = comicBookRepository.findOne(comicBook.getId());
        updatedComicBook
                .title(UPDATED_TITLE)
                .description(UPDATED_DESCRIPTION)
                .createDate(UPDATED_CREATE_DATE);
        ComicBookDTO comicBookDTO = comicBookMapper.comicBookToComicBookDTO(updatedComicBook);

        restComicBookMockMvc.perform(put("/api/comic-books")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(comicBookDTO)))
                .andExpect(status().isOk());

        // Validate the ComicBook in the database
        List<ComicBook> comicBooks = comicBookRepository.findAll();
        assertThat(comicBooks).hasSize(databaseSizeBeforeUpdate);
        ComicBook testComicBook = comicBooks.get(comicBooks.size() - 1);
        assertThat(testComicBook.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testComicBook.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testComicBook.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void deleteComicBook() throws Exception {
        // Initialize the database
        comicBookRepository.saveAndFlush(comicBook);
        int databaseSizeBeforeDelete = comicBookRepository.findAll().size();

        // Get the comicBook
        restComicBookMockMvc.perform(delete("/api/comic-books/{id}", comicBook.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ComicBook> comicBooks = comicBookRepository.findAll();
        assertThat(comicBooks).hasSize(databaseSizeBeforeDelete - 1);
    }
}
