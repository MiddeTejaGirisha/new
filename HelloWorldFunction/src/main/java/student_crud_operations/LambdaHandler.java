package student_crud_operations;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;

import student_crud_operations.dao.StudentDao;
import student_crud_operations.pojo.Student;
import student_crud_operations.utilities.DynamoDBCli;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse;

/**
 * Handler for requests to Lambda function.
 */
public class LambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final DynamoDbClient dynamoDbClient;
    private final StudentDao studentRepo;

    public LambdaHandler() {
        this.dynamoDbClient = DynamoDBCli.createDynamoDbClient(); // DynamoDB client initialization
        this.studentRepo = new StudentDao(dynamoDbClient); // StudentRepo to handle CRUD
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(
            APIGatewayProxyRequestEvent event, Context context) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        String httpMethod = event.getHttpMethod();

        try {
            // Determine the operation based on the HTTP method and URL path
            switch (httpMethod) {
                case "POST":
                    return createStudent(event);
                case "GET":
                    return getStudent(event);
                case "PUT":
                    return updateStudent(event);
                case "DELETE":
                    return deleteStudent(event);
                default:
                    response.setStatusCode(405); // Method Not Allowed
                    response.setBody("Method not allowed");
                    return response;
            }
        } catch (Exception e) {
            response.setStatusCode(500); // Internal Server Error
            response.setBody("Error: " + e.getMessage());
            return response;
        }
    }

    // Handle POST to create a student
    private APIGatewayProxyResponseEvent createStudent(APIGatewayProxyRequestEvent event) {
        // Extract student information from the body of the event
        String body = event.getBody();
        Student student = parseStudentFromBody(body); // A helper method to parse JSON into Student object

        PutItemResponse res= studentRepo.saveStudent(student); // Save student to DynamoDB

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(res.sdkHttpResponse().statusCode()); // Created
        response.setBody("Student created successfully");
        return response;
    }

    // Handle GET to retrieve a student by ID
    private APIGatewayProxyResponseEvent getStudent(APIGatewayProxyRequestEvent event) {
        String studentId = event.getPathParameters().get("id");

        Student student = studentRepo.getStudentByAdmissionID(studentId);

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        if (student != null) {
            response.setStatusCode(200); // OK
            response.setBody(toJson(student)); // Convert the student object to JSON
        } else {
            response.setStatusCode(404); // Not Found
            response.setBody("Student not found");
        }

        return response;
    }

    // Handle PUT to update a student
    private APIGatewayProxyResponseEvent updateStudent(APIGatewayProxyRequestEvent event) {
        String body = event.getBody();
        String studentId=event.getPathParameters().get("id");
        Student student = parseStudentFromBody(body);

        if (!studentId.equals(student.getAdmissionID())) {
            // Return error response if IDs do not match
            APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
            response.setStatusCode(400); // Bad Request
            response.setBody("Error: URL id does not match the student id in the body");
            return response;
        }

        UpdateItemResponse res=studentRepo.updateStudent(student);

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(res.sdkHttpResponse().statusCode()); // OK
        response.setBody("Student updated successfully");
        return response;
    }

    // Handle DELETE to delete a student by ID
    private APIGatewayProxyResponseEvent deleteStudent(APIGatewayProxyRequestEvent event) {
        String studentId = event.getPathParameters().get("id");

        DeleteItemResponse res= studentRepo.deleteStudentById(studentId);


        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        // If the response contains attributes, deletion was successful
        if (res == null) {
            // Item not found or condition failed
            response.setStatusCode(404);  // Not Found
            response.setBody("Student with AdmissionID " + studentId + " does not exist or could not be deleted.");
        } else {
            // Deletion successful
            response.setStatusCode(200);  // OK
            response.setBody("Student with AdmissionID " + studentId + " deleted successfully.");
        }

        return response;
    }

    // Helper method to parse JSON into Student object
    private Student parseStudentFromBody(String body) {
        Gson gson = new Gson();
        try {
            // Parse JSON string to Student object
            return gson.fromJson(body, Student.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Helper method to convert a Student object to JSON
    private String toJson(Student student) {
        Gson gson = new Gson();
        try {
            // Convert Student object to JSON
            return gson.toJson(student);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }
}
