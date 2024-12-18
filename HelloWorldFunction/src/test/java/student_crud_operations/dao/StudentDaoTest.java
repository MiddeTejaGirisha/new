package student_crud_operations.dao;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse;
import student_crud_operations.pojo.Student;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//public class StudentDaoTest {
//
//    private DynamoDbClient dynamoDbClient;
//    private StudentDAO studentDAO;
//    private Student student;
//
//    @Before
//    public void setUp() {
//        dynamoDbClient = mock(DynamoDbClient.class);
//        SdkHttpResponse sdkHttpResponse = SdkHttpResponse.builder()
//                .statusCode(200)
//                .build();
//
//        PutItemResponse mockResponse = (PutItemResponse) PutItemResponse.builder()
//                .sdkHttpResponse(sdkHttpResponse)
//                .build();
//
//        when(dynamoDbClient.putItem(any(PutItemRequest.class))).thenReturn(mockResponse);
//
////        when()
//        studentDAO = new StudentDAO(dynamoDbClient);
//        student = new Student();
//        student.setS_NO("1");
//        student.setCollegeID("12345");
//        student.setAdmissionID("John Doe");
//        student.setCAMPUS("Main Campus");
//
//        student.setPursuing_branch("Computer Science");
//        student.setPursuing_campus("Engineering Campus");
//        student.setAADHAR_NO("1234-5678-9012");
//        student.setDOB("2000-01-01");
//        student.setSTUDENT_NAME_AS_PER_SSC("John Doe");
//        student.setFATHER_NAME_AS_PER_SSC("Richard Roe");
//        student.setMOTHER_NAME_AS_PER_SSC("Jane Roe");
//        student.setOCCUPATION("Engineer");
//        student.setF_OR_M("M");
//
//        student.setRoll_Number("CS101");
//        student.setSection("A");
//        student.setProfile_picture("profilePic.jpg");
//
//        student.setStudent_Class("2");
//        student.setCAST("General");
//        student.setSUB_CAST("None");
//
//        student.setMOBILE_NO_1("1234567890");
//        student.setMOBILE_NO_2("0987654321");
//
//        student.setDIST_NAME("New York");
//        student.setPIN_CODE("10001");
//
//    }
//
//
//    @Test
//    public void testForDataTypesandSize() {
//
//        // Act
//        PutItemResponse res=studentDAO.saveStudent(student);
//
//        // Assert
//        assertEquals(200,res.sdkHttpResponse().statusCode());
//        verify(dynamoDbClient).putItem(any(PutItemRequest.class));
//
//        ArgumentCaptor<PutItemRequest> captor = ArgumentCaptor.forClass(PutItemRequest.class);
//        verify(dynamoDbClient).putItem(captor.capture());
//
//        // Get the captured PutItemRequest
//        PutItemRequest capturedRequest = captor.getValue();
//
//        // Assert: Verify the content of the captured PutItemRequest
//        Map<String, AttributeValue> capturedItem = capturedRequest.item();
//
//        assertEquals(23, capturedItem.size());
//        assertEquals(student.getAdmissionID(), capturedItem.get("AdmissionID").s());
//        assertEquals(student.getS_NO(), capturedItem.get("S_NO").s());
//        assertEquals(student.getStudent_Class(), capturedItem.get("Student_Class").s());
//        assertEquals(student.getSection(), capturedItem.get("Section").s());
//
//        assertEquals(student.getCollegeID(), capturedItem.get("CollegeID").s());
//        assertEquals(student.getCAMPUS(), capturedItem.get("CAMPUS").s());
//        assertEquals(student.getPursuing_branch(), capturedItem.get("Pursuing_branch").s());
//        assertEquals(student.getPursuing_campus(), capturedItem.get("Pursuing_campus").s());
//        assertEquals(student.getAADHAR_NO(), capturedItem.get("AADHAR_NO").s());
//        assertEquals(student.getDOB(), capturedItem.get("DOB").s());
//        assertEquals(student.getSTUDENT_NAME_AS_PER_SSC(), capturedItem.get("STUDENT_NAME_AS_PER_SSC").s());
//        assertEquals(student.getFATHER_NAME_AS_PER_SSC(), capturedItem.get("FATHER_NAME_AS_PER_SSC").s());
//        assertEquals(student.getMOTHER_NAME_AS_PER_SSC(), capturedItem.get("MOTHER_NAME_AS_PER_SSC").s());
//        assertEquals(student.getOCCUPATION(), capturedItem.get("OCCUPATION").s());
//        assertEquals(student.getF_OR_M(), capturedItem.get("F_OR_M").s());
//
//        assertEquals(student.getRoll_Number(), capturedItem.get("Roll_Number").s());
//        assertEquals(student.getProfile_picture(), capturedItem.get("profile_picture").s());
//
//        assertEquals(student.getCAST(), capturedItem.get("CAST").s());
//        assertEquals(student.getSUB_CAST(), capturedItem.get("SUB_CAST").s());
//
//        assertEquals(student.getMOBILE_NO_1(), capturedItem.get("MOBILE_NO_1").s());
//        assertEquals(student.getMOBILE_NO_2(), capturedItem.get("MOBILE_NO_2").s());
//
//        assertEquals(student.getDIST_NAME(), capturedItem.get("DIST_NAME").s());
//        assertEquals(student.getPIN_CODE(), capturedItem.get("PIN_CODE").s());
//
//    }
//}
//


