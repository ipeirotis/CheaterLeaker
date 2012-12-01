package com.ipeirotis.cl.web.v1;

import java.util.Date;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Component;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.model.ExecutionTimeFilter;
import com.amazonaws.services.simpleworkflow.model.ListOpenWorkflowExecutionsRequest;
import com.amazonaws.services.simpleworkflow.model.WorkflowExecutionInfos;
import com.ipeirotis.cl.runner.LookupService;

@Component
@Path("/v1/lookup")
public class LookupResource extends BaseResource {
	@Inject
	AmazonSimpleWorkflow swfClient;
	
	@Inject
	LookupService lookupService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Object info() {
		ListOpenWorkflowExecutionsRequest request = new ListOpenWorkflowExecutionsRequest()
				.withDomain("cl-domain");

		request.setStartTimeFilter(new ExecutionTimeFilter()
				.withOldestDate(DateUtils.addDays(new Date(), -2)));

		WorkflowExecutionInfos result = swfClient
				.listOpenWorkflowExecutions(request);
		return result;
	}
	
	@POST
	@Path("/new")
	@Produces(MediaType.APPLICATION_JSON)
	public Object start() {
		lookupService.executeLookup();
		
		return info();
	}
}
