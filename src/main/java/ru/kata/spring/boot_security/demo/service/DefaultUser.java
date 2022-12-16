package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service

public class DefaultUser {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public DefaultUser(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    @Transactional
    public void defaultUsers () {
        Set<Role> rolesAdmin = new HashSet<>();
        Role adminRole = new Role("ROLE_ADMIN");
        User adminUser = new User("admin", passwordEncoder.encode("admin"), "Zainetdinova", 26, "mail");

        rolesAdmin.add(adminRole);
        adminUser.setRoles(rolesAdmin);
        roleRepository.save(adminRole);
        userRepository.save(adminUser);


        Set<Role> rolesUser = new HashSet<>();
        Role userRole = new Role("ROLE_USER");
        User userUser = new User("user", passwordEncoder.encode("admin"), "Dubov", 33, "mail");

        rolesUser.add(userRole);
        userUser.setRoles(rolesUser);
        roleRepository.save(userRole);
        userRepository.save(userUser);

        Set<Role> rolesCombo = new HashSet<>();
        User userCombo = new User("combo", passwordEncoder.encode("combo"), "Galeeva", 25,"mail");
        rolesCombo.add(userRole);
        rolesCombo.add(adminRole);
        userCombo.setRoles(rolesCombo);
        userRepository.save(userCombo);
    }
}


