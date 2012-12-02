package com.ipeirotis.cl.runner.workflow.child;

import java.util.Collection;

import com.amazonaws.services.simpleworkflow.flow.annotations.Activities;
import com.amazonaws.services.simpleworkflow.flow.annotations.Activity;
import com.amazonaws.services.simpleworkflow.flow.annotations.ActivityRegistrationOptions;
import com.ipeirotis.cl.model.IPViolationRef;

@Activities
@ActivityRegistrationOptions(defaultTaskScheduleToStartTimeoutSeconds=86400, defaultTaskStartToCloseTimeoutSeconds=3600, defaultTaskList="cl-service-tasklist")
public interface ChildLookupActivities {
	@Activity(name="collect-new-ip-violations", version="2.0")
	public Collection<IPViolationRef> collectNewIpViolations(String questionId);
	
	@Activity(name="submit-new-ip-violations", version="2.0")
	public Collection<IPViolationRef> submitNewIpViolations(Collection<IPViolationRef> ipViolationRef);
}
