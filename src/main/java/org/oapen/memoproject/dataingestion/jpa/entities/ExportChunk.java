package org.oapen.memoproject.dataingestion.jpa.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.SQLInsert;
import org.springframework.lang.NonNull;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity(name = "ExportChunk")
@Table(name = "export_chunk")
@IdClass(ExportChunkId.class)
// handle_title may refer to titles already deleted, ignore the resulting integrity errors and just skip the insert
@SQLInsert(sql="INSERT IGNORE INTO export_chunk (type, handle_title, content) values (?,?,?)")
public class ExportChunk {
	
	@Id @Column	@NonNull
	private String type;

	@Id @Column	@NonNull
	private String handleTitle;

	@Column(name = "content")
    private String content;
	
	public ExportChunk() {}
	
	public ExportChunk(String type, String content) {
		this.type = type;
		this.content = content;
	}
	
	public ExportChunk(String type, String content, String handle) {
		this.type = type;
		this.content = content;
		this.handleTitle = handle;
	}

	public boolean isComplete() {
		
		return (type != null && !type.isBlank() && content != null && !content.isBlank());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((handleTitle == null) ? 0 : handleTitle.hashCode());
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
		ExportChunk other = (ExportChunk) obj;
		if (handleTitle == null) {
			if (other.handleTitle != null)
				return false;
		} else if (!handleTitle.equals(other.handleTitle))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ExportChunk [type=" + type + ", content=" + content + "]";
	}

}


class ExportChunkId implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public String type;
	public String handleTitle;
	
	public ExportChunkId() {}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((handleTitle == null) ? 0 : handleTitle.hashCode());
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
		if (handleTitle == null) {
			if (other.handleTitle != null)
				return false;
		} else if (!handleTitle.equals(other.handleTitle))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ExportChunkId [type=" + type + "]";
	}

	
	
}

