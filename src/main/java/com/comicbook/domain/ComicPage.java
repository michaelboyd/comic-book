package com.comicbook.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ComicPage.
 */
@Entity
@Table(name = "comic_page")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ComicPage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "page_number", nullable = false)
    private Integer pageNumber;

    @NotNull
    @Lob
    @Column(name = "image_data", nullable = false)
    private byte[] imageData;

    @Column(name = "image_data_content_type", nullable = false)
    private String imageDataContentType;

    @ManyToOne
    @NotNull
    private ComicBook comicBook;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public ComicPage pageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public ComicPage imageData(byte[] imageData) {
        this.imageData = imageData;
        return this;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getImageDataContentType() {
        return imageDataContentType;
    }

    public ComicPage imageDataContentType(String imageDataContentType) {
        this.imageDataContentType = imageDataContentType;
        return this;
    }

    public void setImageDataContentType(String imageDataContentType) {
        this.imageDataContentType = imageDataContentType;
    }

    public ComicBook getComicBook() {
        return comicBook;
    }

    public ComicPage comicBook(ComicBook comicBook) {
        this.comicBook = comicBook;
        return this;
    }

    public void setComicBook(ComicBook comicBook) {
        this.comicBook = comicBook;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComicPage comicPage = (ComicPage) o;
        if(comicPage.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, comicPage.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ComicPage{" +
            "id=" + id +
            ", pageNumber='" + pageNumber + "'" +
            ", imageData='" + imageData + "'" +
            ", imageDataContentType='" + imageDataContentType + "'" +
            '}';
    }
}
