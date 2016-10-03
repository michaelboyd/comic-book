package com.comicbook.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ComicBook.
 */
@Entity
@Table(name = "comic_book")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ComicBook implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "create_date", nullable = false)
    private LocalDate createDate;

    @Lob
    @Column(name = "cover_image_data")
    private byte[] coverImageData;

    @Column(name = "cover_image_data_content_type")
    private String coverImageDataContentType;

    @Lob
    @Column(name = "synopsis")
    private String synopsis;

    @OneToMany(mappedBy = "comicBook")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ComicPage> pages = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public ComicBook title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public ComicBook description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public ComicBook createDate(LocalDate createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public byte[] getCoverImageData() {
        return coverImageData;
    }

    public ComicBook coverImageData(byte[] coverImageData) {
        this.coverImageData = coverImageData;
        return this;
    }

    public void setCoverImageData(byte[] coverImageData) {
        this.coverImageData = coverImageData;
    }

    public String getCoverImageDataContentType() {
        return coverImageDataContentType;
    }

    public ComicBook coverImageDataContentType(String coverImageDataContentType) {
        this.coverImageDataContentType = coverImageDataContentType;
        return this;
    }

    public void setCoverImageDataContentType(String coverImageDataContentType) {
        this.coverImageDataContentType = coverImageDataContentType;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public ComicBook synopsis(String synopsis) {
        this.synopsis = synopsis;
        return this;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public Set<ComicPage> getPages() {
        return pages;
    }

    public ComicBook pages(Set<ComicPage> comicPages) {
        this.pages = comicPages;
        return this;
    }

    public ComicBook addPages(ComicPage comicPage) {
        pages.add(comicPage);
        comicPage.setComicBook(this);
        return this;
    }

    public ComicBook removePages(ComicPage comicPage) {
        pages.remove(comicPage);
        comicPage.setComicBook(null);
        return this;
    }

    public void setPages(Set<ComicPage> comicPages) {
        this.pages = comicPages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComicBook comicBook = (ComicBook) o;
        if(comicBook.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, comicBook.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ComicBook{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", description='" + description + "'" +
            ", createDate='" + createDate + "'" +
            ", coverImageData='" + coverImageData + "'" +
            ", coverImageDataContentType='" + coverImageDataContentType + "'" +
            ", synopsis='" + synopsis + "'" +
            '}';
    }
}
