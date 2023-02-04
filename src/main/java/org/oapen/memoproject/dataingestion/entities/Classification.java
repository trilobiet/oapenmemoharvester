package org.oapen.memoproject.dataingestion.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity(name = "Classification")
@Table(name="classification")
public class Classification {
	
    @Id // Varchar(7)
    @Column(name = "id", updatable = false, insertable = true)
    private String Id;

    @Column(name = "description")
    private String description;
    
	@ManyToMany(mappedBy = "classifications")
	public Set<Title> titles = new HashSet<>();


}
