package com.ipeirotis.cl.web.v1;

import static com.jayway.restassured.RestAssured.basePath;
import static com.jayway.restassured.RestAssured.given;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.ipeirotis.cl.web.support.ServerRule;

public class JobResourceTest {
	@Rule
	public ServerRule serverRule = new ServerRule();
	
	@Before
	public void before() throws Exception {
		basePath = "http://127.0.0.1:8080/api/v1";
	}
	
	@Test
	public void testGetJobs() throws Exception {
		given().
		expect().
			statusCode(200).
		when().
			get("/job");
	}
	
	@Test
	public void testDeleteJobs() throws Exception {
		given().//
		expect().//
			statusCode(200).//
		when().//
			delete("/job/j-RU4331NO4Q0H");
	}

}
