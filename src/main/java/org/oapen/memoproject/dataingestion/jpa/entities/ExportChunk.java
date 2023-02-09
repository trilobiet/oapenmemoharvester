package org.oapen.memoproject.dataingestion.jpa.entities;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

//@Getter @Setter
@Entity(name = "ExportChunk")
@Table(name = "export_chunk")
public class ExportChunk {
	
	@EmbeddedId
	private ExportChunkId id;
    
	@Column(name = "content")
    private String content;

	public ExportChunkId getId() {
		return id;
	}

	public void setId(ExportChunkId id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
    
}

