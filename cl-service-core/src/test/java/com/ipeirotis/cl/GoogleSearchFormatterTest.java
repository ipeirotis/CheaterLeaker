package com.ipeirotis.cl;

import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.apache.http.entity.ByteArrayEntity;
import org.codehaus.jackson.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.ipeirotis.cl.di.ContextConfig;
import com.ipeirotis.cl.model.SearchQuery;
import com.ipeirotis.cl.search.GoogleSearchFormatter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = ContextConfig.class)
public class GoogleSearchFormatterTest {
	@Inject
	GoogleSearchFormatter searchFormatter;

	SearchQuery searchQuery;
	
	@Before
	public void setup() {
		searchQuery = new SearchQuery();
		
		searchQuery.setQueryService("Google");
		searchQuery.setQueryText("Suppose MyClass is a class that defines a copy constructor and overloads the assignment operator. In which of the following cases will the copy constructor of MyClass be called?");
	}
	
	
	@Test
	public void testGoogle() throws Exception {
		ObjectNode result = searchFormatter.getFormattedSearch(
				null,
				new ByteArrayEntity(IOUtils.toByteArray(getClass()
						.getResourceAsStream("/google.json"))));

		assertNotNull(result);
	}
}
