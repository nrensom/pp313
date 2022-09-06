package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepo;
import ru.kata.spring.boot_security.demo.repository.UserRepo;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private final static long ROLE_ADMIN = 1;
    private final static long ROLE_USER = 2;
    private final UserService userService;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SetupDataLoader(UserService userService, RoleRepo roleRepo, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Iterable<User> users = userService.getAllUsers();
        if (users.iterator().hasNext()) {
            return;
        }

        Role adminRole = new Role(ROLE_ADMIN, "ROLE_ADMIN");
        Role userRole = new Role(ROLE_USER, "ROLE_USER");
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);
        roleRepo.save(adminRole);
        roleRepo.save(userRole);

        User admin = new User();
        admin.setEmail("admin@mail.com");
        admin.setName("admin");
        admin.setPassword("admin");
        admin.setRoles(adminRoles);
        userService.save(admin);

        User user = new User();
        user.setEmail("user@mail.com");
        user.setName("Peppa");
        user.setLastName("Pig");
        user.setAge((byte)4);
        user.setPassword("user");
        user.setRoles(userRoles);
        userService.save(user);

    }
}
