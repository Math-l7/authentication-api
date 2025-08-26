package com.matheusrodrigues.authentication_api.service;

import com.matheusrodrigues.authentication_api.Exceptions.*;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.matheusrodrigues.authentication_api.dto.LoginReturn;
import com.matheusrodrigues.authentication_api.enums.RoleName;
import com.matheusrodrigues.authentication_api.models.Role;
import com.matheusrodrigues.authentication_api.models.User;
import com.matheusrodrigues.authentication_api.repository.RoleRepository;
import com.matheusrodrigues.authentication_api.repository.UserRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtService jwt;

    @Autowired
    private BCryptPasswordEncoder bcrypt;

    @Autowired
    private AuthenticationManager authenticationManager;

    public User saveUser(User user) {
        boolean existByEmail = repository.existsByEmail(user.getEmail());
        boolean existByUsername = repository.existsByUsername(user.getUsername());

        if (existByEmail || existByUsername) {
            throw new BadRequestException();
        } else {
            Role firstRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new BadRequestException());

            user.setRole(firstRole);

            user.setPassword(bcrypt.encode(user.getPassword()));

            return repository.save(user);

        }
    }

    public LoginReturn login(String login, String password) {

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(login, password);

        Authentication authentication = authenticationManager.authenticate(authToken);

        User user = (User) authentication.getPrincipal();

        String tokenJwt = jwt.generateToken(user);

        return new LoginReturn(user, tokenJwt);

    }

    // métodos protegidos por jwt

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User setRoleUser(Integer id, Role roleSet) {

        User userSet = repository.findById(id).orElseThrow(() -> new UserNotFoundException());

        userSet.setRole(roleSet);
        return repository.save(userSet);

    }

    public User updateUser(String token, Integer id, User updateUSer) {

        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException());

        if (updateUSer.getUsername() != null) {
            user.setUsername(updateUSer.getUsername());
        }
        if (updateUSer.getEmail() != null) {
            user.setEmail(updateUSer.getEmail());
        }
        if (updateUSer.getEmail() == null && updateUSer.getUsername() == null) {
            throw new BadRequestException();
        }

        return repository.save(user);
    }

    public void changePassword(String token, String olderPassword, String newPassword) {
        User user = jwt.getUserFromToken(token, repository);

        if (!bcrypt.matches(olderPassword, user.getPassword())) {
            throw new BadRequestException();
        }
        if (bcrypt.matches(newPassword, user.getPassword())) {
            throw new BadRequestException();
        }

        user.setPassword(bcrypt.encode(newPassword));
        repository.save(user);
    }

    public User getUserById(Integer idUser, String token) {

        return repository.findById(idUser)
                .orElseThrow(() -> new UserNotFoundException());
    }

    public User deleteUser(Integer idUser, String token) {
        User user = jwt.getUserFromToken(token, repository);

        if (user.getId().equals(idUser)) {
            throw new BadRequestException();
        }

        User userDelete = repository.findById(idUser)
                .orElseThrow(() -> new UserNotFoundException());

        repository.deleteById(idUser);
        return userDelete;

    }

    public List<User> getUsersByRole(Role role) {
        return repository.getUserByRole(role);
    }

    public Integer countUsersByRole(Role role) {
        return repository.countByRole(role);
    }

}
