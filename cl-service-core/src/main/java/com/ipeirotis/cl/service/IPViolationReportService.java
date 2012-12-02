package com.ipeirotis.cl.service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.Topic;
import com.ipeirotis.cl.model.IPViolation;

@Component
public class IPViolationReportService {
	@Inject
	AmazonSNS snsClient;

	Topic topic;

	@Inject
	ObjectMapper objectMapper;

	@PostConstruct
	public void init() throws Exception {
		for (Topic t : snsClient.listTopics().getTopics()) {
			if (t.getTopicArn().endsWith("cl-service-ip-violations")) {
				this.topic = t;
				break;
			}
		}
	}

	public void reportNewIPViolation(IPViolation ipViolation) {
		try {
			String message = objectMapper.writeValueAsString(ipViolation);
			String subject = String.format(
					"New Violation Report for Question id %s",
					ipViolation.getId());

			snsClient.publish(new PublishRequest(topic.getTopicArn(), message, subject));
		} catch (Exception exc) {
			throw new RuntimeException(exc);
		}
	}

}
