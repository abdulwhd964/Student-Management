package com.student.controller;

import com.student.dto.ReceiptDTO;
import com.student.entity.Receipt;
import com.student.exception.ReceiptNotFoundException;
import com.student.service.ReceiptService;
import com.student.util.CustomResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@Tag(name = "ReceiptController Controller", description = "Manages fee collection and receipts")
public class ReceiptController {
    public static final Logger log = LogManager.getLogger(ReceiptController.class);
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ReceiptService receiptService;

    /**
     * @param receiptId
     * @return
     * @throws ReceiptNotFoundException
     */
    @GetMapping("/receipts/{receiptid}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "checking receipt for the receipt")})
    public ResponseEntity<CustomResponse> checkReceiptForReceiptId(@PathVariable("receiptid") int receiptId) throws ReceiptNotFoundException {
        try {
            Receipt receipt = receiptService.findByReceiptId(receiptId);
            if (receipt == null) {
                throw new ReceiptNotFoundException("Receipt Not Found");
            }
            return new ResponseEntity<>(CustomResponse.sendSuccess("Receipt", receipt), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * @param receiptId
     * @return CustomResponse
     * @author Abdul Wahid
     * @version 1.0
     * @since 10/09/2023
     */
    @GetMapping("/fees/receipts/{receiptid}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "View Receipt for the student")})
    public ResponseEntity<CustomResponse> viewReceipt(@PathVariable("receiptid") int receiptId) throws ReceiptNotFoundException {
        try {
            Receipt receipt = receiptService.findByReceiptId(receiptId);
            ReceiptDTO dto = mapper.map(receipt, ReceiptDTO.class);
            return new ResponseEntity<>(CustomResponse.sendSuccess("Receipt Details", dto), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * checking whether student have paid the fee
     *
     * @param studentId
     * @return
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "checking whether student have paid the fee")})
    @GetMapping("/student/{studentid}/fee")
    public ResponseEntity<CustomResponse> checkFeeHasBeenPaidForTheStudent(@PathVariable("studentid") int studentId) {
        try {
            Receipt receipt = receiptService.findByStudentId(studentId);
            if (receipt != null) {
                return new ResponseEntity<>(CustomResponse.sendError("Fee has been paid. "), HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(CustomResponse.sendSuccess("receipt details", receipt), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * @param receiptDTO
     * @return CustomResponse
     * @author Abdul Wahid
     * @version 1.0
     * @since 10/09/2023
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @PostMapping("/fees/collect")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "paying the fee for the student")})
    public ResponseEntity<CustomResponse> collectFeesForStudent(@RequestBody ReceiptDTO receiptDTO) {
        try {
            log.info("collecting fee for the student id {} ", receiptDTO.getStudentId());
            Receipt receipt = mapper.map(receiptDTO, Receipt.class);
            receipt.setReceiptDate(new Date());
            ReceiptDTO dto = mapper.map(receiptService.save(receipt), ReceiptDTO.class);
            return new ResponseEntity(CustomResponse.sendSuccess("Payment is successfull , the reference Id is # "+dto.getId(), dto.getId()), HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
