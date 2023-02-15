package org.oapen.memoproject.dataingestion.jpa.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder @NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Entity(name = "Funding")
@Table(name = "Funding")
@IdClass(FundingId.class)
public class Funding {
	
	/* Combined key of three fields
	 * See https://www.baeldung.com/jpa-composite-primary-keys */
	@Id @Column(name="handle_funder",nullable=false)
	private String handleFunder;
	
	@Id @Column(name="handle_title",nullable=false)
	private String handleTitle;
	
    @Column(name = "grant_number")
    private String grantNumber;

    @Column(name = "grant_program")
    private String grantProgram;
	
    @Column(name = "grant_project")
    private String grantProject;

    @Column(name = "grant_acronym")
    private String grantAcronym;
    

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((handleFunder == null) ? 0 : handleFunder.hashCode());
		result = prime * result + ((handleTitle == null) ? 0 : handleTitle.hashCode());
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
		Funding other = (Funding) obj;
		if (handleFunder == null) {
			if (other.handleFunder != null)
				return false;
		} else if (!handleFunder.equals(other.handleFunder))
			return false;
		if (handleTitle == null) {
			if (other.handleTitle != null)
				return false;
		} else if (!handleTitle.equals(other.handleTitle))
			return false;
		return true;
	}

}

class FundingId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public String handleTitle;
	public String handleFunder;
	
	public FundingId() {}

	public FundingId(String idTitle, String handleFunder) {
		super();
		this.handleTitle = idTitle;
		this.handleFunder = handleFunder;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((handleFunder == null) ? 0 : handleFunder.hashCode());
		result = prime * result + ((handleTitle == null) ? 0 : handleTitle.hashCode());
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
		FundingId other = (FundingId) obj;
		if (handleFunder == null) {
			if (other.handleFunder != null)
				return false;
		} else if (!handleFunder.equals(other.handleFunder))
			return false;
		if (handleTitle == null) {
			if (other.handleTitle != null)
				return false;
		} else if (!handleTitle.equals(other.handleTitle))
			return false;
		return true;
	}

}



