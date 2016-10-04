package com.comicbook.web.rest;

import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.comicbook.service.ComicBookService;
import com.comicbook.service.dto.ComicBookDTO;

/**
 * REST controller for managing Home.
 */
@RestController
@RequestMapping("/api")
public class HomeResource {
	
    private final Logger log = LoggerFactory.getLogger(HomeResource.class);
    
    @Inject
    private ComicBookService comicBookService;	
	
    /**
     * GET  /comic-books/:id : get the "id" comicBook.
     *
     * @param id the id of the comicBookDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the comicBookDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/view-comic-book/{id}",
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

}
