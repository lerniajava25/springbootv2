package org.example.helpdesk.config;

import org.example.helpdesk.entity.Role;
import org.example.helpdesk.entity.User;
import org.example.helpdesk.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository users,
                           PasswordEncoder passwordEncoder) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (users.count() == 0) {
            users.save(new User("admin", passwordEncoder.encode("admin"), Role.ADMIN));
            users.save(new User("agent", passwordEncoder.encode("agent"), Role.AGENT));
            users.save(new User("user", passwordEncoder.encode("user"), Role.USER));
        }
    }
}
