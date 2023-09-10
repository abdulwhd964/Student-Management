package com.student;

import com.student.constant.CardType;
import com.student.dto.FeeDTO;
import com.student.dto.ReceiptDTO;
import com.student.dto.StudentDTO;
import com.student.entity.Student;
import com.student.service.ApiService;
import com.student.util.CustomResponse;
import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

// telling the springboot to run with randomPort
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudentServicesApplicationTests {

    // to get the port that springboot test have created for us
    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private static ModelMapper mapper;

    private static TestRestTemplate testRestTemplate;

    @Autowired
    private ApiService apiService;

    @Autowired
    private TestStudentDao testStudentDao;

    // before running all test case, annotate the testResttemplate
    @BeforeAll
    public static void init() {
        testRestTemplate = new TestRestTemplate();
        mapper = new ModelMapper();
    }

    public String setUpUrlforStudentApi() {
        StringBuilder builder = new StringBuilder();
        baseUrl = builder.append(baseUrl).append(":").append(port + "").append("/students").toString();
        return baseUrl;
    }

    public String setUpUrlforCollectFeeApi() {
        StringBuilder builder = new StringBuilder();
        baseUrl = builder.append(baseUrl).append(":").append(port + "").append("/collect/student/fee").toString();
        return baseUrl;
    }

    public String setUpUrlforViewReceiptApi() {
        StringBuilder builder = new StringBuilder();
        baseUrl = builder.append(baseUrl).append(":").append(port + "").append("/view/receipt/").append(1).toString();
        return baseUrl;
    }

    @Test
    @Order(1)
    void testAddStudent() {
        Student student = new Student(0, "Abdul", "C", "507377412", "Sky");

        ResponseEntity<CustomResponse> response = testRestTemplate.postForEntity(setUpUrlforStudentApi(), student, CustomResponse.class);
        StudentDTO studentDTO = mapper.map(response.getBody().getData(), StudentDTO.class);

        assertEquals("Abdul", studentDTO.getStudentName());
        assertEquals(1, studentDTO.getId());
    }

    @Test
    @Order(2)
    void testGetStudent() {
        Student student = testStudentDao.findById(1).get();
        ResponseEntity<StudentDTO> response = testRestTemplate.getForEntity(setUpUrlforStudentApi() + "/" + student.getId(), StudentDTO.class);
        StudentDTO studentDTO = mapper.map(response.getBody(), StudentDTO.class);

        assertEquals("Abdul", studentDTO.getStudentName());
        assertEquals(1, studentDTO.getId());
    }

    // to run this test, please make sure that fee-collection-service is running.
    @Test
    @Order(3)
    void testCollectFee() {
        Optional<Student> student = testStudentDao.findById(1);
        FeeDTO feeDTO = new FeeDTO(0, student.get().getId(), "1234565", CardType.VISA, 10d);
        ResponseEntity<CustomResponse> response = testRestTemplate.postForEntity(setUpUrlforCollectFeeApi(), feeDTO, CustomResponse.class);
        assertEquals(201, response.getStatusCode().value());
    }

    @Test
    @Order(4)
    void testViewReceipt() {
        ResponseEntity<CustomResponse> response = testRestTemplate.getForEntity(setUpUrlforViewReceiptApi(), CustomResponse.class);
        ReceiptDTO dto = mapper.map(response.getBody().getData(), ReceiptDTO.class);
        assertEquals(1, dto.getId());
    }

}