public class StudentDaoTest {
    private DynamoDbClient mockDynamoDbClient;
    private StudentDao studentDAO;

    @Before
    public void setUp() {
        // Mock DynamoDbClient
        mockDynamoDbClient = Mockito.mock(DynamoDbClient.class);
        // Initialize StudentDAO with mocked DynamoDbClient
        studentDAO = new StudentDao(mockDynamoDbClient);
    }

    @Test
    public void testSaveStudent_Success() {
        // Arrange
        Student student = new Student();
        student.setS_NO("1");
        student.setCollegeID("COL123");
        student.setAdmissionID("ADM456");
        student.setCAMPUS("Main Campus");
        student.setPursuing_branch("CS");
        student.setPursuing_campus("Central");
        student.setAADHAR_NO("123456789012");
        student.setDOB("2000-01-01");
        student.setSTUDENT_NAME_AS_PER_SSC("John Doe");
        student.setFATHER_NAME_AS_PER_SSC("Mr. Doe");
        student.setMOTHER_NAME_AS_PER_SSC("Mrs. Doe");
        student.setOCCUPATION("Engineer");
        student.setF_OR_M("M");
        student.setRoll_Number("ROLL123");
        student.setSection("A");
        student.setProfile_picture("profile.jpg");
        student.setStudent_Class("10th");
        student.setCAST("General");
        student.setSUB_CAST("None");
        student.setMOBILE_NO_1("9876543210");
        student.setMOBILE_NO_2("9876543211");
        student.setDIST_NAME("District1");
        student.setPIN_CODE("123456");
        PutItemResponse mockResponse = (PutItemResponse) PutItemResponse.builder()
                .sdkHttpResponse(
                        SdkHttpResponse.builder().statusCode(200).build())
                .build();
        // Mock the DynamoDbClient putItem method
        when(mockDynamoDbClient.putItem(any(PutItemRequest.class))).thenReturn(
                mockResponse);
        // Act
        PutItemResponse response = studentDAO.saveStudent(student);
        // Assert
        assertNotNull(response);
        assertEquals(200, response.sdkHttpResponse().statusCode());
    }

//    @Test
//    public void testSaveStudent_Failure() {
//        // Arrange
//        Student student = new Student();
//        student.setS_NO("1");
//        student.setCollegeID("COL123");
//
//        // Simulate exception when saving to DynamoDB
//        when(mockDynamoDbClient.putItem(any(PutItemRequest.class)))
//                .thenThrow(DynamoDbException.builder().message("DynamoDB error").build());
//
//        // Act & Assert
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            studentDAO.saveStudent(student);
//        });
//
//        assertTrue(exception.getMessage().contains("Failed to save student"));
//    }

