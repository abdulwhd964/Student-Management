package com.student;

import com.student.dao.StudentDAO;
import com.student.entity.Student;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class StudentServiceApplicationUnitTest {
    @MockBean
    private StudentDAO studentDAO;

    @Test
    void testSaveStudent() {
        Student student = new Student(0, "Abdul", "C", "507377412", "Sky");
        Mockito.when(studentDAO.save(student)).thenReturn(student);
        assertEquals(student, studentDAO.save(student));
    }

    @Test
    void testGetStudent() {
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(0, "Abdul", "C", "507377412", "Sky"));
        studentList.add(new Student(0, "Wahid", "C", "9940566527", "Sky"));

        Mockito.when(studentDAO.findAll()).thenReturn(studentList);
        assertEquals(2, studentDAO.findAll().size());
    }

}
