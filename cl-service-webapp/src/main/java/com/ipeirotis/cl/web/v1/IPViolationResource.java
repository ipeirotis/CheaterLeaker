package com.ipeirotis.cl.web.v1;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodb.AmazonDynamoDB;
import com.ipeirotis.cl.service.IPViolationService;

@Component
@Path("/v1/ipviolation")
public class IPViolationResource extends BaseResource {
	@Inject
	AmazonDynamoDB dynamoDb;

	@Inject
	IPViolationService ipVionlationService;

	@Inject
	ObjectMapper objectMapper;

	@Path("/all{ from : (/\\d+)? }")
	@GET
	public ArrayNode findAll(@PathParam("from") String startKey) {
//		ScanRequest scanRequest = new ScanRequest()
//				.withTableName("cl-question")
//				.withAttributesToGet(Arrays.asList("id", "testTopic"))
//				.withLimit(20);
//
//		if (isNotBlank(startKey))
//			scanRequest.withExclusiveStartKey(new Key(new AttributeValue()
//					.withS(startKey)));
//
//		ScanResult scanResult = dynamoDb.scan(scanRequest);

		ArrayNode rootNode = objectMapper.createArrayNode();

//		for (Map<String, AttributeValue> e : scanResult.getItems()) {
//			ObjectNode record = objectMapper.createObjectNode();
//			
//			record.put("questionId", e.get("id").getS());
//			record.put("testTopic", e.get("testTopic").getS());
//			
//			rootNode.add(record);
//		}
		
		return rootNode;
	}
}
