package org.oapen.memoproject.dataingestion.metadata;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A RIS Record representation
 * 
 * @author acdhirr
 *
 */
public class RISChunk implements ExportChunkable {
	
	private final String content; 
	private final Optional<String> handle;
	
	public RISChunk(String content) {
		
		this.content = content;
		handle = initHandle();
	}

	private Optional<String> initHandle() {
		
		Pattern pattern = Pattern.compile("\\nLK *- *[^\\n]*");
		
		Matcher matcher = pattern.matcher(content);

		if (matcher.find()) {
            return Optional.of( parseHandle(matcher.group()) );
        }
		else return Optional.empty();
			
	}
	
	private String parseHandle(String in) {
		
		// <- LK  - http://library.oapen.org/handle/20.500.12657/38110
		// -> 20.500.12657/38110
		String s = in.trim();
		if (s.length() >= 18) return s.substring(s.length()-18);
		else return s;
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
		return ExportType.RIS;
	}

	@Override
	public boolean isValid() {
		
		return handle.isPresent();
	}

}
