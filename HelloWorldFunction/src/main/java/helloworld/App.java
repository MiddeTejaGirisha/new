package helloworld;

import java.util.List;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import helloworld.pojo.Student;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<Map<String, Object>, Map<String, Object>> {
    private final StudentRepo studentRepository;
    private static final Gson gson = new Gson();

    public App() {
        DynamoDbClient client = DynamoDBCli.createDynamoDbClient();
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(client)
                .build();
        studentRepository = new StudentRepo(enhancedClient);
    }

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        try {
            String httpMethod = (String) input.get("httpMethod");
            String path = (String) input.get("path");
            Map<String, String> pathParameters = safeCastToMap(input.get("pathParameters"));
            String body = (String) input.get("body");

            context.getLogger().log("HTTP Method: " + httpMethod + ", Path: " + path);

            switch (httpMethod) {
                case "POST":
                    return handlePost(body);

                case "GET":
                    return handleGet(pathParameters);

                case "DELETE":
                    return handleDelete(pathParameters);

                default:
                    return generateResponse(400, "Unsupported HTTP method: " + httpMethod);
            }
        } catch (Exception e) {
            context.getLogger().log("Error: " + e.getMessage());
            return generateResponse(500, "Internal Server Error: " + e.getMessage());
        }
    }

    private Map<String, Object> handlePost(String body) {
        Student student = gson.fromJson(body, Student.class);
        studentRepository.saveStudent(student);
        return generateResponse(201, "Student created successfully with ID: " + student.getId());
    }

    private Map<String, Object> handleGet(Map<String, String> pathParameters) {
        if (pathParameters != null && pathParameters.containsKey("id")) {
            String id = pathParameters.get("id");
            Student fetchedStudent = studentRepository.getStudentById(id);
            return generateResponse(200, fetchedStudent);
        } else {
            List<Student> students = studentRepository.getAllStudents();
            return generateResponse(200, students);
        }
    }

    private Map<String, Object> handleDelete(Map<String, String> pathParameters) {
        if (pathParameters != null && pathParameters.containsKey("id")) {
            String id = pathParameters.get("id");
            studentRepository.deleteStudentById(id);
            return generateResponse(200, "Student with ID " + id + " deleted successfully.");
        } else {
            return generateResponse(400, "ID not provided for DELETE operation");
        }
    }

    private Map<String, Object> generateResponse(int statusCode, Object body) {
        return Map.of(
                "statusCode", statusCode,
                "headers", Map.of("Content-Type", "application/json"),
                "body", gson.toJson(body)
        );
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> safeCastToMap(Object input) {
        if (input instanceof Map) {
            return (Map<String, String>) input;
        }
        return null;
    }
}
