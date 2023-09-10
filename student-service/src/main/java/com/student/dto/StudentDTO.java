package com.student.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StudentDTO {

    @JsonProperty(required = false)
    private int id;

    @NotBlank(message = "Student name is mandatory")
    @Size(min = 0, max = 255, message = "Student name should be lesser than 255 characters")
    private String studentName;

    @NotBlank(message = "Grade is mandatory")
    @Size(min = 0, max = 255, message = "Grade should be lesser than 255 characters")
    private String grade;

    @NotBlank(message = "Mobile mumber is mandatory")
    @Size(min = 0, max = 13, message = "Mobile number should be lesser than 13 characters")
    private String mobileNumber;

    @NotBlank(message = "School name is mandatory")
    @Size(min = 1, max = 255, message = "School Name should be lesser than 255 characters")
    private String schoolName;

}
