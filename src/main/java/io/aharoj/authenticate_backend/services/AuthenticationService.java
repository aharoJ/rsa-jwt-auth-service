package io.aharoj.authenticate_backend.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.aharoj.authenticate_backend.models.ApplicationUser;
import io.aharoj.authenticate_backend.models.Role;
import io.aharoj.authenticate_backend.repository.RoleRepository;
import io.aharoj.authenticate_backend.repository.UserRepository;

@Service
@Transactional
public class AuthenticationService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public ApplicationUser registerUser(String username, String password) {

    String encodedPassword = passwordEncoder.encode(password);
    Role userRole = roleRepository.findByAuthority("USER").get();

    Set<Role> authorities = new HashSet<>();

    authorities.add(userRole);
    return userRepository.save(new ApplicationUser(0, username, encodedPassword, authorities));
  }

}
