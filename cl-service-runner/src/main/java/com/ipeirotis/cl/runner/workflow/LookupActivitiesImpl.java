package com.ipeirotis.cl.runner.workflow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodb.AmazonDynamoDB;
import com.amazonaws.services.dynamodb.model.AttributeValue;
import com.amazonaws.services.dynamodb.model.Key;
import com.amazonaws.services.dynamodb.model.ScanRequest;
import com.amazonaws.services.dynamodb.model.ScanResult;
import com.amazonaws.services.simpleworkflow.flow.annotations.Activity;
import com.amazonaws.services.simpleworkflow.flow.annotations.ActivityRegistrationOptions;
import com.ipeirotis.cl.model.IPViolation;
import com.ipeirotis.cl.model.Question;
import com.ipeirotis.cl.service.IPViolationService;
import com.ipeirotis.cl.service.QuestionService;
import com.ipeirotis.cl.service.UpdateService;

@Component
public class LookupActivitiesImpl implements LookupActivities {
	@Inject
	QuestionService questionService;

	@Inject
	IPViolationService ipViolationService;

	@Inject
	UpdateService updateService;

	@Inject
	AmazonDynamoDB dynamoDb;

	@Override
	public Collection<Integer> getAllQuestions() {
		List<Integer> result = new ArrayList<Integer>();

		boolean done = false;
		Key startKey = null;
		
		do {
			ScanRequest scanRequest = new ScanRequest().withTableName("cl-question").withAttributesToGet(
					"id").withExclusiveStartKey(startKey);

			ScanResult scanResult = dynamoDb.scan(scanRequest);
			
			for (Map<String, AttributeValue> v : scanResult.getItems()) {
				result.add(Integer.valueOf(v.get("id").getS()));
			}
			
			startKey = scanResult.getLastEvaluatedKey();
			done = (null == startKey);
		} while (!done);

		return result;
	}

	@Override
	public Collection<IPViolation> lookupQuestion(Integer questionId) {
		Question q = questionService.findByPrimaryKey("" + questionId);
		
		try {
			Set<IPViolation> ipViolations = new TreeSet<IPViolation>(
					updateService.findIPViolations(q));

			Collection<IPViolation> existing = ipViolationService
					.findExisting(ipViolations
							.toArray(new IPViolation[ipViolations.size()]));

			Set<IPViolation> result = new TreeSet<IPViolation>(ipViolations);

			result.removeAll(existing);

			return result;
		} catch (Exception exc) {
			// TODO: Make sure to log errors later
			exc.printStackTrace();

			return Collections.emptySet();
		}
	}

	@Override
	public IPViolation submitIpViolation(IPViolation ipViolation) {
		ipViolationService.save(ipViolation);

		return ipViolation;
	}

	@Override
	@Activity(name = "collect-all-questions", version = "1.0")
	@ActivityRegistrationOptions(defaultTaskScheduleToStartTimeoutSeconds = 86400, defaultTaskStartToCloseTimeoutSeconds = 120)
	public void collectAllQuestions(Collection<Integer> questions) {
	}
}
