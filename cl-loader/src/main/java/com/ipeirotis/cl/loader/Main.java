package com.ipeirotis.cl.loader;

import static org.apache.commons.lang.StringUtils.isBlank;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.FileTypeSelector;
import org.apache.commons.vfs2.VFS;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.ipeirotis.cl.di.ContextConfig;
import com.ipeirotis.cl.model.Question;
import com.ipeirotis.cl.service.QuestionService;

@Component
public class Main {
	@Inject
	QuestionService questionService;

	public static FileSystemManager fsManager;

	static {
		try {
			fsManager = VFS.getManager();
		} catch (Exception exc) {
			throw new RuntimeException(exc);
		}
	}

	public static void main(String[] args) throws Exception {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
				ContextConfig.class);

		Main main = ctx.getBean(Main.class);

		main.execute(args);
	}

	public void execute(String[] args) throws Exception {
		for (String a : args) {
			File parent = new File(a);
			for (String p : parent.list(FileFilterUtils
					.suffixFileFilter(".zip")))
				executeOn(new File(parent, p).getPath());
		}

	}

	public void executeOn(String a) throws Exception {
		FileObject rootZipObject = fsManager.resolveFile("zip:file:///" + a);
		String testTopic = new File(a).getName().replaceAll("\\.zip$", "");

		for (FileObject o : rootZipObject.findFiles(new FileTypeSelector(
				FileType.FILE))) {
			boolean matches = o.getName().getPath().endsWith(".xml");

			if (!matches)
				continue;

			String contents = IOUtils.toString(o.getContent().getInputStream());

			Document doc = new SAXBuilder().build(new ByteArrayInputStream(
					contents.getBytes()));

			Collection<Question> questions = parseDoc(testTopic, doc);
			
			System.err.println("Saving " + questions.size() + " for testTopic " + testTopic);

			questionService.save(questions.toArray(new Question[questions.size()]));

			return;
		}
	}

	private Collection<Question> parseDoc(String testTopic, Document doc) {
		List<Question> results = new ArrayList<Question>();

		for (Element e : doc.getRootElement().getChildren()) {
			Question question = new Question();

			question.setQuestionId(e.getChildText("QuestionId"));
			question.setQuestionText(e.getChildText("Question"));
			question.setTestTopic(testTopic);

			for (Element opt : e.getChildren()) {
				if (!opt.getName().startsWith("Option"))
					continue;

				if (isBlank(opt.getText()))
					continue;

				question.getAnswers().add(opt.getText());
			}

			results.add(question);
		}

		return results;
	}
}
