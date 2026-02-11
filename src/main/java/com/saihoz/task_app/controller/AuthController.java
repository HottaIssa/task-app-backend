package com.saihoz.task_app.controller;

import com.saihoz.task_app.dto.AuthDTO.LoginResponse;
import com.saihoz.task_app.dto.UserDTO.UserResponse;
import com.saihoz.task_app.model.User;
import com.saihoz.task_app.model.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.saihoz.task_app.service.JwtService;
import com.saihoz.task_app.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody User user) {
        UserResponse savedUser = service.saveUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody User user) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername().toLowerCase(),
                            user.getPassword()
                    )
            );

            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            String token = jwtService.generateToken(principal);

            return ResponseEntity.ok(new LoginResponse(token));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorrectos");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno: " + e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal UserDetails user){
        service.deleteUser(user.getUsername());
        return ResponseEntity.noContent().build();
    }
}
