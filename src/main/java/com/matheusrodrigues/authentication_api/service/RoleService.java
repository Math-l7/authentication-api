package com.matheusrodrigues.authentication_api.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matheusrodrigues.authentication_api.Exceptions.BadRequestException;
import com.matheusrodrigues.authentication_api.Exceptions.UnauthorizedException;
import com.matheusrodrigues.authentication_api.enums.RoleName;
import com.matheusrodrigues.authentication_api.models.Role;
import com.matheusrodrigues.authentication_api.models.User;
import com.matheusrodrigues.authentication_api.repository.RoleRepository;
import com.matheusrodrigues.authentication_api.repository.UserRepository;

@Service
public class RoleService {

    @Autowired
    private RoleRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwt;

    public void userAuthentication(String token) {
        User user = jwt.getUserFromToken(token, userRepository);
        if (!user.getRole().getName().equals(RoleName.ROLE_ADMIN)
                && !user.getRole().getName().equals(RoleName.ROLE_MANAGER)) {
            throw new UnauthorizedException();
        }

    }

    public Role saveRole(String token, Role role) {
        userAuthentication(token);

        return repository.save(role);
    }

    public Role getRole(String token, Integer roleId) {
        userAuthentication(token);

        return repository.findById(roleId)
                .orElseThrow(() -> new BadRequestException());

    }

    public Role setRole(String token, Integer idRole, Role updatedRole) {
        userAuthentication(token);

        Role newRole = repository.findById(idRole).orElseThrow(() -> new BadRequestException());
        newRole.setName(updatedRole.getName());
        return repository.save(newRole);
    }

    public void deleteRole(String token, Integer roleId) {
        userAuthentication(token);
        Role roleDelet = repository.findById(roleId).orElseThrow(() -> new BadRequestException());

        repository.delete(roleDelet);

    }

}
