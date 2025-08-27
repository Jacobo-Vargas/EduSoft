package com.uniquindio.edu.edusoft.utils;

import java.util.Random;

public class CodeGenerator {

    // Método estático para generar un código de 4 dígitos aleatorios
    public static String generateCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000); // Genera un número entre 1000 y 9999
        return String.valueOf(code); // Devuelve el código como un String
    }
}