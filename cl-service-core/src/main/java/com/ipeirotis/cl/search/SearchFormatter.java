package com.ipeirotis.cl.search;

import org.apache.http.HttpEntity;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;

import com.ipeirotis.cl.model.SearchQuery;

public abstract class SearchFormatter {
	@Autowired
	protected ObjectMapper objectMapper;
	
	public abstract ObjectNode getFormattedSearch(SearchQuery searchQuery, HttpEntity httpEntity) throws Exception ;
}
