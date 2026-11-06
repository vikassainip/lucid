package com.lucid.api.controller;

import com.lucid.core.auth.AuthService;
import com.lucid.core.auth.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(
       origins = "http://localhost:63342",
       allowedHeaders = "*"
)
public class AuthController {

   private final AuthService authService;

   private final JwtService jwtService;

   public AuthController(AuthService authService, JwtService jwtService) {
       this.authService = authService;
       this.jwtService = jwtService;
   }

   @PostMapping(
           value = "/login",
           consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
           produces = MediaType.APPLICATION_JSON_VALUE
   )
   public ResponseEntity<String> loginUser(@RequestParam String username, @RequestParam String password) {
       boolean legitLogin = authService.authenticateUser(username, password);

       if (!legitLogin) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("username/password incorrect");
       }

       String token = jwtService.generateToken(username);
       return ResponseEntity.ok("{\"jwt_token\":\"" + token + "\"}");
   }

   @PostMapping(
           value = "/register",
           consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
   )
   public ResponseEntity<String> registerUser(@RequestParam String username, @RequestParam String password) {
       String response = authService.registerUser(username, password);
       return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }

}
