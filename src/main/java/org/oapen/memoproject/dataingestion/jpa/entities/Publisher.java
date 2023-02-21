package org.oapen.memoproject.dataingestion.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.oxm.annotations.XmlPath;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity(name = "Publisher")
@Table(name = "publisher")
@XmlRootElement(name = "element")
@XmlAccessorType(XmlAccessType.FIELD)
public class Publisher {
	
	@Id
    @Column(name = "handle", updatable = false, insertable = true, unique = true)
	@XmlPath("field[@name='handle']/text()")
    private String handle;

    @Column(name = "name")
    @XmlPath("element[@name='publisher.name']/field/text()")
    private String name;

    @Column(name = "website")
    private String website;
    
    public Publisher() {}
    
	public Publisher(String handle, String name) {
		this.handle = handle;
		this.name = name;
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
		Publisher other = (Publisher) obj;
		if (handle == null) {
			if (other.handle != null)
				return false;
		} else if (!handle.equals(other.handle))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Publisher [handle=" + handle + ", name=" + name + "]";
	}
    
	
}
