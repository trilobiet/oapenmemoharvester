package org.oapen.memoproject.dataingestion.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity(name = "Funding")
@Table(name = "Funding")
@IdClass(FundingId.class)
public class Funding {
	
	// @EmbeddedId
	// private FundingId id = new FundingId();
	
	/* Combined key of three fields
	 * See https://www.baeldung.com/jpa-composite-primary-keys */
	@Id @Column(name="handle_funder",nullable=false)
	private String handleFunder;
	@Id @Column(name="id_title",nullable=false)
	private String idTitle;
	
	

	/*
	@Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")    
	@Column(name = "id", updatable = false, insertable = true, unique = true)
    private String id;
    */
	
	
    @Column(name = "grant_number")
    private String grantNumber;

    @Column(name = "grant_program")
    private String grantProgram;
	
    @Column(name = "grant_project")
    private String grantProject;

    @Column(name = "grant_acronym")
    private String grantAcronym;
	
}

