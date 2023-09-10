package com.student.dao;

import com.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentDAO extends CrudRepository<Student, Integer>, JpaRepository<Student, Integer> {

    Student findByStudentName(String studentName);
}
