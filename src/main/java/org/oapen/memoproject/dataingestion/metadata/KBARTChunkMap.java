package org.oapen.memoproject.dataingestion.metadata;

import java.util.HashMap;
import java.util.Map;

public class KBARTChunkMap implements ExportChunkMap {
	
	private final String sourceData; 
	private final Map<String, String> map = new HashMap<>();

	
	public KBARTChunkMap(String sourceData) {
		
		this.sourceData = sourceData;
		buildMap();
	}

	
	@Override
	public String getById(String id) {
		
		return map.get(id);
	}
	
	
	private void buildMap() {
		
		String[] segs = sourceData.split("\\n");
		
		// first row contains column names
		if (segs.length > 1) for (int i = 1; i < segs.length; i++) {
			
			String seg = segs[i];
			String[] cols = seg.split("\t");
			
			if (cols.length > 11) {
				String id = cols[11].trim();
				map.put(id, seg);
			}
		}
	
	}
	

}
