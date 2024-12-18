package student_crud_operations.dao;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.HashMap;
import java.util.Map;

import student_crud_operations.pojo.Student;

public class StudentDao {
    private final DynamoDbClient dynamoDbClient;
    private final String tableName = "Student";

    public StudentDao(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    // Create student
    public PutItemResponse saveStudent(Student student) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("S_NO", AttributeValue.builder().s(student.getS_NO()).build());
        item.put("CollegeID", AttributeValue.builder().s(student.getCollegeID()).build());
        item.put("AdmissionID", AttributeValue.builder().s(student.getAdmissionID()).build());
        item.put("CAMPUS", AttributeValue.builder().s(student.getCAMPUS()).build());
        item.put("Pursuing_branch", AttributeValue.builder().s(student.getPursuing_branch()).build());
        item.put("Pursuing_campus", AttributeValue.builder().s(student.getPursuing_campus()).build());
        item.put("AADHAR_NO", AttributeValue.builder().s(student.getAADHAR_NO()).build());
        item.put("DOB", AttributeValue.builder().s(student.getDOB()).build());
        item.put("STUDENT_NAME_AS_PER_SSC", AttributeValue.builder().s(student.getSTUDENT_NAME_AS_PER_SSC()).build());
        item.put("FATHER_NAME_AS_PER_SSC", AttributeValue.builder().s(student.getFATHER_NAME_AS_PER_SSC()).build());
        item.put("MOTHER_NAME_AS_PER_SSC", AttributeValue.builder().s(student.getMOTHER_NAME_AS_PER_SSC()).build());
        item.put("OCCUPATION", AttributeValue.builder().s(student.getOCCUPATION()).build());
        item.put("F_OR_M", AttributeValue.builder().s(student.getF_OR_M()).build());
        item.put("Roll_Number", AttributeValue.builder().s(student.getRoll_Number()).build());
        item.put("Section", AttributeValue.builder().s(student.getSection()).build());
        item.put("profile_picture", AttributeValue.builder().s(student.getProfile_picture()).build());
        item.put("Student_Class", AttributeValue.builder().s(student.getStudent_Class()).build());
        item.put("CAST", AttributeValue.builder().s(student.getCAST()).build());
        item.put("SUB_CAST", AttributeValue.builder().s(student.getSUB_CAST()).build());
        item.put("MOBILE_NO_1", AttributeValue.builder().s(student.getMOBILE_NO_1()).build());
        item.put("MOBILE_NO_2", AttributeValue.builder().s(student.getMOBILE_NO_2()).build());
        item.put("DIST_NAME", AttributeValue.builder().s(student.getDIST_NAME()).build());
        item.put("PIN_CODE", AttributeValue.builder().s(student.getPIN_CODE()).build());

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build();
        try {
            PutItemResponse response = dynamoDbClient.putItem(putItemRequest);
            // Checking the response
            System.out.println("HTTP Status Code: " + response.sdkHttpResponse().statusCode());

            return response;
        } catch (DynamoDbException e) {
            System.err.println(
                    "Error saving student to DynamoDB: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to save student", e);
        }
    }

    // Read student by ID
    public Student getStudentByAdmissionID(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("AdmissionID", AttributeValue.builder().s(id).build());
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
        student.setS_NO(item.get("S_NO").s());
        student.setCollegeID(item.get("CollegeID").s());
        student.setAdmissionID(item.get("AdmissionID").s());
        student.setCAMPUS(item.get("CAMPUS").s());
        student.setPursuing_branch(item.get("Pursuing_branch").s());
        student.setPursuing_campus(item.get("Pursuing_campus").s());
        student.setAADHAR_NO(item.get("AADHAR_NO").s());
        student.setDOB(item.get("DOB").s());
        student.setSTUDENT_NAME_AS_PER_SSC(item.get("STUDENT_NAME_AS_PER_SSC").s());
        student.setFATHER_NAME_AS_PER_SSC(item.get("FATHER_NAME_AS_PER_SSC").s());
        student.setMOTHER_NAME_AS_PER_SSC(item.get("MOTHER_NAME_AS_PER_SSC").s());
        student.setOCCUPATION(item.get("OCCUPATION").s());
        student.setF_OR_M(item.get("F_OR_M").s());
        student.setRoll_Number(item.get("Roll_Number").s());
        student.setSection(item.get("Section").s());
        student.setProfile_picture(item.get("profile_picture").s());
        student.setStudent_Class(item.get("Student_Class").s());
        student.setCAST(item.get("CAST").s());
        student.setSUB_CAST(item.get("SUB_CAST").s());
        student.setMOBILE_NO_1(item.get("MOBILE_NO_1").s());
        student.setMOBILE_NO_2(item.get("MOBILE_NO_2").s());
        student.setDIST_NAME(item.get("DIST_NAME").s());
        student.setPIN_CODE(item.get("PIN_CODE").s());
        return student;
    }

