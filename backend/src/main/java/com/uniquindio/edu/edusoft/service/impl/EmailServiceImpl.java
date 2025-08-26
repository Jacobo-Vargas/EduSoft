package com.uniquindio.edu.edusoft.service.impl;

import com.uniquindio.edu.edusoft.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    /**
     * platilla pra correos
     *
     * @param title
     * @param bodyContent
     * @return
     */
    private String buildTemplate(String title, String bodyContent) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                  <meta charset="UTF-8">
                  <style>
                    body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }
                    .container { max-width: 600px; margin: auto; background: #ffffff; border-radius: 12px;
                                 box-shadow: 0 4px 12px rgba(0,0,0,0.1); padding: 20px; }
                    .header { background: #004b23; color: white; text-align: center; padding: 15px;
                              border-radius: 12px 12px 0 0; }
                    .content { padding: 20px; font-size: 14px; color: #333; line-height: 1.5; }
                    .highlight { font-size: 22px; font-weight: bold; color: #006400; margin: 20px 0; text-align: center; }
                    .button { display: inline-block; padding: 12px 20px; background: #004b23; color: white;
                              text-decoration: none; border-radius: 25px; margin-top: 20px; }
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

    @Override
    public void sendCodeVerifaction(String email, String code) throws Exception {
        String subject = "Código de verificación EduSoft";
        String content = """
                    <p>Hola 👋,</p>
                    <p>Para completar tu registro en <b>EduSoft</b>, utiliza el siguiente código:</p>
                    <div class="highlight">%s</div>
                    <p>⚠️ Este código expirará en 1 minuto.</p>
                """.formatted(code);

        sendHtmlMail(email, subject, buildTemplate("Verificación de cuenta", content));
    }

    @Override
    public void SendMailHome(String email) throws Exception {
        String subject = "¡Bienvenido a EduSoft - Universidad del Quindío!";
        String content = """
                    <p>Hola 👋,</p>
                    <p>Gracias por registrarte en <b>EduSoft</b>, la plataforma académica de la <b>Universidad del Quindío</b>. 
                    Estamos muy felices de tenerte con nosotros.</p>
                    <p>Con EduSoft podrás acceder a herramientas diseñadas para apoyar tu aprendizaje y mantenerte conectado con la comunidad universitaria.</p>
                """;

        sendHtmlMail(email, subject, buildTemplate("Bienvenido a EduSoft", content));
    }

    @Override
    public void sendmailcourse(String email, String nameCourse) throws Exception {
        String subject = "Inscripción exitosa al curso " + nameCourse;
        String content = """
                    <p>Hola 👋,</p>
                    <p>Te confirmamos que te has inscrito exitosamente en el curso:</p>
                    <div class="highlight">%s</div>
                    <p>A partir de ahora podrás acceder a todos los contenidos, actividades y recursos disponibles en la plataforma EduSoft de la Universidad del Quindío.</p>
                    <p>Te deseamos muchos éxitos en tu proceso de aprendizaje. 📘</p>
                """.formatted(nameCourse);

        sendHtmlMail(email, subject, buildTemplate("Inscripción confirmada", content));
    }

    @Override
    public void sendMailTeacher(String email, String nameCourse) throws Exception {
        String subject = "Solicitud de registro del curso " + nameCourse;
        String content = """
                    <p>Hola 👋,</p>
                    <p>Te confirmamos que se ha enviado la solicitud de registro del curso:</p>
                    <div class="highlight">%s</div>
                    <p>Nuestro equipo de auditores verificará el contenido del curso según los estándares de la Universidad del Quindío.</p>
                    <p>Pronto se te informará si el curso necesita modificaciones o si fue publicado correctamente.</p>
                """.formatted(nameCourse);

        sendHtmlMail(email, subject, buildTemplate("Solicitud recibida", content));
    }

    @Override
    public void sendMailAuditor(String email, String nameCourse, String information) throws Exception {
        String subject = "Solicitud de modificación del curso " + nameCourse;
        String content = """
                    <p>Hola 👋,</p>
                    <p>Se solicita realizar las siguientes modificaciones en el curso:</p>
                    <div class="highlight">%s</div>
                    <p>Una vez aplicadas, el curso será nuevamente verificado por nuestros auditores.</p>
                """.formatted(information);

        sendHtmlMail(email, subject, buildTemplate("Modificaciones requeridas", content));
    }

    @Override
    public void sendMailAuditorOK(String email, String nameCourse) throws Exception {
        String subject = "Registro exitoso del curso " + nameCourse;
        String content = """
                    <p>Hola 👋,</p>
                    <p>¡Excelente noticia! 🎉</p>
                    <p>Tu curso:</p>
                    <div class="highlight">%s</div>
                    <p>ha sido aceptado y publicado correctamente en nuestra plataforma.</p>
                """.formatted(nameCourse);

        sendHtmlMail(email, subject, buildTemplate("Curso aprobado", content));
    }

    /**
     * metodo para enviar correos
     *
     * @param to
     * @param subject
     * @param htmlContent
     * @throws Exception
     */
    private void sendHtmlMail(String to, String subject, String htmlContent) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    @Override
    public void sendCodeVerifactionPassword(String email, String code) throws Exception {
        String subject = "Código de verificación EduSoft";
        String content = """
                    <p>Hola 👋,</p>
                    <p>Para completar tu cambio de contraseña en <b>EduSoft</b>, utiliza el siguiente código:</p>
                    <div class="highlight">%s</div>
                """.formatted(code);

        sendHtmlMail(email, subject, buildTemplate("Verificación de cuenta", content));
    }
}
