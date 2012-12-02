package com.ipeirotis.cl.runner.workflow.child;

import com.amazonaws.services.simpleworkflow.flow.annotations.Execute;
import com.amazonaws.services.simpleworkflow.flow.annotations.Workflow;
import com.amazonaws.services.simpleworkflow.flow.annotations.WorkflowRegistrationOptions;

@Workflow
@WorkflowRegistrationOptions(defaultExecutionStartToCloseTimeoutSeconds = 86400, defaultTaskList = "cl-service-tasklist", defaultTaskStartToCloseTimeoutSeconds = 3600)
public interface ChildLookupWorkflow {
	@Execute(name="execute-question-lookup", version="2.1")
	public void executeQuestionLookup(String questionId);
}
