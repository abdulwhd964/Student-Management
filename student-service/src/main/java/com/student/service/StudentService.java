package com.student.service;

import com.student.dao.StudentDAO;
import com.student.dto.ReceiptDTO;
import com.student.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class StudentService {

    @Autowired
    private StudentDAO studentDao;

    public Optional<Student> findByStudentId(int studentId) {
        return studentDao.findById(studentId);
    }

    public Student save(Student student) {
        return studentDao.save(student);
    }

    public ReceiptDTO populateReceipt(ReceiptDTO receipt, Optional<Student> student) {
        ReceiptDTO newReceipt = new ReceiptDTO();
        if (student.isPresent()) {
            newReceipt.setId(receipt.getId());
            newReceipt.setStudentId(receipt.getStudentId());
            newReceipt.setStudentName(student.get().getStudentName());
            newReceipt.setCardNumber(receipt.getCardNumber());
            newReceipt.setCardType(receipt.getCardType());
            newReceipt.setReceiptDate(receipt.getReceiptDate());
            newReceipt.setReference(receipt.getId());
            return newReceipt;
        }
        return receipt;
    }

    public Student findByStudentName(String studentName) {
        return studentDao.findByStudentName(studentName);
    }
}
