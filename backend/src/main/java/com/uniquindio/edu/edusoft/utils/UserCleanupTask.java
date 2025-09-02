package com.uniquindio.edu.edusoft.utils;

import com.uniquindio.edu.edusoft.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import java.util.Date;
import java.util.Calendar;

@Component
@RequiredArgsConstructor
public class UserCleanupTask {

    private final UserRepository userRepository;

    // Se ejecuta cada 30 minutos
    @Scheduled(fixedRate = 30 * 60 * 1000)
    public void removeUnverifiedUsers() {
        Date thirtyMinutesAgo = new Date(System.currentTimeMillis() - 30 * 60 * 1000);

        userRepository.deleteAllByVerificationFalseAndCreatedAtBefore(thirtyMinutesAgo);
        System.out.println("Usuarios no verificados eliminados antes de: " + thirtyMinutesAgo);
    }
}
