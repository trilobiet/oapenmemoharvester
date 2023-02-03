package org.oapen.memoproject.playground.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.oxm.annotations.XmlPath;

@XmlRootElement(name = "element")
@XmlAccessorType(XmlAccessType.FIELD)
public class Author {
	
	@XmlPath("field[@name='value']/text()")
	public String fullName;

	@XmlPath("field[@name='whatever']/text()")
	public String whatever;
	
	@XmlPath("field[@name='authority']/text()")
	public String authority;
	
	@XmlPath("field[@name='confidence']/text()")
	public String confidence;


	@Override
	public String toString() {
		return "Author3 [fullName=" + fullName + ", whatever=" + whatever + ", authority=" + authority + ", confidence="
				+ confidence + "]";
	}
}
