package student_crud_operations.utilities;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;


import java.net.URI;

public class DynamoDBCli {

    public static DynamoDbClient createDynamoDbClient() {
        try {
            return DynamoDbClient.builder()
                    .endpointOverride(URI.create("http://host.docker.internal:8000")) // DynamoDB Local endpoint
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create("ASIA46ZDFQOUKY5UL2UB", "a89oRKr/hIhFZK9qHNVr4nIQH1z2CKIkuYfcCMV9"))) // Dummy credentials
                    .region(Region.US_EAST_1)
                    .build();
        } catch (SdkClientException e) {
            System.err.println("Error creating DynamoDB client: " + e.getMessage());
            return null; // Or handle as needed
        }

    }

}

