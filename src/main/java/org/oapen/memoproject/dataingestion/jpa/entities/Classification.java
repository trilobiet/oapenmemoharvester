package org.oapen.memoproject.dataingestion.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.lang.NonNull;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity(name = "Classification")
@Table(name="classification")
public class Classification {
	
	@Id
    @Column(name = "code", updatable = false, insertable = true)
	@NonNull
    private String code;

    @Column(name = "description")
    @NonNull
    private String description;
    
	public Classification() {}

	public Classification(String code, String description) {
		this.code = code;
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
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
		Classification other = (Classification) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Classification [" + code + ": " + description + "]";
	}

	
	
}
