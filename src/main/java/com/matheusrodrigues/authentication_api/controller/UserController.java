package com.matheusrodrigues.authentication_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matheusrodrigues.authentication_api.dto.ChangePassword;
import com.matheusrodrigues.authentication_api.dto.LoginRequest;
import com.matheusrodrigues.authentication_api.dto.LoginReturn;
import com.matheusrodrigues.authentication_api.models.Role;
import com.matheusrodrigues.authentication_api.models.User;
import com.matheusrodrigues.authentication_api.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service;

    // metodo geral
    @PostMapping("/save-users")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveUser(user));

    }

    // metodo geral
    @PostMapping("/login")
    public ResponseEntity<LoginReturn> login(@RequestBody LoginRequest user) {
        return ResponseEntity.ok(service.login(user.getUsername(), user.getPassword()));
    }

    // manager e admin
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.getUserById(id, token));
    }

    // metodo geral
    @GetMapping("/users-role")
    public ResponseEntity<List<User>> getUserByRole(@RequestParam Role role) {
        return ResponseEntity.ok(service.getUsersByRole(role));
    }

    // metodo geral
    @GetMapping("/count-users-role")
    public ResponseEntity<Integer> countUsersByRole(@RequestParam Role role) {
        return ResponseEntity.ok(service.countUsersByRole(role));
    }

    // metodo Manager
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PutMapping("/set-role-user/{id}")
    public ResponseEntity<User> setRoleUser(@PathVariable Integer id, @RequestBody Role role) {

        return ResponseEntity.ok(service.setRoleUser(id, role));
    }

    // metodo manager e admin
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PutMapping("/set-user/{id}")
    public ResponseEntity<User> updateUser(@RequestHeader("Authorization") String token, @PathVariable Integer id,
            @RequestBody User userSet) {

        return ResponseEntity.ok(service.updateUser(token, id, userSet));
    }

    // metodo geral
    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestHeader("Authorization") String token,
            @RequestBody ChangePassword obj) {
        service.changePassword(token, obj.getOlderPassword(), obj.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    // metodo manager e admin
    @PreAuthorize("hasAnyRole('ROLE_ADMIN' , 'ROLE_MANAGER')")
    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Integer idUser, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.deleteUser(idUser, token));
    }
}
