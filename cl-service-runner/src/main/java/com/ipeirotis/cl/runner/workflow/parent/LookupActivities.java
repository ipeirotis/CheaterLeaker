package com.ipeirotis.cl.runner.workflow.parent;

import java.util.Collection;

import com.amazonaws.services.simpleworkflow.flow.annotations.Activities;
import com.amazonaws.services.simpleworkflow.flow.annotations.Activity;
import com.amazonaws.services.simpleworkflow.flow.annotations.ActivityRegistrationOptions;
import com.ipeirotis.cl.model.IPViolation;

@Activities
@ActivityRegistrationOptions(defaultTaskScheduleToStartTimeoutSeconds=600, defaultTaskStartToCloseTimeoutSeconds=86400, defaultTaskList="cl-service-tasklist")
public interface LookupActivities {
	@Activity(name="get-all-questions", version="2.1")
	public Collection<Integer> getAllQuestions();
	
	@Activity(name="lookup-question", version="2.1")
	public Collection<IPViolation> lookupQuestion(Collection<Integer> questions);
	
	@Activity(name="submit-ip-violation", version="2.1")
	public Collection<IPViolation> submitIpViolation(Collection<IPViolation> ipViolation);

	@Activity(name="collect-all-questions", version="2.1")
	public void collectAllQuestions(Collection<Integer> questions);
}
