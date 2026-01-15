package com.saihoz.task_app.controller;

import com.saihoz.task_app.dto.UserDTO.UserResponse;
import com.saihoz.task_app.model.User;
import com.saihoz.task_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getAllUsers(@PathVariable Long id){
        return ResponseEntity.ok().body(service.getUserById(id));
    }

}
