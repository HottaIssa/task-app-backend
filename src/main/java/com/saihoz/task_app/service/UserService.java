package com.saihoz.task_app.service;

import com.saihoz.task_app.dto.AuthDTO.PatchPasswordRequest;
import com.saihoz.task_app.dto.UserDTO.GuestResponse;
import com.saihoz.task_app.dto.UserDTO.UserResponse;
import com.saihoz.task_app.dto.UserDTO.UserSimpleResponse;
import com.saihoz.task_app.mapper.ProjectMemberMapper;
import com.saihoz.task_app.mapper.UserMapper;
import com.saihoz.task_app.model.ProjectMember;
import com.saihoz.task_app.model.Role;
import com.saihoz.task_app.model.User;
import com.saihoz.task_app.repo.MemberRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.saihoz.task_app.repo.UserRepo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepo repo;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MemberRepo memberRepo;

    @Autowired
    private ProjectMemberMapper memberMapper;

    @Autowired
    private StorageService storageService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public UserResponse saveUser(User user) {
        user.setUsername(user.getUsername().toLowerCase());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        user.setAvatar_url("images/profile-default.webp");
        User savedUser = repo.save(user);
        return userMapper.toResponse(savedUser);
    }

    public UserResponse getUserById(UUID id) {
        User user = repo.findById(id).orElse(null);
        return userMapper.toResponse(user);
    }

    public UserSimpleResponse getUserByUsername(String username) {
        return userMapper.toSimpleResponse(repo.findByUsername(username));
    }

    public void updateAvatar(String username, MultipartFile file){
        User user = repo.findByUsername(username);
        String newAvatarUrl = storageService.uploadImage(user.getId(), file);
        user.setAvatar_url(newAvatarUrl);
        repo.save(user);
    }

    public void deleteUser(String username) {
        repo.deleteByUsername(username);
    }

    public List<GuestResponse> getGuest(String username) {
        User user = repo.findByUsername(username);
        List<ProjectMember> members = memberRepo.findByInvitedBy(user);

        return members.stream().map(projectMember -> memberMapper.toGuestResponse(projectMember)).collect(Collectors.toList());
    }

    public void updatePassword(PatchPasswordRequest request, String username) {
        if(!request.newPassword().equals(request.confirmationPassword())) throw new RuntimeException("Confirmation password not match");
        User user = repo.findByUsername(username);
        if(!encoder.matches(request.currentPassword(), user.getPassword())) throw new RuntimeException("Not is your current password");
        user.setPassword(encoder.encode(request.newPassword()));
        repo.save(user);
    }
}
