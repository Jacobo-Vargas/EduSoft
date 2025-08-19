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

    private final JavaMailSender mailSender;

    /**
     * Envía un correo al usuario con un código de verificación
     * para completar el registro en la plataforma
     * @param email
     * @param code
     * @throws Exception
     */
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

    /**
     * Envía un correo de bienvenida al estudiante
     * después de registrarse en la plataforma
     * @param email
     * @throws Exception
     */
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

    /* Envía un correo de confirmación al estudiante
     * cuando se inscribe en un curso */
    @Override
    public void sendmailcourse(String email, String nameCourse) throws Exception {
        String subject = "Inscripción exitosa al curso " + nameCourse;
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

    /* Envía un correo al profesor confirmando la solicitud
     * de registro de un curso en la plataforma */
    @Override
    public void sendMailTeacher(String email, String nameCourse) throws Exception {
        String subject = "Solicitud de registro del curso " + nameCourse;
        String emailBody = """
                ¡Hola!
                
                Te confirmamos que te has enviado la solicitud de regisitro del curso : %s exitosamente.
                
                A partir de ahora nustro equipo de auditores verificara el contenido del curso segun los estndares de la Universidad del Quindío.
                Pronto se te informara de si el curso necesita alguna modificacion o se publica correctamente
                
                Atentamente, Equipo EduSoft, Universidad del Quindío
                """.formatted(nameCourse);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(emailBody);

        mailSender.send(message);
    }

    /* Envía un correo al profesor con las modificaciones
     * solicitadas por los auditores en un curso */
    @Override
    public void sendMailAuditor(String email, String nameCourse, String information) throws Exception {
        String subject = "Solictud de modificacion del curso " + nameCourse;
        String emailBody = """
                
                Se solicita la modificaion en los siguentes puntos:
                
                %s
                
                al hacer la modificacion de estos cursos se volvera a verificar el curso
                
                Atentamente, Equipo EduSoft, Universidad del Quindío
                """.formatted(information);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(emailBody);

        mailSender.send(message);
    }

    /* Envía un correo al profesor confirmando que
     * su curso fue aprobado y publicado exitosamente */
    @Override
    public void sendMailAuditorOK(String email, String nameCourse) throws Exception {
        String subject = "Registro exitoso del curso " + nameCourse;
        String emailBody = """
                su curso fue aceptado y publicado correctamente en nustra plataforma.
                
                Atentamente, Equipo EduSoft, Universidad del Quindío
                """;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(emailBody);

        mailSender.send(message);
    }
}
