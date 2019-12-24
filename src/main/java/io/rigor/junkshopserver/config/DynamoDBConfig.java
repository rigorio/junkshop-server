package io.rigor.junkshopserver.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDynamoDBRepositories(basePackages = "io.rigor.junkshopserver")
public class DynamoDBConfig {
  @Value("${amazon.dynamodb.endpoint}")
  private String amazonDynamoDBEndpoint;

  @Value("${amazon.dynamodb.signingregion}")
  private String amazonDynamoDBSigningRegion;

  @Value("${amazon.aws.accesskey}")
  private String amazonAWSAccessKey;

  @Value("${amazon.aws.secretkey}")
  private String amazonAWSSecretKey;


  @Bean
  public AmazonDynamoDB amazonDynamoDB() {
//    AmazonDynamoDB amazonDynamoDB
//        = new AmazonDynamoDBClient(amazonAWSCredentials());
//
//    if (!StringUtils.isEmpty(amazonDynamoDBEndpoint)) {
//      amazonDynamoDB.setEndpoint(amazonDynamoDBEndpoint);
//    }
    AmazonDynamoDBClientBuilder amazonDynamoDBClientBuilder = AmazonDynamoDBClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(amazonAWSCredentials()));

    AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration(amazonDynamoDBEndpoint, amazonDynamoDBSigningRegion);
    amazonDynamoDBClientBuilder.setEndpointConfiguration(endpoint);

    return amazonDynamoDBClientBuilder.build();
  }

  @Bean
  public AWSCredentials amazonAWSCredentials() {
    return new BasicAWSCredentials(
        amazonAWSAccessKey, amazonAWSSecretKey);
  }
}
