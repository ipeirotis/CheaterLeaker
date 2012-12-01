package com.ipeirotis.cl.runner.workflow;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.amazonaws.services.simpleworkflow.flow.annotations.Asynchronous;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.ipeirotis.cl.model.IPViolation;

@Component
public class LookupWorkflowImpl implements LookupWorkflow {
	LookupActivitiesClient client = new LookupActivitiesClientImpl();
	
	@Override
	public void doLookup() {
		Promise<Collection<Integer>> allQuestions = client.getAllQuestions();
		
		submitQuestions(allQuestions);
	}

	@Asynchronous
	public void submitQuestions(Promise<Collection<Integer>> allQuestions) {
		for (Integer i : allQuestions.get()) {
			Promise<Collection<IPViolation>> ipViolations = client.lookupQuestion(i, allQuestions);
			
			if (!ipViolations.isReady())
				continue;
			
			for (IPViolation ipViolation : ipViolations.get())
				client.submitIpViolation(ipViolation, ipViolations);
		}
	}
}
