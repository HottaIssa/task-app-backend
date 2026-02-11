package com.saihoz.task_app.controller;

import com.saihoz.task_app.dto.AuthDTO.PatchPasswordRequest;
import com.saihoz.task_app.dto.UserDTO.GuestResponse;
import com.saihoz.task_app.dto.UserDTO.UserResponse;
import com.saihoz.task_app.dto.UserDTO.UserSimpleResponse;
import com.saihoz.task_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID id){
        return ResponseEntity.ok().body(service.getUserById(id));
    }

    @GetMapping("me")
    public ResponseEntity<UserSimpleResponse> getMe(@AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok().body(service.getUserByUsername(user.getUsername()));
    }

    @PatchMapping("password")
    public ResponseEntity<String> updatePassword(@RequestBody PatchPasswordRequest request, @AuthenticationPrincipal UserDetails user){
        service.updatePassword(request, user.getUsername());
        return ResponseEntity.ok().body("Password updated");
    }

    @GetMapping("invitations")
    public ResponseEntity<List<GuestResponse>> getGuest(@AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok().body(service.getGuest(user.getUsername()));
    }

    @PatchMapping("update-avatar")
    public ResponseEntity<?> updateAvatar(@AuthenticationPrincipal UserDetails user, @RequestParam("file") MultipartFile file){
        service.updateAvatar(user.getUsername(), file);
        return ResponseEntity.ok().body("Avatar updated successfully");
    }

}
