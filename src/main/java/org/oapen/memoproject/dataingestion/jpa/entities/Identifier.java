package org.oapen.memoproject.dataingestion.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name="identifier")
public class Identifier {
	
	@Id
    @Column(name="identifier", nullable=false)
	public String date;

    @Column(name="identifier_type", nullable=true)
	public String identifier_type;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((identifier_type == null) ? 0 : identifier_type.hashCode());
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
		Identifier other = (Identifier) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (identifier_type == null) {
			if (other.identifier_type != null)
				return false;
		} else if (!identifier_type.equals(other.identifier_type))
			return false;
		return true;
	}
}	