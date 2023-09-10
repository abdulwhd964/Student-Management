package com.student;

import com.student.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestReceiptDao extends JpaRepository<Receipt,Integer> {
}
