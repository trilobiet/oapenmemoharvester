package org.oapen.memoproject.dataingestion.entities;

import java.util.HashSet;
import java.util.List;
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
import javax.persistence.Table;

@Entity(name = "Title")
@Table(name="title")
public class Title {
	
    @Id
    @Column(name = "id", updatable = false, insertable = true)
    private String Id;

    @Column(name = "handle", updatable = false, insertable = true)
    private String handle;

    @Column(name = "collection_no")
    private String collection;

    @Column(name = "download_url")
    private String downloadUrl;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "license")
    private String license;

    @Column(name = "webshop_url")
    private String webshopUrl;

    @Column(name = "dc_date_available")
    private String dateAvailable;

    @Column(name = "dc_date_issued")
    private String dateIssued;

    @Column(name = "dc_description")
    private String description;

    @Column(name = "dc_description_abstract")
    private String descriptionAbstract;

    @Column(name = "dc_description_provenance")
    private String descriptionProvenance;

    @Column(name = "dc_identifier_issn")
    private String issn;

    @Column(name = "dc_relation_ispartofseries")
    private String isPartOfSeries;

    @Column(name = "dc_title")
    private String title;

    @Column(name = "dc_title_alternative")
    private String titleAlternative;

    @Column(name = "dc_type")
    private String type;

    @Column(name = "dc_terms_abstract")
    private String termsAbstract;

    @Column(name = "oapen_abstractotherlanguage")
    private String abstractOtherLanguage;

    @Column(name = "oapen_chapternumber")
    private String chapterNumber;

    @Column(name = "oapen_description_otherlanguage")
    private String descriptionOtherlanguage;

    @Column(name = "oapen_embargo")
    private String embargo;

    @Column(name = "oapen_identifier")
    private String identifierOapen;

    @Column(name = "oapen_identifier_doi")
    private String identifierDoi;

    @Column(name = "oapen_identifier_ocn")
    private String identifierOcn;

    @Column(name = "oapen_imprint")
    private String imprint;

    @Column(name = "oapen_pages")
    private String pages;

    @Column(name = "oapen_placepublication")
    private String placePublication;

    // no need for mapping here as long as the data is inserted we need no object reference
    @Column(name = "oapen_relation_partofbook") 
    private String partOfBook;

    @Column(name = "oapen_relation_ispublishedby")
    private String isPublishedBy;

    @Column(name = "oapen_seriesnumber")
    private String seriesNumber;
    
    @ElementCollection
    @CollectionTable(name="dc_identifier", joinColumns= @JoinColumn(name="id_title"))
    @Column(name = "identifier")
    private List<String> identifiers;

    @ElementCollection
    @CollectionTable(name="identifier_isbn", joinColumns= @JoinColumn(name="id_title"))
    @Column(name = "identifier")
    private List<String> identifiersISBN;

    // TODO make this a Date object?
    @ElementCollection
    @CollectionTable(name="dc_date_accessioned", joinColumns= @JoinColumn(name="id_title"))
    @Column(name = "date")
    private List<String> datesAccessioned;
    
    @ElementCollection
    @CollectionTable(name="dc_language", joinColumns= @JoinColumn(name="id_title"))
    @Column(name = "language")
    private List<String> languages;
    
    @ElementCollection
    @CollectionTable(name="dc_subject_other", joinColumns= @JoinColumn(name="id_title"))
    @Column(name = "subject")
    private List<String> subjectsOther;
    
    @ManyToOne
    @JoinColumn(name = "oapen_relation_isPublishedBy")
    private Publisher publisher;
    
    
    @ManyToMany(
    	fetch = FetchType.EAGER, // Eager, because there are only a few.	
    	cascade = {CascadeType.PERSIST,CascadeType.MERGE} 
    )
    @JoinTable(name = "dc_subject_classification",
        joinColumns = @JoinColumn(name = "id_title"),
        inverseJoinColumns = @JoinColumn(name = "id_classification")
    )
	private Set<Classification> classifications = new HashSet<>();
    
	public void addClassification(Classification classification) {
        classifications.add(classification);
        classification.titles.add(this);
    }
    

}