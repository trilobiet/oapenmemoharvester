package org.oapen.memoproject.dataingestion.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity(name = "Contributor")
@Table(name = "Contributor")
public class Contributor {
	
	// orcid would be a better id, but it is not always available and will be added later
	@Id
    @Column(name = "handle", updatable = false, insertable = true, unique = true)
    private String name;
	
    @Column(name = "name")
    private String orcid;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((orcid == null) ? 0 : orcid.hashCode());
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
		Contributor other = (Contributor) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (orcid == null) {
			if (other.orcid != null)
				return false;
		} else if (!orcid.equals(other.orcid))
			return false;
		return true;
	}
    
}
