package com.studentresult.management.service;

import com.studentresult.management.entity.User;
import com.studentresult.management.entity.UserRole;
import com.studentresult.management.repository.UserRepository;
import com.studentresult.management.repository.UserRoleRepository;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public CustomUserDetailsService(
            UserRepository userRepository,
            UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        List<SimpleGrantedAuthority> authorities =
                userRoleRepository.findRoleNamesByUsername(username)
                        .stream()
                        .map(roleName -> {
                            String name = roleName.trim()
                                    .toUpperCase(Locale.ROOT);

                            if (!name.startsWith("ROLE_")) {
                                name = "ROLE_" + name;
                            }

                            return new SimpleGrantedAuthority(name);
                        })
                        .toList();

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPasswordHash())
                .authorities(authorities)
                .build();
    }

}
