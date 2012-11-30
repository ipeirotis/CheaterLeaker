package com.ipeirotis.cl.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.codehaus.jackson.node.ObjectNode;
import org.springframework.stereotype.Component;

import com.ipeirotis.cl.model.Question;

@Component
public class TopicScannerService {
	@Inject
	QuestionService questionService;
	
	public ObjectNode getQuestionScanFor(String testTopicId) throws Exception {
		List<Question> questionsForTopic = getQuestionsForTopic(testTopicId);
		
		return null;
	}

	private List<Question> getQuestionsForTopic(String testTopicId) {
		List<Question> result = new ArrayList<Question>();
		
		for (Question q : questionService.scanAll())
			if (q.getTestTopic().equals(testTopicId))
				result.add(q);
		
		return result;
	}

}
