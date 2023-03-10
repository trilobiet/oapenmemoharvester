package org.oapen.memoproject.dataingestion.harvest;


/**
 * An OAI ResumptionToken 
 * 
 * @author acdhirr
 *
 */
class ResumptionToken {
	
	public final String token;
	public final Integer listSize;
	public final Integer cursor;
	
	public ResumptionToken(String token, int listSize, int cursor) {
		this.token = token.trim();
		this.listSize = listSize;
		this.cursor = cursor;
	}

	
	public ResumptionToken(String token, String listSize, String cursor) {
		this.token = token.trim();
		this.listSize = parseIntOrNull(listSize);
		this.cursor = parseIntOrNull(cursor);
	}
	

	@Override
	public String toString() {
		return "ResumptionToken [token=" + token + ", listSize=" + listSize + ", cursor=" + cursor + "]";
	}
	
	static Integer parseIntOrNull(String s) {
		
		try {
			return Integer.parseInt(s);
		}
		catch (NumberFormatException e) {
			return null;
		}
		
	}
	
}
