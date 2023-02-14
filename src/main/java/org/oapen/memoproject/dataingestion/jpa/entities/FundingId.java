package org.oapen.memoproject.dataingestion.jpa.entities;

import java.io.Serializable;

public class FundingId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public String idTitle;
	public String handleFunder;
	
	public FundingId() {}

	public FundingId(String idTitle, String handleFunder) {
		super();
		this.idTitle = idTitle;
		this.handleFunder = handleFunder;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((handleFunder == null) ? 0 : handleFunder.hashCode());
		result = prime * result + ((idTitle == null) ? 0 : idTitle.hashCode());
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
		FundingId other = (FundingId) obj;
		if (handleFunder == null) {
			if (other.handleFunder != null)
				return false;
		} else if (!handleFunder.equals(other.handleFunder))
			return false;
		if (idTitle == null) {
			if (other.idTitle != null)
				return false;
		} else if (!idTitle.equals(other.idTitle))
			return false;
		return true;
	}

}


