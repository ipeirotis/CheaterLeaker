package com.ipeirotis.cl.runner.di;

import org.springframework.context.LifecycleProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.DefaultLifecycleProcessor;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.flow.spring.SpringActivityWorker;
import com.amazonaws.services.simpleworkflow.flow.spring.SpringWorkflowWorker;
import com.ipeirotis.cl.runner.workflow.LookupActivitiesImpl;
import com.ipeirotis.cl.runner.workflow.LookupWorkflowClientExternal;
import com.ipeirotis.cl.runner.workflow.LookupWorkflowClientExternalFactoryImpl;
import com.ipeirotis.cl.runner.workflow.LookupWorkflowImpl;

@Configuration
public class WorkflowContextConfig {
	private String domain;
	private String taskList;

	public WorkflowContextConfig() {
		this.domain = "cl-service";
		this.taskList = "cl-service-tasklist";
	}

	@Bean
	public LookupWorkflowClientExternal getWorkflowClientExternal(
			AmazonSimpleWorkflow workflowClient) {
		return new LookupWorkflowClientExternalFactoryImpl(workflowClient,
				domain).getClient();
	}

	@Bean
	public LifecycleProcessor getLifecycleProcessor() {
		DefaultLifecycleProcessor defaultLifecycleProcessor = new DefaultLifecycleProcessor();

		defaultLifecycleProcessor.setTimeoutPerShutdownPhase(60000L);

		return defaultLifecycleProcessor;
	}

	public String getDomain() {
		return domain;
	}

	public String getTaskList() {
		return taskList;
	}

	@Bean
	public SpringWorkflowWorker getWorkflowWorker(
			AmazonSimpleWorkflow workflowClient) throws InstantiationException,
			IllegalAccessException {
		SpringWorkflowWorker workflowWorker = new SpringWorkflowWorker(
				workflowClient, domain, taskList);

		workflowWorker.addWorkflowImplementation(new LookupWorkflowImpl());
		workflowWorker.setRegisterDomain(false);
		
		//workflowWorker.setDisableTypeRegistrationOnStart(true);
		
		//workflowWorker.setDomainRetentionPeriodInDays(30);
		//workflowWorker.setRegisterDomain(true);

		return workflowWorker;
	}

	@Bean
	public SpringActivityWorker getActivityWorker(
			AmazonSimpleWorkflow workflowClient,
			LookupActivitiesImpl activitiesImpl) throws Exception {
		SpringActivityWorker activityWorker = new SpringActivityWorker(
				workflowClient, domain, taskList);

		activityWorker.addActivitiesImplementation(activitiesImpl);
		activityWorker.setRegisterDomain(false);
		
		//activityWorker.setDisableTypeRegistrationOnStart(true);
		
		//activityWorker.setRegisterDomain(true);
		//activityWorker.setDomainRetentionPeriodInDays(30);

		return activityWorker;
	}
}
