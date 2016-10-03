package com.comicbook.web.rest;

import com.comicbook.ComicBookApp;

import com.comicbook.domain.ComicPage;
import com.comicbook.domain.ComicBook;
import com.comicbook.repository.ComicPageRepository;
import com.comicbook.service.ComicPageService;
import com.comicbook.service.dto.ComicPageDTO;
import com.comicbook.service.mapper.ComicPageMapper;

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
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ComicPageResource REST controller.
 *
 * @see ComicPageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ComicBookApp.class)
public class ComicPageResourceIntTest {


    private static final Integer DEFAULT_PAGE_NUMBER = 1;
    private static final Integer UPDATED_PAGE_NUMBER = 2;

    private static final byte[] DEFAULT_IMAGE_DATA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_DATA = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_DATA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_DATA_CONTENT_TYPE = "image/png";

    @Inject
    private ComicPageRepository comicPageRepository;

    @Inject
    private ComicPageMapper comicPageMapper;

    @Inject
    private ComicPageService comicPageService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restComicPageMockMvc;

    private ComicPage comicPage;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ComicPageResource comicPageResource = new ComicPageResource();
        ReflectionTestUtils.setField(comicPageResource, "comicPageService", comicPageService);
        this.restComicPageMockMvc = MockMvcBuilders.standaloneSetup(comicPageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ComicPage createEntity(EntityManager em) {
        ComicPage comicPage = new ComicPage()
                .pageNumber(DEFAULT_PAGE_NUMBER)
                .imageData(DEFAULT_IMAGE_DATA)
                .imageDataContentType(DEFAULT_IMAGE_DATA_CONTENT_TYPE);
        // Add required entity
        ComicBook comicBook = ComicBookResourceIntTest.createEntity(em);
        em.persist(comicBook);
        em.flush();
        comicPage.setComicBook(comicBook);
        return comicPage;
    }

    @Before
    public void initTest() {
        comicPage = createEntity(em);
    }

    @Test
    @Transactional
    public void createComicPage() throws Exception {
        int databaseSizeBeforeCreate = comicPageRepository.findAll().size();

        // Create the ComicPage
        ComicPageDTO comicPageDTO = comicPageMapper.comicPageToComicPageDTO(comicPage);

        restComicPageMockMvc.perform(post("/api/comic-pages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(comicPageDTO)))
                .andExpect(status().isCreated());

        // Validate the ComicPage in the database
        List<ComicPage> comicPages = comicPageRepository.findAll();
        assertThat(comicPages).hasSize(databaseSizeBeforeCreate + 1);
        ComicPage testComicPage = comicPages.get(comicPages.size() - 1);
        assertThat(testComicPage.getPageNumber()).isEqualTo(DEFAULT_PAGE_NUMBER);
        assertThat(testComicPage.getImageData()).isEqualTo(DEFAULT_IMAGE_DATA);
        assertThat(testComicPage.getImageDataContentType()).isEqualTo(DEFAULT_IMAGE_DATA_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void checkPageNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = comicPageRepository.findAll().size();
        // set the field null
        comicPage.setPageNumber(null);

        // Create the ComicPage, which fails.
        ComicPageDTO comicPageDTO = comicPageMapper.comicPageToComicPageDTO(comicPage);

        restComicPageMockMvc.perform(post("/api/comic-pages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(comicPageDTO)))
                .andExpect(status().isBadRequest());

        List<ComicPage> comicPages = comicPageRepository.findAll();
        assertThat(comicPages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkImageDataIsRequired() throws Exception {
        int databaseSizeBeforeTest = comicPageRepository.findAll().size();
        // set the field null
        comicPage.setImageData(null);

        // Create the ComicPage, which fails.
        ComicPageDTO comicPageDTO = comicPageMapper.comicPageToComicPageDTO(comicPage);

        restComicPageMockMvc.perform(post("/api/comic-pages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(comicPageDTO)))
                .andExpect(status().isBadRequest());

        List<ComicPage> comicPages = comicPageRepository.findAll();
        assertThat(comicPages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllComicPages() throws Exception {
        // Initialize the database
        comicPageRepository.saveAndFlush(comicPage);

        // Get all the comicPages
        restComicPageMockMvc.perform(get("/api/comic-pages?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(comicPage.getId().intValue())))
                .andExpect(jsonPath("$.[*].pageNumber").value(hasItem(DEFAULT_PAGE_NUMBER)))
                .andExpect(jsonPath("$.[*].imageDataContentType").value(hasItem(DEFAULT_IMAGE_DATA_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].imageData").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_DATA))));
    }

    @Test
    @Transactional
    public void getComicPage() throws Exception {
        // Initialize the database
        comicPageRepository.saveAndFlush(comicPage);

        // Get the comicPage
        restComicPageMockMvc.perform(get("/api/comic-pages/{id}", comicPage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(comicPage.getId().intValue()))
            .andExpect(jsonPath("$.pageNumber").value(DEFAULT_PAGE_NUMBER))
            .andExpect(jsonPath("$.imageDataContentType").value(DEFAULT_IMAGE_DATA_CONTENT_TYPE))
            .andExpect(jsonPath("$.imageData").value(Base64Utils.encodeToString(DEFAULT_IMAGE_DATA)));
    }

    @Test
    @Transactional
    public void getNonExistingComicPage() throws Exception {
        // Get the comicPage
        restComicPageMockMvc.perform(get("/api/comic-pages/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateComicPage() throws Exception {
        // Initialize the database
        comicPageRepository.saveAndFlush(comicPage);
        int databaseSizeBeforeUpdate = comicPageRepository.findAll().size();

        // Update the comicPage
        ComicPage updatedComicPage = comicPageRepository.findOne(comicPage.getId());
        updatedComicPage
                .pageNumber(UPDATED_PAGE_NUMBER)
                .imageData(UPDATED_IMAGE_DATA)
                .imageDataContentType(UPDATED_IMAGE_DATA_CONTENT_TYPE);
        ComicPageDTO comicPageDTO = comicPageMapper.comicPageToComicPageDTO(updatedComicPage);

        restComicPageMockMvc.perform(put("/api/comic-pages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(comicPageDTO)))
                .andExpect(status().isOk());

        // Validate the ComicPage in the database
        List<ComicPage> comicPages = comicPageRepository.findAll();
        assertThat(comicPages).hasSize(databaseSizeBeforeUpdate);
        ComicPage testComicPage = comicPages.get(comicPages.size() - 1);
        assertThat(testComicPage.getPageNumber()).isEqualTo(UPDATED_PAGE_NUMBER);
        assertThat(testComicPage.getImageData()).isEqualTo(UPDATED_IMAGE_DATA);
        assertThat(testComicPage.getImageDataContentType()).isEqualTo(UPDATED_IMAGE_DATA_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void deleteComicPage() throws Exception {
        // Initialize the database
        comicPageRepository.saveAndFlush(comicPage);
        int databaseSizeBeforeDelete = comicPageRepository.findAll().size();

        // Get the comicPage
        restComicPageMockMvc.perform(delete("/api/comic-pages/{id}", comicPage.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ComicPage> comicPages = comicPageRepository.findAll();
        assertThat(comicPages).hasSize(databaseSizeBeforeDelete - 1);
    }
}
