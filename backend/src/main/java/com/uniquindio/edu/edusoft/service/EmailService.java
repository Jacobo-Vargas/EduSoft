package com.uniquindio.edu.edusoft.service;

public interface EmailService {

    void sendCodeVerifaction(String email, String code) throws Exception;
    void SendMailHome(String email) throws Exception;
    void sendmailcourse(String email, String nameCourse) throws Exception;



}

