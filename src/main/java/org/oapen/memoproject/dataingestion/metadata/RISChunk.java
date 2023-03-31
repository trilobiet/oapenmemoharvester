package org.oapen.memoproject.dataingestion.metadata;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RISChunk implements ExportChunk {
	
	private final String chunk; 
	private final Optional<String> handle;
	private final Optional<String> id;
	
	public RISChunk(String chunk) {
		
		this.chunk = chunk;
		handle = initHandle();
		id = Optional.empty();
	}

	private Optional<String> initHandle() {
		
		Pattern pattern = Pattern.compile("\\nLK *- *[^\\n]*");
		
		Matcher matcher = pattern.matcher(chunk);

		if (matcher.find()) {
            return Optional.of( parseId(matcher.group()) );
        }
		else return Optional.empty();
			
	}
	
	private String parseId(String in) {
		
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
	public Optional<String> getId() {
		return id;
	}

}
