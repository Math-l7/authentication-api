package com.matheusrodrigues.authentication_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matheusrodrigues.authentication_api.models.Role;
import com.matheusrodrigues.authentication_api.models.User;

public interface UserRepository extends JpaRepository<User, Integer>{

    public Optional<User> findByUsername(String username);
    public Optional<User> findByEmail(String email);
    public boolean existsByUsername(String username);
    public boolean existsByEmail(String email);
    public List<User> getUserByRole (Role role);
    public Integer countByRole (Role name);
}
