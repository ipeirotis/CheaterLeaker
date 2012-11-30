package com.ipeirotis.cl.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Component;

import com.ipeirotis.cl.model.Question;

@Component
public class QuestionService extends AbstractHashService<Question> {
	public Map<String, List<Question>> getQuestionsByTopic() {
		Map<String, List<Question>> questionsByTopic = new TreeMap<String, List<Question>>();
		
		for (Question q : scanAll()) {
			String topic = q.getTestTopic();
			
			List<Question> qbtEntry = new ArrayList<Question>();
			
			if (questionsByTopic.containsKey(topic))
				qbtEntry.addAll(questionsByTopic.get(topic));
			
			
			qbtEntry.add(q);
			
			questionsByTopic.put(topic, qbtEntry);
		}
		
		return questionsByTopic;
	}
}
