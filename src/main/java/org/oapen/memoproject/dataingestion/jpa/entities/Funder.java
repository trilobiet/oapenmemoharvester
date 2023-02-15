package org.oapen.memoproject.dataingestion.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity(name = "Funder")
@Table(name = "Funder")
public class Funder {
	
	@Id
    @Column(name = "handle", updatable = false, insertable = true, unique = true)
    private String handle;
	
    @Column(name = "name")
    private String name;

    @Column(name = "acronyms")
    private String acronyms;  // a list

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((handle == null) ? 0 : handle.hashCode());
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
		Funder other = (Funder) obj;
		if (handle == null) {
			if (other.handle != null)
				return false;
		} else if (!handle.equals(other.handle))
			return false;
		return true;
	}
    
}
