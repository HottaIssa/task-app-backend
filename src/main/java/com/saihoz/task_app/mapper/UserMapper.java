package com.saihoz.task_app.mapper;

import com.saihoz.task_app.dto.UserDTO.UserRequest;
import com.saihoz.task_app.dto.UserDTO.UserResponse;
import com.saihoz.task_app.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getPassword());
    }


}
