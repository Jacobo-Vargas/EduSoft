package com.uniquindio.edu.edusoft.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

public class BaseResponse {


    public static ResponseEntity response(String message) {

        var responseData = new ResponseData<>(new ArrayList<>(), message);
        // Return the answer
        return ResponseEntity.ok(responseData);
    }

    public static ResponseEntity response(String message, String typeMessage) {

        var responseData = new ResponseData<>(new ArrayList<>(), message, typeMessage);
        // Return the answer
        return ResponseEntity.ok(responseData);
    }

    public static <T extends Object> ResponseEntity response(T data, String message) {

        var responseData = new ResponseData<T>(data, message);
        // Return the answer
        return ResponseEntity.ok(responseData);
    }

    public static <T extends Object> ResponseEntity response(T data, String message, String typeMessage) {

        var responseData = new ResponseData<T>(data, message, typeMessage);
        // Return the answer
        return ResponseEntity.ok(responseData);
    }

    public static <T extends Object> ResponseEntity response(T data, T additionalData, String message,
                                                             String typeMessage) {

        var responseData = new ResponseData<T>(data, additionalData, message, typeMessage);
        // Return the answer
        return ResponseEntity.ok(responseData);
    }

    public static <T extends Object> ResponseEntity response(T data, String message, String typeMessage, HttpStatus status) {

        var responseData = new ResponseData<T>(data, message, typeMessage);
        // Return the answer
        return ResponseEntity.status(status).body(responseData);
    }

}
