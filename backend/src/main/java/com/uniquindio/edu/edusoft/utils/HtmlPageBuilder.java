package com.uniquindio.edu.edusoft.utils;

import org.springframework.stereotype.Component;

/**
 * Utilidad para generar páginas HTML de respuesta
 */
@Component
public class HtmlPageBuilder {

    /**
     * Genera una página HTML de verificación exitosa
     */
    public String buildSuccessPage() {
        return """
            <!DOCTYPE html>
            <html lang="es">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Cuenta Verificada - EduSoft</title>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        background: linear-gradient(135deg, #004b23, #006400);
                        margin: 0;
                        padding: 0;
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        min-height: 100vh;
                        color: #333;
                    }
                    .container {
                        background: white;
                        border-radius: 15px;
                        box-shadow: 0 10px 30px rgba(0,0,0,0.2);
                        padding: 40px;
                        text-align: center;
                        max-width: 500px;
                        width: 90%;
                    }
                    .success-icon {
                        font-size: 4rem;
                        color: #28a745;
                        margin-bottom: 20px;
                    }
                    h1 {
                        color: #004b23;
                        margin-bottom: 20px;
                    }
                    p {
                        color: #666;
                        margin-bottom: 30px;
                        line-height: 1.6;
                    }
                    .btn {
                        display: inline-block;
                        background: #004b23;
                        color: white;
                        padding: 12px 30px;
                        text-decoration: none;
                        border-radius: 25px;
                        font-weight: bold;
                        transition: background 0.3s;
                    }
                    .btn:hover {
                        background: #006400;
                    }
                    .footer {
                        margin-top: 30px;
                        font-size: 12px;
                        color: #999;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="success-icon">✅</div>
                    <h1>¡Cuenta Verificada!</h1>
                    <p>Tu cuenta ha sido verificada exitosamente. Ya puedes acceder a todas las funcionalidades de EduSoft.</p>
                    <a href="http://localhost:3000/login" class="btn">Iniciar Sesión</a>
                    <div class="footer">
                        <p>© 2025 EduSoft - Universidad del Quindío</p>
                    </div>
                </div>
                <script>
                    // Cerrar automáticamente después de 8 segundos (opcional)
                    setTimeout(function() {
                        window.close();
                    }, 8000);
                </script>
            </body>
            </html>
            """;
    }

    /**
     * Genera una página HTML de error de verificación
     */
    public String buildErrorPage(String message) {
        return """
            <!DOCTYPE html>
            <html lang="es">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Error de Verificación - EduSoft</title>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        background: linear-gradient(135deg, #dc3545, #c82333);
                        margin: 0;
                        padding: 0;
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        min-height: 100vh;
                        color: #333;
                    }
                    .container {
                        background: white;
                        border-radius: 15px;
                        box-shadow: 0 10px 30px rgba(0,0,0,0.2);
                        padding: 40px;
                        text-align: center;
                        max-width: 500px;
                        width: 90%;
                    }
                    .error-icon {
                        font-size: 4rem;
                        color: #dc3545;
                        margin-bottom: 20px;
                    }
                    h1 {
                        color: #dc3545;
                        margin-bottom: 20px;
                    }
                    p {
                        color: #666;
                        margin-bottom: 30px;
                        line-height: 1.6;
                    }
                    .btn {
                        display: inline-block;
                        background: #dc3545;
                        color: white;
                        padding: 12px 30px;
                        text-decoration: none;
                        border-radius: 25px;
                        font-weight: bold;
                        transition: background 0.3s;
                        margin: 10px;
                    }
                    .btn:hover {
                        background: #c82333;
                    }
                    .footer {
                        margin-top: 30px;
                        font-size: 12px;
                        color: #999;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="error-icon">❌</div>
                    <h1>Error de Verificación</h1>
                    <p>%s</p>
                    <a href="http://localhost:3000/register" class="btn">Registrarse de nuevo</a>
                    <a href="mailto:soporte@edusoft.uniquindio.edu.co" class="btn">Contactar Soporte</a>
                    <div class="footer">
                        <p>© 2025 EduSoft - Universidad del Quindío</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(message);
    }
}