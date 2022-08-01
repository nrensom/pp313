package ru.kata.spring.boot_security.demo.service;



import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    public List<User> getAllUsers();

    public boolean save(User user);

    public User getUser(long id);

    public boolean delete(long id);

    public void update(User user);
}
