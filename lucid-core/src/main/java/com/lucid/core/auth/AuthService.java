package com.lucid.core.auth;

import com.lucid.core.user.UserEntity;
import com.lucid.core.user.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean authenticateUser(String username, String password) {
        return userRepository.findByUserId(username)
                .map(user -> passwordMatches(password, user.getPassword()))
                .orElse(false);
    }

    public String registerUser(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return "Username and password are required";
        }

        if (userRepository.existsById(username)) {
            return "User already exists";
        }

        String encodedPassword = passwordEncoder.encode(password);
        userRepository.save(new UserEntity(username, encodedPassword));
        return "User registered successfully";
    }

    private boolean passwordMatches(String rawPassword, String storedEncodedPassword) {
        return passwordEncoder.matches(rawPassword, storedEncodedPassword);
    }
}
