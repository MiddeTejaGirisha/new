package helloworld;

import helloworld.pojo.Student;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;
import java.util.stream.Collectors;

public class StudentRepo {
    private final DynamoDbTable<Student> studentTableDB;

    public StudentRepo(DynamoDbEnhancedClient enhancedClient) {
        this.studentTableDB=enhancedClient.table("Student", TableSchema.fromBean(
                Student.class));
    }

    public void saveStudent(Student student) {
        studentTableDB.putItem(student);
    }

    // Read
    public Student getStudentById(String id) {
        return studentTableDB.getItem(r -> r.key(k -> k.partitionValue(id)));
    }

    // Delete
    public void deleteStudentById(String id) {
        studentTableDB.deleteItem(r -> r.key(k -> k.partitionValue(id)));
    }

    // List All
    public List<Student> getAllStudents() {
        return studentTableDB.scan().items().stream().collect(Collectors.toList());
    }
}
