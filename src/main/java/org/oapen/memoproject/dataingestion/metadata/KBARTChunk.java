package org.oapen.memoproject.dataingestion.metadata;

import java.util.Optional;

/**
 * A KBART (.tsv) type ExportChunkable.
 * This type relies on the handle to be in the 11th column. This may be a bit risky
 * when the data definition changes.
 * 
 * @author acdhirr
 */
public class KBARTChunk implements ExportChunkable {
	
	private final String content; 
	private final Optional<String> handle;
	
	public KBARTChunk(String content) {
		
		this.content = content;
		handle = initHandle();
	}
	
	
	private Optional<String> initHandle() {
		
		String[] cols = content.split("\t");
		
		if (cols.length > 11) {
			// 11th column contains handle (in quotes)
			return Optional.of(cols[11].trim().replaceAll("\"", ""));
		}
		else return Optional.empty();
	}


	@Override
	public Optional<String> getHandle() {
		return handle;
	}

	@Override
	public String getContent() {
		return content;
	}

	@Override
	public ExportType getType() {
		return ExportType.KBART;
	}

	@Override
	public boolean isValid() {
		
		return handle.isPresent();
	}

}
