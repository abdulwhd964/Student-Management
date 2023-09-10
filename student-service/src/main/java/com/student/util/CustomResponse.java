package com.student.util;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class CustomResponse {

    private String message;
    private Object data;

    public CustomResponse() {
    }

    public CustomResponse(String msg) {
        this.message = msg;
    }

    public CustomResponse(Object data) {
        this.data = data;
    }

    public CustomResponse(String msg, Object object) {
        this.message = msg;
        this.data = object;
    }

    public static CustomResponse sendSuccess(String message, Object data) {
        return new CustomResponse(message, data);
    }

    public static CustomResponse sendSuccess(String message) {
        return new CustomResponse(message);
    }

    public static CustomResponse sendData(String message, Object data) {
        return new CustomResponse(message, data);
    }

    public static CustomResponse errorValidation(String message) {
        return new CustomResponse(message);
    }

    public static CustomResponse formValidation(String message, Object data) {
        return new CustomResponse(message, data);
    }

    public static CustomResponse sendError(String message) {
        return new CustomResponse(message);
    }

    public static CustomResponse sendError(String message, Object data) {
        return new CustomResponse(message, data);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
