package com.ipeirotis.cl.runner.workflow.child;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.ipeirotis.cl.model.IPViolationRef;

@Component
public class ChildLookupWorkflowImpl implements ChildLookupWorkflow {
	ChildLookupActivitiesClient client = new ChildLookupActivitiesClientImpl();

	@Override
	public void executeQuestionLookup(String questionId) {
		Promise<Collection<IPViolationRef>> collectedNewIpViolations = client.collectNewIpViolations(questionId);
		
		client.submitNewIpViolations(collectedNewIpViolations);
	}

}
