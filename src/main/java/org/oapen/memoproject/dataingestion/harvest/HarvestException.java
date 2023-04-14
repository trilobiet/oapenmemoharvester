package org.oapen.memoproject.dataingestion.harvest;

import java.util.Optional;

public class HarvestException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	private final Optional<ResumptionToken> resumptionToken;

	public HarvestException(Exception e) {
		
		super(e);
		this.resumptionToken = Optional.empty();
	}

	public HarvestException(Exception e, Optional<ResumptionToken> rst) {
		
		super(e);
		this.resumptionToken = rst;
	}
	
	public Optional<ResumptionToken> resumptionToken() {
		return resumptionToken;
	}
	
	@Override
	public String getMessage() {
		
		String message = super.getMessage();
		if (resumptionToken.isPresent()) message += " === resumptionToken:" + resumptionToken.get().token;
		
		return message;
	}
	
}
