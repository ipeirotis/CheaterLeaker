package com.ipeirotis.cl.model;

import java.util.Date;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.codehaus.jackson.node.ObjectNode;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMarshalling;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBTable;
import com.ipeirotis.cl.model.marshal.DateMarshaller;
import com.ipeirotis.cl.model.marshal.ObjectNodeMarshaller;

@DynamoDBTable(tableName = "cl-ip-violation")
public class IPViolation extends Entity implements Comparable<IPViolation> {
	private static final long serialVersionUID = -1260846175533151313L;

	String id;

	@DynamoDBHashKey(attributeName = "id")
	public String getId() {
		return id;
	}

	public void setId(String questionId) {
		this.id = questionId;
	}

	String url;

	@DynamoDBRangeKey(attributeName = "url")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	ObjectNode meta;

	@DynamoDBAttribute(attributeName = "meta")
	@DynamoDBMarshalling(marshallerClass = ObjectNodeMarshaller.class)
	public ObjectNode getMeta() {
		return meta;
	}

	public void setMeta(ObjectNode meta) {
		this.meta = meta;
	}

	Date created = new Date();

	@DynamoDBAttribute(attributeName = "created")
	@DynamoDBMarshalling(marshallerClass = DateMarshaller.class)
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Override
	public int compareTo(IPViolation o) {
		if (null == o)
			return 1;

		if (this == o)
			return 0;

		return new CompareToBuilder().append(this.id, o.id)
				.toComparison();
	}
}
