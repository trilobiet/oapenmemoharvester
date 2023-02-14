package org.oapen.memoproject.dataingestion.jpa.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity(name = "Contribution")
@Table(name = "Contribution")
@IdClass(ContributionId.class)
public class Contribution {
	
	/* Combined key of three fields
	 * See https://www.baeldung.com/jpa-composite-primary-keys */
	
	@Id @Column(name="name_contributor",nullable=false)
	public String nameContributor;
	@Id @Column(name="id_title",nullable=false)
	public String idTitle;
	@Id @Column(name="type",nullable=false)
	public String type;
	
}

class ContributionId implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public String nameContributor;
	public String idTitle;
	public String type;
	
	public ContributionId() {}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idTitle == null) ? 0 : idTitle.hashCode());
		result = prime * result + ((nameContributor == null) ? 0 : nameContributor.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		if (idTitle == null) {
			if (other.idTitle != null)
				return false;
		} else if (!idTitle.equals(other.idTitle))
			return false;
		if (nameContributor == null) {
			if (other.nameContributor != null)
				return false;
		} else if (!nameContributor.equals(other.nameContributor))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

}

