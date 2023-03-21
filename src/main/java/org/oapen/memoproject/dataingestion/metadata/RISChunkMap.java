package org.oapen.memoproject.dataingestion.metadata;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RISChunkMap implements ExportChunkMap {
	
	private final String sourceData; 
	private final Map<String, String> map = new HashMap<>();

	
	public RISChunkMap(String sourceData) {
		
		this.sourceData = sourceData;
		buildMap();
	}

	
	@Override
	public String getById(String id) {
		
		return map.get(id);
	}
	
	
	private void buildMap() {
		
		String[] segs = sourceData.split("\\n[ ]*\\n");
		Pattern pattern = Pattern.compile("\\nLK *- *[^\\n]*");
		
		for (int i=0; i < segs.length; i++) {
			
			String seg = segs[i];
			Matcher matcher = pattern.matcher(seg);

			if (matcher.find()) {
	            String id = parseId(matcher.group());
	            map.put(id, seg);
	        }
			
		}
	
	}
	
	
	private String parseId(String in) {
		
		// <- LK  - http://library.oapen.org/handle/20.500.12657/38110
		// -> 20.500.12657/38110
		String s = in.trim();
		if (s.length() >= 18) return s.substring(s.length()-18);
		else return s;
	}


}
