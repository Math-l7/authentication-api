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

    private void userAuthentication(String token) {
        User user = jwt.getUserFromToken(token, repository);
        if (!user.getRole().getName().equals(RoleName.ROLE_ADMIN)
                && !user.getRole().getName().equals(RoleName.ROLE_MANAGER)) {
            throw new UnauthorizedException();
        }

    }

    private void roleAuthentication(Role role) {
        if (!role.getName().equals(RoleName.ROLE_ADMIN) && !role.getName().equals(RoleName.ROLE_MANAGER)
                && !role.getName().equals(RoleName.ROLE_USER)) {
            throw new BadRequestException();
        }
    }

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

    public LoginReturn loginByEmail(String email, String password) {

        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException());

        boolean passowrdTrue = bcrypt.matches(password, user.getPassword());

        if (!passowrdTrue) {
            throw new BadRequestException();
        } else {
            String token = jwt.generateToken(user);
            return new LoginReturn(user, token);
        }
    }

    public LoginReturn loginByUsername(String username, String password) {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException());

        boolean passowrdTrue = bcrypt.matches(password, user.getPassword());

        if (!passowrdTrue) {
            throw new BadRequestException();
        } else {
            String token = jwt.generateToken(user);
            return new LoginReturn(user, token);
        }
    }

    // métodos protegidos por jwt

    public List<User> getAllUsers(String token) {

        userAuthentication(token);
        return repository.findAll();

    }

    public User setRoleUser(String token, Role roleSet) {
        User user = jwt.getUserFromToken(token, repository);

        RoleName role = user.getRole().getName();
        if (role.equals(RoleName.ROLE_MANAGER)) {
            user.setRole(roleSet);
            return repository.save(user);

        } else {
            throw new UnauthorizedException();
        }

    }

    public User updateUser(String token, Integer id, User updateUSer) {

        userAuthentication(token);

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

        userAuthentication(token);

        return repository.findById(idUser)
                .orElseThrow(() -> new UserNotFoundException());
    }

    public User deleteUser(Integer idUser, String token) {
        User user = jwt.getUserFromToken(token, repository);

        userAuthentication(token);

        if (user.getId().equals(idUser)) {
            throw new BadRequestException();
        }

        User userDelete = repository.findById(idUser)
                .orElseThrow(() -> new UserNotFoundException());

        repository.deleteById(idUser);
        return userDelete;

    }

    public List<User> getUsersByRole(Role role) {
        roleAuthentication(role);

        return repository.getUserByRole(role);
    }

    public Integer countUsersByRole(Role role) {
        roleAuthentication(role);
        return repository.countByRole(role);
    }

}
