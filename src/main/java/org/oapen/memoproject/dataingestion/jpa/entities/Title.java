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
    @Column(name = "handle", updatable = false, insertable = true)
    private String handle;

    @Column(name = "sysid", updatable = false, insertable = true)
    private String id;

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

    @Column(name = "description_other_language")
    private String descriptionOtherlanguage;

    @Column(name = "description_abstract")
    private String descriptionAbstract;

    @Column(name = "description_provenance")
    private String descriptionProvenance;

    @Column(name = "terms_abstract")
    private String termsAbstract;

    @Column(name = "abstract_other_language")
    private String abstractOtherLanguage;

    @Column(name = "is_part_of_series")
    private String partOfSeries;

    @Column(name = "title")
    private String title;

    @Column(name = "title_alternative")
    private String titleAlternative;

    @Column(name = "type")
    private String type;

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
    @CollectionTable(name="language", joinColumns= @JoinColumn(name="handle_title", nullable = false))
    @Column(name = "language")
    private Set<String> languages;
    
    // TODO make this a Date object?
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="date_accessioned", joinColumns= @JoinColumn(name="handle_title", nullable = false))
    @Column(name = "date", nullable = false)
    private Set<String> datesAccessioned;
    
    @ElementCollection
    @CollectionTable(name="subject_other", joinColumns= @JoinColumn(name="handle_title", nullable = false))
    @Column(name = "subject")
    private Set<String> subjectsOther;

    @OneToMany(mappedBy = "handleTitle", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Setter(AccessLevel.PRIVATE) // Enforce usage of addIdentifier
    private Set<Identifier> identifiers = new HashSet<>();
    
    public void addIdentifier(Identifier identifier) {
    	identifier.setHandleTitle(this.handle);
    	identifiers.add(identifier);
    }

    @OneToMany(mappedBy = "handleTitle", fetch = FetchType.EAGER, cascade = CascadeType.ALL) // an ExportChunkId!
    @Setter(AccessLevel.PRIVATE) // Enforce usage of addExportChunk
    private Set<ExportChunk> exportChunks = new HashSet<>();
    
    public void addExportChunk(ExportChunk chunk) {
    	
    	chunk.setHandleTitle(this.handle);
    	exportChunks.add(chunk);
    }

    
    @OneToMany(mappedBy = "handleTitle", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Setter(AccessLevel.PRIVATE) // Enforce usage of addGrantData
    private Set<GrantData> grantdata = new HashSet<>();
    
    public void addGrantData(GrantData grantfield) {
    	
    	grantfield.setHandleTitle(this.handle);
    	grantdata.add(grantfield);
    }
    
    
    @ManyToMany(
    	fetch = FetchType.EAGER, // Eager, because there are only a few.	
    	cascade = {CascadeType.PERSIST,CascadeType.MERGE} 
    )
    @JoinTable(name = "funding",
        joinColumns = @JoinColumn(name = "handle_title", nullable = false),
        inverseJoinColumns = @JoinColumn(name = "handle_funder", nullable = false)
    )
	private Set<Funder> funders = new HashSet<>();
    
	public void addFunder(Funder funder) {
        funders.add(funder);
    }
    
    
    @OneToMany(mappedBy = "handleTitle", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Setter(AccessLevel.PRIVATE) // Enforce usage of addFunding
    private Set<Contribution> contributions = new HashSet<>();
    
    public void addContribution(Contribution contribution) {
    	
    	contribution.setHandleTitle(this.handle);
    	contributions.add(contribution);
    }
    
    
    @ManyToOne(
       	fetch = FetchType.EAGER,
       	cascade = {CascadeType.PERSIST,CascadeType.MERGE} 
    )
    @JoinColumn(name = "handle_publisher", nullable = true)
    private Publisher publisher;
    
    @ManyToMany(
    	fetch = FetchType.EAGER, // Eager, because there are only a few.	
    	cascade = {CascadeType.PERSIST,CascadeType.MERGE} 
    )
    @JoinTable(name = "subject_classification",
        joinColumns = @JoinColumn(name = "handle_title", nullable = false),
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