package com.uniquindio.edu.edusoft.service.impl;

import com.uniquindio.edu.edusoft.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void SendMailHome(String email, String token) throws Exception {
        String subject = "¡Bienvenido a EduSoft - Universidad del Quindío!";
        String verifyLink = "https://localhost:8443/api/users/verify?token=" + token;

        String content = """
            <p>Hola,</p>
            <p>Gracias por registrarte en <b>EduSoft</b>, la plataforma académica de la <b>Universidad del Quindío</b>.</p>
            <p><strong>Para activar tu cuenta, haz clic en el botón de abajo:</strong></p>
            <div style="text-align: center; margin: 30px 0;">
                <a href="%s" style="display: inline-block; padding: 15px 30px; background: #004b23; color: white; text-decoration: none; border-radius: 25px; font-weight: bold; font-size: 16px;"> Verificar mi cuenta </a>
            </div>
            <p style="font-size: 12px; color: #666; margin-top: 20px;">
                <strong>Nota:</strong> Al hacer clic se abrirá una ventana que se cerrará automáticamente después de verificar tu cuenta.
            </p>
        """.formatted(verifyLink);

        sendHtmlMail(email, subject, buildTemplate("Bienvenido a EduSoft", content));
    }

    @Override
    public void sendVerificationConfirmation(String email) throws Exception {
        String subject = "¡Cuenta verificada exitosamente! - EduSoft";
        String content = """
            <div style="text-align: center; margin: 30px 0;">
                <div style="font-size: 48px; margin-bottom: 20px;">✅</div>
                <h2 style="color: #004b23;">¡Cuenta Verificada!</h2>
            </div>
            <p>Tu cuenta de EduSoft ha sido verificada exitosamente.</p>
        """;
        sendHtmlMail(email, subject, buildTemplate("Verificación Completada", content));
    }

    @Override
    public void sendCodeVerifaction(String email, String code) throws Exception {
        String subject = "Código de verificación EduSoft";
        String content = """
            <p>Hola 👋,</p>
            <p>Utiliza el siguiente código:</p>
            <div class="highlight">%s</div>
            <p>⚠️ Este código expirará en 1 minuto.</p>
        """.formatted(code);
        sendHtmlMail(email, subject, buildTemplate("Verificación de cuenta", content));
    }

    @Override
    public void sendCodeVerifactionPassword(String email, String code) throws Exception {
        String subject = "Código de verificación EduSoft";
        String content = """
            <p>Hola 👋,</p>
            <p>Para completar tu cambio de contraseña en <b>EduSoft</b>, utiliza el siguiente código:</p>
            <div class="highlight">%s</div>
        """.formatted(code);

        sendHtmlMail(email, subject, buildTemplate("Cambio de contraseña", content));
    }

    @Override
    public void sendmailcourse(String email, String nameCourse) throws Exception {
        String subject = "Inscripción exitosa al curso " + nameCourse;
        String content = """
            <p>Hola 👋,</p>
            <p>Te confirmamos que te has inscrito exitosamente en el curso:</p>
            <div class="highlight">%s</div>
        """.formatted(nameCourse);
        sendHtmlMail(email, subject, buildTemplate("Inscripción confirmada", content));
    }

    @Override
    public void sendMailTeacher(String email, String nameCourse) throws Exception {
        String subject = "Solicitud de registro del curso " + nameCourse;
        String content = """
            <p>Hola 👋,</p>
            <p>Se ha enviado la solicitud del curso:</p>
            <div class="highlight">%s</div>
        """.formatted(nameCourse);
        sendHtmlMail(email, subject, buildTemplate("Solicitud recibida", content));
    }

    @Override
    public void sendMailAuditor(String email, String nameCourse, String information) throws Exception {
        String subject = "Solicitud de modificación del curso " + nameCourse;
        String content = """
            <p>Hola 👋,</p>
            <p>Se solicita realizar las siguientes modificaciones:</p>
            <div class="highlight">%s</div>
        """.formatted(information);
        sendHtmlMail(email, subject, buildTemplate("Modificaciones requeridas", content));
    }

    @Override
    public void sendMailAuditorOK(String email, String nameCourse) throws Exception {
        String subject = "Registro exitoso del curso " + nameCourse;
        String content = """
            <p>Hola 👋,</p>
            <p>¡Tu curso ha sido publicado correctamente!</p>
            <div class="highlight">%s</div>
        """.formatted(nameCourse);
        sendHtmlMail(email, subject, buildTemplate("Curso aprobado", content));
    }

    // ---------------------------
    private void sendHtmlMail(String to, String subject, String htmlContent) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    private String buildTemplate(String title, String bodyContent) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }
                    .container { max-width: 600px; margin: auto; background: #ffffff; border-radius: 12px; padding: 20px; }
                    .header { background: #004b23; color: white; text-align: center; padding: 15px; border-radius: 12px 12px 0 0; }
                    .content { padding: 20px; font-size: 14px; color: #333; }
                    .highlight { font-size: 22px; font-weight: bold; color: #006400; margin: 20px 0; text-align: center; }
                    .footer { margin-top: 20px; font-size: 12px; color: #777; text-align: center; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header"><h2>%s</h2></div>
                    <div class="content">%s</div>
                    <div class="footer">© 2025 EduSoft - Universidad del Quindío</div>
                </div>
            </body>
            </html>
        """.formatted(title, bodyContent);
    }
}