    @Test
    public void testGetStudentByAdmissionID_Success() {
        // Arrange
        String admissionID = "ADM456";

        // Mock the full item with all expected keys to avoid NPE
        Map<String, AttributeValue> mockItem = new HashMap<>();
        mockItem.put("S_NO", AttributeValue.builder().s("1").build());
        mockItem.put("CollegeID", AttributeValue.builder().s("COL123").build());
        mockItem.put("AdmissionID", AttributeValue.builder().s(admissionID).build());
        mockItem.put("CAMPUS", AttributeValue.builder().s("Main Campus").build());
        mockItem.put("Pursuing_branch", AttributeValue.builder().s("Computer Science").build());
        mockItem.put("Pursuing_campus", AttributeValue.builder().s("North").build());
        mockItem.put("AADHAR_NO", AttributeValue.builder().s("123456789012").build());
        mockItem.put("DOB", AttributeValue.builder().s("2000-01-01").build());
        mockItem.put("STUDENT_NAME_AS_PER_SSC", AttributeValue.builder().s("John Doe").build());
        mockItem.put("FATHER_NAME_AS_PER_SSC", AttributeValue.builder().s("Mr. Doe").build());
        mockItem.put("MOTHER_NAME_AS_PER_SSC", AttributeValue.builder().s("Mrs. Doe").build());
        mockItem.put("OCCUPATION", AttributeValue.builder().s("Engineer").build());
        mockItem.put("F_OR_M", AttributeValue.builder().s("M").build());
        mockItem.put("Roll_Number", AttributeValue.builder().s("R123").build());
        mockItem.put("Section", AttributeValue.builder().s("A").build());
        mockItem.put("profile_picture", AttributeValue.builder().s("profile.jpg").build());
        mockItem.put("Student_Class", AttributeValue.builder().s("10th Grade").build());
        mockItem.put("CAST", AttributeValue.builder().s("General").build());
        mockItem.put("SUB_CAST", AttributeValue.builder().s("None").build());
        mockItem.put("MOBILE_NO_1", AttributeValue.builder().s("9876543210").build());
        mockItem.put("MOBILE_NO_2", AttributeValue.builder().s("8765432109").build());
        mockItem.put("DIST_NAME", AttributeValue.builder().s("XYZ District").build());
        mockItem.put("PIN_CODE", AttributeValue.builder().s("123456").build());

        GetItemResponse mockResponse = GetItemResponse.builder()
                .item(mockItem)
                .build();

        // Correctly mock the DynamoDB client behavior
        when(mockDynamoDbClient.getItem(any(GetItemRequest.class))).thenReturn(mockResponse);

        // Act
        Student result = studentDAO.getStudentByAdmissionID(admissionID);

        // Assert
        assertNotNull(result);
        assertEquals("1", result.getS_NO());
        assertEquals("COL123", result.getCollegeID());
        assertEquals(admissionID, result.getAdmissionID());
        assertEquals("Main Campus", result.getCAMPUS());
        assertEquals("Computer Science", result.getPursuing_branch());
        assertEquals("123456789012", result.getAADHAR_NO());
        assertEquals("John Doe", result.getSTUDENT_NAME_AS_PER_SSC());
        assertEquals("Engineer", result.getOCCUPATION());
        assertEquals("9876543210", result.getMOBILE_NO_1());
    }


    @Test
    public void testGetStudentByAdmissionID_NotFound() {
        // Arrange
        String admissionID = "UNKNOWN";

        GetItemResponse emptyResponse = GetItemResponse.builder()
                .item(null) // Simulate no item found
                .build();

        // Correctly mock the DynamoDB client behavior
        when(mockDynamoDbClient.getItem(any(GetItemRequest.class))).thenReturn(emptyResponse);

        // Act
        Student result = studentDAO.getStudentByAdmissionID(admissionID);

        // Assert
        assertNull(result); // No student should be found
    }

