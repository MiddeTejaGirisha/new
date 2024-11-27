package helloworld;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;

import java.net.URI;

public class DynamoDBCli {
    public static DynamoDbClient createDynamoDbClient(){
        return DynamoDbClient.builder()
                .endpointOverride(URI.create("http://localhost:8000"))
                .build();
    }

    public static void createStudentTable(DynamoDbClient dynamoDbClient) {
        CreateTableRequest request = CreateTableRequest.builder()
                .tableName("Student")
                .keySchema(KeySchemaElement.builder().attributeName("id").keyType(
                        KeyType.HASH).build())
                .attributeDefinitions(
                        AttributeDefinition.builder().attributeName("id").attributeType(
                                ScalarAttributeType.S).build())
                .provisionedThroughput(ProvisionedThroughput.builder()
                                               .readCapacityUnits(5L)
                                               .writeCapacityUnits(5L)
                                               .build())
                .build();

        dynamoDbClient.createTable(request);
    }

}
