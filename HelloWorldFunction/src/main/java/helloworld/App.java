package helloworld;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import helloworld.pojo.Student;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<Map<String, Object>, String> {
    private final StudentRepo studentRepository;

    public App() {
        DynamoDbClient client = DynamoDBCli.createDynamoDbClient();
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(client)
                .build();
        studentRepository = new StudentRepo(enhancedClient);
    }

    @Override
    public String handleRequest(Map<String, Object> input, Context context) {
        String action = (String) input.get("action");
        if ("create".equalsIgnoreCase(action)) {
            Student student = parseStudent(input);
            studentRepository.saveStudent(student);
            return "Student saved successfully.";
        } else if ("read".equalsIgnoreCase(action)) {
            String id = (String) input.get("id");
            return studentRepository.getStudentById(id).toString();
        } else if ("delete".equalsIgnoreCase(action)) {
            String id = (String) input.get("id");
            studentRepository.deleteStudentById(id);
            return "Student deleted successfully.";
        } else if ("list".equalsIgnoreCase(action)) {
            return studentRepository.getAllStudents().toString();
        }
        return "Invalid action.";
    }

    private Student parseStudent(Map<String, Object> input) {
        Student student = new Student();
        student.setId((String) input.get("id"));
        student.setName((String) input.get("name"));
        student.setAge((Integer) input.get("age"));
        student.setGrade((String) input.get("grade"));
        return student;
    }
}