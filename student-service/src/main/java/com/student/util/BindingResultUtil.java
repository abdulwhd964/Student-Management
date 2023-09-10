package com.student.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

public class BindingResultUtil {
    public static final Logger log = LogManager.getLogger(BindingResultUtil.class);

    public static CustomResponse validate(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("Form Validation Error occurred");
            return CustomResponse.formValidation("Form Validation Errors",
                    getDefaultMessageFromFieldError(bindingResult.getFieldErrors()));
        } else {
            return null;
        }
    }

    private static List<String> getDefaultMessageFromFieldError(List<FieldError> errors) {
        List<String> messages = new ArrayList<>();
        for (FieldError error : errors) {
            messages.add(error.getDefaultMessage());
        }
        return messages;
    }

    private BindingResultUtil() {

    }

}
