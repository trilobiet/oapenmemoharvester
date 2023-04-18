package org.oapen.memoproject.dataingestion.jpa.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;


/**
 * This class represents records in the <em>grant</em> table
 * each record has a field 'property' and a field 'value' containing the corresponding property value.
 * <p>
 * properties can be (but are technically not limited to) PROGRAM, PROJECT, NUMBER or ACRONYM 
 *  
 * @author acdhirr
 *
 */
@Getter @Setter
@Entity(name = "Grant")
@Table(name = "grant_data")
@IdClass(GrantId.class)
public class GrantData {
	
	// PROGRAM, PROJECT, NUMBER, ACRONYM
	@Id @Column(name = "property")
    private String property;

	@Id @Column(name = "value")
    private String value;

	@Id @Column(name="handle_title") 
	private String handleTitle;
	
	public GrantData() {}
	
	public GrantData(String property, String value) {
		this.property = property;
		this.value = value;
	}
	
	/**
	 * @return True, if both property and value are given.
	 */
	public boolean isComplete() {
		
		return (property != null && !property.isBlank() && value != null && !value.isBlank());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((handleTitle == null) ? 0 : handleTitle.hashCode());
		result = prime * result + ((property == null) ? 0 : property.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		GrantData other = (GrantData) obj;
		if (handleTitle == null) {
			if (other.handleTitle != null)
				return false;
		} else if (!handleTitle.equals(other.handleTitle))
			return false;
		if (property == null) {
			if (other.property != null)
				return false;
		} else if (!property.equals(other.property))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GrantData [property=" + property + ", value=" + value + "]";
	}
	
}

class GrantId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public String property;
	public String value;
	public String handleTitle;
	
	public GrantId() {}

	public GrantId(String property, String value) {
		this.property = property;
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((handleTitle == null) ? 0 : handleTitle.hashCode());
		result = prime * result + ((property == null) ? 0 : property.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		GrantId other = (GrantId) obj;
		if (handleTitle == null) {
			if (other.handleTitle != null)
				return false;
		} else if (!handleTitle.equals(other.handleTitle))
			return false;
		if (property == null) {
			if (other.property != null)
				return false;
		} else if (!property.equals(other.property))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}



