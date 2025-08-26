package com.matheusrodrigues.authentication_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.matheusrodrigues.authentication_api.models.User;
import com.matheusrodrigues.authentication_api.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException{
        User user = repository.findByUsername(login).or(() -> repository.findByEmail(login))
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não identificado."));

        return user;
    }

}
