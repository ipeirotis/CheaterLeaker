package com.ipeirotis.cl;

import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import org.codehaus.jackson.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.ipeirotis.cl.di.ContextConfig;
import com.ipeirotis.cl.model.SearchQuery;
import com.ipeirotis.cl.service.HttpService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = ContextConfig.class)
public class HttpServiceTest {
	@Inject
	HttpService httpService;
	
	SearchQuery searchQuery;
	
	@Before
	public void setup() {
		searchQuery = new SearchQuery();
		
		searchQuery.setQueryService("CopyScape");
		searchQuery.setQueryText("Suppose MyClass is a class that defines a copy constructor and overloads the assignment operator. In which of the following cases will the copy constructor of MyClass be called?");
	}
	
	@Test
	public void testCopyScapeSearch() throws Exception {
		ObjectNode result = httpService.doQuery(searchQuery);
		
		assertNotNull(result);
	}

	@Test
	public void testGoogleSearch() throws Exception {
		searchQuery.setQueryService("Google");
		
		ObjectNode result = httpService.doQuery(searchQuery);
		
		assertNotNull(result);
	}
}
