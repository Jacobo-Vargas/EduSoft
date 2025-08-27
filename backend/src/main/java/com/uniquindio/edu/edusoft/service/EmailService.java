package com.uniquindio.edu.edusoft.service;

public interface EmailService {
    void SendMailHome(String email, String token) throws Exception;
    void sendVerificationConfirmation(String email) throws Exception;
    void sendCodeVerifaction(String email, String code) throws Exception;
    void sendmailcourse(String email, String nameCourse) throws Exception;
    void sendMailTeacher(String email, String nameCourse) throws Exception;
    void sendMailAuditor(String email, String nameCourse, String information) throws Exception;
    void sendMailAuditorOK(String email, String nameCourse) throws Exception;
    void sendCodeVerifactionPassword(String email, String code) throws Exception;
}
