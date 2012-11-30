package com.ipeirotis.cl.service;

import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodb.datamodeling.KeyPair;
import com.ipeirotis.cl.model.IPViolation;

@Component
public class IPViolationService extends AbstractRangeService<IPViolation> {
	@Override
	protected KeyPair getKeyPairFor(IPViolation obj) {
		return new KeyPair().withHashKey(obj.getId()).withRangeKey(obj.getUrl());
	}
}
