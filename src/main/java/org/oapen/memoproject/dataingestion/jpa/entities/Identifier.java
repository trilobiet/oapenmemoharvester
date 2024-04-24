package org.oapen.memoproject.dataingestion.jpa.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
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
@IdClass(IdentifierId.class)
@EqualsAndHashCode
public class Identifier {
	
	@Id @Column(name="identifier")
	//@Column(name="identifier", updatable = false, insertable = true)
	private String id;

    @Id @Column(name="identifier_type", nullable = false)
	private String type;

    @Id @Column(name = "handle_title", nullable = false)
	private String handleTitle;
    
	public Identifier() {}

    public Identifier(String id, String type) {
		this.id = id;
		this.type = type;
	}
    
    public Identifier(String id, String type, String handleTitle) {
		this.id = id;
		this.type = type;
		this.handleTitle = handleTitle;
	}

    public boolean isComplete() {
		
		return (id != null && !id.isBlank() && type != null && !type.isBlank());
	}

	@Override
	public String toString() {
		return "Identifier [" + id + ", " + type + "]";
	}
}

@EqualsAndHashCode
class IdentifierId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public String id;
	public String type;
	public String handleTitle;
	
	public IdentifierId() {}
	
	/*public IdentifierId(String id, String type) {

		this.id = id;
		//this.type =type;
	}*/
}	

