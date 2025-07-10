package org.oapen.memoproject.dataingestion.jpa.entities;

import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.oxm.annotations.XmlPath;
import org.springframework.lang.NonNull;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * This class represents records in the <em>funder</em> table
 * 
 * @author acdhirr
 *
 */
@Getter @Setter
@Entity(name = "Funder")
@Table(name = "Funder")
@XmlRootElement(name = "element")
@XmlAccessorType(XmlAccessType.FIELD)
public class Funder {
	
	@Id @NonNull
    @Column(name = "handle", updatable = false, insertable = true)
	@XmlPath("field[@name='handle']/text()")
	private String handle;

    @Column(name = "name")
    @XmlPath("element[@name='grantor.name']/field/text()")
    @Convert(converter = StringConverter.class)
    private String name;

    @Column(name = "acronyms")
    @XmlPath("element[@name='grantor.acronym']/field/text()")
    @Convert(converter = SetToStringConverter.class)
    private Set<String> acronyms;
    
    @Column(name = "number")
    @XmlPath("element[@name='grantor.number']/field/text()")
    private String number;  // a list
    
	public Funder() {}
	
	public Funder(String handle, String name) {
		this.handle = handle;
		this.name = name;
	}
	
	public boolean isComplete() {
		
		return (handle != null && !handle.isBlank() && name != null && !name.isBlank());
	}

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

	@Override
	public String toString() {
		return "Funder [handle=" + handle + ", name=" + name + "]";
	}

	
}
