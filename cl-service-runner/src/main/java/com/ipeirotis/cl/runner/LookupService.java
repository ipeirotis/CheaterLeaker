package com.ipeirotis.cl.runner;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.ipeirotis.cl.runner.workflow.LookupWorkflowClientExternal;

@Component
public class LookupService {
	@Inject
	LookupWorkflowClientExternal workflowClientExternal;
	
	public void executeLookup() {
		workflowClientExternal.doLookup();
	}
}
