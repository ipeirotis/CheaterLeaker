package com.ipeirotis.cl.service;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodb.datamodeling.KeyPair;
import com.amazonaws.services.dynamodb.model.AttributeValue;
import com.ipeirotis.cl.model.Entity;

public abstract class AbstractRangeService<K extends Entity> {
	private Class<K> clazz;

	@SuppressWarnings("unchecked")
	public AbstractRangeService() {
		this.clazz = (Class<K>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	@Autowired
	DynamoDBMapper mapper;

	public Iterable<K> scanAll() {
		return mapper.scan(clazz, new DynamoDBScanExpression());
	}
	
	public Iterable<K> findAllByPrimaryKey(String id) {
		return mapper.query(clazz, new DynamoDBQueryExpression(new AttributeValue().withS(id)));
	}
	
	public K findByPrimaryKey(String hashKey, String rangeKey) {
		return mapper.load(clazz, hashKey, rangeKey);
	}
	
	public void save(K... objects) {
		mapper.batchSave((Object[]) objects);
	}


	public void delete(K... objsToDelete) {
		mapper.batchDelete((Object[]) objsToDelete);
	}
	
	@SuppressWarnings("unchecked")
	public Collection<K> findExisting(K... objects) {
		List<K> result = new ArrayList<K>();
		
		Map<Class<?>, List<KeyPair>> recordsToLookup = new HashMap<Class<?>, List<KeyPair>>();
		
		List<KeyPair> keyPairList = new ArrayList<KeyPair>();
		
		for (K obj : objects) {
			keyPairList.add(getKeyPairFor(obj));
		}
		
		recordsToLookup.put(clazz, keyPairList);

		for (List<?> x : mapper.batchLoad(recordsToLookup).values()) {
			result.addAll((Collection<? extends K>) x);
		}
		
		return result;
	}

	protected abstract KeyPair getKeyPairFor(K obj);
}
