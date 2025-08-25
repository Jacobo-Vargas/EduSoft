package com.uniquindio.edu.edusoft.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

public class ResponseData<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean success;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private T data;

    private T additionalData;

    private String message;

    private String type_message;

    public ResponseData(T data, String message) {
        this.data = data;
        this.message = message;
    }

    public ResponseData(T data, String message, String type_message) {
        this.data = data;
        this.message = message;
        this.type_message = type_message;
    }

    public ResponseData(T data, T additionalData, String message, String type_message) {
        this.data = data;
        this.additionalData = additionalData;
        this.message = message;
        this.type_message = type_message;
    }

    public Boolean getSuccess() {
        if (success != null) {
            return success;
        } else {
            return true;
        }
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType_message() {
        if (type_message != null) {
            return type_message;
        } else {
            return "success";
        }
    }

    public void setType_message(String type_message) {
        this.type_message = type_message;
    }

    public T getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(T additionalData) {
        this.additionalData = additionalData;
    }

    @Override
    public String toString() {
        return "ResponseData{" + "success=" + success + ", data=" + data + "additionalData" + additionalData
                + ", message=" + message + ", type_message=" + type_message + '}';
    }

}
