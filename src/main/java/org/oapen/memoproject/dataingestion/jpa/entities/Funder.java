package org.oapen.memoproject.dataingestion.jpa.entities;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.oxm.annotations.XmlPath;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity(name = "Funder")
@Table(name = "Funder")
@XmlRootElement(name = "element")
@XmlAccessorType(XmlAccessType.FIELD)
public class Funder {
	
	@Id
    @Column(name = "handle", updatable = false, insertable = true, unique = true)
	@XmlPath("field[@name='handle']/text()")
	private String handle;

    @Column(name = "name")
    @XmlPath("element[@name='grantor.name']/field/text()")
    private String name;

    @Transient // Setter for Xpath mapper (Non-persisted, the concatenated acronymsList is persisted)
    @XmlPath("element[@name='grantor.acronym']/field/text()")
    private Set<String> acronyms; 
    
    @Column(name = "acronyms")
    private String acronymsJoined;
    
    @Column(name = "number")
    @XmlPath("element[@name='grantor.number']/field/text()")
    private String number;  // a list
    
	public Funder() {}
	
	public Funder(String handle, String name) {
		this.handle = handle;
		this.name = name;
	}
	
	public void setAcronyms(Set<String> acronyms) {
		
		this.acronyms = acronyms;
		// Also set the concatenated persistence value (alphabetically)
		this.acronymsJoined = acronyms.stream().sorted().collect(Collectors.joining("||"));
	}
	
	public List<String> getAcronyms() {
		
		return Arrays.asList(acronymsJoined.split("\\|\\|"));
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
