package com.student;

import com.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestStudentDao extends JpaRepository<Student,Integer> {

}
