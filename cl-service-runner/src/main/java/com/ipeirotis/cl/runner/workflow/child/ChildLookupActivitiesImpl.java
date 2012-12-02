package com.ipeirotis.cl.runner.workflow.child;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.ipeirotis.cl.model.IPViolation;
import com.ipeirotis.cl.model.IPViolationRef;
import com.ipeirotis.cl.model.Question;
import com.ipeirotis.cl.service.IPViolationReportService;
import com.ipeirotis.cl.service.IPViolationService;
import com.ipeirotis.cl.service.QuestionService;
import com.ipeirotis.cl.service.UpdateService;

@Component
public class ChildLookupActivitiesImpl implements ChildLookupActivities {
	@Inject
	QuestionService questionService;

	@Inject
	UpdateService updateService;

	@Inject
	IPViolationService ipViolationService;

	@Inject
	IPViolationReportService reportService;

	@Inject
	AmazonSimpleWorkflow swfService;

	@Override
	public Collection<IPViolationRef> collectNewIpViolations(String questionId) {
		// ActivityExecutionContextProvider provider = new
		// ActivityExecutionContextProviderImpl();
		//
		// WorkflowExecutionDetail describeWorkflowExecution =
		// swfService.describeWorkflowExecution(new
		// DescribeWorkflowExecutionRequest().withExecution(provider.getActivityExecutionContext().getWorkflowExecution()));
		//
		// for (String tag :
		// describeWorkflowExecution.getExecutionInfo().getTagList()) {
		// if (! tag.startsWith("p:"))
		// continue;
		//
		// String parentId = tag.split(":")[1];
		//
		// WorkflowExecutionDetail parentWorkflowExecution =
		// swfService.describeWorkflowExecution(new
		// DescribeWorkflowExecutionRequest().withExecution(new
		// WorkflowExecution().withWorkflowId(parentId)));
		//
		// if
		// ("CLOSED".equals(parentWorkflowExecution.getExecutionInfo().getExecutionStatus()))
		// {
		// int i = 0;
		//
		// i++;
		// }
		// }

		Question q = questionService.findByPrimaryKey(questionId);

		try {
			Set<IPViolation> ipViolations = new TreeSet<IPViolation>(
					updateService.findIPViolations(q));

			Collection<IPViolation> existing = ipViolationService
					.findExisting(ipViolations
							.toArray(new IPViolation[ipViolations.size()]));

			Set<IPViolation> tmpResult = new TreeSet<IPViolation>(ipViolations);
			Set<IPViolationRef> result = new TreeSet<IPViolationRef>();

			tmpResult.removeAll(existing);

			for (IPViolation ipViolation : tmpResult) {
				ipViolationService.save(ipViolation);

				IPViolationRef ref = new IPViolationRef();

				ref.setId(ipViolation.getId());
				ref.setUrl(ipViolation.getUrl());

				result.add(ref);
			}

			return result;
		} catch (Exception exc) {
			// TODO: Make sure to log errors later
			exc.printStackTrace();

			return null;
		}
	}

	@Override
	public Collection<IPViolationRef> submitNewIpViolations(
			Collection<IPViolationRef> ipViolationRefs) {
		for (IPViolationRef ref : ipViolationRefs) {
			IPViolation ipViolation = ipViolationService.findByPrimaryKey(
					ref.getId(), ref.getUrl());
			
			reportService.reportNewIPViolation(ipViolation);
		}
		// TODO Implement This Properly

		return ipViolationRefs;
	}
}
