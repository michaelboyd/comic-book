package com.comicbook.repository;

import com.comicbook.domain.ComicPage;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ComicPage entity.
 */
@SuppressWarnings("unused")
public interface ComicPageRepository extends JpaRepository<ComicPage,Long> {

}
