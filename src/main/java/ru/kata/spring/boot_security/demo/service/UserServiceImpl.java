package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    final
    UserRepo userRepo;
    final
    RoleRepo roleRepo;

    final
    EntityManager em;





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
        user.setRoles(Collections.singleton(new Role(2L, "ROLE_ADMIN")));
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
        return user;
    }
}
