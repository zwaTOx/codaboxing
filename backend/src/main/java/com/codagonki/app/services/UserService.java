package com.codagonki.app.services;

import com.codagonki.app.DTO.User.UpdatePasswordRequest;
import com.codagonki.app.DTO.User.UpdateProfileRequest;
import com.codagonki.app.DTO.User.UserProfileResponse;
import com.codagonki.app.models.User;
import com.codagonki.app.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserProfileResponse getUserInfo(User user) {
        return new UserProfileResponse(user);
    }
    
    public UserProfileResponse updateUserProfile(User user, UpdateProfileRequest updateRequest) {
        if (updateRequest.getNickname() != null && !updateRequest.getNickname().equals(user.getNickname())) {
            user.setNickname(updateRequest.getNickname());
        } 
        
        User updatedUser = userRepository.save(user);
        
        return UserProfileResponse.builder()
                .email(updatedUser.getEmail())
                .nickname(updatedUser.getNickname())
                .role(updatedUser.getRole())
                .rating(updatedUser.getRating())
                .build();
    }

    public UserProfileResponse updateUserPassword(User user, UpdatePasswordRequest updateRequest){
        if (!passwordEncoder.matches(updateRequest.getOldPassword(), user.getHashedPassword())){
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Неверный пароль"
            );
        }
        if (passwordEncoder.matches(updateRequest.getNewPassword(), user.getHashedPassword())){
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Нельзя изменить пароль на старый"
            );
        }
        user.setHashedPassword(passwordEncoder.encode(updateRequest.getNewPassword()));
        User updatedUser = userRepository.save(user);
        return UserProfileResponse.builder()
            .email(updatedUser.getEmail())
            .nickname(updatedUser.getNickname())
            .role(updatedUser.getRole())
            .rating(updatedUser.getRating())
            .build();
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }
}