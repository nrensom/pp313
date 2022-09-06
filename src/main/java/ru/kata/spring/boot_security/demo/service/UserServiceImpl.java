package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final EntityManager em;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, RoleRepo roleRepo, EntityManager em, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.em = em;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public boolean save(User user) {
        User userFromDB = userRepo.findByEmail(user.getEmail());
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

    @Override
    @Transactional
    public void update(long id, User user) {
        User temp = em.find(User.class, id);
        temp.setName(user.getName());
        temp.setLastName(user.getLastName());
        temp.setAge(user.getAge());
        temp.setEmail(user.getEmail());
        temp.setRoles(user.getRoles());
        temp.setPassword(user.getPassword());
        em.merge(temp);
    }

    @Override
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getAuthorities());
    }


    public Long getIdByUsername(String email) {
        Long id = userRepo.findByEmail(email).getId();
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

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

}
