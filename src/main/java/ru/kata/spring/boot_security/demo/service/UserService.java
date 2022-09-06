package ru.kata.spring.boot_security.demo.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;
import java.util.Set;


public interface UserService extends UserDetailsService {
    public List<User> getAllUsers();

    public boolean save(User user);

    public User getUser(long id);

    public boolean delete(long id);

    public void update(long id, User user);

    UserDetails loadUserByEmail(String email) throws UsernameNotFoundException;

    public Long getIdByUsername(String username);

    public Set<Role> getRoles(List<Long> roles);

    public User getUserByEmail(String email);
}
