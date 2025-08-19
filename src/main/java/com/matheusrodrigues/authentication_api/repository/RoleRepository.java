package com.matheusrodrigues.authentication_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matheusrodrigues.authentication_api.enums.RoleName;
import com.matheusrodrigues.authentication_api.models.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    public Optional<Role> findByName(RoleName name);

    public boolean existsByName(RoleName name);

    

}
