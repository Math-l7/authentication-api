package com.matheusrodrigues.authentication_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matheusrodrigues.authentication_api.models.Role;
import com.matheusrodrigues.authentication_api.service.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private RoleService service;

    @PostMapping("/save-role")
    public ResponseEntity<Role> saveRole(@RequestHeader("Authorization") String token, @RequestBody Role role) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveRole(token, role));
    }

    @GetMapping("/get-role/{id}")
    public ResponseEntity<Role> getRole(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        return ResponseEntity.ok(service.getRole(token, id));
    }

    @PutMapping("set-role/{id}")
    public ResponseEntity<Role> setRole(@RequestHeader("Authorization") String token, @PathVariable Integer id,
            @RequestBody Role updatedRole) {
        return ResponseEntity.ok(service.setRole(token, id, updatedRole));
    }

    @DeleteMapping("/delete-role/{id}")
    public ResponseEntity<Void> deleteRole(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        service.deleteRole(token, id);
        return ResponseEntity.noContent().build();
    }
}
