package com.uniquindio.edu.edusoft.service.impl;


import com.uniquindio.edu.edusoft.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendCodeVerifaction(String email, String code) throws Exception {
        String subject = "Código de verificación EduSoft";
        String emailBody = """
        ¡Hola!
        
        Para completar tu registro en EduSoft, utiliza el siguiente código:

        👉 CÓDIGO: %s

        Este código expirará en 1 minuto.

        Atentamente, Equipo EduSoft, Universidad del Quindío
        """.formatted(code);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(emailBody);
        mailSender.send(message);
    }


    @Override
    public void SendMailHome(String email) throws Exception {

        String subject = "Gracias por registrarse a Edusoft";
        String emailBody = "¡Hola!\n\n" +
                "Gracias por registrarte en EduSoft, la plataforma académica de la Universidad del Quindío estamos muy felices de tenerte con nosotros.\n\n" +
                "Con EduSoft podrás acceder a herramientas diseñadas para apoyar tu aprendizaje, facilitar tu experiencia académica y mantenerte conectado con la comunidad universitaria.\n\n" +
                "Tu registro es el primer paso para vivir una experiencia innovadora en la educación.\n\n" +
                "¡Bienvenido a la familia UQ!\n\n" +
                "Atentamente, Equipo EduSoft, Universidad del Quindío";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(emailBody);

        mailSender.send(message);
    }

    @Override
    public void sendmailcourse(String email, String nameCourse) throws Exception {

        String subject = "Inscripción exitosa al curso - " + nameCourse;

        String emailBody = """
        ¡Hola!

        Te confirmamos que te has inscrito exitosamente en el curso: %s.

        A partir de ahora podrás acceder a todos los contenidos, actividades y recursos disponibles en la plataforma EduSoft de la Universidad del Quindío.

        Te deseamos muchos éxitos en tu proceso de aprendizaje.

        Atentamente, Equipo EduSoft, Universidad del Quindío
        """.formatted(nameCourse);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(emailBody);

        mailSender.send(message);
    }



}
