package com.student;

import com.student.constant.CardType;
import com.student.dto.ReceiptDTO;
import com.student.util.CustomResponse;
import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = FeeCollectionServiceApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FeeCollectionServiceApplicationTests {


    // to get the port that springboot test have created for us
    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private String baseUrlForViewReceipts = "http://localhost";

    private static ModelMapper mapper;

    private static TestRestTemplate testRestTemplate;
//    @Autowired
//    private TestStudentDao testStudentDao;

    @BeforeAll
    public static void init() {
        testRestTemplate = new TestRestTemplate();
        mapper = new ModelMapper();
    }

    @BeforeEach
    public void setUpUrl() {
        StringBuilder builder = new StringBuilder();
        baseUrl = builder.append(baseUrl).append(":").append(port + "").append("/fees/collect").toString();
    }

    @BeforeEach
    public void setUpUrlForViewReceipt() {
        StringBuilder builder = new StringBuilder();
        baseUrlForViewReceipts = builder.append(baseUrlForViewReceipts).append(":").append(port + "").append("/fees/receipts").toString();
    }

    public String constructUrlToCheckReceiptForReceiptId(int id) {
        StringBuilder builder = new StringBuilder();
        String receiptUrl = builder.append("http://localhost").append(":").append(port + "").append("/receipts/" + id).toString();
        return receiptUrl;
    }

    public String constructUrlToCheckFeeHasBeenPaidForTheStudent(int studentId) {
        StringBuilder builder = new StringBuilder();
        String receiptUrl = builder.append("http://localhost").append(":").append(port + "").append("/student/" + studentId).append("/fee").toString();
        return receiptUrl;
    }

    @Test
    @Order(1)
    void testCollectFee() {
        ReceiptDTO dto = new ReceiptDTO(0, 1, 10d, "123456", CardType.MASTER, new Date());
        ResponseEntity<CustomResponse> response = testRestTemplate.postForEntity(baseUrl, dto, CustomResponse.class);
        assertEquals(201, response.getStatusCode().value());
    }

    @Test
    @Order(2)
    void testCheckReceiptForReceiptId() {
        ResponseEntity<CustomResponse> response = testRestTemplate.getForEntity(constructUrlToCheckReceiptForReceiptId(1), CustomResponse.class);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    @Order(3)
    void testCheckFeeHasBeenPaidForTheStudent() {
        ResponseEntity<CustomResponse> response = testRestTemplate.getForEntity(constructUrlToCheckFeeHasBeenPaidForTheStudent(1), CustomResponse.class);
        // checking whether student have the paid the fee or not, 409 means he have paid the fees
        assertEquals(409, response.getStatusCode().value());
    }


    @Test
    @Order(4)
    void testViewFeeReceipt() {
        ResponseEntity<CustomResponse> response = testRestTemplate.getForEntity(baseUrlForViewReceipts + "/1", CustomResponse.class);
        ReceiptDTO receiptDTO = mapper.map(response.getBody().getData(), ReceiptDTO.class);
        assertEquals(1, receiptDTO.getId());
    }

}
