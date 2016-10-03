package com.comicbook.service.mapper;

import com.comicbook.domain.*;
import com.comicbook.service.dto.ComicBookDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ComicBook and its DTO ComicBookDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ComicBookMapper {

    ComicBookDTO comicBookToComicBookDTO(ComicBook comicBook);

    List<ComicBookDTO> comicBooksToComicBookDTOs(List<ComicBook> comicBooks);

    @Mapping(target = "pages", ignore = true)
    ComicBook comicBookDTOToComicBook(ComicBookDTO comicBookDTO);

    List<ComicBook> comicBookDTOsToComicBooks(List<ComicBookDTO> comicBookDTOs);
}