    // Update student
    public UpdateItemResponse updateStudent(Student student) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("AdmissionID", AttributeValue.builder().s(student.getAdmissionID()).build());
        Map<String, AttributeValueUpdate> updates = new HashMap<>();
        // Updating attributes
        updates.put("S_NO", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(student.getS_NO()).build())
                .action(AttributeAction.PUT) // PUT means replace the existing value
                .build());

        updates.put("CollegeID", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(student.getCollegeID()).build())
                .action(AttributeAction.PUT)
                .build());

        updates.put("CAMPUS", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(student.getCAMPUS()).build())
                .action(AttributeAction.PUT)
                .build());

        updates.put("Pursuing_branch", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(student.getPursuing_branch()).build())
                .action(AttributeAction.PUT)
                .build());

        updates.put("Pursuing_campus", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(student.getPursuing_campus()).build())
                .action(AttributeAction.PUT)
                .build());

        updates.put("AADHAR_NO", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(student.getAADHAR_NO()).build())
                .action(AttributeAction.PUT)
                .build());

        updates.put("DOB", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(student.getDOB()).build())
                .action(AttributeAction.PUT)
                .build());

        updates.put("STUDENT_NAME_AS_PER_SSC", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(student.getSTUDENT_NAME_AS_PER_SSC()).build())
                .action(AttributeAction.PUT)
                .build());

        updates.put("FATHER_NAME_AS_PER_SSC", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(student.getFATHER_NAME_AS_PER_SSC()).build())
                .action(AttributeAction.PUT)
                .build());

        updates.put("MOTHER_NAME_AS_PER_SSC", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(student.getMOTHER_NAME_AS_PER_SSC()).build())
                .action(AttributeAction.PUT)
                .build());

        updates.put("OCCUPATION", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(student.getOCCUPATION()).build())
                .action(AttributeAction.PUT)
                .build());

        updates.put("F_OR_M", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(student.getF_OR_M()).build())
                .action(AttributeAction.PUT)
                .build());

        updates.put("Roll_Number", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(student.getRoll_Number()).build())
                .action(AttributeAction.PUT)
                .build());

        updates.put("Section", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(student.getSection()).build())
                .action(AttributeAction.PUT)
                .build());

        updates.put("profile_picture", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(student.getProfile_picture()).build())
                .action(AttributeAction.PUT)
                .build());

        updates.put("Student_Class", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(student.getStudent_Class()).build())
                .action(AttributeAction.PUT)
                .build());

        updates.put("CAST", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(student.getCAST()).build())
                .action(AttributeAction.PUT)
                .build());

        updates.put("SUB_CAST", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(student.getSUB_CAST()).build())
                .action(AttributeAction.PUT)
                .build());

        updates.put("MOBILE_NO_1", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(student.getMOBILE_NO_1()).build())
                .action(AttributeAction.PUT)
                .build());

        updates.put("MOBILE_NO_2", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(student.getMOBILE_NO_2()).build())
                .action(AttributeAction.PUT)
                .build());

        updates.put("DIST_NAME", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(student.getDIST_NAME()).build())
                .action(AttributeAction.PUT)
                .build());

        updates.put("PIN_CODE", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(student.getPIN_CODE()).build())
                .action(AttributeAction.PUT)
                .build());
        UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .attributeUpdates(updates)
                .build();
        UpdateItemResponse response=dynamoDbClient.updateItem(updateItemRequest);
        return response;
    }

    // Delete student by ID
    public DeleteItemResponse deleteStudentById(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("AdmissionID", AttributeValue.builder().s(id).build());

        // Build the DeleteItemRequest with ConditionExpression
        DeleteItemRequest deleteItemRequest = DeleteItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .conditionExpression("AdmissionID = :id")
                .expressionAttributeValues(Map.of(
                        ":id", AttributeValue.builder().s(id).build()
                ))
                .build();

        try {
            DeleteItemResponse deleteItemResponse = dynamoDbClient.deleteItem(deleteItemRequest);
            return deleteItemResponse;
        } catch (ConditionalCheckFailedException e) {
            System.out.println("Item with ID " + id + " does not exist or condition failed.");
            return null; // Return null or handle as needed
        }
    }
}
