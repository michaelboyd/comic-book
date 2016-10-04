package com.comicbook.service.dto;

import java.time.LocalDate;

import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import javax.persistence.Lob;

import com.comicbook.domain.ComicPage;


/**
 * A DTO for the ComicBook entity.
 */
public class ComicBookDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    private String description;

    @NotNull
    private LocalDate createDate;

    @Lob
    private byte[] coverImageData;

    private String coverImageDataContentType;
    @Lob
    private String synopsis;
    
    private Set<ComicPage> pages = new HashSet<>();    


    
    /**
	 * @return the pages
	 */
	public Set<ComicPage> getPages() {
		return pages;
	}

	/**
	 * @param pages the pages to set
	 */
	public void setPages(Set<ComicPage> pages) {
		this.pages = pages;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }
    public byte[] getCoverImageData() {
        return coverImageData;
    }

    public void setCoverImageData(byte[] coverImageData) {
        this.coverImageData = coverImageData;
    }

    public String getCoverImageDataContentType() {
        return coverImageDataContentType;
    }

    public void setCoverImageDataContentType(String coverImageDataContentType) {
        this.coverImageDataContentType = coverImageDataContentType;
    }
    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ComicBookDTO comicBookDTO = (ComicBookDTO) o;

        if ( ! Objects.equals(id, comicBookDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ComicBookDTO{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", description='" + description + "'" +
            ", createDate='" + createDate + "'" +
            ", coverImageData='" + coverImageData + "'" +
            ", synopsis='" + synopsis + "'" +
            ", pages='" + pages + "'" +
            '}';
    }
}
