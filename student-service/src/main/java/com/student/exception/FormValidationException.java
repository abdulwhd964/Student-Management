package com.student.exception;

import java.io.Serializable;

public class FormValidationException extends Exception implements Serializable {

    private static final long serialVersionUID = 7530694835545060358L;

    public FormValidationException(String message) {
        super(message);
    }
}
