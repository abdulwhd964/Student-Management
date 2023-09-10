package com.student.controller;

import com.student.dto.FeeDTO;
import com.student.entity.Student;
import com.student.exception.StudentNotFoundException;
import com.student.service.ApiService;
import com.student.service.StudentService;
import com.student.util.BindingResultUtil;
import com.student.util.CustomResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RestController
@Tag(name = "CollectFeeController", description = "API's to collect fee from student")
public class CollectFeeController {

    public static final Logger log = LogManager.getLogger(CollectFeeController.class);
    @Autowired
    ApiService apiService;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StudentService studentService;
    @Autowired
    private ModelMapper mapper;

    /**
     * @param feeDTO
     * @param result
     * @return
     * @throws StudentNotFoundException
     * @author Abdul Wahid
     * @version 1.0
     * @since 10/09/2023
     */

    @PostMapping("/collect/student/fee")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Collecting the fee for the student")})
    public ResponseEntity<CustomResponse> collectFeeForStudent(@Valid @RequestBody FeeDTO feeDTO, BindingResult result) throws StudentNotFoundException {
        try {
            CustomResponse response = BindingResultUtil.validate(result);
            if (response != null) {
                // handling multipe error message in the response.getData()
                return new ResponseEntity<>(CustomResponse.sendData("Validation Error", response.getData()), HttpStatus.BAD_REQUEST);
            }
            // amount should not be null or empty
            if (feeDTO.getAmount() == null) {
                return new ResponseEntity<>(CustomResponse.sendData("Validation Error", "amount should not be empty"), HttpStatus.BAD_REQUEST);
            }
            Optional<Student> student = studentService.findByStudentId(feeDTO.getStudentId());
            if (student.isEmpty()) {
                log.debug("No Student Record, Throw student Not Found Exception");
                throw new StudentNotFoundException("Student Not Found, please provide an valid student id");
            }
            // check Whether fees is already paid for the student
            ResponseEntity<CustomResponse> responseObject = apiService.checkWhetherFeeAlreadyPaidForStudent(student.get().getId());
            if (responseObject.getStatusCode().value() == HttpStatus.CONFLICT.value()) {
                return new ResponseEntity<>(CustomResponse.sendError("Payment has been done for the student!!"), HttpStatus.CONFLICT);
            }
            // rest call to fee collection service to pay the fee for the student
            return apiService.callReceiptServiceToPayTheFeeForTheStudent(feeDTO, student);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
