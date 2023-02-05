package org.oapen.memoproject.dataingestion.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name = "Publisher")
@Table(name = "publisher")
public class Publisher {
	
    @Id
    @Column(name = "id", updatable = false, insertable = true)
    private String Id;

    @Column(name = "name")
    private String name;

    @Column(name = "website")
    private String website;
    
    @OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Title> titles;
}
