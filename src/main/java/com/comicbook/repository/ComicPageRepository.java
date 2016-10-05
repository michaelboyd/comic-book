package com.comicbook.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.comicbook.domain.ComicBook;
import com.comicbook.domain.ComicPage;

/**
 * Spring Data JPA repository for the ComicPage entity.
 */
@SuppressWarnings("unused")
public interface ComicPageRepository extends JpaRepository<ComicPage,Long> {
	
	Set <ComicPage> findByComicBookOrderByPageNumber(ComicBook comicBook);

}
