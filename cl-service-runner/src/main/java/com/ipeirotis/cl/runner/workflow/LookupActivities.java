package com.ipeirotis.cl.runner.workflow;

import java.util.Collection;

import com.amazonaws.services.simpleworkflow.flow.annotations.Activities;
import com.amazonaws.services.simpleworkflow.flow.annotations.Activity;
import com.amazonaws.services.simpleworkflow.flow.annotations.ActivityRegistrationOptions;
import com.ipeirotis.cl.model.IPViolation;

@Activities
public interface LookupActivities {
	@Activity(name="get-all-questions", version="1.0")
	@ActivityRegistrationOptions(defaultTaskScheduleToStartTimeoutSeconds=600, defaultTaskStartToCloseTimeoutSeconds=600)
	public Collection<Integer> getAllQuestions();
	
	@Activity(name="lookup-question", version="1.0")
	@ActivityRegistrationOptions(defaultTaskScheduleToStartTimeoutSeconds=86400, defaultTaskStartToCloseTimeoutSeconds=120)
	public Collection<IPViolation> lookupQuestion(Integer questionId);
	
	@Activity(name="submit-ip-violation", version="1.0")
	@ActivityRegistrationOptions(defaultTaskScheduleToStartTimeoutSeconds=600, defaultTaskStartToCloseTimeoutSeconds=60)
	public IPViolation submitIpViolation(IPViolation ipViolation);

	@Activity(name="collect-all-questions", version="1.0")
	@ActivityRegistrationOptions(defaultTaskScheduleToStartTimeoutSeconds=86400, defaultTaskStartToCloseTimeoutSeconds=120)
	public void collectAllQuestions(Collection<Integer> questions);
}
