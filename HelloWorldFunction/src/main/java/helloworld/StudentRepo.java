package helloworld;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import helloworld.pojo.Student;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class  StudentRepo {
    private final DynamoDbClient dynamoDbClient;
    private final String tableName = "Student";

    public StudentRepo(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    // Create student
    public void saveStudent(Student student) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.builder().s(student.getId()).build());
        item.put("name", AttributeValue.builder().s(student.getName()).build());
        item.put("age", AttributeValue.builder().n(
                String.valueOf(student.getAge())).build());
        item.put("grade",
                 AttributeValue.builder().s(student.getGrade()).build());
        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build();
        dynamoDbClient.putItem(putItemRequest);
    }

    // Read student by ID
    public Student getStudentById(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", AttributeValue.builder().s(id).build());
        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .build();
        GetItemResponse response = dynamoDbClient.getItem(getItemRequest);
        if (!response.hasItem()) {
            return null; // No student found
        }
        Map<String, AttributeValue> item = response.item();
        Student student = new Student();
        student.setId(item.get("id").s());
        student.setName(item.get("name").s());
        student.setAge(Integer.parseInt(item.get("age").n()));
        student.setGrade(item.get("grade").s());
        return student;
    }

    // Update student
    public void updateStudent(Student student) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", AttributeValue.builder().s(student.getId()).build());
        Map<String, AttributeValueUpdate> updates = new HashMap<>();
        updates.put("name", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(student.getName()).build())
                .action(AttributeAction.PUT)
                .build());
        updates.put("age", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().n(
                        String.valueOf(student.getAge())).build())
                .action(AttributeAction.PUT)
                .build());
        updates.put("grade", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(student.getGrade()).build())
                .action(AttributeAction.PUT)
                .build());
        UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .attributeUpdates(updates)
                .build();
        dynamoDbClient.updateItem(updateItemRequest);
    }

    // Delete student by ID
    public void deleteStudentById(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", AttributeValue.builder().s(id).build());
        DeleteItemRequest deleteItemRequest = DeleteItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .build();
        dynamoDbClient.deleteItem(deleteItemRequest);
    }
}