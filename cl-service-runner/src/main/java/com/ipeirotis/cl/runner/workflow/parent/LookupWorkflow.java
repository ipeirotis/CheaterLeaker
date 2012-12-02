package com.ipeirotis.cl.runner.workflow.parent;

import com.amazonaws.services.simpleworkflow.flow.annotations.Execute;
import com.amazonaws.services.simpleworkflow.flow.annotations.Workflow;
import com.amazonaws.services.simpleworkflow.flow.annotations.WorkflowRegistrationOptions;
import com.amazonaws.services.simpleworkflow.model.ChildPolicy;

@Workflow
@WorkflowRegistrationOptions(defaultExecutionStartToCloseTimeoutSeconds=86400, defaultChildPolicy = ChildPolicy.TERMINATE, defaultTaskList="cl-service-tasklist")
public interface LookupWorkflow {
	@Execute(version="2.0", name="parent-question-lookup")
	void doLookup();
}
