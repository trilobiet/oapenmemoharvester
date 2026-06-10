package org.oapen.memoproject.dataingestion.jpa.entities;

import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.oxm.annotations.XmlPath;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@Entity(name = "PeerReview")
@Table(name = "peerreview")
@XmlRootElement(name = "element")
@XmlAccessorType(XmlAccessType.FIELD)
public class PeerReview {
	
	/*
	 * Look for element root.peerreview, 
	 *              NOT root.oapen.peerreview
	 */
	@Id
	@Column(name = "id")
	@XmlPath("element[@name='id']/element/field[@name='value']/text()")
	private String id;
	
	@Column(name = "title")
	@XmlPath("element[@name='title']/element/field/text()")
	private String title;
	
	@Column(name = "anonymity")
	@XmlPath("element[@name='anonymity']/element/field/text()")
	private String anonymity;
	
	@Column(name = "stage")
	@XmlPath("element[@name='review']//element[@name='stage']/element/field/text()")
	private String stage;
	
	@Column(name = "comments")
	@XmlPath("element[@name='comments']/element/field/text()")
	private String comments;
	
	@Column(name = "is_open_review")
	@XmlPath("element[@name='open']/element[@name='review']/element/field/text()")
	@Convert(converter = StringToBooleanConverter.class)
	private String isOpenReview;
	
	// Sets are stored in a single (delimited) field
	
	@Column(name = "responsibilities")
	@XmlPath("element[@name='publish']/element[@name='responsibility']/element/field/text()")
    @Convert(converter = SetToStringConverter.class)
	private Set<String> responsibilities;
	
	@Column(name = "type")
	@XmlPath("element[@name='review']/element[@name='type']/element/field/text()")
    @Convert(converter = SetToStringConverter.class)
	private Set<String> types;
	
	@Column(name = "reviewer_type")
	@XmlPath("element[@name='reviewer']/element[@name='type']/element/field/text()")
    @Convert(converter = SetToStringConverter.class)
	private Set<String> reviewerTypes;
	

	public boolean isComplete() {
		
		return (id != null && !id.isBlank());
	}
	
}

