package com.saihoz.task_app.service;


import com.saihoz.task_app.dto.RegisterResponse;
import com.saihoz.task_app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.saihoz.task_app.repo.UserRepo;

@Service
public class UserService {

    @Autowired
    private UserRepo repo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public RegisterResponse saveUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        User savedUser = repo.save(user);
        return new RegisterResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getPassword());
    }

}
