package com.ipeirotis.cl.service;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ipeirotis.cl.model.SearchQuery;
import com.ipeirotis.cl.search.CopyScapeSearchFormatter;
import com.ipeirotis.cl.search.GoogleSearchFormatter;

@Component
public class HttpService {
	@Autowired
	GoogleSearchFormatter googleSearchFormatter;

	@Autowired
	CopyScapeSearchFormatter copyScapeSearchFormatter;

	public ObjectNode doQuery(SearchQuery searchQuery) {
		try {
			if ("Google".equals(searchQuery.getQueryService())) {
				return doGoogleQuery(searchQuery);
			} else if ("CopyScape".equals(searchQuery.getQueryService())) {
				return doCopyScapeQuery(searchQuery);
			}
		} catch (Exception exc) {
			throw new RuntimeException(exc);
		}

		throw new IllegalArgumentException("Invalid query Service");

	}

	private ObjectNode doCopyScapeQuery(SearchQuery searchQuery)
			throws Exception {
		DefaultHttpClient httpClient = new DefaultHttpClient();

		HttpPost httpPost = new HttpPost("http://www.copyscape.com/api/");

		List<BasicNameValuePair> nvps = Arrays.<BasicNameValuePair> asList(
				new BasicNameValuePair("u", "ipeirotis"),
				new BasicNameValuePair("k", "3a3p4fnw0bvt2o5l"),
				new BasicNameValuePair("o", "csearch"), new BasicNameValuePair(
						"e", "UTF-8"),
				new BasicNameValuePair("t", searchQuery.getQueryText()));

		httpPost.setEntity(new UrlEncodedFormEntity(nvps));

		HttpResponse response = httpClient.execute(httpPost);
		HttpEntity entity = response.getEntity();
		
		//IOUtils.copy(entity.getContent(), new FileOutputStream("response.bin"));

		try {
			return copyScapeSearchFormatter.getFormattedSearch(searchQuery,
					entity);
		} finally {
			EntityUtils.consume(entity);
		}
	}

	private ObjectNode doGoogleQuery(SearchQuery searchQuery) throws Exception {
		DefaultHttpClient httpClient = new DefaultHttpClient();

		HttpGet httpGet  = new HttpGet("https://www.googleapis.com/customsearch/v1?" 
				+ "&cx=011623744360334870808:plz5huqvq_e" 
				+ "&key=AIzaSyAP0fH9aEndZbSDFT87g46YY0gjhkQY8Zc" 
				+ "&q=" +  URLEncoder.encode(searchQuery.getQueryText(), "UTF-8"));
		
		HttpEntity entity = null;
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);
			
			entity = response.getEntity();
			
			//IOUtils.copy(entity.getContent(), new FileOutputStream("src/test/resources/google.json"));

			return googleSearchFormatter.getFormattedSearch(searchQuery,
					entity);
		} finally {
			EntityUtils.consume(entity);
		}
	}
}
