package com.student.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.student.constant.CardType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int studentId;

    private String studentName;

    private int reference;


    private String cardNumber;

    @Enumerated(EnumType.STRING)
    private CardType cardType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT +4")
    private Date receiptDate;

}
