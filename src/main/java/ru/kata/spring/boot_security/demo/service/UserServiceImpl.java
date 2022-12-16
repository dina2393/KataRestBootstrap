package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }


    @Transactional
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

//    @Transactional
//    public void saveUser(User user) {
//        Role roleUser = roleRepository.findRoleByRole("ROLE_USER");
//        user.addRoleToUser(roleUser);
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        userRepository.save(user);
//    }
@Transactional
public void saveNewUser(User user, String role) {
    Set<Role> roles = new HashSet<>();
    if(role==null) {
        if(roleRepository.findRoleByRole("ROLE_USER")==null) {
            roleRepository.save(new Role("ROLE_USER"));
            roles.add(roleRepository.findRoleByRole("ROLE_USER"));
        } else {
            Role newRole = roleRepository.findRoleByRole("ROLE_USER");
            roles.add(newRole);
        }
    } else {
        if(roleRepository.findRoleByRole(role)==null) {
            roleRepository.save(new Role(role));
        }
        roles.add(roleRepository.findRoleByRole(role));
    }
    user.setRoles(roles);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userRepository.save(user);
}

    @Transactional
    public void updateUser(User userUpdate, String roleUpdate) {
        User foundUser = userRepository.getById(userUpdate.getId());
        Set<Role> roles = new HashSet<>();

        if (roleUpdate == null) {
            roles = foundUser.getRoles();
            userUpdate.setRoles(roles);
        } else {
            roles.add(roleRepository.findRoleByRole(roleUpdate));
            userUpdate.setRoles(roles);
        }
        userUpdate.setPassword(passwordEncoder.encode(userUpdate.getPassword()));
        userRepository.save(userUpdate);
    }

    @Transactional
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public User getUser(int id) {
        Optional<User> foundUser =userRepository.findById(id);
        return foundUser.orElse(null);
    }

    @Transactional
    public User getUserByName(String userName) {
        return userRepository.findByUsername(userName);
    }


    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User foundUser = userRepository.findByUsername(username);
        if(foundUser==null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found!", username));
        }
        return new org.springframework.security.core.userdetails.User(foundUser.getUsername(), foundUser.getPassword(), foundUser.getAuthorities());

    }
}
