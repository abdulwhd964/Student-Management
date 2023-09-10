package com.student.util;

import com.student.constant.CardType;
import com.student.dto.ReceiptDTO;

import java.util.Date;

public class ReceiptUtil {
	public static ReceiptDTO constructDefaultResponse() {
		return new ReceiptDTO(1,1,"Abdul",1234567,"123",CardType.VISA,new Date());
	}
	private ReceiptUtil(){

	}

}
