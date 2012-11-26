package com.ipeirotis.cl;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.ipeirotis.cl.di.ContextConfig;
import com.ipeirotis.cl.model.Question;
import com.ipeirotis.cl.service.QuestionService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = ContextConfig.class)
public class QuestionServiceTest {
	@Inject
	QuestionService questionService;
	
	@Test
	@Ignore
	public void testLoader() throws Exception {
		List<String> buf = new ArrayList<String>(IOUtils.readLines(new FileInputStream("cl.txt")));
		
		buf.remove(0);
		
		Set<Question> questionList = new TreeSet<Question>();
		
		Pattern PATTERN_LINESTART = Pattern.compile("^[\\-\\w+]+\t\\d+.*");
		
		List<String> realBuf = new ArrayList<String>();
		
		StringBuilder lastLine = new StringBuilder();
		
		for (ListIterator<String> listIterator = buf.listIterator(); listIterator.hasNext(); ) {
			String line = listIterator.next();
			
			if (PATTERN_LINESTART.matcher(line).matches()) {
				if (lastLine.length() > 0) {
					realBuf.add(lastLine.toString());
					
					lastLine = new StringBuilder(line);
				} else {
					lastLine.append(line).append("\n");
				}
			} else {
				lastLine.append(line).append("\n");
			}
		}
		
		for (String b : realBuf) {
			String[] e = b.split("\"?\\t\"?");
			
			Question q = new Question();
			
			q.setTestTopic(e[0]);
			q.setQuestionId(e[1]);
			q.setQuestionText(e[2]);
			
			List<String> answers = new ArrayList<String>();
			
			for (int i = 3; i < e.length; i++) {
				if (isNotBlank(e[i]))
					answers.add(e[i].trim());
			}
			
			q.setAnswers(answers);
			
			questionList.add(q);
		}
		
		for (Question q : questionList) {
			assertTrue(q.getQuestionId().matches("^\\d+$"));
		}
		
		questionService.save(questionList.toArray(new Question[questionList.size()]));
		

	}
	
	@Test
	@Ignore
	public void testFixing() throws Exception {
		Pattern PATTERN_ID = Pattern.compile("^\\d+$");
		Set<Question> questionsToDelete = new TreeSet<Question>();
		
		int i = 0;
		
		for (Question q : questionService.scanAll()) {
			Matcher m = PATTERN_ID.matcher(q.getQuestionId());
			
			if (! m.matches())
				questionsToDelete.add(q);
			
			i++;
		}
		
		System.out.println(String.format("i: %d; qTD.size(): %d: ", i, questionsToDelete.size()));
		
		
		questionService.delete(questionsToDelete.toArray(new Question[questionsToDelete.size()]));
	}


}
