package com.saihoz.task_app.service;


import com.saihoz.task_app.dto.RegisterResponse;
import com.saihoz.task_app.dto.TaskDTO.TaskResponse;
import com.saihoz.task_app.dto.UserDTO.UserResponse;
import com.saihoz.task_app.mapper.TaskMapper;
import com.saihoz.task_app.mapper.UserMapper;
import com.saihoz.task_app.model.Role;
import com.saihoz.task_app.model.User;
import com.saihoz.task_app.repo.TaskRepo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.saihoz.task_app.repo.UserRepo;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepo repo;

    @Autowired
    private TaskRepo taskRepo;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TaskMapper taskMapper;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public RegisterResponse saveUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        User savedUser = repo.save(user);
        return new RegisterResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getPassword(), savedUser.getFirstName(), savedUser.getLastName(), savedUser.getRole().getDisplayName(), savedUser.getAvatar_url());
    }

    public UserResponse getUserById(UUID id) {
        User user = repo.findById(id).orElse(null);
        return userMapper.toResponse(user);
    }

    public List<TaskResponse> getTasksByUser(UUID id) {
        User user = repo.findById(id).orElse(null);
        return taskRepo.findByAssignedTo(user).stream().map(taskMapper::toResponse).toList();
    }

    public UserResponse getUserByUsername(String username) {
        return userMapper.toResponse(repo.findByUsername(username));
    }
}
