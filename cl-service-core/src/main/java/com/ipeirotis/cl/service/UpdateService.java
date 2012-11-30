package com.ipeirotis.cl.service;

import static org.apache.commons.lang3.StringUtils.join;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import net.htmlparser.jericho.Renderer;
import net.htmlparser.jericho.Source;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.stereotype.Component;

import com.ipeirotis.cl.model.IPViolation;
import com.ipeirotis.cl.model.Question;
import com.ipeirotis.cl.model.SearchQuery;

@Component
public class UpdateService {
	public static final Collection<String> SERVICES = Arrays.asList("Google",
			"CopyScape");

	@Inject
	HttpService httpService;
	
	@Inject
	IPViolationService ipViolationService;

	public List<IPViolation> findIPViolations(Question q) throws Exception {
		List<IPViolation> results = new ArrayList<IPViolation>();

		for (String service : SERVICES) {
			SearchQuery searchQuery = getSearchQueryFor(q, service);

			ObjectNode result = httpService.doQuery(searchQuery);

			for (JsonNode x : result.path("results")) {
				ObjectNode n = (ObjectNode) x;

				IPViolation ipViolation = new IPViolation();

				ipViolation.setId(q.getQuestionId());
				ipViolation.setUrl(n.path("url").getTextValue());
				ipViolation.setMeta(n);

				results.add(ipViolation);
			}
		}

		return results;
	}

	private SearchQuery getSearchQueryFor(Question q, String service) {
		SearchQuery searchQuery = new SearchQuery();

		searchQuery.setQueryService(service);
		searchQuery.setQueryText(fromRawHtml(q.getQuestionText() + "\n"
				+ join(q.getAnswers(), "\n")));
		
		return searchQuery;
	}

	private String fromRawHtml(String htmlSource) {
		try {
			StringWriter stringWriter = new StringWriter();

			new Renderer(new Source(htmlSource)).writeTo(stringWriter);

			return stringWriter.toString().replaceAll("\\n", " ")
					.replaceAll("\\s{2,}", " ");
		} catch (Exception exc) {
			throw new RuntimeException(exc);
		}
	}
}
