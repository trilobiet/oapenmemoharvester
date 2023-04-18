package org.oapen.memoproject.dataingestion.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * This class represents records in the <em>identifier</em> table
 * 
 * @author acdhirr
 *
 */
@Getter @Setter
@Entity(name = "Identifier" )
@Table(name="identifier")
public class Identifier {
	
	@Id
    @Column(name="identifier", updatable = false, insertable = true)
	private String id;

    @Column(name="identifier_type", nullable = false)
	private String type;

    @Column(name = "handle_title", nullable = false)
	private String handleTitle;
    
	public Identifier() {}

    public Identifier(String id, String type) {
		this.id = id;
		this.type = type;
	}
    
	public boolean isComplete() {
		
		return (id != null && !id.isBlank() && type != null && !type.isBlank());
	}
    

	@Override
	public String toString() {
		return "Identifier [" + id + ", " + type + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}	