package com.ipeirotis.cl.service;

import java.lang.reflect.ParameterizedType;

import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBScanExpression;
import com.ipeirotis.cl.model.Entity;

public abstract class AbstractHashService<K extends Entity> {
	private Class<K> clazz;

	@SuppressWarnings("unchecked")
	public AbstractHashService() {
		this.clazz = (Class<K>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	@Autowired
	DynamoDBMapper mapper;

	public Iterable<K> scanAll() {
		return mapper.scan(clazz, new DynamoDBScanExpression());
	}

	public K findByPrimaryKey(String id) {
		return mapper.load(clazz, id);
	}
	
	public void save(K... objects) {
		mapper.batchSave((Object[]) objects);
	}


	public void delete(K... objsToDelete) {
		mapper.batchDelete((Object[]) objsToDelete);
	}
}
