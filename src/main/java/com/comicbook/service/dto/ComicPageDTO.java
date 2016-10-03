package com.comicbook.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;


/**
 * A DTO for the ComicPage entity.
 */
public class ComicPageDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer pageNumber;

    @NotNull
    @Lob
    private byte[] imageData;

    private String imageDataContentType;

    private Long comicBookId;
    

    private String comicBookTitle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getImageDataContentType() {
        return imageDataContentType;
    }

    public void setImageDataContentType(String imageDataContentType) {
        this.imageDataContentType = imageDataContentType;
    }

    public Long getComicBookId() {
        return comicBookId;
    }

    public void setComicBookId(Long comicBookId) {
        this.comicBookId = comicBookId;
    }


    public String getComicBookTitle() {
        return comicBookTitle;
    }

    public void setComicBookTitle(String comicBookTitle) {
        this.comicBookTitle = comicBookTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ComicPageDTO comicPageDTO = (ComicPageDTO) o;

        if ( ! Objects.equals(id, comicPageDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ComicPageDTO{" +
            "id=" + id +
            ", pageNumber='" + pageNumber + "'" +
            ", imageData='" + imageData + "'" +
            '}';
    }
}
