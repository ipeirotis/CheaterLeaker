package com.ipeirotis.cl.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.CompareToBuilder;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMarshalling;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodb.datamodeling.JsonMarshaller;

@DynamoDBTable(tableName = "cl-question")
public class Question extends Entity implements Comparable<Question> {
	private static final long serialVersionUID = -1260846175533151313L;
	
	String testTopic;

	@DynamoDBAttribute
	public String getTestTopic() {
		return testTopic;
	}

	public void setTestTopic(String testTopic) {
		this.testTopic = testTopic;
	}

	String questionId;

	@DynamoDBHashKey(attributeName="id")
	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	
	String questionText;

	@DynamoDBAttribute
	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	
	List<String> answers = new ArrayList<String>();

	@DynamoDBAttribute
	@DynamoDBMarshalling(marshallerClass=AnswersJsonMarshaller.class)
	public List<String> getAnswers() {
		return answers;
	}

	public void setAnswers(List<String> answers) {
		this.answers = answers;
	}
	
	
	public static class AnswersJsonMarshaller extends JsonMarshaller<List<String>> {
	}


	@Override
	public int compareTo(Question o) {
		if (null == o)
			return 1;
		
		if (this == o)
			return 0;
		
		return new CompareToBuilder().append(this.questionId, o.questionId).toComparison();
	}
}
