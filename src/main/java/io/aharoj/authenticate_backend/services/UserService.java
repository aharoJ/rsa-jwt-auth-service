package io.aharoj.authenticate_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.aharoj.authenticate_backend.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

  @Autowired
  private PasswordEncoder encoder;

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    System.out.println("In the user details service");

    return userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("user is not valid"));
  }

  /**
   * @Override
   *           public UserDetails loadUserByUsername(String username) throws
   *           UsernameNotFoundException {
   * 
   *           System.out.println("In the user details service");
   * 
   *           if (!username.equals("Angel"))
   *           throw new UsernameNotFoundException("Not Angel");
   * 
   *           Set<Role> roles = new HashSet<>();
   *           roles.add(new Role(1, "USER"));
   * 
   *           return new ApplicationUser(1, "Angel", encoder.encode("password"),
   *           roles);
   *           }
   */
}
