package com.uniquindio.edu.edusoft;
import com.uniquindio.edu.edusoft.model.entities.User;
import com.uniquindio.edu.edusoft.model.enums.EnumUserType;
import com.uniquindio.edu.edusoft.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User buildUser(String document, String email, String phone) {
        User user = new User();
        user.setDocumentNumber(document);
        user.setName("Test User");
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress("Calle Falsa 123");
        user.setPassword("password");
        user.setUserType(EnumUserType.ESTUDIANTE);
        user.setVerification(false);
        user.setVerificationToken("token123");
        return user;
    }

    @Test
    void testFindByEmail() {
        User user = buildUser("1001", "test1@uqvirtual.edu.co", "3100000001");
        userRepository.save(user);

        Optional<User> result = userRepository.findByEmail("test1@uqvirtual.edu.co");

        assertTrue(result.isPresent());
        assertEquals("1001", result.get().getDocumentNumber());
    }

    @Test
    void testFindByDocumentNumber() {
        User user = buildUser("1002", "test2@uqvirtual.edu.co", "3100000002");
        userRepository.save(user);

        Optional<User> result = userRepository.findByDocumentNumber("1002");

        assertTrue(result.isPresent());
        assertEquals("test2@uqvirtual.edu.co", result.get().getEmail());
    }

    @Test
    void testFindByEmailOrDocumentNumber_Email() {
        User user = buildUser("1003", "test3@uqvirtual.edu.co", "3100000003");
        userRepository.save(user);

        Optional<User> result = userRepository.findByEmailOrDocumentNumber("test3@uqvirtual.edu.co", "9999");

        assertTrue(result.isPresent());
        assertEquals("1003", result.get().getDocumentNumber());
    }

    @Test
    void testFindByEmailOrDocumentNumber_Document() {
        User user = buildUser("1004", "test4@uqvirtual.edu.co", "3100000004");
        userRepository.save(user);

        Optional<User> result = userRepository.findByEmailOrDocumentNumber("wrong@email.com", "1004");

        assertTrue(result.isPresent());
        assertEquals("test4@uqvirtual.edu.co", result.get().getEmail());
    }

    @Test
    void testFindByVerificationToken() {
        User user = buildUser("1005", "test5@uqvirtual.edu.co", "3100000005");
        user.setVerificationToken("verif-token-123");
        userRepository.save(user);

        Optional<User> result = userRepository.findByVerificationToken("verif-token-123");

        assertTrue(result.isPresent());
        assertEquals("1005", result.get().getDocumentNumber());
    }
}