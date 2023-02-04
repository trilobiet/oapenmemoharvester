package org.oapen.memoproject.dataingestion.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity(name = "ExportChunk")
@Table(name="export_chunk")
@IdClass(ExportChunkId.class)
public class ExportChunk {
	
	/* Combined key of three fields
	 * See https://www.baeldung.com/jpa-composite-primary-keys */
    @Id @Column(name="type")
	public String type;
    @Id @Column(name="id_title")
	public String idTitle;

    @Column(name = "content")
    private String content;

}

class ExportChunkId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public String type;
	public String idTitle;
	
	public ExportChunkId() {}
	
	public ExportChunkId(String type, String idTitle) {
		super();
		this.type = type;
		this.idTitle = idTitle;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idTitle == null) ? 0 : idTitle.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		ExportChunkId other = (ExportChunkId) obj;
		if (idTitle == null) {
			if (other.idTitle != null)
				return false;
		} else if (!idTitle.equals(other.idTitle))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
}