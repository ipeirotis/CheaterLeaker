package com.ipeirotis.cl.model;

import java.io.Serializable;

public class SearchQuery implements Serializable {
	private static final long serialVersionUID = 9190043054660435049L;

	String queryText;

	public String getQueryText() {
		return queryText;
	}

	public void setQueryText(String queryText) {
		this.queryText = queryText;
	}

	String queryService;

	public String getQueryService() {
		return queryService;
	}

	public void setQueryService(String queryService) {
		this.queryService = queryService;
	}
}
