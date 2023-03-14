package org.oapen.memoproject.dataingestion.jpa.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.springframework.lang.NonNull;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity(name = "Contribution")
@Table(name = "Contribution")
@IdClass(ContributionId.class)
public class Contribution {
	
	/* Combined key of three fields
	 * See https://www.baeldung.com/jpa-composite-primary-keys */
	
	@Id @Column(name="name_contributor")
	@NonNull
	public String contributorName;
	
	@Id @Column(name="handle_title")
	@NonNull
	public String handleTitle;
	
	@Id @Column(name="role")
	@NonNull
	public String role;
	
	public Contribution() {}

	public Contribution(String name, String role) {

		this.contributorName = name;
		this.role = role;
	}
	
	public boolean isComplete() {
		
		return (contributorName != null && !contributorName.isBlank() && role != null && !role.isBlank());
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((handleTitle == null) ? 0 : handleTitle.hashCode());
		result = prime * result + ((contributorName == null) ? 0 : contributorName.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
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
		Contribution other = (Contribution) obj;
		if (handleTitle == null) {
			if (other.handleTitle != null)
				return false;
		} else if (!handleTitle.equals(other.handleTitle))
			return false;
		if (contributorName == null) {
			if (other.contributorName != null)
				return false;
		} else if (!contributorName.equals(other.contributorName))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Contribution [contributorName=" + contributorName + ", role=" + role + "]";
	}
	
	
	
}

class ContributionId implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public String contributorName;
	public String handleTitle;
	public String role;
	
	public ContributionId() {}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((handleTitle == null) ? 0 : handleTitle.hashCode());
		result = prime * result + ((contributorName == null) ? 0 : contributorName.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
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
		ContributionId other = (ContributionId) obj;
		if (handleTitle == null) {
			if (other.handleTitle != null)
				return false;
		} else if (!handleTitle.equals(other.handleTitle))
			return false;
		if (contributorName == null) {
			if (other.contributorName != null)
				return false;
		} else if (!contributorName.equals(other.contributorName))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		return true;
	}

}

