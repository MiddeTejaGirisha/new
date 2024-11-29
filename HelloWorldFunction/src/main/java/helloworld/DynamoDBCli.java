package helloworld;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;


import java.net.URI;

public class DynamoDBCli {
//    public static DynamoDbClient createDynamoDbClient(){
//        return DynamoDbClient.builder()
//                .endpointOverride(URI.create("http://host.docker.internal:8000"))
//                .build();
//    }

    public static DynamoDbClient createDynamoDbClient() {
        return DynamoDbClient.builder()
                .endpointOverride(URI.create("http://host.docker.internal:8000")) // DynamoDB Local endpoint
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("dummy-access-key", "dummy-secret-key"))) // Dummy credentials
                .region(Region.US_EAST_1) // Any valid AWS region; used locally only for client validation
                .build();
    }

}
