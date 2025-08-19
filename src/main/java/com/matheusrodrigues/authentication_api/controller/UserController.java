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

    @PostMapping("/save-users")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveUser(user));

    }

    @PostMapping("/login-email")
    public ResponseEntity<LoginReturn> loginByEmail(@RequestBody LoginRequest user) {
        return ResponseEntity.ok(service.loginByEmail(user.getEmail(), user.getPassword()));
    }

    @PostMapping("/login-username")
    public ResponseEntity<LoginReturn> loginByUsername(@RequestBody LoginRequest user) {
        return ResponseEntity.ok(service.loginByUsername(user.getUsername(), user.getPassword()));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.getUserById(id, token));
    }

    @GetMapping("/users-role")
    public ResponseEntity<List<User>> getUserByRole(@RequestParam Role role) {
        return ResponseEntity.ok(service.getUsersByRole(role));
    }

    @GetMapping("/count-users-role")
    public ResponseEntity<Integer> countUsersByRole(@RequestParam Role role) {
        return ResponseEntity.ok(service.countUsersByRole(role));
    }

    @PutMapping("/set-role-user")
    public ResponseEntity<User> setRoleUser(@RequestHeader("Authorization") String token, @RequestBody Role role) {

        return ResponseEntity.ok(service.setRoleUser(token, role));
    }

    @PutMapping("/set-user/{id}")
    public ResponseEntity<User> updateUser(@RequestHeader("Authorization") String token, @PathVariable Integer id,
            @RequestBody User userSet) {

        return ResponseEntity.ok(service.updateUser(token, id, userSet));
    }

    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestHeader("Authorization") String token,
            @RequestBody ChangePassword obj) {
        service.changePassword(token, obj.getOlderPassword(), obj.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Integer idUser, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(service.deleteUser(idUser, token));
    }
}
