package com.ipeirotis.cl.search;

import org.apache.http.HttpEntity;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.stereotype.Component;

import com.ipeirotis.cl.model.SearchQuery;

@Component
public class GoogleSearchFormatter extends SearchFormatter {
	@Override
	public ObjectNode getFormattedSearch(SearchQuery searchQuery,
			HttpEntity httpEntity) throws Exception {
		ObjectNode inputObjectNode = (ObjectNode) objectMapper.readTree(httpEntity.getContent());

		ObjectNode rootNode = objectMapper.createObjectNode();
		
		ArrayNode resultsNode = objectMapper.createArrayNode();

		rootNode.put("results", resultsNode);
		
		int i = 1;
		
		for (JsonNode t : inputObjectNode.path("items")) {
			ObjectNode e = (ObjectNode) t;
			
			ObjectNode nextResultNode = objectMapper.createObjectNode();
			
			nextResultNode.put("index", i++);
			nextResultNode.put("url", e.path("link").getTextValue());
			nextResultNode.put("title", e.path("title").getTextValue());
			nextResultNode.put("snippet", e.path("snippet").getTextValue());
			nextResultNode.put("meta", t);
			
			resultsNode.add(nextResultNode);
		}

		return rootNode;
	}
}
