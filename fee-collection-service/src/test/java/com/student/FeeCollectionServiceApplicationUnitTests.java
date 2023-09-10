package com.student;

import com.student.constant.CardType;
import com.student.entity.Receipt;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FeeCollectionServiceApplicationUnitTests {

    @MockBean
    TestReceiptDao testReceiptDao;

    @Test
    void testCollectFee() {
        Receipt receipt = new Receipt(0, 1, 10d, "123456", CardType.MASTER, new Date());
        Mockito.when(testReceiptDao.save(receipt)).thenReturn(receipt);
        assertEquals(receipt, testReceiptDao.save(receipt));
    }

    @Test
    void testViewReceipts() {
        List<Receipt> receiptList = new ArrayList<>();
        receiptList.add(new Receipt(0, 1, 10d, "123456", CardType.MASTER, new Date()));
        receiptList.add(new Receipt(0, 2, 20d, "765788", CardType.MASTER, new Date()));
        Mockito.when(testReceiptDao.findAll()).thenReturn(receiptList);
        assertEquals(2, testReceiptDao.findAll().size());
    }

}
