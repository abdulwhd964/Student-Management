package com.student.service;

import com.student.dao.ReceiptDAO;
import com.student.entity.Receipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReceiptService {

    @Autowired
    private ReceiptDAO receiptDAO;


    public Receipt save(Receipt receipt) {
        return receiptDAO.save(receipt);
    }

    public Receipt findByReceiptId(int receiptId) {
        return receiptDAO.findById(receiptId);
    }

    public Receipt findByStudentId(int student) {
        return receiptDAO.findByStudentId(student);
    }
}
