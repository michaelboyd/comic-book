package com.comicbook.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.comicbook.domain.ComicPage;
import com.comicbook.service.ComicBookService;
import com.comicbook.service.dto.ComicBookDTO;
import com.comicbook.web.rest.util.HeaderUtil;

/**
 * REST controller for managing ComicBook.
 */
@RestController
@RequestMapping("/api")
public class ComicBookResource {

    private final Logger log = LoggerFactory.getLogger(ComicBookResource.class);
        
    @Inject
    private ComicBookService comicBookService;

    /**
     * POST  /comic-books : Create a new comicBook.
     *
     * @param comicBookDTO the comicBookDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new comicBookDTO, or with status 400 (Bad Request) if the comicBook has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/comic-books",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ComicBookDTO> createComicBook(@Valid @RequestBody ComicBookDTO comicBookDTO) throws URISyntaxException {
        log.debug("REST request to save ComicBook : {}", comicBookDTO);
        if (comicBookDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("comicBook", "idexists", "A new comicBook cannot already have an ID")).body(null);
        }
        ComicBookDTO result = comicBookService.save(comicBookDTO);
        return ResponseEntity.created(new URI("/api/comic-books/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("comicBook", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /comic-books : Updates an existing comicBook.
     *
     * @param comicBookDTO the comicBookDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated comicBookDTO,
     * or with status 400 (Bad Request) if the comicBookDTO is not valid,
     * or with status 500 (Internal Server Error) if the comicBookDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/comic-books",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ComicBookDTO> updateComicBook(@Valid @RequestBody ComicBookDTO comicBookDTO) throws URISyntaxException {
        log.debug("REST request to update ComicBook : {}", comicBookDTO);
        if (comicBookDTO.getId() == null) {
            return createComicBook(comicBookDTO);
        }
        ComicBookDTO result = comicBookService.save(comicBookDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("comicBook", comicBookDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /comic-books : get all the comicBooks.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of comicBooks in body
     */
    @RequestMapping(value = "/comic-books",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ComicBookDTO> getAllComicBooks() {
        log.debug("REST request to get all ComicBooks");
        return comicBookService.findAll();
    }

    /**
     * GET  /comic-books/:id : get the "id" comicBook.
     *
     * @param id the id of the comicBookDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the comicBookDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/comic-books/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ComicBookDTO> getComicBook(@PathVariable Long id) {
        log.debug("REST request to get ComicBook : {}", id);
        ComicBookDTO comicBookDTO = comicBookService.findOne(id);
        return Optional.ofNullable(comicBookDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    /**
     * DELETE  /comic-books/:id : delete the "id" comicBook.
     *
     * @param id the id of the comicBookDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/comic-books/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteComicBook(@PathVariable Long id) {
        log.debug("REST request to delete ComicBook : {}", id);
        comicBookService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("comicBook", id.toString())).build();
    }

}
