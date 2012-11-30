package com.ipeirotis.cl.search;

import java.io.ByteArrayInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.springframework.stereotype.Component;

import com.ipeirotis.cl.model.SearchQuery;

@Component
public class CopyScapeSearchFormatter extends SearchFormatter {
	@Override
	public ObjectNode getFormattedSearch(SearchQuery searchQuery,
			HttpEntity httpEntity) throws Exception {
		ObjectNode rootNode = objectMapper.createObjectNode();

		ArrayNode resultsNode = objectMapper.createArrayNode();

		rootNode.put("results", resultsNode);

		Document doc = new SAXBuilder().build(new ByteArrayInputStream(IOUtils
				.toByteArray(httpEntity.getContent())));
		
		String querywords = doc.getRootElement().getChildText("querywords");

		for (Element e : doc.getRootElement().getChildren("result")) {
			ObjectNode nextResultNode = objectMapper.createObjectNode();
			ObjectNode metaNode = objectMapper.createObjectNode();
			
			metaNode.put("minwordsmatched", e.getChildText("minwordsmatched"));
			metaNode.put("samplewords", querywords);

			nextResultNode.put("index", e.getChildText("index"));
			nextResultNode.put("url", e.getChildText("url"));
			nextResultNode.put("title", e.getChildText("title"));
			nextResultNode.put("snippet", e.getChildText("textsnippet"));
			
			nextResultNode.put("meta", metaNode);

			resultsNode.add(nextResultNode);
		}

		return rootNode;
	}
}
