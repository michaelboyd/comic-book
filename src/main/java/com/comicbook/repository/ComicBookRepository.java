package com.comicbook.repository;

import com.comicbook.domain.ComicBook;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ComicBook entity.
 */
@SuppressWarnings("unused")
public interface ComicBookRepository extends JpaRepository<ComicBook,Long> {

}
