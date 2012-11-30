package com.ipeirotis.cl.web.v1;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Arrays;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodb.AmazonDynamoDB;
import com.amazonaws.services.dynamodb.model.AttributeValue;
import com.amazonaws.services.dynamodb.model.Key;
import com.amazonaws.services.dynamodb.model.ScanRequest;
import com.amazonaws.services.dynamodb.model.ScanResult;
import com.ipeirotis.cl.model.Question;
import com.ipeirotis.cl.service.QuestionService;

@Component
@Path("/v1/question")
public class QuestionResource extends BaseResource {
	@Inject
	AmazonDynamoDB dynamoDb;

	@Inject
	QuestionService questionService;

	@Inject
	ObjectMapper objectMapper;

	@Path("/all{ from : (/\\d+)? }")
	@GET
	public ArrayNode findAll(@PathParam("from") String startKey) {
		ScanRequest scanRequest = new ScanRequest()
				.withTableName("cl-question")
				.withAttributesToGet(Arrays.asList("id", "testTopic"))
				.withLimit(20);

		if (isNotBlank(startKey))
			scanRequest.withExclusiveStartKey(new Key(new AttributeValue()
					.withS(startKey)));

		ScanResult scanResult = dynamoDb.scan(scanRequest);

		ArrayNode rootNode = objectMapper.createArrayNode();

		for (Map<String, AttributeValue> e : scanResult.getItems()) {
			ObjectNode record = objectMapper.createObjectNode();
			
			record.put("questionId", e.get("id").getS());
			record.put("testTopic", e.get("testTopic").getS());
			
			rootNode.add(record);
		}
		
		return rootNode;
	}

	@GET
	@Path("/{id : \\w+}")
	public Question findById(@PathParam("id") String id) {
		return questionService.findByPrimaryKey(id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id : \\w+}")
	public Question submitNew(@PathParam("id") String id, Question q) {
		q.setQuestionId(id);

		questionService.save(q);

		return q;
	}
}
