package io.aharoj.authenticate_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.aharoj.authenticate_backend.models.ApplicationUser;
import io.aharoj.authenticate_backend.models.RegistrationDTO;
import io.aharoj.authenticate_backend.services.AuthenticationService;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthenticationController {

  @Autowired
  private AuthenticationService authenticationService;

  @PostMapping("/register")
  public ApplicationUser registerUser(@RequestBody RegistrationDTO body) {
    return authenticationService.registerUser(body.getUsername(), body.getPassword());
  }

  /**
   * skeleton before injecting
   * @PostMapping("/register")
   * public ApplicationUser registerUser() {
   * return authenticationService.registerUser("", "");
   * }
   */
}
