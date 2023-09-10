package com.student.controller;

import com.student.dto.StudentDTO;
import com.student.entity.Student;
import com.student.exception.StudentNotFoundException;
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
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/students")
@Tag(name = "Student Controller", description = "API's to insert student and get student record")
public class StudentController {

    public static final Logger log = LogManager.getLogger(StudentController.class);
    @Autowired
    private StudentService studentService;
    @Autowired
    private ModelMapper mapper;

    /**
     * @param studentId
     * @return Student Object
     * @throws StudentNotFoundException
     * @author Abdul Wahid
     * @version 1.0
     * @since 10/09/2023
     */
    @GetMapping("/{studentId}")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Getting one single student object")})
    public ResponseEntity<StudentDTO> getStudent(@PathVariable("studentId") int studentId) throws StudentNotFoundException {
        try {
            Optional<Student> student = studentService.findByStudentId(studentId);
            log.info("Inside get Student method");
            if (student.isEmpty()) {
                throw new StudentNotFoundException("Student Not Found, please provide an valid student id");
            }
            StudentDTO studentDTO = mapper.map(student.get(), StudentDTO.class);
            return new ResponseEntity<>(studentDTO, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * @param studentDTO
     * @param result
     * @return
     * @author Abdul Wahid
     * @version 1.0
     * @since 10/09/2023
     */
    @PostMapping
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Inserting student record")})
    public ResponseEntity<CustomResponse> insertStudent(@Valid @RequestBody StudentDTO studentDTO,
                                                        BindingResult result) {

        log.info("checking whether student name already exists in DB");
        Student student = studentService.findByStudentName(studentDTO.getStudentName());
        if (student != null) {
            log.debug("Student name exists");
            return new ResponseEntity<>(CustomResponse.sendError("Student name already exists"), HttpStatus.CONFLICT);
        }
        try {
            log.info("checking form validation");
            CustomResponse response = BindingResultUtil.validate(result);
            if (response != null) {
                log.debug("Form validation occurred {}", response.getData());
                // handling multipe error message in the response.getData()
                return new ResponseEntity<>(CustomResponse.sendData("Validation Error", response.getData()),
                        HttpStatus.BAD_REQUEST);
            }
            Student studentObject = mapper.map(studentDTO, Student.class);
            studentDTO = mapper.map(studentService.save(studentObject), StudentDTO.class);

            return new ResponseEntity<>(CustomResponse.sendSuccess("Student inserted successfully", studentDTO),
                    HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
