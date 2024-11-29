package helloworld;

import java.util.List;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import helloworld.pojo.Student;
//import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final DynamoDbClient dynamoDbClient;
    private final StudentRepo studentRepo;

    public App() {
        this.dynamoDbClient = DynamoDBCli.createDynamoDbClient(); // DynamoDB client initialization
        this.studentRepo = new StudentRepo(dynamoDbClient); // StudentRepo to handle CRUD
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(
            APIGatewayProxyRequestEvent event, Context context) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        String path = event.getPath();
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

        studentRepo.saveStudent(student); // Save student to DynamoDB

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(201); // Created
        response.setBody("Student created successfully");
        return response;
    }

    // Handle GET to retrieve a student by ID
    private APIGatewayProxyResponseEvent getStudent(APIGatewayProxyRequestEvent event) {
        String studentId = event.getPathParameters().get("id");

        Student student = studentRepo.getStudentById(studentId);

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
        Student student = parseStudentFromBody(body);

        studentRepo.updateStudent(student);

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(200); // OK
        response.setBody("Student updated successfully");
        return response;
    }

    // Handle DELETE to delete a student by ID
    private APIGatewayProxyResponseEvent deleteStudent(APIGatewayProxyRequestEvent event) {
        String studentId = event.getPathParameters().get("id");

        studentRepo.deleteStudentById(studentId);

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(200); // OK
        response.setBody("Student deleted successfully");
        return response;
    }

    // Helper method to parse JSON into Student object (use your preferred library, e.g., Jackson)
    private Student parseStudentFromBody(String body) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Parse the JSON string to the Student object
            return objectMapper.readValue(body, Student.class);
        } catch (Exception e) {
            // Handle the exception if JSON parsing fails
            e.printStackTrace();
            return null; // You could throw an exception or return a default object if needed
        }
    }

    // Helper method to convert a Student object to JSON (use Jackson, Gson, etc.)
    private String toJson(Student student) {
        // Convert Student object to JSON
        return "{\"id\":\"" + student.getId() + "\", \"name\":\"" + student.getName() + "\", \"age\": " + student.getAge() + ", \"grade\":\"" + student.getGrade() + "\"}";
    }
}
