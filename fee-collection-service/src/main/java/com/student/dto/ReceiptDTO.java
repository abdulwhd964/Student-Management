package com.student.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.student.constant.CardType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptDTO {

    private int id;
    private int studentId;
    private double amount;
    private String cardNumber;
    @Enumerated(EnumType.STRING)
    private CardType cardType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT +4")
    private Date receiptDate;

}
