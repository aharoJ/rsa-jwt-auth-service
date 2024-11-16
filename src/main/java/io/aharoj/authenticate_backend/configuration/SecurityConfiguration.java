package io.aharoj.authenticate_backend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import io.aharoj.authenticate_backend.utils.RSAKeyProperties;

/**
 * SecurityConfiguration
 */
@Configuration
public class SecurityConfiguration {

  private final RSAKeyProperties keys;

  public SecurityConfiguration(RSAKeyProperties keys) {
    this.keys = keys;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authManager(UserDetailsService userDetailsService) {
    DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
    daoProvider.setUserDetailsService(userDetailsService);
    daoProvider.setPasswordEncoder(passwordEncoder()); // Added this line
    return new ProviderManager(daoProvider);
  }

  // Spring 7.0.
  @SuppressWarnings("removal")
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> {
          auth.requestMatchers("/auth/**").permitAll();
          auth.requestMatchers("/admin/**").hasRole("ADMIN");
          auth.requestMatchers("/user/**").hasAnyRole("ADMIN", "USER");
          auth.anyRequest().authenticated();
        });
    http
        .oauth2ResourceServer()
        .jwt()
        .jwtAuthenticationConverter(jwtAuthenticationConverter());

    http
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }

  /**
   * // Spring 7.0.
   * public SecurityFilterChain filterChain(HttpSecurity http) throws
   * Exception {
   * return http
   * .csrf(csrf -> csrf.disable())
   * .authorizeHttpRequests(auth -> {
   * auth.requestMatchers("/auth/**").permitAll();
   * auth.anyRequest().authenticated();
   * })
   * .httpBasic(Customizer.withDefaults())
   * .build();
   * }
   * 
   * ----------------------------------------------------------------------------------
   * // Spring 7.0.
   * public SecurityFilterChain filterChain(HttpSecurity http) throws
   * Exception {
   * return http
   * .csrf(csrf -> csrf.disable()) // Disable CSRF protection for simplicity
   * .authorizeHttpRequests(auth -> auth
   * .anyRequest().authenticated() // Require authentication for all
   * requests
   * )
   * .httpBasic(Customizer.withDefaults()) // Use HTTP Basic Authentication
   * with defaults
   * .build();
   * }
   *
   * 
   * ----------------------------------------------------------------------------------
   * public SecurityFilterChain filterChain(HttpSecurity http) throws
   * Exception {
   * return http
   * .csrf(csrf -> csrf.disable())
   * .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
   * .build();
   * }
   * 
   */

  @Bean
  public JwtDecoder jwtDecoder() {
    return NimbusJwtDecoder.withPublicKey(keys.getPublicKey()).build(); // Decodes and verifies JWT tokens.
  }

  @Bean
  public JwtEncoder jwtEncoder() {
    JWK jwk = new RSAKey.Builder(keys.getPublicKey()).privateKey(keys.getPrivateKey()).build();
    JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));

    // Generates and signs JWT tokens.Used when your application needs to issue

    // tokens (e.g., after a user logs in).
    return new NimbusJwtEncoder(jwks);
  }

  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
    jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
    JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
    jwtConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

    return jwtConverter;
  }

}
