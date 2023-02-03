package org.oapen.memoproject.dataingestion.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="title")
public class Title {
	
    @Id
    @Column(name = "id", updatable = false, insertable = true)
    private String ID;

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
    private String identifier;

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

    @Column(name = "oapen_relation_partofbook")
    private String partOfBook;

    @Column(name = "oapen_relation_ispublishedby")
    private String isPublishedBy;

    @Column(name = "oapen_seriesnumber")
    private String seriesNumber;

}