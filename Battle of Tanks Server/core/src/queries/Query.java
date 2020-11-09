package queries;

import java.io.Serializable;

public class Query implements Serializable {
	private static final long serialVersionUID = 1L;
	private String query;
	
	public Query (String query) {
		this.query = query;
	}
	
	public String getQuery () {
		return query;
	}
}