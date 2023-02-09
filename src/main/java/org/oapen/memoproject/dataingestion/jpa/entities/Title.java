package org.oapen.memoproject.dataingestion.jpa.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter 
@Entity(name = "Title")
@Table(name="title")
public class Title {
	
    @Id
    @Column(name = "id", updatable = false, insertable = true)
    private String id;

    @Column(name = "handle", updatable = false, insertable = true)
    private String handle;

    @Column(name = "collection")
    private String collection;

    @Column(name = "download_url")
    private String downloadUrl;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "license")
    private String license;

    @Column(name = "webshop_url")
    private String webshopUrl;

    @Column(name = "date_available")
    private String dateAvailable;

    @Column(name = "date_issued")
    private String dateIssued;

    @Column(name = "description")
    private String description;

    @Column(name = "description_abstract")
    private String descriptionAbstract;

    @Column(name = "description_provenance")
    private String descriptionProvenance;

    @Column(name = "is_part_of_series")
    private String isPartOfSeries;

    @Column(name = "title")
    private String title;

    @Column(name = "title_alternative")
    private String titleAlternative;

    @Column(name = "type")
    private String type;

    @Column(name = "terms_abstract")
    private String termsAbstract;

    @Column(name = "abstract_other_language")
    private String abstractOtherLanguage;

    @Column(name = "description_other_language")
    private String descriptionOtherlanguage;

    @Column(name = "chapter_number")
    private String chapterNumber;

    @Column(name = "embargo")
    private String embargo;

    @Column(name = "imprint")
    private String imprint;

    @Column(name = "pages")
    private String pages;

    @Column(name = "place_publication")
    private String placePublication;

    @Column(name = "series_number")
    private String series_number;
    
    // no need for mapping here as long as the data is inserted we need no object reference
    @Column(name = "part_of_book") 
    private String partOfBook;

    @ElementCollection
    @CollectionTable(name="language", joinColumns= @JoinColumn(name="id_title", nullable = false))
    @Column(name = "language")
    private Set<String> languages;
    
    // TODO make this a Date object?
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="date_accessioned", joinColumns= @JoinColumn(name="id_title", nullable = false))
    @Column(name = "date", nullable = false)
    private Set<String> datesAccessioned;
    
    @ElementCollection
    @CollectionTable(name="subject_other", joinColumns= @JoinColumn(name="id_title", nullable = false))
    @Column(name = "subject")
    private Set<String> subjectsOther;

    @OneToMany
    @JoinColumn(name = "id_title", nullable = false)
    private Set<Identifier> identifiers;

    @OneToMany(mappedBy = "id.title", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Setter(AccessLevel.PRIVATE) // Enforce usage of addExportChunk
    private Set<ExportChunk> exportChunks = new HashSet<>();
    
    public void addExportChunk(String type, String content) {
    	
    	ExportChunk c = new ExportChunk();
    	ExportChunkId id = new ExportChunkId();
    	id.setTitle(this);
    	id.setType(type);
    	c.setId(id);
    	c.setContent(content);
    	
    	exportChunks.add(c);
    }
    
    @ManyToOne(
       	fetch = FetchType.EAGER,
       	cascade = {CascadeType.PERSIST,CascadeType.MERGE} 
    )
    @JoinColumn(name = "is_published_by", nullable = true)
    private Publisher publisher;
    
    @ManyToMany(
    	fetch = FetchType.EAGER, // Eager, because there are only a few.	
    	cascade = {CascadeType.PERSIST,CascadeType.MERGE} 
    )
    @JoinTable(name = "subject_classification",
        joinColumns = @JoinColumn(name = "id_title", nullable = false),
        inverseJoinColumns = @JoinColumn(name = "code_classification", nullable = false)
    )
	private Set<Classification> classifications = new HashSet<>();
    
	public void addClassification(Classification classification) {
        classifications.add(classification);
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Title other = (Title) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	
}