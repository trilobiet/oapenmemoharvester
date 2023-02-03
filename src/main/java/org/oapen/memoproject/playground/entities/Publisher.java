package org.oapen.memoproject.playground.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.oxm.annotations.XmlPath;

@XmlRootElement(name = "element")
@XmlAccessorType(XmlAccessType.FIELD)
public class Publisher {
	
	@XmlPath("field[@name='handle']/text()")
	String handle;
	
	@XmlPath("element[@name='publisher.name']/field/text()")
	String name;
	
	@Override
	public String toString() {
		return "Publisher [name=" + name + ", handle=" + handle + "]";
	}

	
	
}