    @Test
    public void testUpdateStudent_Success() {
        // Arrange
        Student mockStudent = new Student();
        mockStudent.setAdmissionID("ADM001");
        mockStudent.setS_NO("S123");
        mockStudent.setCollegeID("C123");
        mockStudent.setCAMPUS("Main");
        mockStudent.setPursuing_branch("CSE");
        mockStudent.setPursuing_campus("North Campus");
        mockStudent.setAADHAR_NO("123456789123");
        mockStudent.setDOB("2001-12-01");
        mockStudent.setSTUDENT_NAME_AS_PER_SSC("John Doe");
        mockStudent.setFATHER_NAME_AS_PER_SSC("Father Doe");
        mockStudent.setMOTHER_NAME_AS_PER_SSC("Mother Doe");
        mockStudent.setOCCUPATION("Engineer");
        mockStudent.setF_OR_M("M");
        mockStudent.setRoll_Number("R123");
        mockStudent.setSection("A");
        mockStudent.setProfile_picture("profile.jpg");
        mockStudent.setStudent_Class("10");
        mockStudent.setCAST("General");
        mockStudent.setSUB_CAST("None");
        mockStudent.setMOBILE_NO_1("9876543210");
        mockStudent.setMOBILE_NO_2("8765432109");
        mockStudent.setDIST_NAME("XYZ");
        mockStudent.setPIN_CODE("500001");

        // Mock response
        UpdateItemResponse mockResponse = UpdateItemResponse.builder().build();

        // Mock DynamoDB behavior
        when(mockDynamoDbClient.updateItem(any(
                UpdateItemRequest.class))).thenReturn(mockResponse);

        // Act
        UpdateItemResponse response = studentDAO.updateStudent(mockStudent);

        // Assert
        assertNotNull("Response should not be null",response);
        verify(mockDynamoDbClient, times(1)).updateItem(any(UpdateItemRequest.class));
    }

    @Test
    public void testUpdateStudent_Exception() {
        // Arrange
        Student mockStudent = new Student();
        mockStudent.setAdmissionID("ADM002");

        // Mock an exception when updateItem is called
        when(mockDynamoDbClient.updateItem(any(UpdateItemRequest.class)))
                .thenThrow(new RuntimeException("Update failed"));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            studentDAO.updateStudent(mockStudent);
        });

        assertEquals("Exception message should match", exception.getMessage(), "Update failed");
        verify(mockDynamoDbClient, times(1)).updateItem(any(UpdateItemRequest.class));
    }

    @Test
    public void testDeleteStudentById_Success() {
        // Arrange
        String studentId = "ADM001";
        DeleteItemResponse mockResponse = DeleteItemResponse.builder().build();

        // Mock the deleteItem method to return a mock response
        when(mockDynamoDbClient.deleteItem(any(DeleteItemRequest.class)))
                .thenReturn(mockResponse);

        // Act
        DeleteItemResponse response = studentDAO.deleteStudentById(studentId);

        // Assert
        assertNotNull("Response should not be null",response);
        verify(mockDynamoDbClient, times(1)).deleteItem(any(DeleteItemRequest.class));
    }

    @Test
    public void testDeleteStudentById_Exception() {
        // Arrange
        String studentId = "ADM002";

        // Mock an exception (ConditionalCheckFailedException) when deleteItem is called
        when(mockDynamoDbClient.deleteItem(any(DeleteItemRequest.class)))
                .thenThrow(
                        ConditionalCheckFailedException.builder().message("Condition failed").build());

        // Act & Assert
        // Call the method and assert that the response is null
        DeleteItemResponse response = studentDAO.deleteStudentById(studentId);
        assertNull("Response should be null when condition fails",response);

        // Verify that deleteItem was called once
        verify(mockDynamoDbClient, times(1)).deleteItem(any(DeleteItemRequest.class));
    }

}
