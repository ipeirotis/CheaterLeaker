package com.ipeirotis.cl.runner.workflow.parent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodb.AmazonDynamoDB;
import com.amazonaws.services.dynamodb.model.AttributeValue;
import com.amazonaws.services.dynamodb.model.Key;
import com.amazonaws.services.dynamodb.model.ScanRequest;
import com.amazonaws.services.dynamodb.model.ScanResult;
import com.amazonaws.services.simpleworkflow.flow.ActivityExecutionContext;
import com.amazonaws.services.simpleworkflow.flow.ActivityExecutionContextProvider;
import com.amazonaws.services.simpleworkflow.flow.ActivityExecutionContextProviderImpl;
import com.amazonaws.services.simpleworkflow.flow.StartWorkflowOptions;
import com.amazonaws.services.simpleworkflow.flow.annotations.Activity;
import com.amazonaws.services.simpleworkflow.flow.annotations.ActivityRegistrationOptions;
import com.ipeirotis.cl.model.IPViolation;
import com.ipeirotis.cl.runner.workflow.child.ChildLookupWorkflowClientExternal;
import com.ipeirotis.cl.runner.workflow.child.ChildLookupWorkflowClientExternalFactory;

@Component
public class LookupActivitiesImpl implements LookupActivities {
	@Inject
	ChildLookupWorkflowClientExternalFactory childLookupFactory;

	@Inject
	AmazonDynamoDB dynamoDb;

	ActivityExecutionContextProvider provider = new ActivityExecutionContextProviderImpl();

	boolean filterFirstRecords = true;

	@Override
	public Collection<Integer> getAllQuestions() {
		List<Integer> result = new ArrayList<Integer>();

		boolean done = false;
		Key startKey = null;

		do {
			ScanRequest scanRequest = new ScanRequest()
					.withTableName("cl-question").withAttributesToGet("id")
					.withExclusiveStartKey(startKey);

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
	public Collection<IPViolation> lookupQuestion(
			Collection<Integer> questionIds) {
		ActivityExecutionContext executionContext = provider
				.getActivityExecutionContext();

		if (filterFirstRecords) {
			List<Integer> qIdList = new ArrayList<Integer>(questionIds);

			if (qIdList.size() > 50)
				qIdList = qIdList.subList(0, 50);

			questionIds = qIdList;
		}

		for (Integer questionId : questionIds) {
			StartWorkflowOptions startWorkflowOptions = new StartWorkflowOptions();

			startWorkflowOptions.setTagList(Arrays.asList("q:" + questionId,
					"p:"
							+ executionContext.getWorkflowExecution()
									.getWorkflowId()));

			ChildLookupWorkflowClientExternal childLookup = childLookupFactory
					.getClient();

			childLookup.executeQuestionLookup("" + questionId,
					startWorkflowOptions);
		}

		return null;
	}

	@Override
	@Activity(name = "collect-all-questions", version = "1.0")
	@ActivityRegistrationOptions(defaultTaskScheduleToStartTimeoutSeconds = 86400, defaultTaskStartToCloseTimeoutSeconds = 120)
	public void collectAllQuestions(Collection<Integer> questions) {
	}

	@Override
	@Activity(name = "submit-ip-violation", version = "1.0")
	@ActivityRegistrationOptions(defaultTaskScheduleToStartTimeoutSeconds = 600, defaultTaskStartToCloseTimeoutSeconds = 60)
	public Collection<IPViolation> submitIpViolation(
			Collection<IPViolation> ipViolations) {
		return null;
	}
}
