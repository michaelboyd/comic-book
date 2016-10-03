package com.comicbook.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.comicbook.service.ComicPageService;
import com.comicbook.web.rest.util.HeaderUtil;
import com.comicbook.service.dto.ComicPageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing ComicPage.
 */
@RestController
@RequestMapping("/api")
public class ComicPageResource {

    private final Logger log = LoggerFactory.getLogger(ComicPageResource.class);
        
    @Inject
    private ComicPageService comicPageService;

    /**
     * POST  /comic-pages : Create a new comicPage.
     *
     * @param comicPageDTO the comicPageDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new comicPageDTO, or with status 400 (Bad Request) if the comicPage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/comic-pages",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ComicPageDTO> createComicPage(@Valid @RequestBody ComicPageDTO comicPageDTO) throws URISyntaxException {
        log.debug("REST request to save ComicPage : {}", comicPageDTO);
        if (comicPageDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("comicPage", "idexists", "A new comicPage cannot already have an ID")).body(null);
        }
        ComicPageDTO result = comicPageService.save(comicPageDTO);
        return ResponseEntity.created(new URI("/api/comic-pages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("comicPage", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /comic-pages : Updates an existing comicPage.
     *
     * @param comicPageDTO the comicPageDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated comicPageDTO,
     * or with status 400 (Bad Request) if the comicPageDTO is not valid,
     * or with status 500 (Internal Server Error) if the comicPageDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/comic-pages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ComicPageDTO> updateComicPage(@Valid @RequestBody ComicPageDTO comicPageDTO) throws URISyntaxException {
        log.debug("REST request to update ComicPage : {}", comicPageDTO);
        if (comicPageDTO.getId() == null) {
            return createComicPage(comicPageDTO);
        }
        ComicPageDTO result = comicPageService.save(comicPageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("comicPage", comicPageDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /comic-pages : get all the comicPages.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of comicPages in body
     */
    @RequestMapping(value = "/comic-pages",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ComicPageDTO> getAllComicPages() {
        log.debug("REST request to get all ComicPages");
        return comicPageService.findAll();
    }

    /**
     * GET  /comic-pages/:id : get the "id" comicPage.
     *
     * @param id the id of the comicPageDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the comicPageDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/comic-pages/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ComicPageDTO> getComicPage(@PathVariable Long id) {
        log.debug("REST request to get ComicPage : {}", id);
        ComicPageDTO comicPageDTO = comicPageService.findOne(id);
        return Optional.ofNullable(comicPageDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /comic-pages/:id : delete the "id" comicPage.
     *
     * @param id the id of the comicPageDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/comic-pages/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteComicPage(@PathVariable Long id) {
        log.debug("REST request to delete ComicPage : {}", id);
        comicPageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("comicPage", id.toString())).build();
    }

}
