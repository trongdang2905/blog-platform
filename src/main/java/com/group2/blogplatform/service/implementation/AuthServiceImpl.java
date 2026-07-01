package com.group2.blogplatform.service.implementation;

import com.group2.blogplatform.dto.request.UserRegisterRequest;
import com.group2.blogplatform.dto.request.UserRequest;
import com.group2.blogplatform.entity.Role;
import com.group2.blogplatform.entity.User;
import com.group2.blogplatform.exception.EmailExistException;
import com.group2.blogplatform.exception.PasswordWrongException;
import com.group2.blogplatform.repository.UserRepository;
import com.group2.blogplatform.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    @Override
    public User authenticate(UserRequest userRequest) {

        return userRepository.findByEmailAndPassword(
                userRequest.getEmail(), userRequest.getPassword()).orElseThrow(() -> new PasswordWrongException("Sai email hoặc mật khẩu"));
    }


    @Override
    public void register(UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailExistException("Email đã tồn tại");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(request.getPassword());
        user.setRole(Role.MEMBER);
        userRepository.save(user);
    }
}
