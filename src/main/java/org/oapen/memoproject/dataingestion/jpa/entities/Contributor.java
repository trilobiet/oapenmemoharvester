package org.oapen.memoproject.dataingestion.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.lang.NonNull;

import lombok.Getter;
import lombok.Setter;

/**
 * This class represents records in the <em>contributor</em> table
 * 
 * @author acdhirr
 *
 */
@Getter @Setter
@Entity(name = "Contributor")
@Table(name = "Contributor")
public class Contributor {
	
	// orcid would be a better id, but it is not always available and will be added later
	@Id
    @Column(name = "name", updatable = false, insertable = true)
	@NonNull
    private String name;
	
    @Column(name = "orcid")
    private String orcid;

	public Contributor() {}
		
	public Contributor(String name) {
		this.name = name;
	}
	
	public boolean isComplete() {
		
		return (name != null && !name.isBlank());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		return true;
	}

	@Override
	public String toString() {
		return "Contributor [name=" + name + "]";
	}

}
