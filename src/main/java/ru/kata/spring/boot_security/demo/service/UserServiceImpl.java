package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepo;
import ru.kata.spring.boot_security.demo.repository.UserRepo;


import javax.persistence.EntityManager;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    final
    UserRepo userRepo;
    final
    RoleRepo roleRepo;

    final
    EntityManager em;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, RoleRepo roleRepo, EntityManager em) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.em = em;
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public boolean save(User user) {
        User userFromDB = userRepo.findByUsername(user.getUsername());
        if (userFromDB != null) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return true;
    }

    public User getUser(long id) {
        Optional<User> userFromDB = userRepo.findById(id);
        return userFromDB.orElse(new User());
    }

    public boolean delete(long id) {
        if (userRepo.findById(id).isPresent()) {
            userRepo.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public void update(User user) {
        em.merge(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getAuthorities());
    }

    public Long getIdByUsername(String username) {
        Long id = userRepo.findByUsername(username).getId();
        return id;
    }

    @Override
    public Set<Role> getRoles(List<Long> roles) {
        Set<Role> result = new HashSet<>();
        for (Long role : roles) {
            if (role == 1) {
                result.add(new Role(1, "ROLE_ADMIN"));
            } else if (role == 2) {
                result.add(new Role(2, "ROLE_USER"));
            }
        }
        return result;
    }
}
