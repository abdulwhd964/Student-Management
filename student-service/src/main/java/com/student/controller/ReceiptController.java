package com.student.controller;

import com.student.exception.StudentNotFoundException;
import com.student.service.ApiService;
import com.student.service.StudentService;
import com.student.util.CustomResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "ReceiptController", description = "API's to generate receipt for student")
public class ReceiptController {
    public static final Logger log = LogManager.getLogger(ReceiptController.class);
    @Autowired
    ApiService apiService;

    @Autowired
    private StudentService studentService;

    /**
     * @param receiptId
     * @return receipt object
     * @throws StudentNotFoundException
     * @author Abdul Wahid
     * @version 1.0
     * @since 10/09/2023
     */
    @GetMapping("/view/receipt/{receiptid}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "View Receipt")})
    public ResponseEntity<CustomResponse> viewReceiptForStudent(@PathVariable("receiptid") int receiptId) {
        try {
            ResponseEntity<CustomResponse> response = apiService.checkReceiptAvailableForReceiptId(receiptId);
            if (response.getStatusCode().value() == HttpStatus.NO_CONTENT.value()) {
                return new ResponseEntity<>(CustomResponse.sendError("Receipt Not found for the id : " + receiptId), HttpStatus.BAD_REQUEST);
            }
            return apiService.generateReceiptForTheStudent(receiptId);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
