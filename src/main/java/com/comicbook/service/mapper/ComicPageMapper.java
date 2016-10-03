package com.comicbook.service.mapper;

import com.comicbook.domain.*;
import com.comicbook.service.dto.ComicPageDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ComicPage and its DTO ComicPageDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ComicPageMapper {

    @Mapping(source = "comicBook.id", target = "comicBookId")
    @Mapping(source = "comicBook.title", target = "comicBookTitle")
    ComicPageDTO comicPageToComicPageDTO(ComicPage comicPage);

    List<ComicPageDTO> comicPagesToComicPageDTOs(List<ComicPage> comicPages);

    @Mapping(source = "comicBookId", target = "comicBook")
    ComicPage comicPageDTOToComicPage(ComicPageDTO comicPageDTO);

    List<ComicPage> comicPageDTOsToComicPages(List<ComicPageDTO> comicPageDTOs);

    default ComicBook comicBookFromId(Long id) {
        if (id == null) {
            return null;
        }
        ComicBook comicBook = new ComicBook();
        comicBook.setId(id);
        return comicBook;
    }
}
