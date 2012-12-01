package com.ipeirotis.cl.di;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.dynamodb.AmazonDynamoDB;
import com.amazonaws.services.dynamodb.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMapper;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduceClient;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient;

@Configuration
@ComponentScan(basePackages = "com.ipeirotis.cl")
public class ContextConfig {
	@Bean
	public AWSCredentials getAWSCredentials() throws Exception {
		return new PropertiesCredentials(getClass().getResourceAsStream(
				"/aws.properties"));
	}

	@Bean
	public AmazonS3 getAmazonS3(AWSCredentials awsCredentials) {
		return new AmazonS3Client(awsCredentials);
	}

	@Bean
	public AmazonDynamoDB getAmazonDynamo(AWSCredentials awsCredentials) {
		return new AmazonDynamoDBClient(awsCredentials);
	}

	@Bean
	public DynamoDBMapper getDynamoDBMapper(AmazonDynamoDB amazonDynamoDB) {
		return new DynamoDBMapper(amazonDynamoDB);
	}

	@Bean
	public AmazonElasticMapReduce getElasticMapReduce(
			AWSCredentials awsCredentials) {
		return new AmazonElasticMapReduceClient(awsCredentials);
	}

	@Bean
	public ObjectMapper getObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();

		mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);

		return mapper;
	}
	
	@Bean
	public AmazonSimpleWorkflow getSimpleWorkflow(AWSCredentials awsCredentials) {
		return new AmazonSimpleWorkflowClient(awsCredentials);
	}
}