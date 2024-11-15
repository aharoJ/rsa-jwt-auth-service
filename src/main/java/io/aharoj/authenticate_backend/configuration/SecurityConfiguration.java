package io.aharoj.authenticate_backend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.config.Customizer;

/**
 * SecurityConfiguration
 */
@Configuration
public class SecurityConfiguration {
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authManager(UserDetailsService userDetailsService) {
    DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
    daoProvider.setUserDetailsService(userDetailsService);
    return new ProviderManager(daoProvider);
  }

  // Spring 7.0.
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> {
          auth.requestMatchers("/auth/**").permitAll();
          auth.anyRequest().authenticated();
        })
        .httpBasic(Customizer.withDefaults())
        .build();
  }

  /**
   * // Spring 7.0.
   * 
   * @Bean
   *       public SecurityFilterChain filterChain(HttpSecurity http) throws
   *       Exception {
   *       return http
   *       .csrf(csrf -> csrf.disable()) // Disable CSRF protection for simplicity
   *       .authorizeHttpRequests(auth -> auth
   *       .anyRequest().authenticated() // Require authentication for all
   *       requests
   *       )
   *       .httpBasic(Customizer.withDefaults()) // Use HTTP Basic Authentication
   *       with defaults
   *       .build();
   *       }
   *
   *       /
   * 
   * 
   *       /**
   *       public SecurityFilterChain filterChain(HttpSecurity http) throws
   *       Exception {
   *       return http
   *       .csrf(csrf -> csrf.disable())
   *       .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
   *       .build();
   *       }
   * 
   */

}
