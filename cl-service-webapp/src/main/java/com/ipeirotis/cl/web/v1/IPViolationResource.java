package com.ipeirotis.cl.web.v1;

import static org.apache.commons.lang.StringUtils.join;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodb.AmazonDynamoDB;
import com.amazonaws.services.dynamodb.model.AttributeValue;
import com.amazonaws.services.dynamodb.model.GetItemRequest;
import com.amazonaws.services.dynamodb.model.GetItemResult;
import com.amazonaws.services.dynamodb.model.Key;
import com.amazonaws.services.dynamodb.model.ScanRequest;
import com.amazonaws.services.dynamodb.model.ScanResult;
import com.ipeirotis.cl.service.IPViolationService;

@Component
@Path("/v1/ip-violation")
public class IPViolationResource extends BaseResource {
	@Inject
	AmazonDynamoDB dynamoDb;

	@Inject
	IPViolationService ipViolationService;

	@Inject
	ObjectMapper objectMapper;
	
	@Path("/{id : .+ }")
	@GET
	public ObjectNode findRecord(@PathParam("id") String id) throws Exception {
		GetItemResult item = dynamoDb.getItem(new GetItemRequest("cl-ip-violation", decodeKeyFor(id)));
		
		if (null == item)
			throw new WebApplicationException(404);
		
		return formatRecord(item.getItem());
	}

	@Path("/all{ from : (/.{1,})? }")
	@GET
	public ObjectNode findAll(@PathParam("from") String next) throws Exception {
		Key startKey = null;

		if (isNotBlank(next))
			startKey = decodeKeyFor(next.substring(1));

		ScanRequest scanRequest = new ScanRequest()
				.withTableName("cl-ip-violation").withLimit(20)
				.withExclusiveStartKey(startKey);

		ScanResult scanResult = dynamoDb.scan(scanRequest);

		ObjectNode rootNode = objectMapper.createObjectNode();
		ArrayNode resultsNode = objectMapper.createArrayNode();

		rootNode.put("items", resultsNode);

		if (null != scanResult.getLastEvaluatedKey()) {
			Key nextStartKey = scanResult.getLastEvaluatedKey();

			String id = nextStartKey.getHashKeyElement().getS();
			String url = nextStartKey.getRangeKeyElement().getS();

			rootNode.put("next", encodeKeyFor(id, url));
		}

		for (Map<String, AttributeValue> e : scanResult.getItems()) {
			ObjectNode record = formatRecord(e);

			resultsNode.add(record);
		}

		return rootNode;
	}

	private ObjectNode formatRecord(Map<String, AttributeValue> e)
			throws IOException, JsonProcessingException {
		ObjectNode record = objectMapper.createObjectNode();

		String q = e.get("id").getS();
		String url = e.get("url").getS();

		record.put("id", this.encodeKeyFor(q, url));

		record.put("q", q);
		record.put("url", url);
		record.put("created", e.get("created").getS());

		String strMeta = e.get("meta").getS();

		record.put("meta", objectMapper.readTree(strMeta));
		return record;
	}

	private Key decodeKeyFor(String key) {
		String srcNextPairs = new String(Base64.decodeBase64(key));
		String[] strStartKey = srcNextPairs.split(":", 2);

		return new Key(new AttributeValue().withS(strStartKey[0]),
				new AttributeValue().withS(strStartKey[1]));
	}

	private String encodeKeyFor(String id, String url) {
		String plainNextKey = join(new Object[] { id, url }, ':');
		String formattedKey = new String(Base64.encodeBase64(plainNextKey
				.getBytes()));
		return formattedKey;
	}
}
