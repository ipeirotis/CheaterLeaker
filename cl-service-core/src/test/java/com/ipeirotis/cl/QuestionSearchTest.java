package com.ipeirotis.cl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.inject.Inject;

import net.htmlparser.jericho.Renderer;
import net.htmlparser.jericho.Source;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.ipeirotis.cl.di.ContextConfig;
import com.ipeirotis.cl.model.IPViolation;
import com.ipeirotis.cl.model.Question;
import com.ipeirotis.cl.service.IPViolationService;
import com.ipeirotis.cl.service.QuestionService;
import com.ipeirotis.cl.service.UpdateService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = ContextConfig.class)
public class QuestionSearchTest {
	@Inject
	QuestionService questionService;

	@Inject
	UpdateService updateService;

	@Inject
	IPViolationService ipViolationService;

	@Test
	@Ignore
	public void testLookup() throws Exception {
		TreeMap<String, List<Question>> questionsByTopic = new TreeMap<String, List<Question>>(
				questionService.getQuestionsByTopic());

		SerializationUtils.serialize(questionsByTopic, new FileOutputStream(
				"src/test/resources/questionsByTopic.ser"));

		TreeMap<String, String> textsToSearch = new TreeMap<String, String>();

		for (String topic : questionsByTopic.keySet()) {
			List<Question> questions = questionsByTopic.get(topic);
			StringBuilder textToSearch = new StringBuilder();

			for (Question q : questions) {
				textToSearch.append(q.getQuestionText());

				for (String s : q.getAnswers())
					textToSearch.append('\n').append(s).append('\n');
			}

			StringWriter writer = new StringWriter();

			new Renderer(new Source(textToSearch)).writeTo(writer);

			textsToSearch.put(topic, writer.toString());
		}

		SerializationUtils.serialize(textsToSearch,
				new java.io.FileOutputStream(
						"src/test/resources/textsToSearch.ser"));
	}

	@Test
	public void testSearch() throws Exception {
		@SuppressWarnings("unchecked")
		TreeMap<String, List<Question>> questionsByTopic = (TreeMap<String, List<Question>>) SerializationUtils
				.deserialize(new FileInputStream(
						"src/test/resources/questionsByTopic.ser"));

		for (Map.Entry<String, List<Question>> topicEntry : questionsByTopic
				.entrySet()) {
			for (Question q : topicEntry.getValue()) {
				try {
					Set<IPViolation> ipViolations = new TreeSet<IPViolation>(
							updateService.findIPViolations(q));

					if (ipViolations.isEmpty())
						continue;

					Collection<IPViolation> existing = ipViolationService
							.findExisting(ipViolations
									.toArray(new IPViolation[ipViolations
											.size()]));

					ipViolations.removeAll(existing);

					ipViolationService.save((IPViolation[]) ipViolations
							.toArray(new IPViolation[ipViolations.size()]));
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}
		}
	}
	
	@Test
	public void dumpQuestionViolationsToCsv() throws Exception {
		FileOutputStream fos = new FileOutputStream("ip-violations.csv");
		PrintWriter w = new PrintWriter(fos, true);
		
		for (IPViolation ipViolation : ipViolationService.scanAll()) {
			String topic = questionService.findByPrimaryKey(ipViolation.getId()).getTestTopic();
			String questionId = ipViolation.getId();
			String host = new URL(ipViolation.getUrl()).getHost();
			String url = ipViolation.getUrl();
			
			w.println(String.format("%s;%s;%s;%s", questionId, topic, host, url));
		}
		
		w.close();
	}

}
