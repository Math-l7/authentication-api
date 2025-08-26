package com.matheusrodrigues.authentication_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matheusrodrigues.authentication_api.Exceptions.BadRequestException;
import com.matheusrodrigues.authentication_api.models.Role;
import com.matheusrodrigues.authentication_api.repository.RoleRepository;

@Service
public class RoleService {

    @Autowired
    private RoleRepository repository;

    public Role saveRole(Role role) {
        return repository.save(role);
    }

    public Role getRole(Integer roleId) {
        return repository.findById(roleId)
                .orElseThrow(() -> new BadRequestException());

    }

    public Role setRole(Integer idRole, Role updatedRole) {
        Role newRole = repository.findById(idRole).orElseThrow(() -> new BadRequestException());
        newRole.setName(updatedRole.getName());
        return repository.save(newRole);
    }

    public void deleteRole(Integer roleId) {
        Role roleDelet = repository.findById(roleId).orElseThrow(() -> new BadRequestException());

        repository.delete(roleDelet);

    }

}
