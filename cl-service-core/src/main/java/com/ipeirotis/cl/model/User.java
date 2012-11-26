package com.ipeirotis.cl.model;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="cl-user")
public class User extends Entity {
	private static final long serialVersionUID = 7672109625837675608L;
	
	String id;

	@DynamoDBHashKey
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
