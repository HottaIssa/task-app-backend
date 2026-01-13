package com.saihoz.task_app.controller;

import com.saihoz.task_app.dto.LoginResponse;
import com.saihoz.task_app.dto.RegisterResponse;
import com.saihoz.task_app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.saihoz.task_app.service.JwtService;
import com.saihoz.task_app.service.UserService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {
    @Autowired
    private UserService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody User user) {
        RegisterResponse savedUser = service.saveUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody User user) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        if (authentication.isAuthenticated()) {
            LoginResponse token = new LoginResponse(jwtService.generateToken(user.getUsername()));
            return new ResponseEntity<>(token, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Login Failed", HttpStatus.BAD_REQUEST);
        }
    }
}
