package com.comicbook.service;

import com.comicbook.domain.ComicPage;
import com.comicbook.repository.ComicPageRepository;
import com.comicbook.service.dto.ComicPageDTO;
import com.comicbook.service.mapper.ComicPageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ComicPage.
 */
@Service
@Transactional
public class ComicPageService {

    private final Logger log = LoggerFactory.getLogger(ComicPageService.class);
    
    @Inject
    private ComicPageRepository comicPageRepository;

    @Inject
    private ComicPageMapper comicPageMapper;

    /**
     * Save a comicPage.
     *
     * @param comicPageDTO the entity to save
     * @return the persisted entity
     */
    public ComicPageDTO save(ComicPageDTO comicPageDTO) {
        log.debug("Request to save ComicPage : {}", comicPageDTO);
        ComicPage comicPage = comicPageMapper.comicPageDTOToComicPage(comicPageDTO);
        comicPage = comicPageRepository.save(comicPage);
        ComicPageDTO result = comicPageMapper.comicPageToComicPageDTO(comicPage);
        return result;
    }

    /**
     *  Get all the comicPages.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<ComicPageDTO> findAll() {
        log.debug("Request to get all ComicPages");
        List<ComicPageDTO> result = comicPageRepository.findAll().stream()
            .map(comicPageMapper::comicPageToComicPageDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one comicPage by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ComicPageDTO findOne(Long id) {
        log.debug("Request to get ComicPage : {}", id);
        ComicPage comicPage = comicPageRepository.findOne(id);
        ComicPageDTO comicPageDTO = comicPageMapper.comicPageToComicPageDTO(comicPage);
        return comicPageDTO;
    }

    /**
     *  Delete the  comicPage by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ComicPage : {}", id);
        comicPageRepository.delete(id);
    }
}
