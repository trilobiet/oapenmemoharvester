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
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter 
@Entity(name = "Title")
@Table(name="title")
public class Title {
	
    @Id
    @Column(name = "handle", updatable = false, insertable = true)
    private String handle;

    @Transient
    private String status;
    
    @Column(name = "sysid")
    private String sysId;

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

    @Column(name = "year_available")
    private Integer yearAvailable;

    @Column(name = "description_other_language")
    private String descriptionOtherLanguage;

    @Column(name = "description_abstract")
    private String descriptionAbstract;

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

    @Column(name = "imprint")
    private String imprint;

    @Column(name = "pages")
    private String pages;

    @Column(name = "place_publication")
    private String placePublication;

    @Column(name = "series_number")
    private String seriesNumber;
    
    // no need for mapping here as long as the data is inserted we need no object reference
    @Column(name = "part_of_book") 
    private String partOfBook;

    @ElementCollection
    @CollectionTable(name="language", joinColumns= @JoinColumn(name="handle_title", nullable = false))
    @Column(name = "language")
    private Set<String> languages = new HashSet<>();
    
    @ElementCollection
    @CollectionTable(name="subject_other", joinColumns= @JoinColumn(name="handle_title", nullable = false))
    @Column(name = "subject")
    private Set<String> subjectsOther = new HashSet<>();

    @OneToMany(mappedBy = "handleTitle", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Identifier> identifiers = new HashSet<>();
    
    public void setIdentifiers(Set<Identifier> values) {
    	identifiers.clear();
    	values.forEach(this::addIdentifier);
    }
    
    public void addIdentifier(Identifier identifier) {
    	
    	if (identifier != null) {
    		identifier.setHandleTitle(this.handle);
    		identifiers.add(identifier);
    	}	
    }

    @OneToMany(mappedBy = "handleTitle", fetch = FetchType.EAGER, cascade = CascadeType.ALL) // an ExportChunkId!
    private Set<ExportChunk> exportChunks = new HashSet<>();
    
    public void setExportChunks(Set<ExportChunk> values) {
    	exportChunks.clear();
    	values.forEach(this::addExportChunk);
    }
    
    public void addExportChunk(ExportChunk chunk) {
    	
    	if (chunk != null) {
    		chunk.setHandleTitle(this.handle);
    		exportChunks.add(chunk);
    	}
    	
    }

    
    @OneToMany(mappedBy = "handleTitle", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<GrantData> grantData = new HashSet<>();
    
    public void setGrantData(Set<GrantData> values) {
    	grantData.clear();
    	values.forEach(this::addGrantData);
    }
    
    public void addGrantData(GrantData grantfield) {
    	
    	if (grantfield != null) {
    		grantfield.setHandleTitle(this.handle);
    		grantData.add(grantfield);
    	}	
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
    
    public void setFunders(Set<Funder> values) {
    	
    	values.forEach(this::addFunder);
    }
    
	public void addFunder(Funder funder) {
        
		if (funder != null) funders.add(funder);
    }
    
    
    @OneToMany(mappedBy = "handleTitle", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Contribution> contributions = new HashSet<>();
    
    public void setContributions(Set<Contribution> values) {
    	contributions.clear();
    	values.forEach(this::addContribution);
    }
    
    public void addContribution(Contribution contribution) {
    	
    	if (contribution != null) {
    		contribution.setHandleTitle(this.handle);
    		contributions.add(contribution);
    	}	
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
    
    public void setClassifications(Set<Classification> values) {

    	values.forEach(this::addClassification);
    }
    
	public void addClassification(Classification classification) {
		
		if (classification != null) classifications.add(classification);
    }

	// Constructors
	public Title() {}
	
	public Title(String handle) {

		this.handle = handle;
	}
	
	public boolean isComplete() {
		
		return (handle != null && !handle.isBlank());
	}
	
	public boolean isDeleted() {
		
		return (status != null && status.equals("deleted"));
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sysId == null) ? 0 : sysId.hashCode());
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
		if (sysId == null) {
			if (other.sysId != null)
				return false;
		} else if (!sysId.equals(other.sysId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Title [handle=" + handle + ", publisher:" + (publisher!=null)
				+ ", identifiers:" + identifiers.size()
				+ ", exportChunks:" + exportChunks.size() + ", grantdata:" + grantData.size() + ", funders:" + funders.size()
				+ ", contributions:" + contributions.size() + ", classifications:"
				+ classifications.size() + "]";
	}
	
	
	

}