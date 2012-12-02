package com.ipeirotis.cl.runner.workflow.parent;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.amazonaws.services.simpleworkflow.flow.core.Promise;

@Component
public class LookupWorkflowImpl implements LookupWorkflow {
	LookupActivitiesClient client = new LookupActivitiesClientImpl();
	
	@Override
	public void doLookup() {
		Promise<Collection<Integer>> allQuestions = client.getAllQuestions();
		
		client.lookupQuestion(allQuestions);
		
		//client.submitIpViolation(ipViolations);
	}
}
