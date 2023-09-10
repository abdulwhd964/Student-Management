package com.student.service;

import com.student.constant.SysConstant;
import com.student.dto.FeeDTO;
import com.student.dto.ReceiptDTO;
import com.student.entity.Student;
import com.student.util.CustomResponse;
import com.student.util.ReceiptUtil;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@Slf4j
public class ApiService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    StudentService studentService;

    @Autowired
    private ModelMapper mapper;

    public String constructUrlForFeeCollect() {
        StringBuilder builder = new StringBuilder();
        builder.append(SysConstant.HTTP).append(SysConstant.GATEWAY + "/").append(SysConstant.FEE_COLLECTION_SERVICE).append(SysConstant.FEE_COLLECTION_API);
        return builder.toString();
    }

    public String constructUrlForRecepit(int receiptId) {
        StringBuilder builder = new StringBuilder();
        builder.append(SysConstant.HTTP).append(SysConstant.GATEWAY + "/").append(SysConstant.FEE_COLLECTION_SERVICE).append(SysConstant.VIEW_RECEIPT_API + receiptId);
        return builder.toString();
    }

    @Retry(name = "default", fallbackMethod = "fallBackDefaultResponse")
    public ResponseEntity<CustomResponse> callReceiptServiceToPayTheFeeForTheStudent(FeeDTO feeDTO, Optional<Student> student) {

        ResponseEntity<CustomResponse> responseObject = restTemplate.postForEntity(constructUrlForFeeCollect(), feeDTO, CustomResponse.class);
        log.debug("checking response code {}", responseObject.getStatusCode());
        if (responseObject.getStatusCode().value() != 201) {
            log.error("collect fee services is failed due to {}" + responseObject.getBody());
            return new ResponseEntity<>(CustomResponse.sendError(responseObject.getBody().toString()), responseObject.getStatusCode());
        }
        return new ResponseEntity<>(CustomResponse.sendSuccess(responseObject.getBody().getMessage(), responseObject.getBody().getData()), HttpStatus.CREATED);
    }

    @Retry(name = "default", fallbackMethod = "fallBackDefaultResponse")
    public ResponseEntity<CustomResponse> generateReceiptForTheStudent(int receiptId) {
        ResponseEntity<CustomResponse> responseObject = restTemplate.getForEntity(constructUrlForRecepit(receiptId), CustomResponse.class);
        if (responseObject.getStatusCode().value() == HttpStatus.BAD_REQUEST.value()) {
            return new ResponseEntity<>(CustomResponse.sendError(responseObject.getBody().getMessage()), HttpStatus.BAD_REQUEST);
        }
        ReceiptDTO dto = mapper.map(responseObject.getBody().getData(), ReceiptDTO.class);
        Optional<Student> student = studentService.findByStudentId(dto.getStudentId());
        dto = studentService.populateReceipt(dto, student);
        return new ResponseEntity<>(CustomResponse.sendSuccess("Receipt Details", dto), HttpStatus.OK);
    }

    /**
     * default response if the collect-fee-service microservice is down
     *
     * @param ex
     * @return
     * @author Abdul Wahid
     * @version 1.0
     * @since 10/09/2023
     */
    public ResponseEntity<CustomResponse> fallBackDefaultResponse(Exception ex) {
        if (ex instanceof HttpClientErrorException) {
            return new ResponseEntity<>(CustomResponse.sendError(((HttpClientErrorException) ex).getStatusText()), HttpStatus.BAD_REQUEST);
        }
        log.info("fee collection services might be down hence sending default response " + ex.getMessage());
        ReceiptDTO receipt = ReceiptUtil.constructDefaultResponse();
        return new ResponseEntity<>(CustomResponse.sendSuccess("This is default Receipt Details response as the fee-collection-service is down", receipt), HttpStatus.OK);
    }

    public ResponseEntity<CustomResponse> checkWhetherFeeAlreadyPaidForStudent(int studentId) {

        String url = constructUrlToCheckWhetherFeeHasBeenCollected(studentId);
        ResponseEntity<CustomResponse> responseObject = restTemplate.getForEntity(url, CustomResponse.class);
        if (responseObject.getStatusCode().value() == HttpStatus.CONFLICT.value()) {
            return new ResponseEntity<>(CustomResponse.sendError("Fee has been paid"), HttpStatus.CONFLICT);
        } else if (responseObject.getStatusCode().value() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            return new ResponseEntity<>(CustomResponse.sendError("Internal Server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(responseObject.getBody(), HttpStatus.OK);
    }

    private String constructUrlToCheckWhetherFeeHasBeenCollected(int studentId) {
        StringBuilder builder = new StringBuilder();
        builder.append(SysConstant.HTTP).append(SysConstant.GATEWAY + "/").append(SysConstant.FEE_COLLECTION_SERVICE).append(SysConstant.CHECK_FEE_COLLECTION_API).append(studentId).append("/fee");
        return builder.toString();
    }

    private String constructUrlToCheckReceipt(int receiptId) {
        StringBuilder builder = new StringBuilder();
        builder.append(SysConstant.HTTP).append(SysConstant.GATEWAY + "/").append(SysConstant.FEE_COLLECTION_SERVICE).append(SysConstant.CHECK_RECEIPT_API).append(receiptId);
        return builder.toString();
    }

    public ResponseEntity<CustomResponse> checkReceiptAvailableForReceiptId(int receiptId) {
        return restTemplate.getForEntity(constructUrlToCheckReceipt(receiptId), CustomResponse.class);
    }
}
