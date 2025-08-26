package com.uniquindio.edu.edusoft.utils;

public class HtmlTemplates {

    public static String verificationSuccessPage() {
        return """
        <html><head><title>Verificando...</title></head>
        <body style="display:flex;justify-content:center;align-items:center;height:100vh;font-family:Arial;">
            <div style="text-align:center;">
                <div style="color:#28a745;font-size:20px;">✓ Cuenta verificada</div>
                <div style="color:#666;">Cerrando en 3 segundos...</div>
            </div>
            <script>
                let seconds = 3;
                const countdown = setInterval(() => {
                    seconds--;
                    document.querySelector("div[style*='color:#666']").innerText = "Cerrando en " + seconds + " segundos...";
                    if(seconds === 0) {
                        clearInterval(countdown);
                        window.close();
                        if(!window.closed) {
                            document.body.innerHTML = '<div style="text-align:center;margin-top:50px;"><h3>Cuenta verificada exitosamente</h3><p>Puedes cerrar esta ventana</p></div>';
                        }
                    }
                }, 3000);
            </script>
        </body></html>
        """;
    }

    public static String verificationErrorPage() {
        return """
        <html><body style="text-align:center;margin-top:50px;font-family:Arial;">
            <h3 style="color:red;">❌ Error al verificar cuenta</h3>
            <p>Esta ventana se cerrará automáticamente.</p>
            <script>setTimeout(() => window.close(), 3000);</script>
        </body></html>
        """;
    }
}