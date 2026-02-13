package com.saihoz.task_app.mapper;

import com.saihoz.task_app.dto.UserDTO.UserResponse;
import com.saihoz.task_app.dto.UserDTO.UserSimpleResponse;
import com.saihoz.task_app.model.User;
import com.saihoz.task_app.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    @Autowired
    private StorageService storageService;

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().getDisplayName(),
//                storageService.getPresignedUrl(user.getAvatar_url())
                user.getAvatar_url()
        );
    }

    public UserSimpleResponse toSimpleResponse(User user){
        if(user == null) return null;
        return new UserSimpleResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
//                storageService.getPresignedUrl(user.getAvatar_url())
                user.getAvatar_url()
        );
    }


}
