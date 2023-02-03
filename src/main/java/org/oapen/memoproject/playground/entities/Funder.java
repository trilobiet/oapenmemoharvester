package org.oapen.memoproject.playground.entities;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.oxm.annotations.XmlPath;

@XmlRootElement(name = "element")
@XmlAccessorType(XmlAccessType.FIELD)
public class Funder {
	
	@XmlPath("element[@name='grantor.name']/field/text()")
	String name;
	
	@XmlPath("field[@name='handle']/text()")
	String handle;
	
	@XmlPath("element[@name='grantor.acronym']/field/text()")
	List<String> acronym = new ArrayList<>();

	@Override
	public String toString() {
		return "Funder [name=" + name + ", handle=" + handle + ", acronym=" + acronym + "]";
	}
	
	
}
