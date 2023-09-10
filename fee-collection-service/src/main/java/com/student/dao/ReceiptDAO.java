package com.student.dao;

import com.student.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptDAO extends CrudRepository<Receipt, Integer>, JpaRepository<Receipt, Integer> {
    Receipt findById(int id);

    Receipt findByStudentId(int student);
}
