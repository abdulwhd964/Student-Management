package com.student.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.student.constant.CardType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeeDTO {

    @JsonProperty(required = false)
    private int id;
    @Min(value = 1, message = "please provide student id")
    private Integer studentId;
    @NotBlank(message = "Card number should not be empty")
    private String cardNumber;
    @Enumerated(EnumType.STRING)
    private CardType cardType;

    private Double amount;


}
